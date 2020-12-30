package com.huang.dao;

import com.huang.pojo.Books;

import java.util.List;

/**
 * @author Yuyuan Huang
 * @create 2020-12-30 17:02
 */
public interface BookMapper {
    //add a certain book
    int addBook(Books books);
    //delete a book by id
    int deleteBook(int id);
    //update a book with new book
    int updateBook(Books books);
    //query a book by bookId
    Books queryBookById(int id);
    //this method query all book in MySQL
    List<Books> queryAllBook();
}
