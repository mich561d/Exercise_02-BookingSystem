package servicelayer.customer;

import dto.Customer;

import java.util.Collection;
import java.sql.Date;

public interface CustomerService {
    int createCustomer(String firstName, String lastName, Date birthdate, String phone) throws CustomerServiceException;

    Customer getCustomerById(int id) throws CustomerServiceException;

    Collection<Customer> getCustomers() throws CustomerServiceException;
}
