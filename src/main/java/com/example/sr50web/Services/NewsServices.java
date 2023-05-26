package com.example.sr50web.Services;

import com.example.sr50web.Models.News;
import com.example.sr50web.repo.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServices {

    @Autowired
    private NewsRepository repository;

    public List<News> allNews() { return(List<News>) repository.findAllNews(); }

    public void save(News news) {
        if(repository.findNewsById(news.getId()) != null){
            repository.update(news);
        } else{
            repository.save(news);
        }
    }

    public News get(Integer id) {
        News news = repository.findNewsById(id);
        return news;
    }

    public void delete(Integer id) { repository.delete(id); }
}