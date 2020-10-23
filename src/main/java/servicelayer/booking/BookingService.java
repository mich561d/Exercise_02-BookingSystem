package servicelayer.booking;

import dto.Booking;

import java.sql.Date;
import java.sql.Time;
import java.util.Collection;

public interface BookingService {
    int createBooking(int customerId, int employeeId, Date date, Time start, Time end) throws BookingServiceException;

    Collection<Booking> getBookingsForCustomer(int customerId) throws BookingServiceException;

    Collection<Booking> getBookingsForEmployee(int employeeId) throws BookingServiceException;
}
