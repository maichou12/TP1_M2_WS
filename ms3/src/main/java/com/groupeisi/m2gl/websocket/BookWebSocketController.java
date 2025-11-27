package com.groupeisi.m2gl.websocket;

import com.groupeisi.m2gl.domain.Book;
import com.groupeisi.m2gl.repository.BookRepository;
import com.groupeisi.m2gl.websocket.dto.BookMessage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contrôleur WebSocket pour la gestion des livres en temps réel.
 */
@Controller
@Transactional
public class BookWebSocketController {

    private static final Logger LOG = LoggerFactory.getLogger(BookWebSocketController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final BookRepository bookRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public BookWebSocketController(BookRepository bookRepository, SimpMessagingTemplate messagingTemplate) {
        this.bookRepository = bookRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Créer un nouveau livre.
     * Le client envoie un message à /app/book/create
     * La réponse est envoyée à /topic/books
     */
    @MessageMapping("/book/create")
    @SendTo("/topic/books")
    public BookMessage createBook(BookMessage message) {
        LOG.debug("WebSocket request to create Book : {}", message);

        try {
            Book book = new Book();
            book.setTitle(message.getTitle());
            book.setPrix(message.getPrix());
            book.setAuthor(message.getAuthor());
            if (message.getDatePub() != null && !message.getDatePub().isEmpty()) {
                book.setDate_pub(LocalDate.parse(message.getDatePub(), DATE_FORMATTER));
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

            // Notifier tous les clients
            messagingTemplate.convertAndSend("/topic/books", response);

            return response;
        } catch (Exception e) {
            LOG.error("Error creating book", e);
            BookMessage errorResponse = new BookMessage("CREATE");
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Error creating book: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Récupérer un livre par son ID.
     * Le client envoie un message à /app/book/get
     */
    @MessageMapping("/book/get")
    @SendTo("/topic/book")
    public BookMessage getBook(BookMessage message) {
        LOG.debug("WebSocket request to get Book : {}", message.getId());

        return bookRepository
            .findById(message.getId())
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
                errorResponse.setMessage("Book not found with id: " + message.getId());
                return errorResponse;
            });
    }

    /**
     * Récupérer tous les livres.
     * Le client envoie un message à /app/book/getAll
     */
    @MessageMapping("/book/getAll")
    @SendTo("/topic/books")
    public BookMessage getAllBooks(BookMessage message) {
        LOG.debug("WebSocket request to get all Books");

        try {
            List<Book> books = bookRepository.findAll();
            BookMessage response = new BookMessage("GET_ALL");
            response.setSuccess(true);
            response.setMessage("Found " + books.size() + " book(s)");

            // Envoyer la liste des livres via un message séparé
            // Note: Pour une vraie liste, on pourrait créer un DTO différent
            messagingTemplate.convertAndSend("/topic/books/list", books);

            return response;
        } catch (Exception e) {
            LOG.error("Error getting all books", e);
            BookMessage errorResponse = new BookMessage("GET_ALL");
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Error getting books: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Mettre à jour un livre.
     * Le client envoie un message à /app/book/update
     */
    @MessageMapping("/book/update")
    @SendTo("/topic/books")
    public BookMessage updateBook(BookMessage message) {
        LOG.debug("WebSocket request to update Book : {}", message.getId());

        return bookRepository
            .findById(message.getId())
            .map(book -> {
                book.setTitle(message.getTitle());
                book.setPrix(message.getPrix());
                book.setAuthor(message.getAuthor());
                if (message.getDatePub() != null && !message.getDatePub().isEmpty()) {
                    book.setDate_pub(LocalDate.parse(message.getDatePub(), DATE_FORMATTER));
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

                // Notifier tous les clients
                messagingTemplate.convertAndSend("/topic/books", response);

                return response;
            })
            .orElseGet(() -> {
                BookMessage errorResponse = new BookMessage("UPDATE");
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Book not found with id: " + message.getId());
                return errorResponse;
            });
    }

    /**
     * Supprimer un livre.
     * Le client envoie un message à /app/book/delete
     */
    @MessageMapping("/book/delete")
    @SendTo("/topic/books")
    public BookMessage deleteBook(BookMessage message) {
        LOG.debug("WebSocket request to delete Book : {}", message.getId());

        if (bookRepository.existsById(message.getId())) {
            bookRepository.deleteById(message.getId());

            BookMessage response = new BookMessage("DELETE");
            response.setId(message.getId());
            response.setSuccess(true);
            response.setMessage("Book deleted successfully");

            // Notifier tous les clients
            messagingTemplate.convertAndSend("/topic/books", response);

            return response;
        } else {
            BookMessage errorResponse = new BookMessage("DELETE");
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Book not found with id: " + message.getId());
            return errorResponse;
        }
    }
}

