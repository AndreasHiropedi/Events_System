package tests;

import command.*;
import controller.Context;
import controller.Controller;
import model.*;
import org.junit.jupiter.api.*;
import state.BookingState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookEventSystemTest
{
    @BeforeEach
    void printTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

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

    private static void loginConsumer3(Controller controller)
    {
        controller.runCommand(new LoginCommand("i-will-kick-your@gmail.com", "it is wednesday my dudes"));
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

    private static void register3Consumers(Controller controller)
    {
        controller.runCommand(new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        ));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand(
                "Jane Giantsdottir",
                "jane@inf.ed.ac.uk",
                "04462187232",
                "giantsRverycool",
                "jane@aol.com"
        ));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand(
                "Wednesday Kebede",
                "i-will-kick-your@gmail.com",
                "-",
                "it is wednesday my dudes",
                "i-will-kick-your@gmail.com"
        ));
        controller.runCommand(new LogoutCommand());
    }

    // ================================================================================

    // ================================================================================
    //                                     BOOKINGS
    // ================================================================================

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

    private static void createBuskingProviderWith1Event(Controller controller)
    {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "No org",
                "Leith Walk",
                "a hat on the ground",
                "the best musicican ever",
                "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!",
                Collections.emptyList(),
                Collections.emptyList()
        ));

        CreateNonTicketedEventCommand eventCmd = new CreateNonTicketedEventCommand(
                "Music for everyone!",
                EventType.Music
        );
        controller.runCommand(eventCmd);
        long eventNumber = eventCmd.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber,
                "Leith as usual",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        ));
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber,
                "You know it",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        ));

        controller.runCommand(new LogoutCommand());
    }

    private static void createOlympicProviderWithPastEvent(Controller controller)
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
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().minusMonths(1).plusHours(8),
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
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().minusHours(8),
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
                LocalDateTime.now().minusMonths(1).plusDays(1),
                LocalDateTime.now().minusMonths(1).plusDays(1).plusHours(6),
                List.of("Everyone in javelin throw and long jump"),
                false,
                true,
                true,
                3000,
                3000
        ));
    }

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

    // ================================================================================

    // ================================================================================
    //                                      TESTS
    // ================================================================================

    @Test
    @DisplayName("Test for one valid booking of one ticketed event")
    void bookOneValidTicketedEventTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();

        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        assertEquals(1, bookings.size(),
                "Incorrect number of bookings was added");
        assertEquals(bookings, booker.getBookings(),
                "Bookings should be added to the consumer's booking list");
        assertEquals(booker, bookings.get(0).getBooker(),
                "Booker should be added correctly");
    }

    @Test
    @DisplayName("Test for one booking of a non-ticketed event")
    void bookNonTicketedEventTest()
    {
        Controller controller = new Controller();

        createBuskingProviderWith1Event(controller);

        register1Consumer(controller);
        loginConsumer1(controller);

        ListEventsCommand cmd = new ListEventsCommand(false, true);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();
        Event event = events.get(0);

        Collection<EventPerformance> performances = event.getPerformances();
        BookEventCommand bookEvent = new BookEventCommand(
                event.getEventNumber(),
                performances.iterator().next().getPerformanceNumber(),
                1);
        controller.runCommand(bookEvent);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();

        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);

        // Ensure that no bookings have been added for a non-ticketed event as it cannot be booked.
        assertEquals(0, bookings.size(),
                "Non ticketed events cannot be booked");
        assertEquals(0, booker.getBookings().size(),
                "Non ticketed events should not be added to the consumer's booking list.");
    }

    @Test
    @DisplayName("Test for one consumer booking multiple valid ticketed events")
    void oneConsumerMultipleBookingsTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);
        bookNthTicketedEvent(controller, 2, 1);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();

        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);

        // Expect to have added exactly 1 booking.
        assertEquals(1, bookings.size(),
                "First booking was not added! Got " + bookings.size() + " bookings.");

        bookings.addAll(bookingState.findBookingsByEventNumber(2));

        // Expect to have added exactly 2 bookings.
        assertEquals(2, bookings.size(),
                "Second booking was not added! Got " + bookings.size() + " bookings.");

        // Expect the booker to have exact same bookings as in 'bookings'.
        assertEquals(bookings, booker.getBookings(),
                "Bookings were not added to the consumer's booking list!");

        // Booker of the event is expected to appear first on the list for the event bookings.
        assertEquals(booker, bookings.get(0).getBooker(),
                "Booker at bookings index 0 is not the same as current!");

        // Booker of the event is expected to appear second on the list for the event bookings.
        assertEquals(booker, bookings.get(1).getBooker(),
                "Booker at bookings index 1 is not the same as current!");
    }

    @Test
    @DisplayName("Test for multiple consumers booking one valid ticketed event")
    void multipleConsumersOneEventValidTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        register3Consumers(controller);

        // ------> Consumer 1
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);
        Consumer booker1 = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        // ------> Consumer 2
        loginConsumer2(controller);
        bookNthTicketedEvent(controller, 1, 1);
        Consumer booker2 = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        // ------> Consumer 3
        loginConsumer3(controller);
        bookNthTicketedEvent(controller, 1, 1);
        Consumer booker3 = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();

        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);

        // Expect to have exactly 3 bookings.
        assertEquals(3, bookings.size(),
                "Incorrect number of bookings added!");

        // First booking should belong to the first booker.
        assertEquals(bookings.get(0), booker1.getBookings().get(0),
                "Booking at index 0 should be added to the first consumer's booking list!");

        // Second booking should belong to the second booker.
        assertEquals(bookings.get(1), booker2.getBookings().get(0),
                "Booking at index 1 should be added to the second consumer's booking list!");

        // Third booking should belong to the third booker.
        assertEquals(bookings.get(2), booker3.getBookings().get(0),
                "Booking at index 2 should be added to the third consumer's booking list!");

        // First booking should have the first booker.
        assertEquals(booker1, bookings.get(0).getBooker(),
                "First booker is not the same as expected!");

        // Second booking should have the second booker.
        assertEquals(booker2, bookings.get(1).getBooker(),
                "Second booker is not the same as expected!");

        // Third booking should have the third booker.
        assertEquals(booker3, bookings.get(2).getBooker(),
                "Third booker is not the same as expected!");
    }

    @Test
    @DisplayName("Test for multiple consumers booking multiple valid ticketed events")
    void multipleConsumersMultipleEventsValidTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        // ------> Consumer 1
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);
        bookNthTicketedEvent(controller, 2, 1);
        Consumer booker1 = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        // ------> Consumer 2
        loginConsumer2(controller);
        bookNthTicketedEvent(controller, 1, 1);
        Consumer booker2 = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        // ------> Consumer 3
        loginConsumer3(controller);
        bookNthTicketedEvent(controller, 2, 1);
        Consumer booker3 = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();

        // ------> Assertions
        // Expect 2 bookings for event number 1.
        assertEquals(2, bookingState.findBookingsByEventNumber(1).size(),
                "Expecting 2 bookings added for event with event number 1!"
                        + "Got " + bookingState.findBookingsByEventNumber(1).size() + ".");

        // Expect 2 bookings for event number 2.
        assertEquals(2, bookingState.findBookingsByEventNumber(2).size(),
                "Expecting 2 bookings added for event with event number 2!"
                        + "Got " + bookingState.findBookingsByEventNumber(1).size() + ".");

        // Booking 1 should also be found as first in consumer 1 bookings.
        assertEquals(bookingState.findBookingByNumber(1),
                booker1.getBookings().get(0),
                "Booking with booking number 1 expected as the first booking of the first booker, " +
                        "but was not found!");

        // Booking 2 should also be found as second in consumer 1 bookings.
        assertEquals(bookingState.findBookingByNumber(2),
                booker1.getBookings().get(1),
                "Booking with booking number 2 expected as the second booking of the first booker, " +
                        "but was not found!");

        // Booking 3 should also be found as first in consumer 2 bookings.
        assertEquals(bookingState.findBookingByNumber(3),
                booker2.getBookings().get(0),
                "Booking with booking number 3 expected as the first booking of the second booker, " +
                        "but was not found!");

        // Booking 4 should also be found as first in consumer 3 bookings.
        assertEquals(bookingState.findBookingByNumber(4),
                booker3.getBookings().get(0),
                "Booking with booking number 4 expected as the first booking of the third booker, " +
                        "but was not found!");

        // First booker expected to be the booker of booking 1.
        assertEquals(booker1, bookingState.findBookingByNumber(1).getBooker(),
                "First booker expected to be the booker of event with number 1! Got otherwise!");

        // First booker expected to be the booker of booking 2.
        assertEquals(booker1, bookingState.findBookingByNumber(2).getBooker(),
                "First booker expected to be the booker of event with number 2! Got otherwise!");

        // Second booker expected t be the booker of booking 3.
        assertEquals(booker2, bookingState.findBookingByNumber(3).getBooker(),
                "Second booker expected to be the booker of event with number 3! Got otherwise!");

        // Third booker expected to be the booker of booking 4.
        assertEquals(booker3, bookingState.findBookingByNumber(4).getBooker(),
                "Third booker expected to be the booker of event with number 4! Got otherwise!");
    }

    @Test
    @DisplayName("Test for booking 1 ticket more than the available number of tickets " +
            "for a valid event")
    void notEnoughTicketsBy1Test()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 123457);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect no bookings from unavailable tickets.
        assertEquals(0, bookings.size(),
                "Expecting no bookings from unavailable tickets!"
                        + "Got " + bookings.size() + ".");

        assertEquals(0, booker.getBookings().size(),
                "Expecting no bookings from unavailable tickets for consumer bookings!"
                        + "Got " + bookings.size() + ".");
    }

    @Test
    @DisplayName("Test for booking more tickets than the available number of tickets " +
            "for a valid event")
    void notEnoughTicketsByManyTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 420666);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect no bookings from unavailable tickets.
        assertEquals(0, bookings.size(),
                "Expecting no bookings from unavailable tickets!"
                        + "Got " + bookings.size() + ".");

        assertEquals(0, booker.getBookings().size(),
                "Expecting no bookings from unavailable tickets for consumer bookings!"
                        + "Got " + bookings.size() + ".");
    }

    @Test
    @DisplayName("Test for booking all available number of tickets for a valid event")
    void lastValidTicketTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 123456);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect only one booking for last available ticket.
        assertEquals(1, bookings.size(),
                "Expecting exactly 1 booking!"
                        + "Got " + bookings.size() + ".");

        // Bookings should be the same as consumer bookings
        assertEquals(bookings, booker.getBookings(),
                "Bookings were not found in consumer bookings!");

        // Booker should be the same as for the booker for the first booking.
        assertEquals(booker, bookings.get(0).getBooker(),
                "Booker of the first booking does not match the current!");
    }

    @Test
    @DisplayName("Test for booking negative number of tickets for a valid event")
    void negativeTicketCountTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, -69);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect no bookings from negative ticket counts.
        assertEquals(0, bookings.size(),
                "Expecting no bookings from negative tickets!"
                        + "Got " + bookings.size() + ".");

        assertEquals(0, booker.getBookings().size(),
                "Expecting no bookings from negative tickets for consumer bookings!"
                        + "Got " + bookings.size() + ".");
    }

    @Test
    @DisplayName("Test for booking a ticket for a non-existing event")
    void wrongEventNumberTest()
    {
        Controller controller = new Controller();

        createBuskingProviderWith1Event(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        BookEventCommand bookEvent = new BookEventCommand(
                69,
                420,
                666);
        controller.runCommand(bookEvent);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect no bookings from wrong event numbers.
        assertEquals(0, bookings.size(),
                "Expecting no bookings from an incorrect event!"
                        + "Got " + bookings.size() + ".");

        assertEquals(0, booker.getBookings().size(),
                "Expecting no bookings from an incorrect event for consumer bookings!"
                        + "Got " + bookings.size() + ".");
    }

    @Test
    @DisplayName("Test for booking a ticket for an existing event, but non-existing " +
            "performance")
    void wrongPerformanceNumberTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        BookEventCommand bookEvent = new BookEventCommand(
                1,
                1337,
                1);
        controller.runCommand(bookEvent);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect no bookings from wrong performance numbers.
        assertEquals(0, bookings.size(),
                "Expecting no bookings from an incorrect performance!"
                        + "Got " + bookings.size() + ".");

        assertEquals(0, booker.getBookings().size(),
                "Expecting no bookings from an incorrect performance for consumer bookings! "
                        + "Got " + bookings.size() + ".");
    }

    @Test
    @DisplayName("Test for booking an event as an event organiser")
    void organiserBookEventTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        loginOlympicsProvider(controller);
        bookNthTicketedEvent(controller, 1, 1);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect no bookings for event organisers.
        assertEquals(0, bookings.size(),
                "Unexpected booking from an organiser!");
    }

    @Test
    @DisplayName("Test for booking an event as a government representative")
    void govRepBookEventTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        loginGovernmentRepresentative(controller);
        bookNthTicketedEvent(controller, 1, 1);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect no bookings for government representatives.
        assertEquals(0, bookings.size(),
                "Unexpected booking from a government representative!");
    }

    @Test
    @DisplayName("Test for booking an event after it ended")
    void bookTicketsForPastEventTest()
    {
        Controller controller = new Controller();

        createOlympicProviderWithPastEvent(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        bookNthTicketedEvent(controller, 1, 1);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // Expect no bookings for past events.
        assertEquals(0, bookings.size(),
                "Expecting no bookings from a past event! "
                        + "Got " + bookings.size() + ".");

        assertEquals(0, booker.getBookings().size(),
                "Expecting no bookings from a past event for consumer bookings! "
                        + "Got " + bookings.size() + ".");
    }

    @Test
    @DisplayName("Test for booking an event after it ended")
    void paymentUnsuccessfulTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register1Consumer(controller);
        loginConsumer1(controller);
        Consumer booker = (Consumer) controller.getContext().getUserState().getCurrentUser();
        booker.setPaymentAccountEmail(null);
        bookNthTicketedEvent(controller, 1, 1);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        List<Booking> bookings = context.getBookingState().findBookingsByEventNumber(1);
        bookings.addAll(context.getBookingState().findBookingsByEventNumber(2));

        // expect no bookings for  unsuccessful payments.
        assertEquals(0, bookings.size(),
                "Expecting no bookings from an unsuccessful payment! "
                        + "Got " + bookings.size() + ".");

        assertEquals(0, booker.getBookings().size(),
                "Expecting no bookings from an unsuccessful payment for consumer bookings! "
                        + "Got " + bookings.size() + ".");
    }
}
