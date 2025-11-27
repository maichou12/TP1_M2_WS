package com.groupeisi.m2gl.websocket.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO pour la réponse contenant une liste de livres.
 */
public class BookListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String action;
    private Boolean success;
    private String message;
    private List<BookMessage> books;
    private Integer count;

    public BookListResponse() {
    }

    public BookListResponse(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BookMessage> getBooks() {
        return books;
    }

    public void setBooks(List<BookMessage> books) {
        this.books = books;
        if (books != null) {
            this.count = books.size();
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

