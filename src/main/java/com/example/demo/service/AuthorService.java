package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Author;

@Service
public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(long id);

    Author saveAuthor(Author author);

    Author updateAuthor(long id, Author author);

    void deleteAuthor(long id);
}
