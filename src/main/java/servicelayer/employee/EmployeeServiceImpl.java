package servicelayer.employee;

import datalayer.employee.EmployeeStorage;
import datalayer.employee.EmployeeStorageException;
import dto.Employee;

import java.sql.SQLException;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeStorage employeeStorage;

    public EmployeeServiceImpl(EmployeeStorage employeeStorage) {
        this.employeeStorage = employeeStorage;
    }

    @Override
    public int createEmployee(Employee employee) throws EmployeeServiceException {
        try {
            return employeeStorage.createEmployee(employee);
        } catch (SQLException ex) {
            ex.printStackTrace(); // For local testing purpose
            throw new EmployeeServiceException("Unable to create employee..");
        }
    }

    @Override
    public Employee getEmployeeWithId(int employeeId) throws EmployeeServiceException {
        try {
            return employeeStorage.getEmployeeWithId(employeeId);
        } catch (SQLException | EmployeeStorageException ex) {
            ex.printStackTrace(); // For local testing purpose
            throw new EmployeeServiceException("Unable to retrieve employee or there is no employee stored..");
        }
    }
}
