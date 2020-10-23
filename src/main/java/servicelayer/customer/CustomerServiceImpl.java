package servicelayer.customer;

import datalayer.customer.CustomerStorage;
import datalayer.customer.CustomerStorageException;
import dto.Customer;

import java.sql.SQLException;
import java.util.Collection;
import java.sql.Date;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerStorage customerStorage;

    public CustomerServiceImpl(CustomerStorage customerStorage) {
        this.customerStorage = customerStorage;
    }

    @Override
    public int createCustomer(String firstName, String lastName, Date birthdate, String phone) throws CustomerServiceException {
        try {
            return customerStorage.createCustomer(new Customer(firstName, lastName, birthdate, phone));
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new CustomerServiceException("Unable to create customer..");
        }
    }

    @Override
    public Customer getCustomerById(int id) throws CustomerServiceException {
        try {
            return customerStorage.getCustomerWithId(id);
        } catch (SQLException | CustomerStorageException ex) {
            ex.printStackTrace();
            throw new CustomerServiceException("Unable to retrieve customer or there is no customer stored..");
        }
    }

    @Override
    public Collection<Customer> getCustomers() throws CustomerServiceException {
        try {
            return customerStorage.getCustomers();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new CustomerServiceException("Unable to retrieve customers..");
        }
    }
}
