package tests;

import command.*;
import controller.Context;
import controller.Controller;
import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInfo;
import state.BookingState;
import state.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CancelBookingSystemTest {

    // ================================================================================
    //                                    DECORATORS
    // ================================================================================

    // Output colors - for flagging errors, successes and warnings.
    private final String
            g = "\u001B[32m",       // GREEN
            y = "\u001B[33m",       // YELLOW
            r = "\u001B[31m",       // RED
            rst   = "\u001B[0m";    // DEFAULT

    @BeforeEach
    void printTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    private void highlightPass(String message)
    {
        System.out.println(g+message+rst);
    }

    // ================================================================================

    // ================================================================================
    //                             REGISTRATIONS AND LOGINS
    // ================================================================================

    private static void loginOlympicsProvider(Controller controller)
    {
        controller.runCommand(new LoginCommand("anonymous@gmail.com", "anonymous"));
    }

    private static void loginConsumer1(Controller controller)
    {
        controller.runCommand(new LoginCommand("jbiggson1@hotmail.co.uk", "jbiggson2"));
    }

    private static void loginConsumer2(Controller controller)
    {
        controller.runCommand(new LoginCommand("jane@inf.ed.ac.uk", "giantsRverycool"));
    }

    private static void loginGovernmentRepresentative(Controller controller)
    {
        controller.runCommand(new LoginCommand("margaret.thatcher@gov.uk", "The Good times  "));
    }

    private static void register1Consumer(Controller controller)
    {
        controller.runCommand(new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        ));
        controller.runCommand(new LogoutCommand());
    }

    private static void bookNthTicketedEvent(Controller controller, int n, int ticketCount)
    {
        ListEventsCommand cmd = new ListEventsCommand(false, true);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        for (Event event : events)
        {
            if (event instanceof TicketedEvent)
            {
                n--;
            }

            if (n <= 0)
            {
                Collection<EventPerformance> performances = event.getPerformances();
                BookEventCommand bookEvent = new BookEventCommand(
                        event.getEventNumber(),
                        performances.iterator().next().getPerformanceNumber(),
                        ticketCount);
                controller.runCommand(bookEvent);
                return;
            }
        }
    }

    // ================================================================================

    // ================================================================================
    //                                     CREATORS
    // ================================================================================

    private static void createOlympicsProviderWith2Events(Controller controller)
    {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        ));

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25,
                true
        );
        controller.runCommand(eventCmd1);
        long eventNumber1 = eventCmd1.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(8),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                3000
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Swimming arena",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusHours(8),
                List.of("Everyone in swimming"),
                true,
                true,
                false,
                200,
                300
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1).plusDays(1),
                LocalDateTime.now().plusMonths(1).plusDays(1).plusHours(6),
                List.of("Everyone in javelin throw and long jump"),
                false,
                true,
                true,
                3000,
                3000
        ));

        CreateTicketedEventCommand eventCmd2 = new CreateTicketedEventCommand(
                "Winter Olympics",
                EventType.Sports,
                40000,
                400,
                true
        );
        controller.runCommand(eventCmd2);
        long eventNumber2 = eventCmd2.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber2,
                "The Alps",
                LocalDateTime.now().plusYears(2).plusMonths(7),
                LocalDateTime.now().plusYears(2).plusMonths(7).plusDays(3),
                List.of("Everyone in slalom skiing"),
                true,
                true,
                true,
                4000,
                10000
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber2,
                "Somewhere else",
                LocalDateTime.now().plusYears(2).plusMonths(7).plusDays(2),
                LocalDateTime.now().plusYears(2).plusMonths(7).plusDays(4),
                List.of("Everyone in ski jump"),
                true,
                true,
                true,
                4000,
                10000
        ));

        controller.runCommand(new LogoutCommand());
    }

    private static void createOlympicsProviderWithLessThan24HoursEvent(Controller controller)
    {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        ));

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "London Summer Olympics",
                EventType.Sports,
                123456,
                25,
                true
        );
        controller.runCommand(eventCmd1);
        long eventNumber1 = eventCmd1.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Wimbledon",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(8),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                3000
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Swimming arena",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusHours(8),
                List.of("Everyone in swimming"),
                true,
                true,
                false,
                200,
                300
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "Wimbledon",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(6),
                List.of("Everyone in javelin throw and long jump"),
                false,
                true,
                true,
                3000,
                3000
        ));

        controller.runCommand(new LogoutCommand());
    }

    // ================================================================================

    // ================================================================================
    //                                      TESTS
    // ================================================================================

    @Test
    @DisplayName("Cancel a valid booking of a ticketed Event")
    void cancelValidBookingTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        Booking b = bookings.get(0);
        long bookingNumber = b.getBookingNumber();

        CancelBookingCommand cbCmd = new CancelBookingCommand(bookingNumber);
        controller.runCommand(cbCmd);

        // Expect command result to be true for successful event cancellation.
        assertTrue(cbCmd.getResult(),
                "Booking has not been cancelled successfully!");

        // Expect new booking status to be CANCELLEDBYCONSUMER.
        assertEquals(BookingStatus.CANCELLEDBYCONSUMER, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "Unexpected state of the booking!");

        assertEquals(BookingStatus.CANCELLEDBYCONSUMER, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "Unexpected state of the booking with booking number " + bookingNumber + "!");

        highlightPass("Cancel valid booking passed!");
    }

    @Test
    @DisplayName("Try cancelling a valid booking of a ticketed Event while logged out")
    void tryCancelValidBookingLoggedOutTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        Booking b = bookings.get(0);
        long bookingNumber = b.getBookingNumber();

        controller.runCommand(
                new LogoutCommand()
        );

        CancelBookingCommand cbCmd = new CancelBookingCommand(bookingNumber);
        controller.runCommand(cbCmd);

        // Expect false command result for failed booking cancellation when not logged in.
        assertFalse(cbCmd.getResult(),
                "Unexpected booking cancellation while logged out!");

        // Expect booking state to remain ACTIVE.
        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "Unexpected state of the booking!");

        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "Unexpected state of the booking with booking number " + bookingNumber + "!");

        highlightPass("Try cancelling booking while logged out passed!");
    }

    @Test
    @DisplayName("Try cancelling an invalid booking of a ticketed Event")
    void tryCancelInvalidBookingTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        Booking b = bookings.get(0);
        long bookingNumber = b.getBookingNumber();

        CancelBookingCommand cbCmd = new CancelBookingCommand(bookingNumber+10);
        controller.runCommand(cbCmd);

        // Expect false command result for invalid booking cancellation.
        assertFalse(cbCmd.getResult(),
                "Unexpected booking cancellation for an invalid booking!");

        // Expect booking state to remain ACTIVE.
        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "Unexpected state of the booking!");

        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "Unexpected state of the booking with booking number " + bookingNumber + "!");

        highlightPass("Try cancelling an invalid booking of a ticketed Event passed!");
    }

    @Test
    @DisplayName("Try cancelling a booking of a ticketed Event that's less than 24 hours away")
    void tryCancellingLessThan24HoursBookingTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWithLessThan24HoursEvent(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        Booking b = bookings.get(0);
        long bookingNumber = b.getBookingNumber();

        CancelBookingCommand cbCmd = new CancelBookingCommand(bookingNumber+10);
        controller.runCommand(cbCmd);

        // Expect false command result from cancelling a booking less than 24 hours before the event.
        assertFalse(cbCmd.getResult(),
                "Unexpected booking cancellation less than 24 hours before the event!");

        // Expect booking state to remain ACTIVE.
        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "Unexpected state of the booking!");

        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "Unexpected state of the booking with booking number " + bookingNumber + "!");

        highlightPass("Try cancelling booking less than 24 hours passed!");
    }

    @Test
    @DisplayName("Try cancelling a valid booking of a ticketed Event while logged in as Gov Rep")
    void tryCancelValidBookingGovRepTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        Booking b = bookings.get(0);
        long bookingNumber = b.getBookingNumber();

        controller.runCommand(
                new LogoutCommand()
        );
        loginGovernmentRepresentative(controller);

        CancelBookingCommand cbCmd = new CancelBookingCommand(bookingNumber);
        controller.runCommand(cbCmd);

        // Expect false command result from cancelling booking as a government representative.
        assertFalse(cbCmd.getResult(),
                "Unexpected booking cancellation by a government representative!");

        // Expect booking state to remain ACTIVE.
        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "Unexpected state of the booking!");

        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "Unexpected state of the booking with booking number " + bookingNumber + "!");

        highlightPass("Try cancelling a valid booking of a ticketed Event while logged in as Gov Rep passed");
    }

    @Test
    @DisplayName("Try cancelling a valid booking of a ticketed Event while logged in as an entertainment provider")
    void tryCancelValidBookingEntProTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        Booking b = bookings.get(0);
        long bookingNumber = b.getBookingNumber();

        controller.runCommand(
                new LogoutCommand()
        );
        loginOlympicsProvider(controller);

        CancelBookingCommand cbCmd = new CancelBookingCommand(bookingNumber);
        controller.runCommand(cbCmd);

        // Expect false command result from cancelling booking as a provider.
        assertEquals (false, cbCmd.getResult(),
                "Unexpected booking cancellation by the entertainment provider!");

        // Expect booking state to remain ACTIVE.
        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "Unexpected state of the booking!");

        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "Unexpected state of the booking with booking number " + bookingNumber + "!");

        highlightPass("Try cancelling a valid booking of a ticketed Event while logged in as an entertainment provider passed!");
    }

    @Test
    @DisplayName("Try cancelling a booking of a ticketed Event twice")
    void tryCancelValidBookingTwiceTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        Booking b = bookings.get(0);
        long bookingNumber = b.getBookingNumber();

        CancelBookingCommand cbCmd = new CancelBookingCommand(bookingNumber);
        controller.runCommand(cbCmd);

        // ------> Cancellation 1
        // Expect true command result for a successful booking cancellation.
        assertTrue(cbCmd.getResult(),
                "Booking has not been cancelled successfully!");

        // Expect new booking status to be CANCELLEDBYCONSUMER.
        assertEquals(BookingStatus.CANCELLEDBYCONSUMER, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "There should be no active bookings for this event");

        assertEquals(BookingStatus.CANCELLEDBYCONSUMER, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "There should be no active bookings under this booking number");

        controller.runCommand(cbCmd);

        // ------> Cancellation 2
        // Expect false command result for failed booking cancellation when not logged in.
        assertFalse(cbCmd.getResult(),
                "Unexpected booking cancellation for a cancelled event!");

        // Expect booking state to remain CANCELLEDBYCONSUMER.
        assertEquals(BookingStatus.CANCELLEDBYCONSUMER, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "Unexpected state of the booking!");

        assertEquals(BookingStatus.CANCELLEDBYCONSUMER, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "Unexpected state of the booking with booking number " + bookingNumber + "!");

        highlightPass("Try cancelling an valid booking of a ticketed Event twice passed!");
    }

    @Test
    @DisplayName("Try cancelling a booking of a ticketed Event as wrong consumer")
    void tryCancellingBookingAsWrongConsumer()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        Booking b = bookings.get(0);
        long bookingNumber = b.getBookingNumber();

        controller.runCommand(new LogoutCommand());
        loginConsumer2(controller);

        CancelBookingCommand cbCmd = new CancelBookingCommand(bookingNumber);
        controller.runCommand(cbCmd);

        // Expect command result to be false for cancellation as the wrong consumer.
        assertFalse(cbCmd.getResult(),
                "Unexpected booking cancellation for incorrect booker!");

        // Expect booking state to remain ACTIVE.
        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingsByEventNumber(1).get(0).getStatus(),
                "Unexpected state of the booking!");

        assertEquals(BookingStatus.ACTIVE, bookingState.findBookingByNumber(bookingNumber).getStatus(),
                "Unexpected state of the booking with booking number " + bookingNumber + "!");

        highlightPass("Try cancelling a booking of a ticketed Event as wrong consumer passed!");
    }
}
