package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.AuthorRequestDTO;
import com.example.demo.dto.response.AuthorResponseDTO;
import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    // ****
    private AuthorResponseDTO toAuthorResponseDTO(Author author) {
        AuthorResponseDTO responseDto = new AuthorResponseDTO();
        responseDto.setId(author.getId());
        responseDto.setAuthorName(author.getAuthorName());

        return responseDto;
    }
    // ****

    @Override
    public List<AuthorResponseDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorResponseDTO> authorDTOs = authors.stream().map(this::toAuthorResponseDTO)
                .collect(Collectors.toList());
        return authorDTOs;
    }
    // ---

    @Override
    public AuthorResponseDTO getAuthorById(long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author is not found with id: " + id));

        return toAuthorResponseDTO(author);
    }
    // ---

    @Override
    public AuthorResponseDTO saveAuthor(AuthorRequestDTO authorRequestDTO) {
        Optional<Author> authorWithSameName = authorRepository.findAuthorByAuthorName(authorRequestDTO.getAuthorName());

        if (authorWithSameName.isPresent()) {
            throw new IllegalArgumentException(
                    "Author with name " + authorRequestDTO.getAuthorName() + " already exists");
        }

        // create a new author
        Author authorToSave = new Author();
        authorToSave.setAuthorName(authorRequestDTO.getAuthorName());
        return toAuthorResponseDTO(authorRepository.save(authorToSave));
    }
    // ---

    @Override
    public AuthorResponseDTO updateAuthor(long id, AuthorRequestDTO authorRequestDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author is not found with id: " + id));

        if ((existingAuthor.getAuthorName()).equals(authorRequestDTO.getAuthorName()) &&
                authorRepository.findAuthorByAuthorName(authorRequestDTO.getAuthorName()).isPresent()) {
            throw new IllegalArgumentException(
                    "Author with name " + authorRequestDTO.getAuthorName() + " already exists");
        }

        // update
        existingAuthor.setAuthorName(authorRequestDTO.getAuthorName());
        return toAuthorResponseDTO(authorRepository.save(existingAuthor));
    }
    // ---

    @Override
    public void deleteAuthor(long id) {

        if (!authorRepository.existsById(id)) {
            throw new IllegalArgumentException("Author is not found with id: " + id);
        }

        // Check if any books are associated with the author
        boolean hasBooks = bookRepository.existsByAuthorId(id);
        if (hasBooks) {
            throw new IllegalStateException("Cannot delete Author with associated Books.");
        }

        authorRepository.deleteById(id);
        logger.info("Author with id {} was deleted.", id);
    }
    // ---
}
