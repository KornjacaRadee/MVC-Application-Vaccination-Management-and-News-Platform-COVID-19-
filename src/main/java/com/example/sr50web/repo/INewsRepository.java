package com.example.sr50web.repo;


import com.example.sr50web.Models.News;

import java.util.List;

public interface INewsRepository {
    public List<News> findAllNews();

    public News findNewsById(Integer id);

    public int save(News news);

    public int update(News news);

    public int delete(Integer id);
}