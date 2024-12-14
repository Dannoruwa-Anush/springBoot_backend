package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.AuthorRequestDTO;
import com.example.demo.dto.response.AuthorResponseDTO;

@Service
public interface AuthorService {

    List<AuthorResponseDTO> getAllAuthors();

    AuthorResponseDTO getAuthorById(long id);

    AuthorResponseDTO saveAuthor(AuthorRequestDTO authorRequestDTO);

    AuthorResponseDTO updateAuthor(long id, AuthorRequestDTO authorRequestDTO);

    void deleteAuthor(long id);
}
