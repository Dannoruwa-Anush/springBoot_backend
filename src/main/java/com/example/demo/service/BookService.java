package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.BookSaveRequestDTO;
import com.example.demo.entity.Book;

@Service
public interface BookService {

    List<Book> getAllBooks();

    Book getBookById(long id);

    Book saveBook(BookSaveRequestDTO bookSaveRequestDTO);

    Book updateBook(long id, BookSaveRequestDTO bookSaveRequestDTO);

    void deleteBook(long id);

    List<Book> getAllBooksBySubCategoryId(Long inputSubCategoryId);

    List<Book> getAllBooksByAuthorId(Long inputAuthorId);

    List<Book> getAllBooksByCategoryId(Long inputCategoryId);
}
