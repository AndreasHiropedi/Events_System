package tests;

import command.*;
import controller.Context;
import controller.Controller;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.BookingState;
import state.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CancelEventSystemTest
{
    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
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

    private static void governmentAcceptAllSponsorships(Controller controller)
    {
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.runCommand(new RespondSponsorshipCommand(
                    request.getRequestNumber(), 25
            ));
        }
    }

    private static void governmentRejectAllSponsorships(Controller controller)
    {
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.runCommand(new RespondSponsorshipCommand(
                    request.getRequestNumber(), 0
            ));
        }
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
                LocalDateTime.of(1993, 3, 20, 4, 20),
                LocalDateTime.of(1993, 3, 20, 6, 45),
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
                LocalDateTime.now(),
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
                LocalDateTime.now().plusMonths(1),
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

    private static void createOlympicsProviderWith2Events(Controller controller) {
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


    @Test
    @DisplayName("Test cancel event for valid sponsored event")
    void validCancelEventTest1()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(2, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, events.get(0).getStatus(),
                "Event status should be cancelled!");
        System.out.println("Cancel sponsored event test passes!");
    }

    @Test
    @DisplayName("Test cancel event for valid non-sponsored event")
    void validCancelEventTest2()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentRejectAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(2, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, events.get(0).getStatus(),
                "Event status should be cancelled!");
        System.out.println("Cancel non-sponsored event test passes!");
    }

    @Test
    @DisplayName("Test cancel event for valid non-ticketed event")
    void validCancelEventTest3()
    {
        Controller controller = new Controller();

        createCinemaProviderWith3Events(controller);

        controller.runCommand(new LoginCommand("odeon@cineworld.com", "F!ghT th3 R@Pture"));

        CancelEventCommand cmd = new CancelEventCommand(3, "Trololol lololololol");
        controller.runCommand(cmd);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(3, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(3).getStatus(),
                "Event status should be cancelled!");
        System.out.println("Cancel non-ticketed event test passes!");
    }

    @Test
    @DisplayName("Test cancel event for non-logged in user")
    void invalidCancelEventTest1()
    {
        Controller controller = new Controller();

        createCinemaProviderWith3Events(controller);

        CancelEventCommand cmd = new CancelEventCommand(3, "Trololol lololololol");
        controller.runCommand(cmd);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(3, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(3).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Can't cancel without logging in test passes!");
    }

    @Test
    @DisplayName("Test cancel event for logged-in user not being an entertainment provider")
    void invalidCancelEventTest2()
    {
        Controller controller = new Controller();

        createCinemaProviderWith3Events(controller);

        register3Consumers(controller);
        loginConsumer2(controller);

        CancelEventCommand cmd = new CancelEventCommand(3, "Trololol lololololol");
        controller.runCommand(cmd);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(3, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(3).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Can't cancel without being an entertainment provider test passes!");
    }

    @Test
    @DisplayName("Test cancel event for invalid event")
    void invalidCancelEventTest3()
    {
        Controller controller = new Controller();

        createCinemaProviderWith3Events(controller);

        controller.runCommand(new LoginCommand("odeon@cineworld.com", "F!ghT th3 R@Pture"));

        CancelEventCommand cmd = new CancelEventCommand(5, "Trololol lololololol");
        controller.runCommand(cmd);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(3, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(3).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Can't cancel an invalid event test passes!");
    }

    @Test
    @DisplayName("Test cancel event for current user not being the event organiser")
    void invalidCancelEventTest4()
    {
        Controller controller = new Controller();

        createBuskingProviderWith1Event(controller);
        createCinemaProviderWith3Events(controller);

        controller.runCommand(new LoginCommand("busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!"));

        CancelEventCommand cmd = new CancelEventCommand(4, "Trololol lololololol");
        controller.runCommand(cmd);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(4, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(4).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Can't cancel an event without being the organiser test passes!");
    }

    @Test
    @DisplayName("Test cancel event for performance already started")
    void invalidCancelEventTest5()
    {
        Controller controller = new Controller();

        createBuskingProviderWith1Event(controller);
        createCinemaProviderWith3Events(controller);

        controller.runCommand(new LoginCommand("busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!"));

        CancelEventCommand cmd = new CancelEventCommand(1, "Trololol lololololol");
        controller.runCommand(cmd);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(4, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(1).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Can't cancel when a performance already started test passes!");
    }

    @Test
    @DisplayName("Test cancel event for performance already ended")
    void invalidCancelEventTest6()
    {
        Controller controller = new Controller();

        createBuskingProviderWith1Event(controller);
        createCinemaProviderWith3Events(controller);

        controller.runCommand(new LoginCommand("busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!"));

        CancelEventCommand cmd = new CancelEventCommand(1, "Trololol lololololol");
        controller.runCommand(cmd);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(4, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(1).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Can't cancel when a performance already ended test passes!");
    }

    @Test
    @DisplayName("Test cancel event for cancelling multiple valid events")
    void validCancelMultipleEventsTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        CancelEventCommand command = new CancelEventCommand(2, "Lmao it got cancelled");
        controller.runCommand(command);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(2, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(1).getStatus(),
                "Event status should be cancelled!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(2).getStatus(),
                "Event status should be cancelled!");
        System.out.println("Cancel multiple events test passes!");
    }

    @Test
    @DisplayName("Test cancel event for cancelling one valid event and one invalid attempt")
    void cancelOneValidOneInvalidTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        loginConsumer2(controller);
        CancelEventCommand cmd = new CancelEventCommand(2, "Cancelled lol");
        controller.runCommand(cmd);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(2, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(1).getStatus(),
                "Event status should be cancelled!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(2).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Cancel one event and one invalid cancel request test passes!");
    }

    @Test
    @DisplayName("Test cancel event for cancelling one valid event and multiple invalid attempts")
    void cancelOneValidMultipleInvalidTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        loginConsumer2(controller);
        CancelEventCommand cmd = new CancelEventCommand(2, "Cancelled lol");
        controller.runCommand(cmd);

        loginConsumer1(controller);
        CancelEventCommand cmd2 = new CancelEventCommand(3, "Hehehehe lololololol");
        controller.runCommand(cmd2);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(3, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(1).getStatus(),
                "Event status should be cancelled!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(2).getStatus(),
                "Event status should not be cancelled!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(3).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Cancel one event and multiple invalid cancel requests test passes!");
    }

    @Test
    @DisplayName("Test cancel event for multiple valid events and multiple invalid attempts")
    void cancelMultipleValidMultipleInvalidTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createCinemaProviderWith3Events(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        CancelEventCommand command = new CancelEventCommand(2, "Lmao it got cancelled");
        controller.runCommand(command);
        controller.runCommand(new LogoutCommand());

        loginConsumer2(controller);
        CancelEventCommand cmd = new CancelEventCommand(2, "Cancelled lol");
        controller.runCommand(cmd);

        loginConsumer1(controller);
        CancelEventCommand cmd2 = new CancelEventCommand(3, "Hehehehe lololololol");
        controller.runCommand(cmd2);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(5, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(1).getStatus(),
                "Event status should be cancelled!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(2).getStatus(),
                "Event status should be cancelled!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(3).getStatus(),
                "Event status should not be cancelled!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(4).getStatus(),
                "Event status should not be cancelled!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(5).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Cancel multiple events and multiple invalid requests test passes!");
    }

    @Test
    @DisplayName("Test cancel event for multiple valid events and one invalid attempt")
    void cancelMultipleValidOneInvalidTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        createBuskingProviderWith1Event(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 2);
        controller.runCommand(new LogoutCommand());

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        CancelEventCommand command = new CancelEventCommand(2, "Lmao it got cancelled");
        controller.runCommand(command);
        controller.runCommand(new LogoutCommand());

        loginConsumer2(controller);
        CancelEventCommand cmd = new CancelEventCommand(2, "Cancelled lol");
        controller.runCommand(cmd);

        loginConsumer1(controller);
        CancelEventCommand cmd2 = new CancelEventCommand(3, "Hehehehe lololololol");
        controller.runCommand(cmd2);

        Context context = controller.getContext();
        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(3, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(1).getStatus(),
                "Event status should be cancelled!");
        assertEquals(EventStatus.CANCELLED, eventState.findEventByNumber(2).getStatus(),
                "Event status should be cancelled!");
        assertEquals(EventStatus.ACTIVE, eventState.findEventByNumber(3).getStatus(),
                "Event status should not be cancelled!");
        System.out.println("Cancel multiple events and one invalid request test passes!");
    }


    @Test
    @DisplayName("Test cancel event for valid non-sponsored event with bookings and test booking status")
    void validCancelEventTest4()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);
        register3Consumers(controller);

        loginConsumer2(controller);
        consumerBookNthTicketedEvent(controller, 1);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        BookingState bookingState = (BookingState) context.getBookingState();
        EventState eventsState = (EventState) context.getEventState();
        Event bookedEvent = eventsState.getAllEvents().get(0);
        List<Booking> eventBookings = bookingState.findBookingsByEventNumber(bookedEvent.getEventNumber());
        loginGovernmentRepresentative(controller);
        governmentRejectAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        loginOlympicsProvider(controller);
        providerCancelFirstEvent(controller);
        controller.runCommand(new LogoutCommand());

        EventState eventState = (EventState) context.getEventState();
        List<Event> events = eventState.getAllEvents();

        assertEquals(2, events.size(), "Event should not have been deleted!");
        assertEquals(EventStatus.CANCELLED, events.get(0).getStatus(),
                "Event status should be cancelled!");
        assertEquals(eventBookings.get(0).getStatus(), BookingStatus.CANCELLEDBYPROVIDER);
        System.out.println("Cancel non-sponsored event test passes!");
    }

}
