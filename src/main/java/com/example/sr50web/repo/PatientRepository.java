package com.example.sr50web.repo;
import com.example.sr50web.Models.*;
import com.example.sr50web.Models.User;

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
public class PatientRepository implements IPatientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IUserRepository repo;

    private class PatientRowCallbackHandler implements RowCallbackHandler {
        private Map<Integer, Patient> allPatients = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            Boolean vaccinated = resultSet.getBoolean(index++);
            Integer recieved = resultSet.getInt(index++);
            Timestamp lastDose = resultSet.getTimestamp(index++);
            LocalDateTime lastDoseTime = null;

            if(lastDose!=null){
                lastDoseTime = lastDose.toLocalDateTime();
            }
            User user = repo.findUserById(id);


            if (allPatients.get(id) == null) {
                Patient patient = new Patient(id, vaccinated, recieved, lastDoseTime, user);
                allPatients.put(patient.getUserId(), patient);
            }else{
                Patient patient = allPatients.get(id);
            }
        }

        public List<Patient> getPatients() { return new ArrayList<>(allPatients.values()); }
    }

    @Override
    public Patient findPatientById(Integer id) {
        String sql =
                "SELECT temp.userid, temp.vaccinated, temp.received, temp.lastdose " +
                        "FROM patientinfo temp " +
                        "WHERE temp.userid = ? " +
                        "ORDER BY temp.userid";

        PatientRepository.PatientRowCallbackHandler rowCallbackHandler = new PatientRepository.PatientRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);
        if(rowCallbackHandler.getPatients().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getPatients().get(0);
    }

    @Override
    public List<Patient> findAll() {
        String sql =
                "SELECT temp.userid, temp.vaccinated, temp.received, temp.lastdose " +
                        "FROM patientinfo temp " +
                        "ORDER BY temp.userid";

        PatientRepository.PatientRowCallbackHandler rowCallbackHandler = new PatientRepository.PatientRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if(rowCallbackHandler.getPatients().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getPatients();
    }

    @Transactional
    @Override
    public int save(Patient patient) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO patientinfo (vaccinated, received, lastdose,userid) values (?, ?, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                if(patient.getVaccinated() != null){
                    preparedStatement.setBoolean(index++, patient.getVaccinated());
                }else{
                    preparedStatement.setBoolean(index++, false);
                }
                if(patient.getReceived() != 0){
                    preparedStatement.setInt(index++, patient.getReceived());
                }else{
                    preparedStatement.setInt(index++, 1);
                }
                if(patient.getLastdose()!=null){
                    Timestamp timestamp = Timestamp.valueOf(patient.getLastdose());
                    preparedStatement.setString(index++, timestamp.toString());
                }else {
                    preparedStatement.setString(index++, "1000-1-1 1:1:1");
                }
                preparedStatement.setInt(index++, patient.getUserId());

                return preparedStatement;
            }
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Patient patient) {
        String sql = "UPDATE patientinfo SET vaccinated = ?, received = ?, lastdose = ? WHERE userid = ?";
        boolean success = jdbcTemplate.update(sql,
                patient.getVaccinated(),
                patient.getReceived(),
                patient.getLastdose(),
                patient.getUserId()) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM patientinfo WHERE userid = ?";
        return jdbcTemplate.update(sql, id);
    }
}
