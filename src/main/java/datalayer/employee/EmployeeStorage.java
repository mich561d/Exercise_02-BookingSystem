package datalayer.employee;


import dto.Employee;

import java.sql.SQLException;

public interface EmployeeStorage {
    int createEmployee(Employee employee) throws SQLException;

    Employee getEmployeeWithId(int employeeId) throws SQLException, EmployeeStorageException;
}
