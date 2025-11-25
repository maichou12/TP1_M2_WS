package com.groupeisi.m2gl.web.graphql.resolver;

import com.groupeisi.m2gl.domain.Book;
import com.groupeisi.m2gl.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * Resolver GraphQL pour les requêtes (Query) sur les livres.
 */
@Controller
public class BookQueryResolver {

    private final BookRepository bookRepository;

    public BookQueryResolver(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Récupère tous les livres.
     */
    @QueryMapping
    public List<Book> books() {
        return bookRepository.findAll();
    }

    /**
     * Récupère un livre par son ID.
     */
    @QueryMapping
    public Book book(@Argument String id) {
        try {
            Long bookId = Long.parseLong(id);
            Optional<Book> book = bookRepository.findById(bookId);
            return book.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Recherche des livres par titre.
     */
    @QueryMapping
    public List<Book> booksByTitle(@Argument String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Recherche des livres par auteur.
     */
    @QueryMapping
    public List<Book> booksByAuthor(@Argument String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }
}
