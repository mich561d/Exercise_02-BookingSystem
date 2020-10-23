package datalayer.booking;

import dto.Booking;

import java.sql.SQLException;
import java.util.Collection;

public interface BookingStorage {
    int createBooking(Booking booking) throws SQLException;

    Collection<Booking> getBookingsForCustomer(int customerId) throws SQLException;

    Collection<Booking> getBookingsForEmployee(int employeeId) throws SQLException;
}
