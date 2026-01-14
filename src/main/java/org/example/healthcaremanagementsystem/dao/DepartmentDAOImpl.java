package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.config.DatabaseConfig;
import org.example.healthcaremanagementsystem.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of DepartmentDAO interface.
 */
public class DepartmentDAOImpl implements DepartmentDAO {

    private final DatabaseConfig dbConfig;

    public DepartmentDAOImpl() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    @Override
    public Department create(Department department) throws Exception {
        String sql = "INSERT INTO departments (department_name, description, location, " +
                "phone_number, head_doctor_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, department.getDepartmentName());
            pstmt.setString(2, department.getDescription());
            pstmt.setString(3, department.getLocation());
            pstmt.setString(4, department.getPhoneNumber());
            pstmt.setString(5, department.getHeadDoctorId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating department failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    department.setDepartmentId(generatedKeys.getInt(1));
                    return department;
                } else {
                    throw new SQLException("Creating department failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public Optional<Department> findById(Integer departmentId) throws Exception {
        String sql = "SELECT * FROM departments WHERE department_id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, departmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDepartment(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Department> findAll() throws Exception {
        String sql = "SELECT * FROM departments ORDER BY department_name";
        List<Department> departments = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                departments.add(mapResultSetToDepartment(rs));
            }
        }
        return departments;
    }

    @Override
    public boolean update(Department department) throws Exception {
        String sql = "UPDATE departments SET department_name = ?, description = ?, " +
                "location = ?, phone_number = ?, head_doctor_id = ? WHERE department_id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department.getDepartmentName());
            pstmt.setString(2, department.getDescription());
            pstmt.setString(3, department.getLocation());
            pstmt.setString(4, department.getPhoneNumber());
            pstmt.setString(5, department.getHeadDoctorId());
            pstmt.setInt(6, department.getDepartmentId());

            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Integer departmentId) throws Exception {
        String sql = "DELETE FROM departments WHERE department_id = ?";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, departmentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Department mapResultSetToDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setDepartmentId(rs.getInt("department_id"));
        dept.setDepartmentName(rs.getString("department_name"));
        dept.setDescription(rs.getString("description"));
        dept.setLocation(rs.getString("location"));
        dept.setPhoneNumber(rs.getString("phone_number"));
        dept.setHeadDoctorId(rs.getString("head_doctor_id"));
        return dept;
    }
}
