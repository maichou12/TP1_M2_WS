package com.groupeisi.m2gl.websocket.dto;

import java.io.Serializable;

/**
 * DTO pour les messages WebSocket concernant les livres.
 */
public class BookMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String action; // CREATE, UPDATE, DELETE, GET, GET_ALL
    private Long id;
    private String title;
    private Double prix;
    private String author;
    private String datePub; // Format ISO: YYYY-MM-DD
    private String message;
    private Boolean success;

    public BookMessage() {
        // Constructeur par défaut
    }

    public BookMessage(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BookMessage{" +
            "action='" + action + '\'' +
            ", id=" + id +
            ", title='" + title + '\'' +
            ", prix=" + prix +
            ", author='" + author + '\'' +
            ", datePub='" + datePub + '\'' +
            ", message='" + message + '\'' +
            ", success=" + success +
            '}';
    }
}

