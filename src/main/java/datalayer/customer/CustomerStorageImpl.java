package datalayer.customer;

import dto.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerStorageImpl implements CustomerStorage {
    private final String connectionString;
    private final String username;
    private final String password;

    public CustomerStorageImpl(String conStr, String user, String pass) {
        connectionString = conStr;
        username = user;
        password = pass;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public int createCustomer(Customer customer) throws SQLException {
        var sql = "insert into Customers(firstname, lastname, birthdate, phone) values (?, ?, ?, ?)";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getFirstname());
            stmt.setString(2, customer.getLastname());
            stmt.setDate(3, customer.getBirthdate());
            stmt.setString(4, customer.getPhone());

            stmt.executeUpdate();

            // get the newly created id
            try (var resultSet = stmt.getGeneratedKeys()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
    }

    @Override
    public Customer getCustomerWithId(int customerId) throws SQLException, CustomerStorageException {
        var sql = "select ID, firstname, lastname, birthdate, phone from Customers where id = ?";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            try (var resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    var id = resultSet.getInt("ID");
                    var firstname = resultSet.getString("firstname");
                    var lastname = resultSet.getString("lastname");
                    var birthdate = resultSet.getDate("birthdate");
                    var phone = resultSet.getString("phone");

                    return new Customer(id, firstname, lastname, birthdate, phone);
                } else {
                    throw new CustomerStorageException("No customer found with that ID");
                }
            }
        }
    }

    @Override
    public List<Customer> getCustomers() throws SQLException {
        try (var con = getConnection();
             var stmt = con.createStatement()) {
            var results = new ArrayList<Customer>();

            ResultSet resultSet = stmt.executeQuery("select ID, firstname, lastname, birthdate, phone from Customers");

            while (resultSet.next()) {
                var id = resultSet.getInt("ID");
                var firstname = resultSet.getString("firstname");
                var lastname = resultSet.getString("lastname");
                var birthdate = resultSet.getDate("birthdate");
                var phone = resultSet.getString("phone");

                results.add(new Customer(id, firstname, lastname, birthdate, phone));
            }

            return results;
        }
    }
}
