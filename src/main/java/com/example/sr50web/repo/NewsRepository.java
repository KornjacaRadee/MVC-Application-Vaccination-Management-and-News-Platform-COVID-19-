package com.example.sr50web.repo;

import com.example.sr50web.Models.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NewsRepository implements INewsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class NewsRowCallBackHandler implements RowCallbackHandler {

        private Map<Integer, News> allNews = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            String name = resultSet.getString(index++);
            String content = resultSet.getString(index++);
            LocalDateTime dateTime = resultSet.getTimestamp(index++).toLocalDateTime();
            News news = allNews.get(id);
            if (news == null) {
                news = new News(id, name, content, dateTime);
                allNews.put(news.getId(), news);
            }
        }

        public List<News> getNews() {
            return new ArrayList<>(allNews.values());
        }

    }

    @Override
    public News findNewsById(Integer id) {
        String sql =
                "SELECT temp.id, temp.name, temp.content, temp.postedon " +
                        "FROM news temp " +
                        "WHERE temp.id = ? ";

        NewsRepository.NewsRowCallBackHandler rowCallbackHandler = new NewsRepository.NewsRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (rowCallbackHandler.getNews().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getNews().get(0);
    }

    @Override
    public List<News> findAllNews() {
        String sql =
                "SELECT temp.id, temp.name, temp.content, temp.postedon " +
                        "FROM news temp ";

        NewsRepository.NewsRowCallBackHandler rowCallbackHandler = new NewsRepository.NewsRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getNews().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getNews();
    }

    @Transactional
    @Override
    public int save(News news) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO news (name, content, postedon) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, news.getName());
                preparedStatement.setString(index++, news.getContent());
                Timestamp timestamp = Timestamp.valueOf(news.getDate());
                preparedStatement.setString(index++, timestamp.toString());
                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(News news) {
        String sql = "UPDATE news SET name = ?, content = ?, postedon = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                news.getName(),
                news.getContent(),
                Timestamp.valueOf(news.getDate()).toString(),
                news.getId()) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM news WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
