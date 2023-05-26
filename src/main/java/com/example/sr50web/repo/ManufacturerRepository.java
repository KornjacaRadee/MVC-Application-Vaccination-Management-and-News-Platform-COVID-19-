package com.example.sr50web.repo;


import com.example.sr50web.Models.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;

    @Repository
    public class ManufacturerRepository implements IManufaturerRepository {
        @Autowired

        private JdbcTemplate jdbcTemplate;

        private class ManufacturerRowCallBackHandler implements RowCallbackHandler {
            private Map<Integer, Manufacturer> AllManufacturers = new LinkedHashMap<>();
            @Override
            public void processRow(ResultSet data) throws SQLException {
                int index = 1;
                Integer id = data.getInt(index++);
                String name = data.getString(index++);
                String country = data.getString(index++);
                Manufacturer temp = AllManufacturers.get(id);
                if (temp == null) {
                    temp = new Manufacturer(id, name, country);
                    AllManufacturers.put(temp.getId(), temp);
                }
            }
            public List<Manufacturer> getManufacturers() {
                return new ArrayList<>(AllManufacturers.values());
            }
        }

        @Override
        public List<Manufacturer> findAllManufacturers() {
            String command =
                    "SELECT manufacturer.id, manufacturer.name, manufacturer.country " +
                            "FROM manufacturer manufacturer " +
                            "ORDER BY manufacturer.id";
            ManufacturerRowCallBackHandler rowCallbackHandler = new ManufacturerRowCallBackHandler();
            jdbcTemplate.query(command, rowCallbackHandler);
            if (rowCallbackHandler.getManufacturers().size() == 0) {
                return null;
            }
            return rowCallbackHandler.getManufacturers();
        }
        @Override
        public Manufacturer findManucaturerById(Integer id) {
            String command =
                    "SELECT manufacturer.id, manufacturer.name, manufacturer.country " +
                            "FROM manufacturer manufacturer " +
                            "WHERE manufacturer.id = ? " +
                            "ORDER BY manufacturer.id";

            ManufacturerRowCallBackHandler rowCallbackHandler = new ManufacturerRowCallBackHandler();
            jdbcTemplate.query(command, rowCallbackHandler, id);
            if (rowCallbackHandler.getManufacturers().size() == 0) {
                return null;
            }
            return rowCallbackHandler.getManufacturers().get(0);
        }
        @Transactional
        @Override
        public int save(Manufacturer manufacturer) {
            PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String command = "INSERT INTO manufacturer (name, country) VALUES (?, ?)";

                    PreparedStatement statement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
                    int index = 1;
                    statement.setString(index++, manufacturer.getName());
                    statement.setString(index++, manufacturer.getCountry());
                    return statement;
                }

            };
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
            return success ? 1 : 0;
        }
        @Transactional
        @Override
        public int update(Manufacturer newManufacturer) {
            String command = "UPDATE manufacturer SET name = ?, country = ? WHERE id = ?";
            boolean success = jdbcTemplate.update(command,
                    newManufacturer.getName(),
                    newManufacturer.getCountry(),
                    newManufacturer.getId()) == 1;
            return success ? 1 : 0;
        }
        @Transactional
        @Override
        public int delete(int id) {
            String command = "DELETE FROM manufacturer WHERE id = ?";
            return jdbcTemplate.update(command, id);
        }

    }


