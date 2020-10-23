package servicelayer.booking;

import datalayer.booking.BookingStorage;
import datalayer.customer.CustomerStorage;
import datalayer.customer.CustomerStorageException;
import dto.Booking;
import dto.SmsMessage;
import servicelayer.notifications.SmsService;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Collection;

public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final SmsService smsService;
    private final CustomerStorage customerStorage;


    public BookingServiceImpl(BookingStorage bookingStorage, SmsService smsService, CustomerStorage customerStorage) {
        this.bookingStorage = bookingStorage;
        this.smsService = smsService;
        this.customerStorage = customerStorage;
    }

    private String createSmsMsg(int customerId, Date date, int bookingId) {
        String msg = "Hello Customer!\nYour ID is: %d\nA booking is placed and the booking ID is: %d\nStay well until %tF";
        return String.format(msg, customerId, bookingId, date);
    }

    @Override
    public int createBooking(int customerId, int employeeId, Date date, Time start, Time end) throws BookingServiceException {
        try {
            int bookingId = bookingStorage.createBooking(new Booking(customerId, employeeId, date, start, end));
            String phone = customerStorage.getCustomerWithId(customerId).getPhone();
            smsService.sendSms(new SmsMessage(phone, createSmsMsg(customerId, date, bookingId)));
            return bookingId;
        } catch (SQLException | CustomerStorageException ex) {
            ex.printStackTrace(); // For local testing purpose
            throw new BookingServiceException("Unable to create booking..");
        }
    }

    @Override
    public Collection<Booking> getBookingsForCustomer(int customerId) throws BookingServiceException {
        try {
            return bookingStorage.getBookingsForCustomer(customerId);
        } catch (SQLException ex) {
            ex.printStackTrace(); // For local testing purpose
            throw new BookingServiceException("Unable to retrieve bookings or there is no booking stored..");
        }
    }

    @Override
    public Collection<Booking> getBookingsForEmployee(int employeeId) throws BookingServiceException {
        try {
            return bookingStorage.getBookingsForEmployee(employeeId);
        } catch (SQLException ex) {
            ex.printStackTrace(); // For local testing purpose
            throw new BookingServiceException("Unable to retrieve bookings or there is no booking stored..");
        }
    }
}
