package tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.BookingState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBookingState
{

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Testing findBookingByNumber 0 with no bookings added")
    void findBookingByNumber0InitialTest()
    {
        BookingState bookingState = new BookingState();
        assertNull(bookingState.findBookingByNumber(0),
                "The initial configuration should not have any bookings");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Testing findBookingByNumber 1 with no bookings added")
    void findBookingByNumber1InitialTest()
    {
        BookingState bookingState = new BookingState();
        assertNull(bookingState.findBookingByNumber(1),
                "The initial configuration should not have any bookings");
        System.out.println("Test passed successfully!");
    }

    // Create several consumers used for testing
    Consumer consumer1 = new Consumer("Consumer1",
            "consumer1@email.com", "69420666", "b", "z");
    Consumer consumer2 = new Consumer("Consumer2",
            "consumer2@email.com", "694201337", "s", "h");

    // Create several providers used for testing
    EntertainmentProvider provider1 = new EntertainmentProvider("a",
            "b", "c", "d",
            "f", "g", new ArrayList<>(), new ArrayList<>());
    EntertainmentProvider provider2 = new EntertainmentProvider("d",
            "c", "c", "d",
            "x", "l", new ArrayList<>(), new ArrayList<>());

    // Create several events used for testing
    Event event1 = new NonTicketedEvent(0, provider1, "g", EventType.Movie);
    Event event2 = new NonTicketedEvent(1, provider2, "t", EventType.Dance);

    // Create several performances used for testing
    EventPerformance performance1 = new EventPerformance(0, event1,
            "d",
            LocalDateTime.now().plusMonths(1),
            LocalDateTime.now().plusMonths(1).plusHours(8),
            List.of("Everyone in disc throw and 400m sprint"),
            false,
            true,
            true,
            3000,
            3000
    );
    EventPerformance performance2 = new EventPerformance(0, event2,
            "h",
            LocalDateTime.now().plusMonths(2),
            LocalDateTime.now().plusMonths(2).plusHours(8),
            List.of("Everyone in disc throw and 400m sprint"),
            false,
            false,
            false,
            3000,
            3000
    );

    @Test
    @DisplayName("Testing booking number -2 with one booking added")
    void findBookingByNumberTooSmallTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        assertNull(bookingState.findBookingByNumber(-2),
                "No bookings with invalid numbers should be added.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Testing booking number 0 with one booking added")
    void findBookingByNumberRightOutsideTest1()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        assertNull(bookingState.findBookingByNumber(0),
                "No bookings with invalid numbers should be added.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Testing booking number 2 with one booking added")
    void findBookingByNumberRightOutsideTest2()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        assertNull(bookingState.findBookingByNumber(2),
                "No bookings with invalid numbers should be added.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Testing booking number 5 with one booking added")
    void findBookingByNumberTooLargeTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        assertNull(bookingState.findBookingByNumber(5),
                "No bookings with invalid numbers should be added.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Testing booking number 1 with one booking added")
    void findBookingByNumberCorrectTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        assertNotEquals(null, bookingState.findBookingByNumber(1),
                "There is a booking with number 1, but it was not found.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking checks for invalid bookers.")
    void invalidBookingCheck1()
    {
        BookingState bookingState = new BookingState();
        Booking booking = bookingState.createBooking(null, performance1, 1, 0);
        assertNull(booking, "A booking with a null booker should not be accepted.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking checks for invalid performances.")
    void invalidBookingCheck2()
    {
        BookingState bookingState = new BookingState();
        Booking booking = bookingState.createBooking(consumer1, null, 1, 0);
        assertNull(booking,
                "A booking with a null booker should not be accepted.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking checks for multiple invalid details.")
    void invalidBookingCheck3()
    {
        BookingState bookingState = new BookingState();
        Booking booking = bookingState.createBooking(null, null, 1, 0);
        assertNull(booking,
                "A booking with a null booker should not be accepted.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking checks for negative tickets.")
    void invalidBookingCheck4()
    {
        BookingState bookingState = new BookingState();
        Booking booking = bookingState.createBooking(consumer1, performance1, -1, 0);
        assertNull(booking,
                "A booking with a negative tickets should not be accepted.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking adds the correct event performance.")
    void createBookingEventPerformanceTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        Booking actual = bookingState.findBookingByNumber(1);
        assertEquals(performance1, actual.getEventPerformance(),
                "The performance in this booking does not match the expected event performance.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking adds the correct booker.")
    void createBookingBookerTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        Booking actual = bookingState.findBookingByNumber(1);
        assertEquals(consumer1, actual.getBooker(),
                "The consumer(booker) in this booking" +
                        " does not match the expected booker.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking adds the correct number of tickets.")
    void createBookingTicketsTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        Booking actual = bookingState.findBookingByNumber(1);
        assertEquals(1, actual.getNumTickets(),
                "The number of tickets in this booking" +
                        " does not match the expected number of tickets.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking adds the correct amount paid.")
    void createBookingAmountPaidTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        Booking actual = bookingState.findBookingByNumber(1);
        assertEquals(0, actual.getAmountPaid(),
                "The amount paid in this booking" +
                        " does not match the expected amount paid.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking with two different performances.")
    void createBookingTwoPerformancesTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        bookingState.createBooking(consumer2, performance2, 4, 0);
        Booking actual1 = bookingState.findBookingByNumber(1);
        Booking actual2 = bookingState.findBookingByNumber(2);
        assertNotEquals(actual1, actual2,
                "The bookings should be different for different performances.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking with two same performances.")
    void createBookingOnePerformanceTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        bookingState.createBooking(consumer1, performance1, 1, 0);
        Booking actual1 = bookingState.findBookingByNumber(1);
        Booking actual2 = bookingState.findBookingByNumber(2);
        assertNotEquals(actual1, actual2,
                "Two bookings should be different even for the same performance.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test createBooking with two performances and one booker.")
    void createBookingOneBookerTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        bookingState.createBooking(consumer1, performance2, 1, 0);
        // The bookings are expected to be numbered from 1
        Booking actual1 = bookingState.findBookingByNumber(1);
        Booking actual2 = bookingState.findBookingByNumber(2);
        assertEquals(actual1.getBooker(), actual2.getBooker(),
                "Two performances booked by the same booker do not have the same booker added.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test findBookingByEventNumber without any bookings added.")
    void findBookingByEventNumberInitialTest1()
    {
        BookingState bookingState = new BookingState();
        assertEquals(0,
                bookingState.findBookingsByEventNumber(0).size(),
                "There should be no booking added at the initial stage.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test findBookingByEventNumber without any bookings added.")
    void findBookingByEventNumberInitialTest2()
    {
        BookingState bookingState = new BookingState();
        assertEquals(0,
                bookingState.findBookingsByEventNumber(1).size(),
                "There should be no booking added at the initial stage.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test findBookingByEventNumber with event number too small.")
    void findBookingByEventNumberTooSmallTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 2, 0);
        assertEquals(0,
                bookingState.findBookingsByEventNumber(performance1.getEvent().getEventNumber() - 5).size(),
                "Bookings with a non-existing event number were found.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test findBookingByEventNumber with event number right outside the correct one.")
    void findBookingByEventRightOutsideTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 2, 0);
        assertEquals(0,
                bookingState.findBookingsByEventNumber(performance1.getEvent().getEventNumber() + 1).size(),
                "Bookings with a non-existing event number were found.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test findBookingByEventNumber with one booking added" +
            " and an existing correct event number.")
    void findBookingByEventCorrectTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 2, 0);
        assertEquals(1,
                bookingState.findBookingsByEventNumber(performance1.getEvent().getEventNumber()).size(),
                "Booking with the correct event number was not found.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test findBookingByEventNumber with two booking added" +
            " and an existing correct event number.")
    void findBookingByEventTwoBookingsTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 2, 0);
        bookingState.createBooking(consumer2, performance1, 2, 0);
        assertEquals(2,
                bookingState.findBookingsByEventNumber(performance1.getEvent().getEventNumber()).size(),
                "Bookings with a non-existing event number were found.");
        System.out.println("Test passed successfully!");
    }

    @Test
    @DisplayName("Test findBookingByEventNumber with two booking added" +
            " and an existing correct event number.")
    void findBookingByEventBookingsNotEqualTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 2, 0);
        bookingState.createBooking(consumer2, performance1, 2, 0);
        assertNotEquals(bookingState.findBookingsByEventNumber(performance1.getEvent().getEventNumber()).get(0),
                bookingState.findBookingsByEventNumber(performance1.getEvent().getEventNumber()).get(1),
                "Two identical bookings identified.");
        System.out.println();
    }

    @Test
    @DisplayName("Test copy constructor with no bookings added")
    void copyConstructorInitialTest()
    {
        BookingState newBookingState = new BookingState(new BookingState());
        assertNull(newBookingState.findBookingByNumber(1),
                "Non-existing booking was found.");
        System.out.println("Deep copying test 1 passed!");
    }

    @Test
    @DisplayName("Test copy constructor copies objects successfully")
    void copyConstructorOneBookingExistsTest()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        BookingState newBookingState = new BookingState(bookingState);
        assertNotEquals(null, newBookingState.findBookingByNumber(1),
                "Copy constructor does not contain a booking which should have been copied");
        System.out.println("Deep copying test 2 passed!");
    }

    @Test
    @DisplayName("Test the copy constructor is using deep copy for copying bookings")
    void copyConstructorDeepCopyTest1()
    {
        BookingState bookingState = new BookingState();
        Booking booking =
                bookingState.createBooking(consumer1, performance1, 1, 0);
        BookingState newBookingState = new BookingState(bookingState);
        booking.setAmountPaid(10); // Change the original bookings amount paid
        // Expect that the deep copied event will stay the same
        assertEquals(0,
                newBookingState.findBookingByNumber(1).getAmountPaid(),
                "After changing the original booking amount paid" +
                        ", the deep copied one changes as well");
        System.out.println("Deep copying test 3 passed!");
    }

    @Test
    @DisplayName("Test the copy constructor is using deep copy for copying bookings")
    void copyConstructorDeepCopyTest2()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        BookingState newBookingState = new BookingState(bookingState);
        // Add a new booking to the deep copied event state
        Booking booking2 =
                newBookingState.createBooking(consumer2, performance2, 1, 0);
        // Expect that the booking was not added to the original event state
        assertNotEquals(bookingState.findBookingByNumber(booking2.getBookingNumber()),
                newBookingState.findBookingByNumber(booking2.getBookingNumber()),
                "After changing the deep copy's list of bookings" +
                        ", the original one changes as well");
        System.out.println("Deep copying test 4 passed!");
    }

    @Test
    @DisplayName("Test the copy constructor is using deep copying")
    void copyConstructorDeepCopyTest3()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 0);
        BookingState newBookingState = new BookingState(bookingState);
        bookingState = null;
        assertNotEquals(bookingState, newBookingState,
                "After changing the original booking state" +
                        ", the deep copied one changes as well");
        System.out.println("Deep copying test 5 passed!");
    }

    @Test
    @DisplayName("Test the copy constructor is copying correct data")
    void copyConstructorDeepCopyTest4()
    {
        BookingState bookingState = new BookingState();
        bookingState.createBooking(consumer1, performance1, 1, 5);
        BookingState newBookingState = new BookingState(bookingState);
        // Check that the deep copy constructor copies the correct properties
        assertEquals(bookingState.findBookingByNumber(1).getAmountPaid(),
                newBookingState.findBookingByNumber(1).getAmountPaid(),
                "The copy constructor did not copy properties correctly");
        System.out.println("Deep copying test 6 passed!");
    }
}
