package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Book;

public interface BookService {

    List<Book> getAllBooks();

    Book getBookById(long id);

    Book saveBook(Book book);

    Book updateBook(long id, Book book);

    void deleteBook(long id);

    List<Book> getAllBooksBySubCategoryId(Long inputSubCategoryId);

    List<Book> getAllBooksByAuthorId(Long inputAuthorId);

    List<Book> getAllBooksByCategoryId(Long inputCategoryId);
}
