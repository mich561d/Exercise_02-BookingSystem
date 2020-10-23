package datalayer.customer;

import dto.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerStorage {
    int createCustomer(Customer customer) throws SQLException;

    Customer getCustomerWithId(int customerId) throws SQLException, CustomerStorageException;

    List<Customer> getCustomers() throws SQLException;
}
