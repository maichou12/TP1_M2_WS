package com.groupeisi.m2gl.web.graphql.resolver;

import com.groupeisi.m2gl.domain.Book;
import com.groupeisi.m2gl.repository.BookRepository;
import com.groupeisi.m2gl.web.graphql.dto.BookInput;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * Resolver GraphQL pour les mutations sur les livres.
 */
@Controller
public class BookMutationResolver {

    private final BookRepository bookRepository;

    public BookMutationResolver(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Crée un nouveau livre.
     */
    @MutationMapping
    public Book createBook(@Argument BookInput input) {
        Book book = new Book();
        book.setTitle(input.getTitle());
        book.setPrix(input.getPrix());
        book.setAuthor(input.getAuthor());

        if (input.getDatePub() != null && !input.getDatePub().isEmpty()) {
            book.setDate_pub(LocalDate.parse(input.getDatePub()));
        }

        return bookRepository.save(book);
    }

    /**
     * Met à jour un livre existant.
     */
    @MutationMapping
    public Book updateBook(@Argument String id, @Argument BookInput input) {
        try {
            Long bookId = Long.parseLong(id);
            Optional<Book> optionalBook = bookRepository.findById(bookId);

            if (optionalBook.isEmpty()) {
                throw new RuntimeException("Livre non trouvé avec l'ID: " + id);
            }

            Book book = optionalBook.get();
            book.setTitle(input.getTitle());
            book.setPrix(input.getPrix());
            book.setAuthor(input.getAuthor());

            if (input.getDatePub() != null && !input.getDatePub().isEmpty()) {
                book.setDate_pub(LocalDate.parse(input.getDatePub()));
            }

            return bookRepository.save(book);
        } catch (NumberFormatException e) {
            throw new RuntimeException("ID invalide: " + id);
        }
    }

    /**
     * Supprime un livre.
     */
    @MutationMapping
    public Boolean deleteBook(@Argument String id) {
        try {
            Long bookId = Long.parseLong(id);
            Optional<Book> optionalBook = bookRepository.findById(bookId);

            if (optionalBook.isEmpty()) {
                return false;
            }

            bookRepository.deleteById(bookId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
