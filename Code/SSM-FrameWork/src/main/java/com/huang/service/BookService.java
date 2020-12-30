package com.huang.service;

import com.huang.pojo.Books;

import java.util.List;

/**
 * @author Yuyuan Huang
 * @create 2020-12-30 17:49
 */
public interface BookService {
    int addBook(Books books);
    int deleteBookById(int id);
    int updateBook(Books books);
    Books queryBookById(int id);
    List<Books> queryAllBook();
}
