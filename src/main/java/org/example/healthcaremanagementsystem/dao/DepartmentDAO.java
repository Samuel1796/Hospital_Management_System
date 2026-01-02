package org.example.healthcaremanagementsystem.dao;

import org.example.healthcaremanagementsystem.model.Department;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Department entity.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public interface DepartmentDAO {
    Department create(Department department) throws Exception;
    Optional<Department> findById(Integer departmentId) throws Exception;
    List<Department> findAll() throws Exception;
    boolean update(Department department) throws Exception;
    boolean delete(Integer departmentId) throws Exception;
}

