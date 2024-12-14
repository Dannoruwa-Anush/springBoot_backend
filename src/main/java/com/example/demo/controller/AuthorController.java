package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.request.AuthorRequestDTO;
import com.example.demo.dto.response.AuthorResponseDTO;
import com.example.demo.service.AuthorService;

@RestController
@RequestMapping("author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

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

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        List<AuthorResponseDTO> authors = authorService.getAllAuthors();

        if (authors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(authors);
    }
    // ---

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAllAuthorById(@PathVariable Long id) {
        try {
            AuthorResponseDTO author = authorService.getAuthorById(id);
            return ResponseEntity.status(HttpStatus.OK).body(author);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // ---

    @PostMapping
    public ResponseEntity<Object> createAuthor(@RequestBody AuthorRequestDTO authorRequestDTO) {
        try {
            AuthorResponseDTO savedAuthor = authorService.saveAuthor(authorRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    @PutMapping("{id}")
    public ResponseEntity<Object> updateAuthor(@PathVariable Long id, @RequestBody AuthorRequestDTO authorRequestDTO) {
        try {
            AuthorResponseDTO updatedAuthor = authorService.updateAuthor(id, authorRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuthor(@PathVariable Long id) {
        try {
            authorService.deleteAuthor(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---
}
