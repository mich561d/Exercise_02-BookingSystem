package datalayer.employee;

import dto.Employee;

import java.sql.*;

public class EmployeeStorageImpl implements EmployeeStorage {
    private final String connectionString;
    private final String username;
    private final String password;

    public EmployeeStorageImpl(String conStr, String user, String pass) {
        connectionString = conStr;
        username = user;
        password = pass;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public int createEmployee(Employee employee) throws SQLException {
        var sql = "insert into Employees(firstname, lastname, birthdate) values (?, ?, ?)";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employee.getFirstname());
            stmt.setString(2, employee.getLastname());
            stmt.setDate(3, employee.getBirthdate());

            stmt.executeUpdate();

            // get the newly created id
            try (var resultSet = stmt.getGeneratedKeys()) {
                resultSet.next();
                int newId = resultSet.getInt(1);
                return newId;
            }
        }
    }

    @Override
    public Employee getEmployeeWithId(int employeeId) throws SQLException, EmployeeStorageException {
        var sql = "select firstname, lastname, birthdate from Employees where ID = ?";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                Date birthdate = resultSet.getDate("birthdate");

                return new Employee(employeeId, firstname, lastname, birthdate);
            } else {
                throw new EmployeeStorageException("No customer found with that ID");
            }
        }
    }
}
