package com.example.sr50web.Models;
import java.time.LocalDateTime;

public class News {

    private int id;
    private String name;
    private String content;
    private LocalDateTime date;

    public News(int id, String name, String content, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.date = date;
    }

    public News() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
