package com.example.sr50web.repo;

import com.example.sr50web.Models.Manufacturer;
import com.example.sr50web.Models.Vaccine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VaccineRepository implements IVaccineRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ManufacturerRepository manufacturerRepo;

    private class VaccineRowCallbackHandler implements RowCallbackHandler {

        private Map<Integer, Vaccine> vaccines = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            String name = resultSet.getString(index++);
            Integer availableNum = resultSet.getInt(index++);
            Integer manufacturerId = resultSet.getInt(index++);

            Manufacturer manufacturer = manufacturerRepo.findManucaturerById(manufacturerId);

            Vaccine vaccine = vaccines.get(id);
            if (vaccine == null) {
                vaccine = new Vaccine(id, name, availableNum, manufacturer);
                vaccines.put(vaccine.getId(), vaccine);
            }
        }

        public List<Vaccine> getVaccines() { return new ArrayList<>(vaccines.values()); }
    }
    @Override
    public List<Vaccine> searchVaccines(String query) {
        String sql =
                "SELECT temp.id, temp.name, temp.available, temp.manufacturer " +
                        "FROM vaccine temp " +
                        "WHERE temp.name LIKE ? OR temp.available LIKE ? OR temp.manufacturer LIKE ? " +
                        "ORDER BY temp.id";

        VaccineRowCallbackHandler rowCallbackHandler = new VaccineRowCallbackHandler();
        String likeQuery = "%" + query + "%";
        jdbcTemplate.query(sql, rowCallbackHandler, likeQuery, likeQuery, likeQuery);

        return rowCallbackHandler.getVaccines();
    }
    @Override
    public List<Vaccine> findSortedVaccines(String sort, String direction) {
        String sql = "SELECT temp.id, temp.name, temp.available, temp.manufacturer " +
                "FROM vaccine temp " +
                "ORDER BY " + sort + " " + direction;

        VaccineRowCallbackHandler rowCallbackHandler = new VaccineRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getVaccines().isEmpty()) {
            return null;
        }
        return rowCallbackHandler.getVaccines();
    }
    @Override
    public Vaccine findVaccineById(Integer id) {
        String sql =
                "SELECT temp.id, temp.name, temp.available, temp.manufacturer " +
                        "FROM vaccine temp " +
                        "WHERE temp.id = ? " +
                        "ORDER BY temp.id";

        VaccineRowCallbackHandler rowCallbackHandler = new VaccineRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if(rowCallbackHandler.getVaccines().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getVaccines().get(0);
    }

    @Override
    public List<Vaccine> findVaccineByManufacturer(Manufacturer manufacturer) {
        String sql =
                "SELECT temp.id, temp.name, temp.available, temp.manufacturer   " +
                        " FROM vaccine temp " +
                        "WHERE temp.manufacturer = ? " +
                        "ORDER BY temp.id";

        VaccineRowCallbackHandler rowCallbackHandler = new VaccineRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, manufacturer.getId());

        if(rowCallbackHandler.getVaccines().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getVaccines();
    }

    @Override
    public List<Vaccine> findAllVaccines() {
        String sql =
                "SELECT temp.id, temp.name, temp.available, temp.manufacturer " +
                        "FROM vaccine temp " +
                        "ORDER BY temp.id";

        VaccineRowCallbackHandler rowCallbackHandler = new VaccineRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if(rowCallbackHandler.getVaccines().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getVaccines();
    }

    @Override
    public List<Vaccine> findAllVaccinesQuanty() {
        String sql = "SELECT temp.id, temp.name, temp.available, temp.manufacturer " +
                "FROM vaccine temp " +
                "WHERE temp.available > 0 " +
                "ORDER BY temp.id";

        VaccineRowCallbackHandler rowCallbackHandler = new VaccineRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if(rowCallbackHandler.getVaccines().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getVaccines();
    }

    @Transactional
    @Override
    public int save(Vaccine vaccine) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO vaccine (name, available, manufacturer) values (?, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, vaccine.getName());
                preparedStatement.setInt(index++, vaccine.getAvailable());
                preparedStatement.setInt(index, vaccine.getManufacturer().getId());

                return preparedStatement;
            }
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return  success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Vaccine vaccine) {
        String sql = "UPDATE vaccine SET name = ?, available = ?, manufacturer = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                vaccine.getName(),
                vaccine.getAvailable(),
                vaccine.getManufacturer().getId(),
                vaccine.getId()) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM vaccine WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

