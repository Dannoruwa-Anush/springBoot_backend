package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Author;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(long id);

    Author saveAuthor(Author author);

    Author updateAuthor(long id, Author author);

    void deleteAuthor(long id);
}
