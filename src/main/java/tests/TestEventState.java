package tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEventState
{
    @BeforeEach
    void displayTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    // Create several entertainment providers with arbitrary details for testing
    EntertainmentProvider provider1 = new EntertainmentProvider("a", "b", "c", "d",
            "f", "g", new ArrayList<String>(), new ArrayList<String>());
    EntertainmentProvider provider2 = new EntertainmentProvider("d", "c", "c", "d",
            "x", "l", new ArrayList<String>(), new ArrayList<String>());

    // Create an event with arbitrary details used for testing
    Event event = new NonTicketedEvent(0, provider1, "g", EventType.Movie);

    @Test
    @DisplayName("Testing eventState initial configuration has no events")
    void eventStateConstructorTest()
    {
        EventState eventState = new EventState();
        assertEquals(0, eventState.getAllEvents().size(),
                "The initial configuration should not have any events");
    }

    @Test
    @DisplayName("Testing eventState copy constructor is empty initially")
    void copyConstructorInitialTest()
    {
        EventState newEventState = new EventState(new EventState());
        assertEquals(0, newEventState.getAllEvents().size(),
                "The copy constructor has events which should not exist");
    }

    @Test
    @DisplayName("Testing eventState copy constructor copies one event")
    void copyConstructorOneEventTest()
    {
        EventState eventState = new EventState();
        eventState.createNonTicketedEvent(provider2, "title", EventType.Theatre);
        EventState newEventState = new EventState(eventState);
        assertEquals(1, newEventState.getAllEvents().size(),
                "The copy constructor does not copy the current number of events");
    }

    @Test
    @DisplayName("Testing eventState copy constructor copies two events")
    void copyConstructorTwoEventsTest()
    {
        EventState eventState = new EventState();
        eventState.createNonTicketedEvent(provider2, "title", EventType.Theatre);
        eventState.createNonTicketedEvent(provider1, "title2", EventType.Dance);
        EventState newEventState = new EventState(eventState);
        assertEquals(2, newEventState.getAllEvents().size(),
                "The copy constructor does not copy the current number of events");
    }

    @Test
    @DisplayName("Testing eventState copy constructor copies correct properties")
    void copyConstructorCorrectPropertiesTest1()
    {
        EventState eventState = new EventState();
        List<Event> events = new ArrayList<>();
        events.add(new TicketedEvent(1, provider2, "c",
                EventType.Dance, 69, 420));
        eventState.setAllEvents(events);
        EventState newEventState = new EventState(eventState);
        // Check that the properties of the copied event and the original match
        assertEquals(69,
                ((TicketedEvent)newEventState.findEventByNumber(1)).getOriginalTicketPrice(),
                "The copy constructor does not copy data correctly");
    }

    @Test
    @DisplayName("Testing eventState copy constructor copies correct properties")
    void copyConstructorCorrectPropertiesTest2()
    {
        EventState eventState = new EventState();
        eventState.createTicketedEvent(provider1, "a", EventType.Movie,
                69, 420);
        EventState newEventState = new EventState(eventState);
        // Check that the properties of the copied event and the original match
        assertEquals(420,
                ((TicketedEvent)newEventState.findEventByNumber(1)).getNumTickets(),
                "The copy constructor does not copy data correctly");
    }

    @Test
    @DisplayName("Testing eventState copy constructor uses deep copy properly")
    void copyConstructorDeepCopyTest1()
    {
        EventState eventState = new EventState();
        Event event = eventState.createTicketedEvent(provider1, "a", EventType.Movie,
                69, 420);
        EventState newEventState = new EventState(eventState);
        event.setOrganiser(provider2); // Change the provider of the original event
        // The deep copied event should not have this change
        assertNotEquals(provider2,
                newEventState.findEventByNumber(1).getOrganiser(),
                "The copy constructor does not use deep copy correctly");
    }

    @Test
    @DisplayName("Testing eventState copy constructor uses deep copy properly")
    void copyConstructorDeepCopyTest2()
    {
        EventState eventState = new EventState();
        TicketedEvent event = eventState.createTicketedEvent(provider1, "a", EventType.Movie,
                69, 420);
        EventState newEventState = new EventState(eventState);
        event.setNumTickets(666); // Change the number of tickets of the original event
        // The deep copied event should still have the previous number of tickets
        assertEquals(420,
                ((TicketedEvent)newEventState.getAllEvents().get(0)).getNumTickets(),
                "The copy constructor does not use deep copy correctly");
    }

    @Test
    @DisplayName("Testing eventState has the correct number of events after adding one event")
    void getAllEventsNonTicketedTest()
    {
        EventState eventState = new EventState();
        eventState.createNonTicketedEvent(provider1, "a", EventType.Movie);
        assertEquals(1, eventState.getAllEvents().size(),
                "getAllEvents should get all created events correctly");
    }

    @Test
    @DisplayName("Testing eventState has the correct number of events after adding one event")
    void getAllEventsTicketedTest()
    {
        EventState eventState = new EventState();
        eventState.createTicketedEvent(provider1, "a", EventType.Movie, 20, 100);
        assertEquals(1, eventState.getAllEvents().size(),
                "getAllEvents should get all created events correctly");
    }

    @Test
    @DisplayName("Testing eventState has the correct number of events after adding one event")
    void getAllEventsSeveralTest()
    {
        EventState eventState = new EventState();
        eventState.createNonTicketedEvent(provider1, "a", EventType.Movie);
        eventState.createTicketedEvent(provider1, "b", EventType.Music, 20, 100);
        eventState.createNonTicketedEvent(provider2, "c", EventType.Dance);
        assertEquals(3, eventState.getAllEvents().size(),
                "getAllEvents should get all created events correctly");
    }

    @Test
    @DisplayName("Testing setAllEvents can empty the event list correctly")
    void setAllEventsEmptyTest()
    {
        EventState eventState = new EventState();
        List<Event> events = new ArrayList<>();
        eventState.setAllEvents(events);
        assertEquals(events, eventState.getAllEvents(),
                "setAllEvents should set the assigned list of events correctly");
    }

    @Test
    @DisplayName("Testing setAllEvents can set several events correctly")
    void setAllEventsSeveralTest()
    {
        EventState eventState = new EventState();
        List<Event> events = new ArrayList<>();
        events.add(new NonTicketedEvent(1, provider1, "a", EventType.Music));
        events.add(new NonTicketedEvent(2, provider2, "b", EventType.Dance));
        events.add(new TicketedEvent(3, provider2, "c",
                EventType.Dance, 30, 100));
        eventState.setAllEvents(events);
        assertEquals(events, eventState.getAllEvents(),
                "setAllEvents should set the assigned list of events correctly");
    }

    @Test
    @DisplayName("Testing findEventByNumber does not find non-existing event")
    void findEventByNumberEmptyTest()
    {
        EventState eventState = new EventState();
        assertNull(eventState.findEventByNumber(1),
                "findEventByNumber should not find non-existing events");
    }

    @Test
    @DisplayName("Testing findEventByNumber does not find non-existing event")
    void findEventByNumberWrongNumberTest1()
    {
        EventState eventState = new EventState();
        eventState.createNonTicketedEvent(provider1, "a", EventType.Movie);
        assertNull(eventState.findEventByNumber(0),
                "findEventByNumber should not find non-existing events");
    }

    @Test
    @DisplayName("Testing findEventByNumber does not find non-existing event")
    void findEventByNumberWrongNumberTest2()
    {
        EventState eventState = new EventState();
        eventState.createNonTicketedEvent(provider1, "a", EventType.Movie);
        assertNull(eventState.findEventByNumber(2),
                "findEventByNumber should not find non-existing events");
    }

    @Test
    @DisplayName("Testing findEventByNumber does not find non-existing event")
    void findEventByNumberCorrectNumberTest()
    {
        EventState eventState = new EventState();
        eventState.createNonTicketedEvent(provider1, "a", EventType.Movie);
        assertNotEquals(null, eventState.findEventByNumber(1),
                "findEventByNumber should find existing events");
    }

    @Test
    @DisplayName("Testing createNonTicketedEvent creates non-ticketed" +
            " event provider correctly")
    void createNonTicketedEventOrganiserTest()
    {
        EventState eventState = new EventState();
        Event event = eventState.createNonTicketedEvent(provider1, "sleeping", EventType.Movie);
        assertEquals(provider1, event.getOrganiser(),
                "createNonTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createNonTicketedEvent creates non-ticketed " +
            "event title correctly")
    void createNonTicketedEventTitleTest()
    {
        EventState eventState = new EventState();
        Event event = eventState.createNonTicketedEvent(provider1, "sleeping", EventType.Sports);
        assertEquals("sleeping", event.getTitle(),
                "createNonTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createNonTicketedEvent creates non-ticketed" +
            " events type correctly")
    void createNonTicketedEventTypeTest()
    {
        EventState eventState = new EventState();
        Event event = eventState.createNonTicketedEvent(provider1, "sleeping", EventType.Sports);
        assertEquals(EventType.Sports, event.getType(),
                "createNonTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createNonTicketedEvent creates non-ticketed events")
    void createNonTicketedEventClassTest()
    {
        EventState eventState = new EventState();
        Event event = eventState.createNonTicketedEvent(provider1, "sleeping",
                EventType.Sports);
        assertTrue(event instanceof NonTicketedEvent,
                "createNonTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createTicketedEvent creates ticketed events provider correctly")
    void createTicketedEventOrganiserTest()
    {
        EventState eventState = new EventState();
        Event event = eventState.createTicketedEvent(provider2, "eating",
                EventType.Sports, 42, 666);
        assertEquals(provider2, event.getOrganiser(),
                "createTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createTicketedEvent creates ticketed events title correctly")
    void createTicketedEventTitleTest()
    {
        EventState eventState = new EventState();
        Event event = eventState.createTicketedEvent(provider2, "eating",
                EventType.Sports, 42, 666);
        assertEquals("eating", event.getTitle(),
                "createTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createNicketedEvent creates ticketed events type correctly")
    void createTicketedEventTypeTest()
    {
        EventState eventState = new EventState();
        Event event = eventState.createTicketedEvent(provider2, "eating",
                EventType.Sports, 42, 666);
        assertEquals(EventType.Sports, event.getType(),
                "createTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createTicketedEvent creates ticketed event")
    void createTicketedEventClassTest()
    {
        EventState eventState = new EventState();
        Event event = eventState.createTicketedEvent(provider2, "eating",
                EventType.Sports, 42, 666);
        assertTrue(event instanceof TicketedEvent,
                "createTicketedEvent should create ticketed events correctly");
    }

    @Test
    @DisplayName("Testing createTicketedEvent creates ticketed event price correctly")
    void createTicketedEventPriceTest()
    {
        EventState eventState = new EventState();
        TicketedEvent event = eventState.createTicketedEvent(provider2, "eating",
                EventType.Sports, 42, 666);
        assertEquals(42,
                event.getOriginalTicketPrice(),
                "createTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createTicketedEvent creates" +
            " ticketed event ticket count correctly")
    void createTicketedEventTicketCountTest()
    {
        EventState eventState = new EventState();
        TicketedEvent event = eventState.createTicketedEvent(provider2, "eating",
                EventType.Sports, 42, 666);
        assertEquals(666,
                event.getNumTickets(),
                "createTicketedEvent should create events correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct event")
    void createEventPerformanceEventTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(1).plusHours(3),
                List.of("Best performance"),
                false,
                true,
                true,
                69,
                420);
        assertEquals(event, performance.getEvent(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct date start")
    void createEventPerformanceDateStartTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performance"),
                false,
                true,
                true,
                69,
                420);
        assertEquals(LocalDateTime.of(2069, 04,
                20, 4, 20), performance.getStartDateTime(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct date end")
    void createEventPerformanceDateEndTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performance"),
                false,
                true,
                true,
                69,
                420);
        assertEquals(LocalDateTime.of(2069, 04, 20, 16, 20),
                performance.getEndDateTime(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct performer names")
    void createEventPerformancePerformersTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                false,
                true,
                true,
                69,
                420);
        assertEquals(List.of("Best performer"), performance.getPerformerNames(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct social distancing")
    void createEventPerformanceDistancingTrueTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                true, // Set to true
                true,
                true,
                69,
                420);
        assertTrue(performance.hasSocialDistancing(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct social distancing")
    void createEventPerformanceDistancingFalseTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                false, // Set to false
                true,
                true,
                69,
                420);
        assertFalse(performance.hasSocialDistancing(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct air filtration")
    void createEventPerformanceAirFiltrationTrueTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                true,
                true, // Set to true
                true,
                69,
                420);
        assertTrue(performance.hasAirFiltration(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct air filtration")
    void createEventPerformanceAirFiltrationFalseTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                true,
                false, // Set to false
                true,
                69,
                420);
        assertFalse(performance.hasAirFiltration(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct outdoor setting")
    void createEventPerformanceOutdoorsTrueTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                true,
                false,
                true, // Set to true
                69,
                420);
        assertTrue(performance.isOutdoors(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct outdoor setting")
    void createEventPerformanceOutdoorsFalseTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                true,
                false,
                false, // Set to false
                69,
                420);
        assertFalse(performance.isOutdoors(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct capacity setting")
    void createEventPerformanceCapacityTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                true,
                false,
                false,
                69,
                420);
        assertEquals(69, performance.getCapacityLimit(),
                "createEventPerformance should create performances correctly");
    }

    @Test
    @DisplayName("Testing createEventPerformance adds correct venue size setting")
    void createEventPerformanceSizeTest()
    {
        EventState eventState = new EventState();
        EventPerformance performance = eventState.createEventPerformance(event,
                "address",
                LocalDateTime.of(2069, 04, 20, 4, 20),
                LocalDateTime.of(2069, 04, 20, 16, 20),
                List.of("Best performer"),
                true,
                false,
                false,
                69,
                420);
        assertEquals(420, performance.getVenueSize(),
                "createEventPerformance should create performances correctly");
    }
}