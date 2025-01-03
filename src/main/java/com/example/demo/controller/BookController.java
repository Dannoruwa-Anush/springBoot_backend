package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.request.BookWithCoverImageRequestDTO;
import com.example.demo.dto.request.BookWithOutCoverImageRequestDTO;
import com.example.demo.dto.response.BookResponseDTO;
import com.example.demo.dto.response.getById.BooKGetByIdResponseDTO;
import com.example.demo.service.BookService;

@RestController
/*
 * It allows you to specify which external origins (i.e., domains or URLs) are
 * permitted to make requests to your API. This is useful when your frontend
 * application (running on a different server or port) needs to interact with
 * your backend
 */
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    /*
     * ResponseEntity is a powerful class in Spring Boot for managing HTTP
     * responses.
     * 
     * It allows you to:
     * 
     * Return custom status codes.
     * Add headers.
     * Set the body of the response.
     * 
     * .build() - You typically use .build() when you want to send an HTTP status
     * without any associated content in the response body.
     */

    @GetMapping("/book")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(books);
    }
    // ---

    @GetMapping("/book/{id}")
    public ResponseEntity<BooKGetByIdResponseDTO> getBookById(@PathVariable Long id) {
        try {
            BooKGetByIdResponseDTO book = bookService.getBookById(id);
            return ResponseEntity.status(HttpStatus.OK).body(book);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // ---

    /*
     * 
     * When using @ModelAttribute, the Spring framework binds form data, query
     * parameters, and uploaded files (multipart data) to a single object.
     */
    @PostMapping("/book")
    public ResponseEntity<Object> createBook(@ModelAttribute BookWithCoverImageRequestDTO bookSaveRequestDTO) {
        try {
            BookResponseDTO savedBook = bookService.saveBook(bookSaveRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    /*
     * 
     * When using @ModelAttribute, the Spring framework binds form data, query
     * parameters, and uploaded files (multipart data) to a single object.
     */
    @PutMapping("/book/with-cover-image/{id}")
    public ResponseEntity<Object> updateBookWithCoverImage(@PathVariable Long id,
            @ModelAttribute BookWithCoverImageRequestDTO bookSaveRequestDTO) {
        try {
            BookResponseDTO updatedBook = bookService.updateBookWithCoverImage(id, bookSaveRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    @GetMapping("/book/subCategory/{subCategoryId}")
    public ResponseEntity<List<BookResponseDTO>> getAllBooksBySubCategoryId(@PathVariable Long subCategoryId) {
        List<BookResponseDTO> books = bookService.getAllBooksBySubCategoryId(subCategoryId);

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(books);
    }
    // ---

    @GetMapping("/book/author/{authorId}")
    public ResponseEntity<List<BookResponseDTO>> getAllBooksByAuthorId(@PathVariable Long authorId) {
        List<BookResponseDTO> books = bookService.getAllBooksByAuthorId(authorId);

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(books);
    }
    // ---

    @GetMapping("/book/category/{categoryId}")
    public ResponseEntity<List<BookResponseDTO>> getAllBooksByCategoryId(@PathVariable Long categoryId) {
        List<BookResponseDTO> books = bookService.getAllBooksByCategoryId(categoryId);

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(books);
    }
    // ---

    @PutMapping("/book/without-cover-image/{id}")
    public ResponseEntity<Object> updateBookWithOutCoverImage(@PathVariable Long id,
            @RequestBody BookWithOutCoverImageRequestDTO bookSaveRequestDTO) {
        try {
            BookResponseDTO updatedBook = bookService.updateBookWithOutCoverImage(id, bookSaveRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---
}
