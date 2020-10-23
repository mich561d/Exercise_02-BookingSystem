package servicelayer.employee;


import dto.Employee;

public interface EmployeeService {
    int createEmployee(Employee employee) throws EmployeeServiceException;

    Employee getEmployeeWithId(int employeeId) throws EmployeeServiceException;
}
