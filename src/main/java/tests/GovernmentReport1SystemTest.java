package tests;

import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GovernmentReport1SystemTest
{
    @BeforeEach
    void printTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs()
    {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    private static void loginGovernmentRepresentative(Controller controller)
    {
        controller.runCommand(new LoginCommand("margaret.thatcher@gov.uk", "The Good times  "));
    }

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

    private static void governmentAcceptAllSponsorships(Controller controller)
    {
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests) {
            controller.runCommand(new RespondSponsorshipCommand(
                    request.getRequestNumber(), 25
            ));
        }
    }

    private static void governmentAcceptNthSponsorship(Controller controller, int n)
    {
        n--;
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();
        controller.runCommand(new RespondSponsorshipCommand(
                requests.get(n).getRequestNumber(), 25
        ));
    }

    private static void providerCancelFirstEvent(Controller controller)
    {
        ListEventsCommand cmd = new ListEventsCommand(true, true);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();
        Event event = events.get(0);
        controller.runCommand(new CancelEventCommand(event.getEventNumber(), "Trololol"));
    }

    private static void consumerBookNthTicketedEvent(Controller controller, int n)
    {
        ListEventsCommand cmd = new ListEventsCommand(false, true);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        for (Event event : events) {
            if (event instanceof TicketedEvent) {
                n--;
            }

            if (n <= 0) {
                Collection<EventPerformance> performances = event.getPerformances();
                BookEventCommand bookEvent = new BookEventCommand(
                        event.getEventNumber(),
                        performances.iterator().next().getPerformanceNumber(),
                        1);
                controller.runCommand(bookEvent);
                return;
            }
        }
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

    private static void createCinemaProviderWith3Events(Controller controller)
    {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "Cinema Conglomerate",
                "Global Office, International Space Station",
                "$$$@there'sNoEmailValidation.wahey!",
                "Mrs Representative",
                "odeon@cineworld.com",
                "F!ghT th3 R@Pture",
                List.of("Dr Strangelove"),
                List.of("we_dont_get_involved@cineworld.com")
        ));

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "The LEGO Movie",
                EventType.Movie,
                50,
                15.75,
                false
        );
        controller.runCommand(eventCmd1);
        long eventNumber1 = eventCmd1.getResult();
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "You know how much it hurts when you step on a Lego piece?!?!",
                LocalDateTime.now().minusWeeks(1),
                LocalDateTime.now().minusWeeks(1).plusHours(2),
                Collections.emptyList(),
                false,
                true,
                false,
                50,
                50
        ));

        CreateTicketedEventCommand eventCmd2 = new CreateTicketedEventCommand(
                "Frozen Ballet",
                EventType.Dance,
                50,
                35,
                true
        );
        controller.runCommand(eventCmd2);
        long eventNumber2 = eventCmd2.getResult();
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber2,
                "Odeon cinema",
                LocalDateTime.now().plusWeeks(1).plusDays(2).plusHours(3),
                LocalDateTime.now().plusWeeks(1).plusDays(2).plusHours(6),
                Collections.emptyList(),
                false,
                true,
                false,
                50,
                50
        ));

        CreateNonTicketedEventCommand eventCmd3 = new CreateNonTicketedEventCommand(
                "The Shining at the Meadows (Free Screening) (Live Action)",
                EventType.Sports
        );
        controller.runCommand(eventCmd3);
        long eventNumber3 = eventCmd3.getResult();
        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber3,
                "The Meadows, Edinburgh",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                List.of("You"),
                false,
                false,
                true,
                1000,
                9999
        ));

        controller.runCommand(new LogoutCommand());
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

    private static void createOlympicsProviderWith1Event(Controller controller)
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
                LocalDateTime.of(2042, 4, 20, 16, 20),
                LocalDateTime.of(2069, 4, 20, 16, 20),
                List.of("Everyone in disc throw and 400m sprint"),
                false,
                true,
                true,
                3000,
                3000
        ));

        controller.runCommand(new LogoutCommand());
    }

    @Test
    @DisplayName("Option 1) get all the bookings for performances" +
            " which belong to active and sponsored events, and take" +
            " place between two times (date+time), including at those exact times")
    void getSponsoredActiveEventBookingsBetween()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginConsumer3(controller);
        consumerBookNthTicketedEvent(controller, 4);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);

        // Only one active sponsored booking is between these two times
        LocalDateTime start = LocalDateTime.now().minusWeeks(2);
        LocalDateTime end = LocalDateTime.now().plusYears(1);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertEquals(1, bookings.size());
        assertEquals("Wednesday Kebede", bookings.get(0).getBooker().getName(),
                "The booker's name in the booking does not match the expected name");
        assertEquals("Frozen Ballet",
                bookings.get(0).getEventPerformance().getEvent().getTitle(),
                "The event performance title does not match the expected title");
    }

    @Test
    @DisplayName("Test for government report command without having logged in")
    void getReportWithoutLoggingInTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        // Accepting all sponsorships to test only the logging-in requirement
        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        // Several bookings for active sponsored events
        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginConsumer3(controller);
        consumerBookNthTicketedEvent(controller, 4);
        controller.runCommand(new LogoutCommand());

        // A wide time interval to include all performances
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);
        // Skip logging in
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Logged out users should not be able " +
                "to get the government report");
    }

    @Test
    @DisplayName("Test for government report command as a consumer")
    void getReportAsConsumerTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        // Accepting all sponsorships to test only the logging-in requirement
        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        // Several bookings for active sponsored events

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginConsumer3(controller);
        consumerBookNthTicketedEvent(controller, 4);
        controller.runCommand(new LogoutCommand());

        // A wide time interval to include all performances
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);

        // Login with consumer instead of government representative
        loginConsumer1(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Consumer users should not be able " +
                "to get the government report");
    }

    @Test
    @DisplayName("Test for government report command as an entertainment provider")
    void getReportAsEntertainmentProviderTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        // Several bookings for active sponsored events

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginConsumer3(controller);
        consumerBookNthTicketedEvent(controller, 4);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        // A wide time interval to include all performances
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);

        // Login with an entertainment provider instead of government representative
        loginOlympicsProvider(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Entertainment providers should not be able " +
                "to get the government report");
    }

    @Test
    @DisplayName("Test for government report command with only non ticketed events")
    void getReportNonTicketedTest()
    {
        Controller controller = new Controller();

        // Create only non-ticketed event
        createBuskingProviderWith1Event(controller);

        // Choose a wide time interval which includes the event
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Non ticketed events cannot be booked " +
                "and should not appear on the government report");
    }

    @Test
    @DisplayName("Test for government report command without any events added")
    void getReportNoEventsTest()
    {
        Controller controller = new Controller();

        // An arbitrarily wide time interval
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "There should be no bookings found as there are " +
                "no events");
    }

    @Test
    @DisplayName("Test for government report command without any bookings added")
    void getReportNoBookingsTest()
    {
        Controller controller = new Controller();

        // Create several providers with events
        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        // Accept all sponsorships to only test the booking requirement
        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        // Choose a wide range to include all performances
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        // Expect to get no bookings since none were made
        assertNull(bookings, "Non existing bookings were found  ");
    }

    @Test
    @DisplayName("Test for government report command without any sponsored events")
    void getReportActiveNoSponsoredTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        // Create several bookings but none are sponsored
        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginConsumer3(controller);
        consumerBookNthTicketedEvent(controller, 4);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        // Choose a wide time interval to include all booked performances
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Non sponsored events should not be found ");
    }

    @Test
    @DisplayName("Test for government report command without any active events")
    void getReportSponsoredNoActiveTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        // Sponsor all events to only test the "Active" requirement
        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        // Make several bookings for the first event
        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer3(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Cancel the first event which has all the bookings
        // This results in several sponsored bookings, none of which are active
        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        // Choose a wide time interval to include all bookings
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Non active events should not be found ");
    }

    @Test
    @DisplayName("Test for government report command without one sponsored event and a" +
            " wide time range")
    void getReportValidTimeSponsoredTest()
    {
        Controller controller = new Controller();

        // Make several active events
        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        // Sponsor the first event for the sponsorship requirement
        loginGovernmentRepresentative(controller);
        governmentAcceptNthSponsorship(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Choose a wide time range to include all bookings
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now().plusYears(30);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        // Check that the correct active sponsored booking is found
        assertEquals(1, bookings.size(),
                "One sponsored booking was not found");
        assertEquals("John Biggson", bookings.get(0).getBooker().getName(),
                "Correct booker's name should be displayed in the booking");
        assertEquals("London Summer Olympics",
                bookings.get(0).getEventPerformance().getEvent().getTitle(),
                "Correct event title should be displayed in the booking");
    }

    @Test
    @DisplayName("Test government report command with one booking " +
            " and given interval before its start and end dates")
    void getReportALotBeforeTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptNthSponsorship(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Choose a time interval which starts and ends before the booked performance
        LocalDateTime start = LocalDateTime.now().minusWeeks(2);
        LocalDateTime end = LocalDateTime.now().minusWeeks(1);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Bookings should not be found " +
                "if they are not strictly in the provided time interval");
    }

    @Test
    @DisplayName("Test government report command with one booking " +
            " and given interval after its start and end dates")
    void getReportALotAfterTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptNthSponsorship(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Choose a time interval which starts and ends after the booked performance
        LocalDateTime start = LocalDateTime.now().plusYears(5);
        LocalDateTime end = LocalDateTime.now().plusYears(6);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Bookings should not be found " +
                "if they are not strictly in the provided time interval");
    }

    @Test
    @DisplayName("Test government report command with one booking just before the " +
            " given interval start time")
    void getReportIntervalStartWrongEndOkTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptNthSponsorship(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Choose the interval start date one minute after the start of performance
        LocalDateTime start = LocalDateTime.now().plusMonths(1).plusMinutes(1);
        // The interval end date should be after the end of performance
        LocalDateTime end = LocalDateTime.now().plusMonths(2);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Bookings should not be found " +
                "if they are not strictly in the provided time interval");
    }

    @Test
    @DisplayName("Test government report command with one booking and " +
            "the given interval end date before the end of booked performance")
    void getReportStartOkEndWrongTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptNthSponsorship(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Choose the interval start time before the start of performance
        LocalDateTime start = LocalDateTime.now().minusMinutes(1);
        // And the interval end time before the end of performance
        LocalDateTime end = LocalDateTime.now().plusMonths(1).plusHours(7);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Bookings should not be found " +
                "if they are not strictly in the provided time interval");
    }

    @Test
    @DisplayName("Test government report command with one booking and " +
            "the given interval start and end dates before the start of booked performance")
    void getReportIntervalJustBeforeTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptNthSponsorship(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Choose the interval start and end times just slightly earlier than the
        // performance start and end times
        LocalDateTime start = LocalDateTime.now().plusMonths(1).minusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMonths(1).plusHours(8).minusMinutes(1);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Bookings should not be found " +
                "if they are not strictly in the provided time interval");
    }

    @Test
    @DisplayName("Test government report command with one booking and " +
            "the given interval start and end dates after the start of booked performance")
    void getReportIntervalJustAfterTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptNthSponsorship(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Choose the interval start and end times just slightly later than the
        // performance start and end times
        LocalDateTime start = LocalDateTime.now().plusMonths(1).plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMonths(1).plusHours(8).plusMinutes(1);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertNull(bookings, "Bookings should not be found " +
                "if they are not strictly in the provided time interval");
    }

    @Test
    @DisplayName("Test government report command with one booking and " +
            "the given interval start and end dates exactly at the start" +
            " and end of booked performance")
    void getReportTimeExtremeCaseTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith1Event(controller);
        register3Consumers(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptNthSponsorship(controller, 1);
        controller.runCommand(new LogoutCommand());

        loginConsumer1(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        // Choose an interval which corresponds exactly to
        // the start and end of the booked performance
        LocalDateTime start = LocalDateTime.of(2042, 4, 20, 16, 20);
        LocalDateTime end = LocalDateTime.of(2069, 4, 20, 16, 20);

        loginGovernmentRepresentative(controller);
        GovernmentReport1Command cmd = new GovernmentReport1Command(start, end);
        controller.runCommand(cmd);
        List<Booking> bookings = cmd.getResult();

        assertEquals(1, bookings.size(), "Bookings should be found " +
                "if they are in the provided time interval");
        assertEquals("John Biggson", bookings.get(0).getBooker().getName(),
                "Correct booker's name should be displayed in the booking");
        assertEquals("London Summer Olympics",
                bookings.get(0).getEventPerformance().getEvent().getTitle(),
                "Correct event title should be displayed in the booking");
    }
}
