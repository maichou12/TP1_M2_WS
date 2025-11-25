package com.groupeisi.m2gl.web.graphql.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO pour les mutations GraphQL de Book.
 */
public class BookInput implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private Double prix;
    private String author;
    private String datePub;

    public BookInput() {
        // Constructeur par défaut
    }

    // Getters Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDatePub() {
        return datePub;
    }

    public void setDatePub(String datePub) {
        this.datePub = datePub;
    }

    /**
     * Convertit la datePub (String) en LocalDate.
     */
    public LocalDate getDatePubAsLocalDate() {
        if (datePub == null || datePub.isEmpty()) {
            return null;
        }
        return LocalDate.parse(datePub);
    }
}
