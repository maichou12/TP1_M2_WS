package com.groupeisi.m2gl.web.graphql.resolver;

import com.groupeisi.m2gl.domain.Book;
import java.time.format.DateTimeFormatter;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

/**
 * Type resolver pour mapper les champs de l'entité Book vers le schéma GraphQL.
 */
@Controller
public class BookTypeResolver {

    /**
     * Mappe le champ datePub du schéma GraphQL avec date_pub de l'entité.
     */
    @SchemaMapping(typeName = "Book", field = "datePub")
    public String datePub(Book book) {
        if (book.getDate_pub() == null) {
            return null;
        }
        return book.getDate_pub().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
