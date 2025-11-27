package com.groupeisi.m2gl.grpc;

import com.groupeisi.m2gl.domain.Book;
import com.groupeisi.m2gl.repository.BookRepository;
import io.grpc.stub.StreamObserver;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service gRPC pour la gestion des livres.
 */
@GrpcService
@Transactional
public class BookGrpcService extends BookServiceGrpc.BookServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(BookGrpcService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final BookRepository bookRepository;

    public BookGrpcService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void getBook(GetBookRequest request, StreamObserver<BookResponse> responseObserver) {
        LOG.debug("gRPC request to get Book : {}", request.getId());

        bookRepository
            .findById(request.getId())
            .ifPresentOrElse(
                book -> {
                    BookResponse response = convertToBookResponse(book);
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                () -> {
                    responseObserver.onError(
                        io.grpc.Status.NOT_FOUND.withDescription("Book not found with id: " + request.getId()).asRuntimeException()
                    );
                }
            );
    }

    @Override
    public void getAllBooks(GetAllBooksRequest request, StreamObserver<GetAllBooksResponse> responseObserver) {
        LOG.debug("gRPC request to get all Books");

        List<Book> books = bookRepository.findAll();
        GetAllBooksResponse.Builder responseBuilder = GetAllBooksResponse.newBuilder();

        for (Book book : books) {
            responseBuilder.addBooks(convertToBookResponse(book));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void createBook(CreateBookRequest request, StreamObserver<BookResponse> responseObserver) {
        LOG.debug("gRPC request to create Book : {}", request);

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setPrix(request.getPrix());
        book.setAuthor(request.getAuthor());
        if (request.getDatePub() != null && !request.getDatePub().isEmpty()) {
            book.setDate_pub(LocalDate.parse(request.getDatePub(), DATE_FORMATTER));
        }

        Book savedBook = bookRepository.save(book);
        BookResponse response = convertToBookResponse(savedBook);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<BookResponse> responseObserver) {
        LOG.debug("gRPC request to update Book : {}", request.getId());

        bookRepository
            .findById(request.getId())
            .ifPresentOrElse(
                book -> {
                    book.setTitle(request.getTitle());
                    book.setPrix(request.getPrix());
                    book.setAuthor(request.getAuthor());
                    if (request.getDatePub() != null && !request.getDatePub().isEmpty()) {
                        book.setDate_pub(LocalDate.parse(request.getDatePub(), DATE_FORMATTER));
                    }

                    Book updatedBook = bookRepository.save(book);
                    BookResponse response = convertToBookResponse(updatedBook);

                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                () -> {
                    responseObserver.onError(
                        io.grpc.Status.NOT_FOUND.withDescription("Book not found with id: " + request.getId()).asRuntimeException()
                    );
                }
            );
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {
        LOG.debug("gRPC request to delete Book : {}", request.getId());

        if (bookRepository.existsById(request.getId())) {
            bookRepository.deleteById(request.getId());
            DeleteBookResponse response = DeleteBookResponse.newBuilder().setSuccess(true).setMessage("Book deleted successfully").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(
                io.grpc.Status.NOT_FOUND.withDescription("Book not found with id: " + request.getId()).asRuntimeException()
            );
        }
    }

    private BookResponse convertToBookResponse(Book book) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        BookResponse.Builder builder = BookResponse.newBuilder().setId(book.getId());

        if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
            builder.setTitle(book.getTitle());
        } else {
            builder.setTitle("");
        }

        if (book.getPrix() != null) {
            builder.setPrix(book.getPrix());
        } else {
            builder.setPrix(0.0);
        }

        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            builder.setAuthor(book.getAuthor());
        } else {
            builder.setAuthor("");
        }

        if (book.getDate_pub() != null) {
            builder.setDatePub(book.getDate_pub().format(DATE_FORMATTER));
        } else {
            builder.setDatePub("");
        }

        return builder.build();
    }
}

