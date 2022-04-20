package tests;

import command.*;
import controller.Context;
import controller.Controller;
import model.EntertainmentProvider;
import model.EventType;
import model.SponsorshipRequest;
import model.SponsorshipStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.SponsorshipState;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RespondSponsorshipSystemTest
{
    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    // ================================================================================

    // ================================================================================
    //                           ALL NECESSARY OPERATIONS
    //     INCLUDES: REGISTRATION, LOGIN, LOGOUT, EVENT CREATION, SPONSORSHIP RESPONSE
    // ================================================================================

    private static void loginGovernmentRepresentative(Controller controller)
    {
        controller.runCommand(new LoginCommand("margaret.thatcher@gov.uk", "The Good times  "));
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

        CreateTicketedEventCommand eventCmd2 = new CreateTicketedEventCommand(
                "Winter Olympics",
                EventType.Sports,
                40000,
                400,
                true
        );
        controller.runCommand(eventCmd2);

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

        CreateTicketedEventCommand eventCmd = new CreateTicketedEventCommand(
                "Some event",
                EventType.Theatre,
                1234,
                15,
                true
        );
        controller.runCommand(eventCmd);

        controller.runCommand(new LogoutCommand());
    }

    private static void governmentAcceptAllSponsorships(Controller controller)
    {
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    25));
        }
    }

    private static void governmentRejectAllSponsorships(Controller controller)
    {
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
             controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    0));
        }
    }

    private static void createCinemaProviderWith1Event(Controller controller)
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
                true
        );
        controller.runCommand(eventCmd1);

        controller.runCommand(new LogoutCommand());
    }

    // ================================================================================

    // ================================================================================
    //                                      TESTS
    // ================================================================================

    @Test
    @DisplayName("Test for multiple valid acceptances of a sponsorship request")
    void validAcceptanceTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since all requests were accepted, there should be no pending requests
        assertEquals(2, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship requests were not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipRequests.get(0).getStatus(),
                "First sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipRequests.get(1).getStatus(),
                "Second sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for multiple valid rejections of a sponsorship request")
    void validRejectionTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        loginGovernmentRepresentative(controller);
        governmentRejectAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since all requests were rejected, there should be no pending requests
        assertEquals(2, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship requests were not rejected");
        assertEquals(SponsorshipStatus.REJECTED, sponsorshipRequests.get(0).getStatus(),
                "First sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.REJECTED, sponsorshipRequests.get(1).getStatus(),
                "Second sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for multiple valid acceptances and one rejection of a sponsorship request")
    void multipleAcceptOneRejectTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        createBuskingProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        governmentRejectAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since all requests were either accepted or rejected, there should be no pending requests
        assertEquals(3, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship requests were not responded to");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipState.findRequestByNumber(1).getStatus(),
                "First sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipState.findRequestByNumber(2).getStatus(),
                "Second sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.REJECTED, sponsorshipState.findRequestByNumber(3).getStatus(),
                "Second sponsorship request was not rejected");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for multiple valid rejections and one acceptance of a sponsorship request")
    void multipleRejectOneAcceptTest()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        loginGovernmentRepresentative(controller);
        governmentRejectAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        createBuskingProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since all requests were either accepted or rejected, there should be no pending requests
        assertEquals(3, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship requests were not responded to");
        assertEquals(SponsorshipStatus.REJECTED, sponsorshipState.findRequestByNumber(1).getStatus(),
                "First sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.REJECTED, sponsorshipState.findRequestByNumber(2).getStatus(),
                "Second sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipState.findRequestByNumber(3).getStatus(),
                "Second sponsorship request was not rejected");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for one valid rejection and one acceptance of a sponsorship request")
    void oneAcceptOneRejectTest()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        createBuskingProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        governmentRejectAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since all requests were either accepted or rejected, there should be no pending requests
        assertEquals(2, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship requests were not responded to");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipState.findRequestByNumber(1).getStatus(),
                "First sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.REJECTED, sponsorshipState.findRequestByNumber(2).getStatus(),
                "Second sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for a single valid acceptance of a sponsorship request")
    void oneAcceptTest()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the request was accepted, it should not be pending anymore
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship requests were not responded to");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipState.findRequestByNumber(1).getStatus(),
                "First sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for a single valid rejection of a sponsorship request")
    void oneRejectTest()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        governmentRejectAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the request was rejected, it should not be pending anymore
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship requests were not responded to");
        assertEquals(SponsorshipStatus.REJECTED, sponsorshipState.findRequestByNumber(1).getStatus(),
                "First sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for user not logged in")
    void invalidRequestTest1()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();
        controller.runCommand(new LogoutCommand());

        for (SponsorshipRequest request : requests)
        {
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    0));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the request was invalid, it should still be pending
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship request was not added");
        assertEquals(1, pendingSponsorshipRequests.size(), "Sponsorship request was responded to");
        assertNull(context.getUserState().getCurrentUser(), "Unexpected user logged in!");
        System.out.println("Test not logged in passes successfully!");
    }

    @Test
    @DisplayName("Test for user not a government representative")
    void invalidRequestTest2()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();
        controller.runCommand(new LogoutCommand());

        controller.runCommand(new LoginCommand("odeon@cineworld.com", "F!ghT th3 R@Pture"));

        for (SponsorshipRequest request : requests)
        {
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    0));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the request was invalid, it should still be pending
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship request was not added");
        assertEquals(1, pendingSponsorshipRequests.size(), "Sponsorship request was responded to");
        assertTrue(context.getUserState().getCurrentUser() instanceof EntertainmentProvider,
                "Current user should be an entertainment provider");
        System.out.println("Test not a government rep passes successfully!");
    }

    @Test
    @DisplayName("Test for already rejected request")
    void invalidRequestTest3()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            request.reject();
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    10));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the request was invalid, it should still be pending
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship request was not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship request was responded to");
        System.out.println("Test already rejected request passes successfully!");
    }

    @Test
    @DisplayName("Test for already accepted request")
    void invalidRequestTest4()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            request.accept(10, request.getSponsorAccountEmail());
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    10));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the request was invalid, it should still be pending
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship request was not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship request was responded to");
        System.out.println("Test already accepted request passes successfully!");
    }

    @Test
    @DisplayName("Test for invalid sponsorship percentage")
    void invalidRequestTest5()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    -1));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the request was invalid, it should still be pending
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship request was not added");
        assertEquals(1, pendingSponsorshipRequests.size(), "Sponsorship request was responded to");
        System.out.println("Test invalid percentage passes successfully!");
    }

    @Test
    @DisplayName("Test for invalid sponsorship percentage")
    void invalidRequestTest6()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    120));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the percentage was invalid, it should still be pending
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship request was not added");
        assertEquals(1, pendingSponsorshipRequests.size(), "Sponsorship request was responded to");
        System.out.println("Test invalid percentage passes successfully!");
    }

    @Test
    @DisplayName("Test for valid sponsorship percentage of 100%")
    void validRequestExtremeTest()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    100));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the sponsorship percentage was valid, the request should not be pending
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship request was not added");
        assertEquals(0, pendingSponsorshipRequests.size(), "Sponsorship request was responded to");
        System.out.println("Test extreme sponsorship percentage passes successfully!");
    }

    @Test
    @DisplayName("Test for failed payment")
    void invalidRequestTest7()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.getContext().getUserState().getCurrentUser().setPaymentAccountEmail(null);
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    100));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // since the payment failed, the request should still be pending
        assertEquals(1, sponsorshipRequests.size(), "Sponsorship request was not added");
        assertEquals(1, pendingSponsorshipRequests.size(), "Sponsorship request was responded to");
        System.out.println("Test failed payment passes successfully!");
    }

    @Test
    @DisplayName("Test for multiple valid and one invalid sponsorship request")
    void multipleValidOneInvalid()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.getContext().getUserState().getCurrentUser().setPaymentAccountEmail(null);
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    100));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // the one invalid request should remain pending
        assertEquals(3, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(1, pendingSponsorshipRequests.size(), "Sponsorship requests were not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipRequests.get(0).getStatus(),
                "First sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipRequests.get(1).getStatus(),
                "Second sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for one valid and multiple invalid sponsorship requests")
    void oneValidMultipleInvalid()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        createOlympicsProviderWith2Events(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.getContext().getUserState().getCurrentUser().setPaymentAccountEmail(null);
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    100));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // the one valid request should not be pending
        assertEquals(3, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(2, pendingSponsorshipRequests.size(), "Sponsorship requests were not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipRequests.get(0).getStatus(),
                "First sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for multiple valid and multiple invalid sponsorship requests")
    void multipleValidMultipleInvalid()
    {
        Controller controller = new Controller();

        createOlympicsProviderWith2Events(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        createCinemaProviderWith1Event(controller);
        createBuskingProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.getContext().getUserState().getCurrentUser().setPaymentAccountEmail(null);
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    100));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // the invalid requests should be pending
        assertEquals(4, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(2, pendingSponsorshipRequests.size(), "Sponsorship requests were not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipRequests.get(0).getStatus(),
                "First sponsorship request was not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipRequests.get(1).getStatus(),
                "Second sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }

    @Test
    @DisplayName("Test for one valid and one invalid sponsorship request")
    void oneValidOneInvalid()
    {
        Controller controller = new Controller();

        createCinemaProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        governmentAcceptAllSponsorships(controller);
        controller.runCommand(new LogoutCommand());

        createBuskingProviderWith1Event(controller);

        loginGovernmentRepresentative(controller);
        ListSponsorshipRequestsCommand cmd = new ListSponsorshipRequestsCommand(true);
        controller.runCommand(cmd);
        List<SponsorshipRequest> requests = cmd.getResult();

        for (SponsorshipRequest request : requests)
        {
            controller.getContext().getUserState().getCurrentUser().setPaymentAccountEmail(null);
            controller.runCommand(new RespondSponsorshipCommand(request.getRequestNumber(),
                    100));
        }

        Context context = controller.getContext();
        SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
        List<SponsorshipRequest> sponsorshipRequests = sponsorshipState.getAllSponsorshipRequests();
        List<SponsorshipRequest> pendingSponsorshipRequests = sponsorshipState.getPendingSponsorshipRequests();

        // the invalid request should be pending, and the valid one accepted
        assertEquals(2, sponsorshipRequests.size(), "Sponsorship requests were not added");
        assertEquals(1, pendingSponsorshipRequests.size(), "Sponsorship requests were not accepted");
        assertEquals(SponsorshipStatus.ACCEPTED, sponsorshipRequests.get(0).getStatus(),
                "First sponsorship request was not accepted");
        System.out.println("Test passes successfully!");
    }
}
