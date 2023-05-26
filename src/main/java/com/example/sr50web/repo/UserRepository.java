package com.example.sr50web.repo;

import com.example.sr50web.repo.IUserRepository;
import com.example.sr50web.Models.*;
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
public class UserRepository implements IUserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    private class UserRowCallBackHandler implements RowCallbackHandler {

        private Map<Integer, User> allUsers = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            String name = resultSet.getString(index++);
            String lastname = resultSet.getString(index++);
            String email = resultSet.getString(index++);
            String password = resultSet.getString(index++);
            String address = resultSet.getString(index++);
            String phone = resultSet.getString(index++);
            String jmbg = resultSet.getString(index++);
            Role role = Role.valueOf(resultSet.getString(index++));
            LocalDateTime registration = resultSet.getTimestamp(index++).toLocalDateTime();
            LocalDate birth = resultSet.getDate(index++).toLocalDate();


            User user = allUsers.get(id);
            if (user == null) {
                user = new User(id, email, name, lastname, password, jmbg, address, phone, role, registration, birth);
                allUsers.put(user.getId(), user);
            }
        }

        public List<User> getUsers() {
            return new ArrayList<>(allUsers.values());
        }

    }
    @Override
    public List<User> findAllUsers() {
        String sql =
                "SELECT temp.id, temp.name, temp.lastname, temp.email, temp.password, temp.address, temp.phone, temp.jmbg, temp.role, temp.registrationdate, temp.birth " +
                        "FROM users temp " +
                        "ORDER BY temp.id";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getUsers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getUsers();
    }

    @Override
    public User findUserById(Integer id) {
        String sql =
                "SELECT temp.id, temp.name, temp.lastname, temp.email, temp.password, temp.address, temp.phone, temp.jmbg, temp.role, temp.registrationdate, temp.birth " +
                        "FROM users temp " +
                        "WHERE temp.id = ? " +
                        "ORDER BY temp.id";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);
        if (rowCallbackHandler.getUsers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getUsers().get(0);
    }

    @Override
    public User FindUserByEmail(String email) {
        String sql =
                "SELECT temp.id, temp.name, temp.lastname, temp.email, temp.password, temp.address, temp.phone, temp.jmbg, temp.role, temp.registrationdate, temp.birth " +
                        "FROM users temp " +
                        "WHERE temp.email = ? " +
                        "ORDER BY temp.id";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email);
        if (rowCallbackHandler.getUsers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getUsers().get(0);
    }

    @Override
    public User findUserWithCred(String email, String sifra) {
        String sql =
                "SELECT temp.id, temp.name, temp.lastname, temp.email, temp.password, temp.address, temp.phone, temp.jmbg, temp.role, temp.registrationdate, temp.birth " +
                        "FROM users temp " +
                        "WHERE temp.email = ? AND " +
                        "temp.password = ? " +
                        "ORDER BY temp.id";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email, sifra);

        if (rowCallbackHandler.getUsers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getUsers().get(0);
    }


    @Transactional
    @Override
    public int save(User user) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO users (name, lastname, email, password, address, phone, jmbg, role, registrationdate, birth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, user.getFirstName());
                preparedStatement.setString(index++, user.getLastName());
                preparedStatement.setString(index++, user.getEmail());
                preparedStatement.setString(index++, user.getPassword());
                preparedStatement.setString(index++, user.getAddress());
                preparedStatement.setString(index++, user.getPhoneNum());
                preparedStatement.setString(index++, user.getJmbg());
                preparedStatement.setString(index++, user.getRole().toString());
                Timestamp registeStamp = Timestamp.valueOf(user.getRegistration());
                preparedStatement.setString(index++, registeStamp.toString());
                java.sql.Date date= Date.valueOf(user.getBirth());
                preparedStatement.setString(index++, date.toString());

                return preparedStatement;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(User user) {
        String sql = "UPDATE users SET name = ?, lastname = ?, email = ?, password = ?, address = ?, phone = ?, jmbg = ?, role = ?, registrationdate = ?, birth = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getAddress(),
                user.getPhoneNum(),
                user.getJmbg(),
                user.getRole().toString(),
                Timestamp.valueOf(user.getRegistration()).toString(),
                user.getBirth().toString(),
                user.getId()) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
