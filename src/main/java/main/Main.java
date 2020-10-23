package main;

import datalayer.booking.BookingStorage;
import datalayer.booking.BookingStorageImpl;
import datalayer.customer.CustomerStorage;
import datalayer.customer.CustomerStorageImpl;
import datalayer.employee.EmployeeStorage;
import datalayer.employee.EmployeeStorageImpl;
import dto.Booking;
import dto.Customer;
import dto.Employee;
import servicelayer.booking.BookingService;
import servicelayer.booking.BookingServiceImpl;
import servicelayer.customer.CustomerService;
import servicelayer.customer.CustomerServiceImpl;
import servicelayer.employee.EmployeeService;
import servicelayer.employee.EmployeeServiceImpl;
import servicelayer.notifications.SmsService;
import servicelayer.notifications.SmsServiceImpl;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Main {

    private static final String conStr = "jdbc:mysql://localhost:3306/booking_system";
    private static final String user = "dev";
    private static final String pass = "ax2";

    public static void main(String[] args) throws Exception {
        System.out.println("Starting...");

        System.out.println("Setting up storages...");
        CustomerStorage customerStorage = new CustomerStorageImpl(conStr, user, pass);
        EmployeeStorage employeeStorage = new EmployeeStorageImpl(conStr, user, pass);
        BookingStorage bookingStorage = new BookingStorageImpl(conStr, user, pass);
        System.out.println("Storages created!");

        System.out.println("Setting up services...");
        CustomerService customerService = new CustomerServiceImpl(customerStorage);
        EmployeeService employeeService = new EmployeeServiceImpl(employeeStorage);
        SmsService smsService = new SmsServiceImpl();
        BookingService bookingService = new BookingServiceImpl(bookingStorage, smsService, customerStorage);
        System.out.println("Services created!");

        System.out.println("Setting up test data... - Customers");
        customerService.createCustomer("John", "Doe", Date.valueOf("1987-05-27"), "+45 43 76 34 76");
        customerService.createCustomer("Jane", "Doe", Date.valueOf("1992-03-16"), "+45 12 54 76 87");
        customerService.createCustomer("Hanne", "Hansen", Date.valueOf("1985-11-06"), "+45 89 54 23 67");
        System.out.println("Customer test data is created!");

        System.out.println("Setting up test data... - Employees");
        employeeService.createEmployee(new Employee("Kristian", "Gedde", Date.valueOf("1989-08-30")));
        employeeService.createEmployee(new Employee("Susanne", "Vetter", Date.valueOf("1993-01-22")));
        System.out.println("Employee test data is created!");

        System.out.println("Retrieving test data... - Customers");
        Collection<Customer> customers = customerService.getCustomers();
        System.out.println("Customers is retrieved!");

        System.out.println("Retrieving test data... - Employees");
        Employee e1 = employeeService.getEmployeeWithId(9);
        Employee e2 = employeeService.getEmployeeWithId(10);
        ArrayList<Employee> employees = new ArrayList();
        employees.add(e1);
        employees.add(e2);
        System.out.println("Employees is retrieved!");

        System.out.println("Creating bookings...");
        for (Customer c : customers) {
            int employeeId = new Random().nextBoolean() ? e1.getId() : e2.getId();
            bookingService.createBooking(c.getId(), employeeId, Date.valueOf("2020-12-24"), Time.valueOf("06:00:00"), Time.valueOf("23:59:59"));
        }
        System.out.println("Bookings created!");

        System.out.println("Printing all customer data...");
        System.out.println("----------------------");
        for (Customer c : customers) {
            System.out.println(toString(c));
            Collection<Booking> bookings = bookingService.getBookingsForCustomer(c.getId());
            for (Booking b : bookings) {
                System.out.println(toString(b));
            }
            System.out.println("----------------------");
        }
        System.out.println("Printing all employee data...");
        System.out.println("----------------------");
        for (Employee e : employees) {
            System.out.println(toString(e));
            Collection<Booking> bookings = bookingService.getBookingsForEmployee(e.getId());
            for (Booking b : bookings) {
                System.out.println(toString(b));
            }
            System.out.println("----------------------");
        }
        System.out.println("All data has been printed!");
        System.out.println("The end!");
    }

    public static String toString(Customer c) {
        String msg = "{%d, %s, %s}";
        return String.format(msg, c.getId(), c.getFirstname(), c.getLastname());
    }

    public static String toString(Employee e) {
        String msg = "{%d, %s, %s}";
        return String.format(msg, e.getId(), e.getFirstname(), e.getLastname());
    }

    public static String toString(Booking b) {
        String msg = "{%d, %d, %d, %tF, %tT, %tT}";
        return String.format(msg, b.getId(), b.getCustomerId(), b.getEmployeeId(), b.getDate(), b.getStart(), b.getEnd());
    }
}
