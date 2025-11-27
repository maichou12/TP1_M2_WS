package com.groupeisi.m2gl.repository;

import com.groupeisi.m2gl.domain.Book;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Recherche des livres par titre (contient).
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Recherche des livres par auteur (contient).
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);
}

