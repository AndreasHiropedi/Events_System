package tests;

import model.EntertainmentProvider;
import model.EventType;
import model.SponsorshipRequest;
import model.TicketedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.EventState;
import state.SponsorshipState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestSponsorshipState
{
    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    // create all the necessary objects for the testing
    SponsorshipState sponsorshipState = new SponsorshipState();
    EventState eventState = new EventState();
    EntertainmentProvider organiser1 = new EntertainmentProvider(
            "Omni center", "Around Princess Street", "payment@cinema",
            "John Omni", "john@cinema", "number1cinema", List.of("Maggie Slushie"),
            List.of("maggie@cinema"));
    TicketedEvent event1 = eventState.createTicketedEvent(organiser1, "Event 1",
            EventType.Movie, 4.95, 100);
    TicketedEvent event2 = eventState.createTicketedEvent(organiser1, "Event 2",
            EventType.Movie, 6.95, 200);

    @Test
    @DisplayName("Test copy constructor uses deep copying")
    void deepCopyTest1()
    {
        SponsorshipState newSponsorshipState = new SponsorshipState(sponsorshipState);
        sponsorshipState = null;
        // deep cloned state should not change if the old state is changed
        assertNotNull(newSponsorshipState,
                "After changing the value of the original state, the deep copied state also changes");
        System.out.println("Deep copying test 1 passed!");
    }

    @Test
    @DisplayName("Test copy constructor copies all sponsorship requests")
    void deepCopyTest2()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipState newSponsorshipState = new SponsorshipState(sponsorshipState);
        // since no state is further updated, they should be equal
        assertEquals(sponsorshipState.getAllSponsorshipRequests().size(),
                newSponsorshipState.getAllSponsorshipRequests().size(),
                "The deep copied list of sponsorship requests was not copied correctly");
        System.out.println("Deep copying test 2 passed!");
    }

    @Test
    @DisplayName("Test copy constructor copies all sponsorship requests")
    void deepCopyTest3()
    {
        SponsorshipRequest request = sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipState newSponsorshipState = new SponsorshipState(sponsorshipState);
        // since no state is further updated, they should hae the same sponsorship requests
        assertNotEquals(null,
                newSponsorshipState.findRequestByNumber(request.getRequestNumber()),
                "The deep copied sponsorship state was not copied correctly");
        System.out.println("Deep copying test 3 passed!");
    }

    @Test
    @DisplayName("Test copy constructor uses deep copying for list of sponsorship requests")
    void deepCopyTest4()
    {
        SponsorshipState newSponsorshipState = new SponsorshipState(sponsorshipState);
        SponsorshipRequest request = newSponsorshipState.addSponsorshipRequest(event1);
        // old state should not update if the cloned state is updated
        assertNotEquals(sponsorshipState.findRequestByNumber(request.getRequestNumber()),
                newSponsorshipState.findRequestByNumber(request.getRequestNumber()),
                "After changing the deep copy's list of sponsorship requests" +
                        ", the original one changes as well");
        System.out.println("Deep copying test 4 passed!");
    }

    @Test
    @DisplayName("Test copy constructor initial state")
    void deepCopyTest5()
    {
        SponsorshipState newSponsorshipState = new SponsorshipState(sponsorshipState);
        // since no new requests were added to either state, nothing should be found
        assertNull(newSponsorshipState.findRequestByNumber(1),
                "Invalid sponsorship request was added");
        System.out.println("Deep copying test 5 passed!");
    }

    @Test
    @DisplayName("Test copy constructor uses deep copying for list of sponsorship requests")
    void deepCopyTest6()
    {
        SponsorshipRequest request = sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipState newSponsorshipState = new SponsorshipState(sponsorshipState);
        request.setEvent(event2);
        // deep cloned state should not change if the old state is changed
        assertNotEquals(request.getEvent(),
                newSponsorshipState.findRequestByNumber(1).getEvent(),
                "After changing the original request's event, the deep copy also changes");
        System.out.println("Deep copying test 6 passed!");
    }

    @Test
    @DisplayName("Test addSponsorshipRequest() works as expected for one added request")
    void addSponsorshipRequestTest1()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipRequest request = sponsorshipState.findRequestByNumber(1);
        assertEquals(request, sponsorshipState.getAllSponsorshipRequests().get(0),
                "The added sponsorship request is incorrect");
        System.out.println("Add sponsorship test 1 passed!");
    }

    @Test
    @DisplayName("Test addSponsorshipRequest() works as expected for multiple added requests")
    void addSponsorshipRequestTest2()
    {
        SponsorshipRequest request1 = sponsorshipState.addSponsorshipRequest(event1);
        sponsorshipState.addSponsorshipRequest(event2);
        assertEquals(request1, sponsorshipState.findRequestByNumber(1),
                "The requests were not added successfully!");
        System.out.println("Add sponsorship test 2 passed!");
        System.out.println("Also covers findRequestByNumber() testing for multiple requests");
    }

    @Test
    @DisplayName("Test addSponsorshipRequest() works as expected for multiple added requests")
    void addSponsorshipRequestTest3()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipRequest request2 = sponsorshipState.addSponsorshipRequest(event2);
        assertEquals(request2, sponsorshipState.findRequestByNumber(2),
                "The requests were not added successfully!");
        System.out.println("Add sponsorship test 3 passed!");
        System.out.println("Also covers findRequestByNumber() testing for multiple requests");
    }

    @Test
    @DisplayName("Test addSponsorshipRequest() works as expected for one added request")
    void addInvalidSponsorshipRequestTest()
    {
        SponsorshipRequest invalidRequest = sponsorshipState.addSponsorshipRequest(null);
        assertNull(invalidRequest,
                "An invalid sponsorship request was added");
        System.out.println("Add sponsorship test 4 passed!");
    }

    @Test
    @DisplayName("Test getAllSponsorshipRequests() for initial state (no requests)")
    void getAllSponsorshipRequestsTest1()
    {
        assertEquals(0, sponsorshipState.getAllSponsorshipRequests().size(),
                "Number of sponsorship requests is incorrect");
        System.out.println("Get all sponsorship requests test 1 passed!");
    }

    @Test
    @DisplayName("Test getAllSponsorshipRequests() for one added request")
    void getAllSponsorshipRequestsTest2()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        assertEquals(1, sponsorshipState.getAllSponsorshipRequests().size(),
                "Number of sponsorship requests is incorrect");
        System.out.println("Get all sponsorship requests test 2 passed!");
    }

    @Test
    @DisplayName("Test getAllSponsorshipRequests() for multiple added requests")
    void getAllSponsorshipRequestsTest3()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        sponsorshipState.addSponsorshipRequest(event2);
        assertEquals(2, sponsorshipState.getAllSponsorshipRequests().size(),
                "Number of sponsorship requests is incorrect");
        System.out.println("Get all sponsorship requests test 3 passed!");
    }

    @Test
    @DisplayName("Test getAllSponsorshipRequests() for adding invalid requests")
    void getAllSponsorshipRequestsTest4()
    {
        sponsorshipState.addSponsorshipRequest(null);
        assertEquals(0, sponsorshipState.getAllSponsorshipRequests().size(),
                "Number of sponsorship requests is incorrect");
        System.out.println("Get all sponsorship requests test 4 passed!");
    }

    @Test
    @DisplayName("Test findRequestByNumber() for one request")
    void findRequestByNumberTest1()
    {
        SponsorshipRequest request = sponsorshipState.addSponsorshipRequest(event1);
        assertEquals(request, sponsorshipState.findRequestByNumber(1),
                "A request with that number should exist");
        System.out.println("Find request by number test 1 passed!");
    }

    @Test
    @DisplayName("Test findRequestByNumber() for invalid requests")
    void findRequestByNumberTest2()
    {
        assertNull(sponsorshipState.findRequestByNumber(1),
                "A request with that number should not exist");
        System.out.println("Find request by number test 2 passed!");
    }

    @Test
    @DisplayName("Test findRequestByNumber() for invalid requests after adding one request")
    void findRequestByNumberTest3()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        assertNull(sponsorshipState.findRequestByNumber(2),
                "A request with that number should not exist");
        System.out.println("Find request by number test 3 passed!");
    }

    @Test
    @DisplayName("Test findRequestByNumber() for invalid requests after adding multiple requests")
    void findRequestByNumberTest4()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        sponsorshipState.addSponsorshipRequest(event2);
        assertNull(sponsorshipState.findRequestByNumber(3),
                "A request with that number should not exist");
        System.out.println("Find request by number test 4 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for initial state")
    void getPendingSponsorshipRequestsTest1()
    {
        assertEquals(0, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 1 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for one rejected request")
    void getPendingSponsorshipRequestsTest2()
    {
        SponsorshipRequest request = sponsorshipState.addSponsorshipRequest(event1);
        request.reject();
        assertEquals(0, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 2 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for one accepted request")
    void getPendingSponsorshipRequestsTest3()
    {
        SponsorshipRequest request = sponsorshipState.addSponsorshipRequest(event1);
        request.accept(10, "payment@gov");
        assertEquals(0, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 3 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for multiple accepted requests")
    void getPendingSponsorshipRequestsTest4()
    {
        SponsorshipRequest request1 = sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipRequest request2 = sponsorshipState.addSponsorshipRequest(event2);
        request1.accept(10, "payment@gov");
        request2.accept(10, "payment@gov");
        assertEquals(0, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 4 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for multiple rejected requests")
    void getPendingSponsorshipRequestsTest5()
    {
        SponsorshipRequest request1 = sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipRequest request2 = sponsorshipState.addSponsorshipRequest(event2);
        request1.reject();
        request2.reject();
        assertEquals(0, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 5 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for one accepted and one rejected request")
    void getPendingSponsorshipRequestsTest6()
    {
        SponsorshipRequest request1 = sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipRequest request2 = sponsorshipState.addSponsorshipRequest(event2);
        request1.accept(10, "payment@gov");
        request2.reject();
        assertEquals(0, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 6 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for one pending request")
    void getPendingSponsorshipRequestsTest7()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        assertEquals(1, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 7 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for multiple pending requests")
    void getPendingSponsorshipRequestsTest8()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        sponsorshipState.addSponsorshipRequest(event2);
        assertEquals(2, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 8 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for one pending and one rejected request")
    void getPendingSponsorshipRequestsTest9()
    {
        sponsorshipState.addSponsorshipRequest(event1);
        SponsorshipRequest request2 = sponsorshipState.addSponsorshipRequest(event2);
        request2.reject();
        assertEquals(1, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 9 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for one accepted and one pending request")
    void getPendingSponsorshipRequestsTest10()
    {
        SponsorshipRequest request1 = sponsorshipState.addSponsorshipRequest(event1);
        sponsorshipState.addSponsorshipRequest(event2);
        request1.accept(10, "payment@gov");
        assertEquals(1, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 10 passed!");
    }

    @Test
    @DisplayName("Test getPendingSponsorshipRequests() for invalid requests")
    void getPendingSponsorshipRequestsTest11()
    {
        sponsorshipState.addSponsorshipRequest(null);
        assertEquals(0, sponsorshipState.getPendingSponsorshipRequests().size(),
                "Number of pending sponsorship requests is incorrect");
        System.out.println("Get pending sponsorship requests test 11 passed!");
    }
}
