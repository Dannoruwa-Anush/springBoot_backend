package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.saveRequest.BookSaveRequestDTO;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.commonService.FileUploadService;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FileUploadService fileUploadConfig;

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    // Helper class to validate book save/update request
    private void validateBookSaveRequest(BookSaveRequestDTO bookSaveRequestDTO) {
        if (bookSaveRequestDTO.getTitle() == null || bookSaveRequestDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Book title is required.");
        }

        if (bookSaveRequestDTO.getUnitPrice() < 0) {
            throw new IllegalArgumentException("Unit price must be a positive value.");
        }

        if (bookSaveRequestDTO.getQoh() < 0) {
            throw new IllegalArgumentException("QOH must be a positive value.");
        }

        if (bookSaveRequestDTO.getCoverImage() != null && bookSaveRequestDTO.getCoverImage().isEmpty()) {
            throw new IllegalArgumentException("Cover image is invalid.");
        }
    }
    //---

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    // ---

    @Override
    public Book getBookById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book is not found with id: " + id));
    }
    // ---

    @Override
    public Book saveBook(BookSaveRequestDTO bookSaveRequestDTO) {
        validateBookSaveRequest(bookSaveRequestDTO); //validate requests

        try {
            // Validate and save the uploaded file
            String coverImagePath = fileUploadConfig.saveFile(bookSaveRequestDTO.getCoverImage());

            Book book = new Book();
            book.setTitle(bookSaveRequestDTO.getTitle());
            book.setUnitPrice(bookSaveRequestDTO.getUnitPrice());
            book.setCoverImage(coverImagePath);
            
            return bookRepository.save(book);

        } catch (IOException e) {
            throw new IllegalArgumentException("Error uploading file: " + e.getMessage());
        }
    }
    // ---

    @Override
    public Book updateBook(long id, BookSaveRequestDTO bookSaveRequestDTO) {

        validateBookSaveRequest(bookSaveRequestDTO); //validate requests

        try {
            Book existingBook = getBookById(id);
            existingBook.setTitle(bookSaveRequestDTO.getTitle());
            existingBook.setUnitPrice(bookSaveRequestDTO.getUnitPrice());

            // Handle cover image update
            if (bookSaveRequestDTO.getCoverImage() != null && !bookSaveRequestDTO.getCoverImage().isEmpty()) {
                // Validate and save the uploaded file
                String coverImagePath = fileUploadConfig.saveFile(bookSaveRequestDTO.getCoverImage());
                existingBook.setCoverImage(coverImagePath);
            }

            return bookRepository.save(existingBook);

        } catch (IOException e) {
            throw new IllegalArgumentException("Error uploading file: " + e.getMessage());
        }
    }
    // ---

    @Override
    public void deleteBook(long id) {
        Optional<Book> existingBook = bookRepository.findById(id);

        if (!existingBook.isPresent()) {
            throw new IllegalArgumentException("Book is not found with id: " + id);
        }
        bookRepository.deleteById(id);
        logger.info("Book with id {} was deleted.", id);
    }
    // ---

    @Override
    public List<Book> getAllBooksBySubCategoryId(Long inputSubCategoryId) {
        return bookRepository.findAllBooksBySubCategoryId(inputSubCategoryId);
    }
    // ---

    @Override
    public List<Book> getAllBooksByAuthorId(Long inputAuthorId) {
        return bookRepository.findAllBooksByAuthorId(inputAuthorId);
    }
    // ---

    @Override
    public List<Book> getAllBooksByCategoryId(Long inputCategoryId) {
        return bookRepository.findAllBooksByCategoryId(inputCategoryId);
    }
    // ---
}
