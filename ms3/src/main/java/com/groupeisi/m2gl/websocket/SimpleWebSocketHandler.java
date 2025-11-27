package com.groupeisi.m2gl.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupeisi.m2gl.domain.Book;
import com.groupeisi.m2gl.repository.BookRepository;
import com.groupeisi.m2gl.websocket.dto.BookMessage;
import com.groupeisi.m2gl.websocket.dto.BookListResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Handler WebSocket simple (sans STOMP) pour la compatibilité avec Postman.
 */
@Component
@Transactional
public class SimpleWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleWebSocketHandler.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    public SimpleWebSocketHandler(BookRepository bookRepository, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.debug("WebSocket connection established: {}", session.getId());
        session.sendMessage(new TextMessage("{\"status\":\"connected\",\"message\":\"WebSocket connection established\"}"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.debug("Received WebSocket message: {}", message.getPayload());

        try {
            BookMessage request = objectMapper.readValue(message.getPayload(), BookMessage.class);
            
            // GET_ALL retourne une BookListResponse, les autres retournent BookMessage
            if ("GET_ALL".equalsIgnoreCase(request.getAction())) {
                BookListResponse response = getAllBooksResponse(request);
                String responseJson = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(responseJson));
            } else {
                BookMessage response = processMessage(request);
                String responseJson = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(responseJson));
            }
        } catch (Exception e) {
            LOG.error("Error processing WebSocket message", e);
            BookMessage errorResponse = new BookMessage();
            errorResponse.setAction("ERROR");
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Error processing request: " + e.getMessage());
            try {
                String errorJson = objectMapper.writeValueAsString(errorResponse);
                session.sendMessage(new TextMessage(errorJson));
            } catch (IOException ioException) {
                LOG.error("Error sending error response", ioException);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.debug("WebSocket connection closed: {} with status: {}", session.getId(), status);
    }

    private BookMessage processMessage(BookMessage request) {
        String action = request.getAction();
        if (action == null) {
            throw new IllegalArgumentException("Action is required");
        }

        return switch (action.toUpperCase()) {
            case "CREATE" -> createBook(request);
            case "GET" -> getBook(request);
            case "UPDATE" -> updateBook(request);
            case "DELETE" -> deleteBook(request);
            default -> {
                BookMessage error = new BookMessage();
                error.setAction("ERROR");
                error.setSuccess(false);
                error.setMessage("Unknown action: " + action);
                yield error;
            }
        };
    }

    private BookMessage createBook(BookMessage request) {
        LOG.debug("Creating book: {}", request);

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setPrix(request.getPrix());
        book.setAuthor(request.getAuthor());
        if (request.getDatePub() != null && !request.getDatePub().isEmpty()) {
            book.setDate_pub(LocalDate.parse(request.getDatePub(), DATE_FORMATTER));
        }

        Book savedBook = bookRepository.save(book);

        BookMessage response = new BookMessage("CREATE");
        response.setId(savedBook.getId());
        response.setTitle(savedBook.getTitle());
        response.setPrix(savedBook.getPrix());
        response.setAuthor(savedBook.getAuthor());
        if (savedBook.getDate_pub() != null) {
            response.setDatePub(savedBook.getDate_pub().format(DATE_FORMATTER));
        }
        response.setSuccess(true);
        response.setMessage("Book created successfully");

        return response;
    }

    private BookMessage getBook(BookMessage request) {
        LOG.debug("Getting book: {}", request.getId());

        return bookRepository
            .findById(request.getId())
            .map(book -> {
                BookMessage response = new BookMessage("GET");
                response.setId(book.getId());
                response.setTitle(book.getTitle());
                response.setPrix(book.getPrix());
                response.setAuthor(book.getAuthor());
                if (book.getDate_pub() != null) {
                    response.setDatePub(book.getDate_pub().format(DATE_FORMATTER));
                }
                response.setSuccess(true);
                return response;
            })
            .orElseGet(() -> {
                BookMessage errorResponse = new BookMessage("GET");
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Book not found with id: " + request.getId());
                return errorResponse;
            });
    }


    private BookListResponse getAllBooksResponse(BookMessage request) {
        LOG.debug("Getting all books");

        List<Book> books = bookRepository.findAll();
        BookListResponse response = new BookListResponse("GET_ALL");
        response.setSuccess(true);
        
        // Convertir la liste de livres en BookMessage
        List<BookMessage> bookMessages = books.stream()
            .map(book -> {
                BookMessage bookMsg = new BookMessage("BOOK");
                bookMsg.setId(book.getId());
                bookMsg.setTitle(book.getTitle());
                bookMsg.setPrix(book.getPrix());
                bookMsg.setAuthor(book.getAuthor());
                if (book.getDate_pub() != null) {
                    bookMsg.setDatePub(book.getDate_pub().format(DATE_FORMATTER));
                }
                return bookMsg;
            })
            .toList();
        
        response.setBooks(bookMessages);
        response.setCount(books.size());
        response.setMessage("Found " + books.size() + " book(s)");
        
        return response;
    }

    private BookMessage updateBook(BookMessage request) {
        LOG.debug("Updating book: {}", request.getId());

        return bookRepository
            .findById(request.getId())
            .map(book -> {
                book.setTitle(request.getTitle());
                book.setPrix(request.getPrix());
                book.setAuthor(request.getAuthor());
                if (request.getDatePub() != null && !request.getDatePub().isEmpty()) {
                    book.setDate_pub(LocalDate.parse(request.getDatePub(), DATE_FORMATTER));
                }

                Book updatedBook = bookRepository.save(book);

                BookMessage response = new BookMessage("UPDATE");
                response.setId(updatedBook.getId());
                response.setTitle(updatedBook.getTitle());
                response.setPrix(updatedBook.getPrix());
                response.setAuthor(updatedBook.getAuthor());
                if (updatedBook.getDate_pub() != null) {
                    response.setDatePub(updatedBook.getDate_pub().format(DATE_FORMATTER));
                }
                response.setSuccess(true);
                response.setMessage("Book updated successfully");

                return response;
            })
            .orElseGet(() -> {
                BookMessage errorResponse = new BookMessage("UPDATE");
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Book not found with id: " + request.getId());
                return errorResponse;
            });
    }

    private BookMessage deleteBook(BookMessage request) {
        LOG.debug("Deleting book: {}", request.getId());

        if (bookRepository.existsById(request.getId())) {
            bookRepository.deleteById(request.getId());

            BookMessage response = new BookMessage("DELETE");
            response.setId(request.getId());
            response.setSuccess(true);
            response.setMessage("Book deleted successfully");

            return response;
        } else {
            BookMessage errorResponse = new BookMessage("DELETE");
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Book not found with id: " + request.getId());
            return errorResponse;
        }
    }
}

