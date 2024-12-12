package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService{
    
    @Autowired
    private AuthorRepository authorRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    //---

    @Override
    public Author getAuthorById(long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Author is not found with id: \" + id"));
    }
    //---

    @Override
    public Author saveAuthor(Author author) {
        Optional<Author> authorWithSameName = authorRepository.findAuthorByAuthorName(author.getAuthorName());

        if(authorWithSameName.isPresent()){
            throw new IllegalArgumentException("Author with name " + author.getAuthorName() + " already exists");
        }

        return authorRepository.save(author);
    }
    //---

    @Override
    public Author updateAuthor(long id, Author author) {
        Author existingAuthor = getAuthorById(id);

        if((existingAuthor.getAuthorName()).equals(author.getAuthorName())){
            throw new IllegalArgumentException("Author with name " + author.getAuthorName() + " already exists");
        }

        existingAuthor.setAuthorName(author.getAuthorName());
        return authorRepository.save(existingAuthor);
    }
    //---

    @Override
    public void deleteAuthor(long id) {
       Optional<Author> existingAuthor = authorRepository.findById(id);

       if(!existingAuthor.isPresent()){
        throw new IllegalArgumentException("Author is not found with id: " + id);
       }

       authorRepository.deleteById(id);
       logger.info("Author with id {} was deleted.", id);
    }
    //---
}
