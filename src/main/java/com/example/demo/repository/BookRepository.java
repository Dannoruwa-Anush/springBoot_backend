package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /*
     * JpaRepository is a generic interface, meaning it can work with any entity
     * class and its corresponding ID type
     * It extends CrudRepository and PagingAndSortingRepository, which means it
     * inherits methods for basic CRUD operations (like save, find, delete), as well
     * as methods for pagination and sorting.
     */

    // We can add custom queries here.

    // 1. find all books by subCategory id
    // SQL QUERY : SELECT * FROM books WHERE sub_category_id = inputSubCategoryId;
    // JPQL query
    @Query("SELECT b FROM Book b WHERE b.subCategory.id = :inputSubCategoryId")
    List<Book> findAllBooksBySubCategoryId(@Param("inputSubCategoryId") Long inputSubCategoryId);

    // 2. find all books by author id
    // SQL QUERY : SELECT * FROM books WHERE author_id = inputAuthorId;
    // JPQL query
    @Query("SELECT b FROM Book b WHERE b.author.id = :inputAuthorId")
    List<Book> findAllBooksByAuthorId(@Param("inputAuthorId") Long inputAuthorId);

    // 3. find all books by Category
    /*
     * SQL QUERY :
     * 
     * SELECT b.*
     * FROM books b
     * JOIN subcategories s ON b.sub_category_id = s.id
     * JOIN categories c ON s.category_id = c.id
     * WHERE c.id = :inputCategoryId;
     */
    // JPQL query
    @Query("SELECT b FROM Book b WHERE b.subCategory.category.id = :inputCategoryId")
    List<Book> findAllBooksByCategoryId(@Param("inputCategoryId") Long inputCategoryId);
}
