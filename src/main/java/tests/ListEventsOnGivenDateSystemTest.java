package tests;

import command.*;
import controller.Context;
import controller.Controller;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.UserState;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListEventsOnGivenDateSystemTest
{
    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    private static void loginConsumer2(Controller controller) {
        controller.runCommand(new LoginCommand("jane@inf.ed.ac.uk", "giantsRverycool"));
    }

    private static void register3Consumers(Controller controller) {
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

    private static void createProviderWith1FuturePerformance(Controller controller) {
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
        long eventNumber1 = eventCmd.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
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

        controller.runCommand(new LogoutCommand());
    }

    private static void createProviderWith1OtherFuturePerformance(Controller controller) {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "No",
                "Leith",
                "a hat on the ground",
                "the best",
                "lightweight@baby",
                "When they say 'you can't do this': Ding Dong!",
                Collections.emptyList(),
                Collections.emptyList()
        ));

        CreateNonTicketedEventCommand eventCmd = new CreateNonTicketedEventCommand(
                "Music not for everyone!",
                EventType.Music
        );
        controller.runCommand(eventCmd);
        long eventNumber = eventCmd.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber,
                "Leith",
                LocalDateTime.of(2030, 3, 20, 6, 40),
                LocalDateTime.of(2030, 3, 20, 8, 50),
                List.of("The same musician"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        ));

        controller.runCommand(new LogoutCommand());
    }

    private static void createProviderWith1PastPerformance(Controller controller)
    {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "No org name",
                "Leith Walk 2",
                "a hat on the ground",
                "the best musicican here",
                "bulk@every.day",
                "When they say 'you can't do this': You are wrong!",
                Collections.emptyList(),
                Collections.emptyList()
        ));

        CreateNonTicketedEventCommand eventCmd = new CreateNonTicketedEventCommand(
                "Music for you! Just you",
                EventType.Music
        );
        controller.runCommand(eventCmd);
        long eventNumber2 = eventCmd.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber2,
                "You know it",
                LocalDateTime.of(2020, 3, 21, 4, 20),
                LocalDateTime.of(2020, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        ));

        controller.runCommand(new LogoutCommand());
    }

    private static void createProviderWith1OtherPastPerformance(Controller controller)
    {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "No or",
                "Leith Walk 4",
                "a hat on the ground",
                "Sean the Menace",
                "blackmed@every.day",
                "When they say 'you can't do this': You can't!",
                Collections.emptyList(),
                Collections.emptyList()
        ));

        CreateNonTicketedEventCommand eventCmd = new CreateNonTicketedEventCommand(
                "Music not for you! Come anyways",
                EventType.Music
        );
        controller.runCommand(eventCmd);
        long eventNumber1 = eventCmd.getResult();

        controller.runCommand(new AddEventPerformanceCommand(
                eventNumber1,
                "You know it",
                LocalDateTime.of(2020, 3, 26, 4, 20),
                LocalDateTime.of(2020, 3, 27, 7, 0),
                List.of("The usual"),
                true,
                true,
                true,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        ));

        controller.runCommand(new LogoutCommand());
    }

    private static void cancelEvent(Controller controller, String email, String password)
    {
        controller.runCommand(new LoginCommand(email, password));
        ListEventsCommand cmd = new ListEventsCommand(true, true);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();
        Event event = events.get(0);
        controller.runCommand(new CancelEventCommand(event.getEventNumber(), "Trololol"));
    }

    @Test
    @DisplayName("Test for getting one event that matches preferences")
    void oneEventOnGivenDateTest1()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        // Preferences match "future performance" preferences
        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        // Search date includes the "future performance" time
        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 20, 4, 20);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(1, events.size(), "Not all events on the given date were added!");
        assertEquals(1, events.get(0).getEventNumber(), "Event wasn't created successfully!");
        System.out.println("Test for one event for the valid date for consumer preference only passes!");
    }

    @Test
    @DisplayName("Test for getting multiple events that match preferences")
    void multipleEventsOnGivenDateTest1()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        // Exactly two active events will match these preferences and the given date
        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(2, events.size(), "Not all events on the given date were added!");
        System.out.println("Test for multiple events for the valid date for consumer preference only passes!");
    }

    @Test
    @DisplayName("Test for getting one event that is active")
    void oneEventOnGivenDateTest2()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        // Past event should no longer be active
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        LocalDateTime searchDate = LocalDateTime.of(2020, 3, 22, 3, 30);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        // Expected one since only one event was active during the given time
        assertEquals(1, events.size(), "Not all events on the given date were added!");
        // Expected two since we have two events (one past one future)
        assertEquals(2, events.get(0).getEventNumber(), "Event wasn't created successfully!");
        System.out.println("Test for one event for the valid date for active events only passes!");
    }

    @Test
    @DisplayName("Test for getting multiple events that are active")
    void multipleEventsOnGivenDateTest2()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(2, events.size(), "Not all events on the given date were added!");
        System.out.println("Test for multiple events for the valid date for consumer preference only passes!");
    }

    @Test
    @DisplayName("Test for getting all events when there is only one event")
    void oneEventOnGivenDateTest3()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        // Past event should not be active
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        LocalDateTime searchDate = LocalDateTime.of(2020, 3, 22, 3, 30);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(1, events.size(), "Not all events on the given date were added!");
        assertEquals(2, events.get(0).getEventNumber(), "Event wasn't created successfully!");
        System.out.println("Test for one event for the valid date passes!");
    }

    @Test
    @DisplayName("Test for getting all events when there are multiple events")
    void multipleEventsOnGivenDateTest3()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(2, events.size(), "Not all events on the given date were added!");
        System.out.println("Test for multiple events for the valid date passes!");
    }

    @Test
    @DisplayName("Test for getting all active events matching user preferences when there is only one event")
    void oneEventOnGivenDateTest4()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 20, 4, 20);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(1, events.size(), "Not all events on the given date were added!");
        assertEquals(1, events.get(0).getEventNumber(), "Event wasn't created successfully!");
        System.out.println("Test for one event for the valid date for both active and consumer preferences passes!");
    }

    @Test
    @DisplayName("Test for getting all active events matching user preferences when there are multiple events")
    void multipleEventsOnGivenDateTest4()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(2, events.size(), "Not all events on the given date were added!");
        System.out.println("Test for multiple events for the valid date for both active and consumer preferences passes!");
    }

    @Test
    @DisplayName("Test for one cancellation of an event matching consumer preferences accounting for event status!")
    void cancelEventTest1()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherPastPerformance(controller);

        register3Consumers(controller);

        // cancellation of the event will make it no longer active
        // this event matches the consumer preferences
        cancelEvent(controller, "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!");

        loginConsumer2(controller);

        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "Too many events were added!");
        System.out.println("Test for multiple events for the valid date for both active and consumer preferences passes!");
    }

    @Test
    @DisplayName("Test for one cancellation of an event matching consumer preferences disregarding event status!")
    void cancelEventTest2()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherPastPerformance(controller);

        register3Consumers(controller);

        // cancellation of the event will make it no longer active
        // this event matches the consumer preferences
        cancelEvent(controller, "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!");

        loginConsumer2(controller);

        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                false, searchDate); // active events set to false to include the cancelled
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(1, events.size(), "Not all events were added!");
        System.out.println("Test for multiple events for the valid date for consumer preferences passes!");
    }

    @Test
    @DisplayName("Test for one cancellation of an event accounting for event status!")
    void cancelEventTest3()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);
        createProviderWith1OtherPastPerformance(controller);

        register3Consumers(controller);

        cancelEvent(controller, "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!");

        loginConsumer2(controller);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(1, events.size(), "Not all events were added!");
        System.out.println("Test for multiple events for the valid date for active events only passes!");
    }

    @Test
    @DisplayName("Test for one cancellation of an event disregarding event status!")
    void cancelEventTest4()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherPastPerformance(controller);

        register3Consumers(controller);

        cancelEvent(controller, "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!");

        loginConsumer2(controller);

        // future performance on this time
        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 20, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(1, events.size(), "Not all events were added!");
        System.out.println("Test for multiple events for the valid date passes!");
    }

    @Test
    @DisplayName("Test for one cancellation of multiple events matching consumer preferences!")
    void cancelEventTest5()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);
        createProviderWith1OtherPastPerformance(controller);

        register3Consumers(controller);

        cancelEvent(controller, "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!");

        loginConsumer2(controller);

        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(1, events.size(), "Too many events were added!");
        System.out.println("Test for multiple events for the valid date for both active and consumer preferences passes!");
    }

    @Test
    @DisplayName("Test for one cancellation of multiple events!")
    void cancelEventTest6()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);
        createProviderWith1OtherPastPerformance(controller);

        register3Consumers(controller);

        cancelEvent(controller, "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!");

        loginConsumer2(controller);

        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertEquals(2, events.size(), "Not all events were added!");
        System.out.println("Test for multiple events for the valid date for consumer preferences passes!");
    }

    @Test
    @DisplayName("Test for date outside the range of all active events")
    void invalidDateTest1()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        // no active events happen at this time
        LocalDateTime searchDate = LocalDateTime.of(2050, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Test for invalid date for all active events passes!");
    }

    @Test
    @DisplayName("Test for date outside the range of all active events")
    void invalidDateTest2()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);
        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        // no active events happen at this time
        LocalDateTime searchDate = LocalDateTime.of(2050, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Test for invalid date for all events matching consumer preferences passes!");
    }

    @Test
    @DisplayName("Test for date outside the range of all active events")
    void invalidDateTest3()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);
        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        // no active events happen at this time
        LocalDateTime searchDate = LocalDateTime.of(2050, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Test for invalid date for all active events matching consumer preferences passes!");
    }

    @Test
    @DisplayName("Test for date outside the range of all events")
    void invalidDateTest4()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);
        createProviderWith1OtherFuturePerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        // no events at all happen at this time
        LocalDateTime searchDate = LocalDateTime.of(2050, 3, 21, 3, 40);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Test for invalid date for all events passes!");
    }

    @Test
    @DisplayName("Test for date is exactly one day from the start of the events (searches all active events)")
    void extremeDateTest1()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 4, 20);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Test 1 for extreme date passes!");
    }

    @Test
    @DisplayName("Test for date is exactly one day from the start of" +
            " the event (searches all events matching consumer preferences)")
    void extremeDateTest2()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);
        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 4, 20);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Test 2 for extreme date passes!");
    }

    @Test
    @DisplayName("Test for date is exactly one day from the start of the " +
            "event (searches all active events matching consumer preferences)")
    void extremeDateTest3()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);
        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 4, 20);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Test 3 for extreme date passes!");
    }

    @Test
    @DisplayName("Test for date is exactly one day from the start of the event (searches all events)")
    void extremeDateTest4()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 4, 20);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Test 4 for extreme date passes!");
    }

    @Test
    @DisplayName("Test combining cancellation, invalid date, and consumer preferences for active events")
    void mixedTest()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        // choose preferences which are different from this first event performance
        loginConsumer2(controller);
        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        // cancel the future event
        cancelEvent(controller, "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!");

        // choose a different time than the future event
        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 4, 20);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("Mixed test combining everything passes!");
    }

    @Test
    @DisplayName("Test for user not logged in")
    void noUserLoggedIn()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        Context context = controller.getContext();
        UserState userState = (UserState) context.getUserState();
        userState.setCurrentUser(null); // set to null for testing the logged in requirement

        LocalDateTime searchDate = LocalDateTime.of(2030, 3, 21, 4, 20);
        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(false,
                true, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be found!");
        System.out.println("No user logged in test passes!");
    }

    @Test
    @DisplayName("Test no past events are shown")
    void oneNoPastEvents()
    {
        Controller controller = new Controller();

        createProviderWith1FuturePerformance(controller);
        createProviderWith1PastPerformance(controller);

        register3Consumers(controller);

        loginConsumer2(controller);

        Context context = controller.getContext();
        User user = context.getUserState().getCurrentUser();
        ConsumerPreferences preferences = new ConsumerPreferences();
        preferences.preferAirFiltration = true;
        preferences.preferredMaxVenueSize = Integer.MAX_VALUE;
        preferences.preferredMaxCapacity = Integer.MAX_VALUE;
        preferences.preferOutdoorsOnly = true;
        preferences.preferSocialDistancing = true;
        ((Consumer) user).setPreferences(preferences);

        LocalDateTime searchDate = LocalDateTime.of(2020, 3, 21, 4, 20);

        ListEventsOnGivenDateCommand cmd = new ListEventsOnGivenDateCommand(true,
                false, searchDate);
        controller.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assertNull(events, "No events should be added added!");
        System.out.println("Test for no past events passes!");
    }
}
