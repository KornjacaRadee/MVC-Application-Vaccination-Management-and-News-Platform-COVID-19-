package com.example.sr50web.repo;

import com.example.sr50web.Models.Infected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InfectedRepository implements IInfectedNewsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class InfectedRowCallBackHandler implements RowCallbackHandler {

        private Map<Integer, Infected> NewsMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            Integer infected = resultSet.getInt(index++);
            Integer tested = resultSet.getInt(index++);
            Integer hospitalized = resultSet.getInt(index++);
            Integer onRespirator = resultSet.getInt(index++);
            Integer infectedAllTime = resultSet.getInt(index++);
            LocalDate dateTime = resultSet.getDate(index++).toLocalDate();

            Infected news = NewsMap.get(id);
            if (news == null) {
                news = new Infected(id, infected, tested, hospitalized, onRespirator, infectedAllTime, dateTime);
                NewsMap.put(news.getId(), news); // dodavanje u kolekciju
            }
        }

        public List<Infected> getNews() {
            return new ArrayList<>(NewsMap.values());
        }

    }

    @Override
    public Infected findInfectedById(Integer id) {
        String sql =
                "SELECT temp.id, temp.infected, temp.tested, temp.hospitalized, temp.respirator, (SELECT totalinfected(temp.id)) AS total_infected, temp.date " +
                        "FROM infectednews temp " +
                        "WHERE temp.id = ? " +
                        "ORDER BY temp.id";

        InfectedRepository.InfectedRowCallBackHandler rowCallbackHandler = new InfectedRepository.InfectedRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (rowCallbackHandler.getNews().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getNews().get(0);
    }

    @Override
    public List<Infected> findAllInfected() {
        String sql =
                "SELECT temp.id, temp.infected, temp.tested, temp.hospitalized, temp.respirator,(SELECT totalinfected(temp.id)) AS total_infected , temp.date " +
                        "FROM infectednews temp " +
                        "ORDER BY temp.id";

        InfectedRepository.InfectedRowCallBackHandler rowCallbackHandler = new InfectedRepository.InfectedRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getNews().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getNews();
    }

    @Transactional
    @Override

    public int save(Infected infected) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO infectednews (infected, tested, hospitalized, respirator, date) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setInt(index++, infected.getInfected());
                preparedStatement.setInt(index++, infected.getTested());
                preparedStatement.setInt(index++, infected.getHospitalized());
                preparedStatement.setInt(index++, infected.getRespirator());
                preparedStatement.setDate(index++, Date.valueOf(infected.getDateTime()));
                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Infected infectedNews) {
        String sql = "UPDATE infectednews SET infected = ?, tested = ?, hospitalized = ?, respirator = ?, date = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                infectedNews.getInfected(),
                infectedNews.getTested(),
                infectedNews.getHospitalized(),
                infectedNews.getRespirator(),
                Timestamp.valueOf(infectedNews.getDateTime().atStartOfDay()),
                infectedNews.getId()) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM infectednews WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
