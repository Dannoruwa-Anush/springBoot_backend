package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.BookRequestDTO;
import com.example.demo.dto.response.BookResponseDTO;
import com.example.demo.dto.response.getById.BooKGetByIdResponseDTO;

@Service
public interface BookService {

    List<BookResponseDTO> getAllBooks();

    BooKGetByIdResponseDTO getBookById(long id);

    BookResponseDTO saveBook(BookRequestDTO bookSaveRequestDTO);

    BookResponseDTO updateBook(long id, BookRequestDTO bookSaveRequestDTO);

    void deleteBook(long id);

    List<BookResponseDTO> getAllBooksBySubCategoryId(Long inputSubCategoryId);

    List<BookResponseDTO> getAllBooksByAuthorId(Long inputAuthorId);

    List<BookResponseDTO> getAllBooksByCategoryId(Long inputCategoryId);
}
