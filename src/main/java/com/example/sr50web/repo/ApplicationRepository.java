package com.example.sr50web.repo;


import com.example.sr50web.Models.*;

import com.example.sr50web.Services.PatientServices;
import com.example.sr50web.Services.VaccineService;
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
public class ApplicationRepository implements IApplicationRepository {
    @Autowired

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PatientServices patientServices;

    @Autowired
    private VaccineService vaccineService;

    private class ApplicationRowCallBackHandler implements RowCallbackHandler {
        private Map<Integer, Applicat> AllApplication = new LinkedHashMap<>();
        @Override
        public void processRow(ResultSet data) throws SQLException {
            int index = 1;
            Integer id = data.getInt(index++);
            LocalDateTime date = data.getTimestamp(index++).toLocalDateTime();
            Integer patientId = data.getInt(index++);
            Integer vaccinetId = data.getInt(index++);
            Patient patient = patientServices.getPatientById(patientId);
            Vaccine vaccine = vaccineService.get(vaccinetId);
            Applicat temp = AllApplication.get(id);
            if (temp == null) {
                temp = new Applicat(id, date, vaccine,patient);
                AllApplication.put(temp.getId(), temp);
            }
        }
        public List<Applicat> getApplications() {
            return new ArrayList<>(AllApplication.values());
        }
    }

    @Override
    public List<Applicat> findAllApplications() {
        String command =
                "SELECT application.id, application.date, application.patient, application.vaccine " +
                        "FROM applications application " +
                        "ORDER BY application.id";
        ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationRowCallBackHandler();
        jdbcTemplate.query(command, rowCallbackHandler);
        if (rowCallbackHandler.getApplications().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getApplications();
    }

    @Override
    public Applicat findApplicationById(Integer id) {
        String command =
                "SELECT application.id, application.date, application.patient, application.vaccine " +
                        "FROM applications application " +
                        "WHERE application.id = ? " +
                        "ORDER BY application.id";

        ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationRowCallBackHandler();
        jdbcTemplate.query(command, rowCallbackHandler, id);
        if (rowCallbackHandler.getApplications().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getApplications().get(0);
    }
    @Override
    public List<Applicat> findApplicatiosUserId(Integer id) {
        String command =
                "SELECT application.id, application.date, application.patient, application.vaccine " +
                        "FROM applications application " +
                        "WHERE application.patient = ? " +
                        "ORDER BY application.id";

        ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationRowCallBackHandler();
        jdbcTemplate.query(command, rowCallbackHandler, id);
        if (rowCallbackHandler.getApplications().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getApplications();
    }

    @Override
    public List<Applicat> searchApplications(String query) {
        String sql =
                "SELECT v.id, v.date, v.patient, v.vaccine " +
                        "FROM applications v " +
                        "JOIN users m ON v.patient = m.id " +
                        "WHERE m.name LIKE ? OR m.lastname LIKE ? OR m.jmbg LIKE ?";

        ApplicationRepository.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationRepository.ApplicationRowCallBackHandler();
        String likeQuery = "%" + query + "%";
        jdbcTemplate.query(sql, rowCallbackHandler, query, query, query);

        return rowCallbackHandler.getApplications();
    }

    @Transactional
    @Override
    public List<Applicat> findAllApplicationsByUser(String searchTerm){
        String command = "SELECT application.id, application.date, application.patient, application.vaccine " +
                "FROM applications application " +
                "JOIN patients p ON application.patient_id = p.id " +
                "WHERE p.user_name LIKE '%" + searchTerm + "%' " +
                "   OR p.last_name LIKE '%" + searchTerm + "%' " +
                "   OR p.jmbg LIKE '%" + searchTerm + "%' " +
                "ORDER BY application.id";
        ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationRowCallBackHandler();
        jdbcTemplate.query(command, rowCallbackHandler, searchTerm);
        if (rowCallbackHandler.getApplications().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getApplications();

    }
    @Transactional
    @Override
    public int save(Applicat applicat) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String command = "INSERT INTO applications (date, patient,vaccine) VALUES (?, ?, ?)";

                PreparedStatement statement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                Timestamp timestamp = Timestamp.valueOf(applicat.getDateTime());
                statement.setString(index++, timestamp.toString());
                statement.setInt(index++, applicat.getPatient().getUserId());
                statement.setInt(index++, applicat.getVaccine().getId());
                return statement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }
    @Transactional
    @Override
    public int update(Applicat applicat) {
        String command = "UPDATE manufacturer SET date = ?, patient = ?, vaccine = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(command,
                Timestamp.valueOf(applicat.getDateTime()).toString(),
                applicat.getPatient().getUserId(),
                applicat.getVaccine().getId(),
                applicat.getId()) == 1;
        return success ? 1 : 0;
    }
    @Transactional

    @Override
    public int delete(Integer id) {
        String command = "DELETE FROM applications WHERE id = ?";
        return jdbcTemplate.update(command, id);
    }

}


