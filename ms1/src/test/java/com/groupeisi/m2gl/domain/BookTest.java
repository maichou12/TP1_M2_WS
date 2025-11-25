package com.groupeisi.m2gl.domain;

import static com.groupeisi.m2gl.domain.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.groupeisi.m2gl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }
}
