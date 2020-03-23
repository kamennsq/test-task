package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.exception.ImpossibleToPerformOperation;
import com.haulmont.testtask.exception.doctor.ImpossibleToDeleteDoctor;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAOImpl implements DoctorDAO {

    @Override
    public List<Doctor> getDoctors() {
        List<Doctor> list = new ArrayList<>();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Doctor");
            ResultSet rs  = ps.executeQuery();
            while (rs.next()){
                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("Id"));
                doctor.setName(rs.getString("Name"));
                doctor.setSurname(rs.getString("Surname"));
                doctor.setPatronymic(rs.getString("Patronymic"));
                doctor.setSpecialization(rs.getString("Specialization"));
                list.add(doctor);
            }
            return list;
        }
        catch (SQLException e){
            throw new ImpossibleToPerformOperation();
        }
    }

    @Override
    public void insertDoctor(Doctor doctor) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("insert into Doctor values (next value for idSequence, ?, ?, ?, ?)");
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSurname());
            ps.setString(3, doctor.getPatronymic());
            ps.setString(4, doctor.getSpecialization());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new ImpossibleToPerformOperation();
        }
    }

    @Override
    public Doctor getDoctorById(Long id) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Doctor where id = ?");
            ps.setLong(1, id);

            ResultSet rs  = ps.executeQuery();
            Doctor doctor = new Doctor();
            while (rs.next()){
                doctor.setId(rs.getLong("Id"));
                doctor.setName(rs.getString("Name"));
                doctor.setSurname(rs.getString("Surname"));
                doctor.setPatronymic(rs.getString("Patronymic"));
                doctor.setSpecialization(rs.getString("Specialization"));
            }
            return doctor;
        }
        catch (SQLException e){
            throw new ImpossibleToPerformOperation();
        }
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("merge into Doctor doc " +
                    "using (select Id from Doctor where Id = ?) a " +
                    "on doc.Id = a.Id " +
                    "when matched then update set " +
                    "Name = ?, " +
                    "Surname = ?, " +
                    "Patronymic = ?, " +
                    "Specialization = ?");
            ps.setLong(1, doctor.getId());
            ps.setString(2, doctor.getName());
            ps.setString(3, doctor.getSurname());
            ps.setString(4, doctor.getPatronymic());
            ps.setString(5, doctor.getSpecialization());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new ImpossibleToPerformOperation();
        }
    }

    @Override
    public void deleteDoctor(Doctor doctor) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("delete from Doctor where Id = ?");
            ps.setLong(1, doctor.getId());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new ImpossibleToDeleteDoctor();
        }
    }

    @Override
    public List<String> buildStatistics() {
        List<String> list = new ArrayList<>();
        try{
            PreparedStatement oldestDate = MyConnection.connection.prepareStatement("select min(CreationDate) as result from Prescription");
            ResultSet oldestDateResult = oldestDate.executeQuery();
            oldestDateResult.next();
            PreparedStatement newestDate = MyConnection.connection.prepareStatement("select max(CreationDate) as result from Prescription");
            ResultSet newestDateResult = newestDate.executeQuery();
            newestDateResult.next();
            PreparedStatement ps = MyConnection.connection.prepareStatement("select b.Name, a.CreationDate ,count(a.Id) as count " +
                    "from Prescription a " +
                    "INNER JOIN Doctor b " +
                    "ON a.Doctor = b.Id " +
                    "where a.CreationDate between ? and ? " +
                    "group by a.CreationDate, b.Id");
            ps.setDate(1, oldestDateResult.getDate("result"));
            ps.setDate(2, newestDateResult.getDate("result"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String result = rs.getString("Name") + " " + rs.getInt("count") + " " + rs.getString("CreationDate");
                System.out.println(result);
                list.add(result);
            }
            return list;
        }
        catch (SQLException e){
            //throw new ImpossibleToPerformOperation();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Date> datesList() {
        List<Date> list = new ArrayList<>();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select CreationDate from Prescription " +
                    "group by CreationDate " +
                    "order by CreationDate ASC");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(rs.getDate("CreationDate"));
            }
            return list;
        }
        catch (SQLException e){
            throw new ImpossibleToPerformOperation();
        }
    }

}
