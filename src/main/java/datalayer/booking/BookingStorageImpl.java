package datalayer.booking;

import dto.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class BookingStorageImpl implements BookingStorage {
    private final String connectionString, username, password;

    public BookingStorageImpl(String conStr, String user, String pass) {
        connectionString = conStr;
        username = user;
        password = pass;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public int createBooking(Booking booking) throws SQLException {
        var sql = "insert into Bookings(customerId, employeeId, date, start, end) values (?, ?, ?, ?, ?)";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getCustomerId());
            stmt.setInt(2, booking.getEmployeeId());
            stmt.setDate(3, booking.getDate());
            stmt.setTime(4, booking.getStart());
            stmt.setTime(5, booking.getEnd());

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
    public Collection<Booking> getBookingsForCustomer(int customerId) throws SQLException {
        var sql = "select ID, employeeId, date, start, end from Bookings where customerId = ?";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet resultSet = stmt.executeQuery();

            var results = new ArrayList<Booking>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int employeeId = resultSet.getInt("employeeId");
                Date date = resultSet.getDate("date");
                Time start = resultSet.getTime("start");
                Time end = resultSet.getTime("end");

                Booking b = new Booking(id, customerId, employeeId, date, start, end);
                results.add(b);
            }

            return results;
        }
    }

    @Override
    public Collection<Booking> getBookingsForEmployee(int employeeId) throws SQLException {
        var sql = "select ID, customerId, date, start, end from Bookings where employeeId = ?";
        try (var con = getConnection();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet resultSet = stmt.executeQuery();

            var results = new ArrayList<Booking>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int customerId = resultSet.getInt("customerId");
                Date date = resultSet.getDate("date");
                Time start = resultSet.getTime("start");
                Time end = resultSet.getTime("end");

                Booking b = new Booking(id, customerId, employeeId, date, start, end);
                results.add(b);
            }

            return results;
        }
    }
}
