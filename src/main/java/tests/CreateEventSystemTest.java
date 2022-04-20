package tests;

import command.*;
import controller.Controller;
import model.Event;
import model.EventPerformance;
import model.EventType;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventSystemTest
{
    // ================================================================================
    //                                    DECORATORS
    // ================================================================================

    // Output colors - for flagging errors, successes and warnings.
    private final String
            rst   = "\u001B[0m",    // DEFAULT
            g = "\u001B[32m",       // GREEN
            y = "\u001B[33m",       // YELLOW
            r = "\u001B[31m";       // RED

    private Controller ctrl;

    @BeforeEach
    void printTestName(TestInfo testInfo)
    {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(y+"Running "+testInfo.getDisplayName()+"\n"+rst);
        System.out.println(y+"Creating new controller..."+rst);
        ctrl = new Controller();
    }

    @AfterEach
    void endTest(TestInfo testInfo)
    {
        System.out.println(g+"\nFinished "+testInfo.getDisplayName()+rst);
        //showLogger();
        //System.out.println(y+"Clearing logger..."+rst);
        //Logger.getInstance().clearLog();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++\n");
    }

    // ================================================================================

    // ================================================================================
    //                                      TESTS
    // ================================================================================

    @Test
    @DisplayName("Test for one event with one performance.")
    void oneEventOnePerformance()
    {
        registerOneEntertainmentProvider();

        boolean login = logInAsMatty();
        assert login: "Failed to log in!";
        checkpoint("Log in successful!");

        if(login)
        {
            createTicketedVolleyballEvent();
            newPerformance(Integer.toUnsignedLong(1),
                    2022,6,1,12,0,
                    2022,6,1,13,0,
                    false);

            listEventsAndPerformances(1,1, true);
        }
    }

    @Test
    @DisplayName("Test for two events with two performances")
    void twoEventsTwoPerformances()
    {
        registerOneEntertainmentProvider();

        boolean login = logInAsMatty();
        assert login: "Failed to log in!";
        checkpoint("Log in successful!");

        // Events ------>
        Long evNo = createTicketedVolleyballEvent();
        assert evNo == 1: String.format("Expected event number 1 - got %d.",evNo);
        checkpoint("Valid event number!");

        evNo = createNonTicketedGamesNight();
        assert evNo == 2: String.format("Expected event number 2 - got %d.",evNo);
        checkpoint("Valid event number!");

        // Performances ------>
        newPerformance(Integer.toUnsignedLong(1),
                2022,6,1,12,0,
                2022,6,1,13,0,
                false);

        newPerformance(Integer.toUnsignedLong(2),
                2022,6,1,21,0,
                2022,6,2,0,0,
                false);
        newPerformance(Integer.toUnsignedLong(2),
                2022,6,2,21,0,
                2022, 6,3,0,0,
                false);

        newPerformance(Integer.toUnsignedLong(1),
                2022,6,2,12,0,
                2022,6,2,13,0,
                false);

        listEventsAndPerformances(2,2, true);
    }

    @Test
    @DisplayName("Test for one event with two performances at the same time.")
    void oneEventTwoPerformanceSameTimes()
    {
        registerOneEntertainmentProvider();

        boolean login = logInAsMatty();
        assert login: "Failed to log in!";
        checkpoint("Log in successful!");

        createTicketedVolleyballEvent();
        newPerformance(Integer.toUnsignedLong(1),
                2022,6,1,12,0,
                2022,6,1,13,0,
                false);

        newPerformance(Integer.toUnsignedLong(1),
                2022,6,1,12,0,
                2022,6,1,13,0,
                true);

        listEventsAndPerformances(1,1, true);
    }

    @Test
    @DisplayName("Test for one event with two performances at overlapping times.")
    void oneEventTwoPerformanceOverlappingTimes()
    {
        registerOneEntertainmentProvider();

        boolean login = logInAsMatty();
        assert login: "Failed to log in!";
        checkpoint("Log in successful!");

        createTicketedVolleyballEvent();

        newPerformance(Integer.toUnsignedLong(1),
                2022,6,1,12,0,
                2022,6,1,14,0,
                false);

        newPerformance(Integer.toUnsignedLong(1),
                2022,6,1,13,0,
                2022,6,1,15,0,
                false);

        listEventsAndPerformances(1,2, true);
    }

    @Test
    @DisplayName("Test for two events with two performances from different organisers.")
    void twoEventsTwoPerformancesDifferentProviders()
    {
        registerTwoEntertainmentProviders();

        boolean login = logInAsMatty();
        assert login: "Failed to log in!";
        checkpoint("Log in successful!");

        createTicketedVolleyballEvent();
        newPerformance(Integer.toUnsignedLong(1),
                2022,6,1,12,0,
                2022,6,1,14,0,
                false);

        listEventsAndPerformances(1,1, true);

        System.out.println();
        logout();
        System.out.println();

        login = logInAsSlavomir();
        assert login: "Failed to log in!";
        checkpoint("Log in successful!");

        createNonTicketedGamesNight();
        newPerformance(Integer.toUnsignedLong(2),
                2022,6,1,21,0,
                2022,6,2,0,0,
                false);

        listEventsAndPerformances(2, 1, false);
    }

    @Test
    @DisplayName("Test for adding a performance to an event of a different organiser.")
    void addPerformanceToDifferentProvider()
    {
        registerTwoEntertainmentProviders();

        boolean login = logInAsMatty();
        assert login: "Failed to log in!";
        checkpoint("Log in successful!");

        createTicketedVolleyballEvent();
        newPerformance(Integer.toUnsignedLong(1),
                2022,6,1,12,0,
                2022,6,1,14,0,
                false);

        listEventsAndPerformances(1,1, true);

        System.out.println();
        logout();
        System.out.println();

        login = logInAsSlavomir();
        assert login: "Failed to log in!";
        checkpoint("Log in successful!");

        createNonTicketedGamesNight();
        newPerformance(Integer.toUnsignedLong(1),
                2022,6,1,21,0,
                2022,6,2,0,0,
                true);

        try
        {
            listEventsAndPerformances(2, 1, false);
        }
        catch (AssertionError e)
        {
            checkpoint("Caught expected assertion error; " +
                    "smaller number of performances implies that performance has not been added!");
        }
    }

    @Test
    @DisplayName("Test for event creation by a consumer.")
    void createEventAsConsumer()
    {
        registerConsumer();
        boolean login = logInAsJeremy();

        assert login: "Failed to log in!";
        checkpoint("Log in successful!");;

        createTicketedVolleyballEvent();

        int numberOfEvents = ctrl.getContext().getEventState().getAllEvents().size();
        assert numberOfEvents == 0:
                "Expecting no events from a consumer! "
                        + String.format("Got %s.", numberOfEvents);
        checkpoint("No events added!");
    }
    // ================================================================================

    // ================================================================================
    //                             REGISTRATIONS AND LOGINS
    // ================================================================================

    private void logout()
    {
        LogoutCommand cmd = new LogoutCommand();

        ctrl.runCommand(cmd);
    }

    private void registerOneEntertainmentProvider()
    {
        String orgName = "Awesome Volleyball Rec", orgAddress = "Meadows",
                paymentAccount = "rgb@rbs.co.uk", mainRepName = "Matty",
                mainRepEmail = "matty@gmail.com", password = "pWord";

        List<String> otherRepNames = List.of("One Dude", "Other Dude"),
                otherRepEmails = List.of("oneDude@hotmail.com", "otherDude@gmail.com");

        RegisterEntertainmentProviderCommand cmd = new RegisterEntertainmentProviderCommand(
                orgName,
                orgAddress,
                paymentAccount,
                mainRepName,
                mainRepEmail,
                password,
                otherRepNames,
                otherRepEmails
        );

        ctrl.runCommand(cmd);

        assert cmd.getResult() != null: "Command result is null!";
        checkpoint("Result is not null!");

        assert cmd.getResult().getOrgName().equals(orgName)
                && cmd.getResult().getOrgAddress().equals(orgAddress)
                && cmd.getResult().getPaymentAccountEmail().equals(paymentAccount)
                && cmd.getResult().getEmail().equals(mainRepEmail):
                "Result does not match the user details!";
        checkpoint("User details assigned successfully!");
        System.out.println(cmd.getResult().toString());
    }

    private void registerTwoEntertainmentProviders()
    {
        // Entertainment Provider One ---->
        String orgName = "Awesome Volleyball Rec", orgAddress = "Meadows",
                paymentAccount = "rgb@rbs.co.uk", mainRepName = "Matty",
                mainRepEmail = "matty@gmail.com", password = "pWord";

        List<String> otherRepNames = List.of("One Dude", "Other Dude"),
                otherRepEmails = List.of("oneDude@hotmail.com", "otherDude@gmail.com");

        RegisterEntertainmentProviderCommand cmd = new RegisterEntertainmentProviderCommand(
                orgName,
                orgAddress,
                paymentAccount,
                mainRepName,
                mainRepEmail,
                password,
                otherRepNames,
                otherRepEmails
        );

        ctrl.runCommand(cmd);

        assert cmd.getResult() != null: "Command result is null!";
        checkpoint("Result is not null!");

        assert(cmd.getResult().getOrgName().equals(orgName)
                && cmd.getResult().getOrgAddress().equals(orgAddress)
                && cmd.getResult().getPaymentAccountEmail().equals(paymentAccount)
                && cmd.getResult().getEmail().equals(mainRepEmail)
        );
        checkpoint("Credentials assigned successfully!");
        System.out.println(cmd.getResult().toString());

        // Entertainment Provider Two ---->
        orgName = "Awesome Programming Fun";
        orgAddress = "University of Edinburgh, Appleton Tower";
        paymentAccount = "apf@rbs.co.uk";
        mainRepName = "Slavomir";
        mainRepEmail = "slavoman@gmail.com";
        password = "pWord";

        otherRepNames = List.of("This Dude", "That Dude");
        otherRepEmails = List.of("thisDude@hotmail.com", "thatDude@gmail.com");

        cmd = new RegisterEntertainmentProviderCommand(
                orgName,
                orgAddress,
                paymentAccount,
                mainRepName,
                mainRepEmail,
                password,
                otherRepNames,
                otherRepEmails
        );

        ctrl.runCommand(cmd);

        assert cmd.getResult() != null: "Command result is null!";
        checkpoint("Result is not null!");

        assert(cmd.getResult().getOrgName().equals(orgName)
                && cmd.getResult().getOrgAddress().equals(orgAddress)
                && cmd.getResult().getPaymentAccountEmail().equals(paymentAccount)
                && cmd.getResult().getEmail().equals(mainRepEmail)
        );
        checkpoint("Credentials assigned successfully!");
        System.out.println(cmd.getResult().toString());
    }

    private void registerConsumer()
    {
        String name="Jeremy", email="jerry@yahoo.com", phoneNumber="08236343846",
                password="pWord", paymentAccount="rgb@rbs.co.uk";

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                name,
                email,
                phoneNumber,
                password,
                paymentAccount
        );
        ctrl.runCommand(cmd);

        assert cmd.getResult() != null: "Command result is null!";
        checkpoint(String.format("Command result is not null! - %s", cmd.getResult().getClass()));

        System.out.println(cmd.getResult());
    }

    private boolean logInAsMatty()
    {
        String email = "matty@gmail.com";
        System.out.println(y+String.format("Logging in with %s", email)+rst);
        LoginCommand cmd = new LoginCommand(email,"pWord");
        ctrl.runCommand(cmd);
        return cmd.getResult() != null;
    }

    private boolean logInAsSlavomir()
    {
        String email = "slavoman@gmail.com";
        System.out.println(y+String.format("Logging in with %s", email)+rst);
        LoginCommand cmd = new LoginCommand(email,"pWord");
        ctrl.runCommand(cmd);
        return cmd.getResult() != null;
    }

    private boolean logInAsJeremy()
    {
        String email = "jerry@yahoo.com";
        System.out.println(y+String.format("Logging in with %s", email)+rst);
        LoginCommand cmd = new LoginCommand(email,"pWord");
        ctrl.runCommand(cmd);
        return cmd.getResult() != null;
    }

    // ================================================================================

    // ================================================================================
    //                                     CREATORS
    // ================================================================================

    private Long createTicketedVolleyballEvent()
    {
        String title="Recreational Volleyball";
        EventType type=EventType.Sports;
        int tickets=50, price=5;

        CreateTicketedEventCommand cmd = new CreateTicketedEventCommand(
                title,
                type,
                tickets,
                price,
                false
        );

        ctrl.runCommand(cmd);

        // Return event number as result.
        return cmd.getResult();
    }

    private Long createNonTicketedGamesNight()
    {
        String title="Games Night";
        EventType type=EventType.Sports;
        CreateNonTicketedEventCommand cmd = new CreateNonTicketedEventCommand(
                title,
                type
        );
        ctrl.runCommand(cmd);

        // Return event number as result.
        return cmd.getResult();
    }

    private void newPerformance(Long eventNo,
                                int startYear,
                                int startMonth,
                                int startDay,
                                int startHr,
                                int startMin,
                                int endYear,
                                int endMonth,
                                int endDay,
                                int endHr,
                                int endMin,
                                boolean expectNull)
    {
        AddEventPerformanceCommand cmd = new AddEventPerformanceCommand(
                eventNo,
                "Meadows",
                LocalDateTime.of(startYear, startMonth, startDay, startHr, startMin),
                LocalDateTime.of(endYear, endMonth, endDay, endHr, endMin),
                new ArrayList<>(),
                false,
                false,
                true,
                12,
                12
        );

        ctrl.runCommand(cmd);

        // Set assertion error message based on whether a null performance is expected. Set assertMsg to
        // "not null" if null performance is expected, otherwise set to "null".
        String assertMsg = expectNull ? "not null": "null";

        EventPerformance result = cmd.getResult();

        // Assertion based on the truth values of result==null and expectNull. We want to assert for when
        // result is null and is expected, and when result is not null and null is not expected.
        // Essentially, we want result==null and expect null to be equal in order to pass.
        assert (result == null) == expectNull : String.format("Unexpected %s performance!",assertMsg);
    }

    private void listEventsAndPerformances(int expectedNumberOfEvents,
                                           int expectedNumberOfPerformances,
                                           boolean userEvents)
    {
        ListEventsCommand cmd = new ListEventsCommand(userEvents, true);
        ctrl.runCommand(cmd);
        List<Event> events = cmd.getResult();

        assert events != null: "Event list is null!";
        checkpoint("List of events is not null");
        assert events.size() > 0: "Event list is empty! At least one item is expected!";
        checkpoint("List of events has at least one item!");

        assert events.size() == expectedNumberOfEvents:
                String.format("Unexpected number of events! Expecting %d - got %d.",
                        expectedNumberOfEvents,
                        events.size());
        checkpoint(String.format("Got %d event(s) in the list!",expectedNumberOfEvents));

        for(int i=0; i<events.size(); i++)
        {
            Event curr_event = events.get(i);
            assert curr_event != null: String.format("Event at index %d is null!",i);
            System.out.println(curr_event.getEventNumber()
                    +": "
                    +curr_event.getTitle()
                    +" by "
                    +curr_event.getOrganiser().getOrgName());
            listPerformances(curr_event);

            assert events.get(i).getPerformances().size() == expectedNumberOfPerformances:
                    String.format("Unexpected number of performances! Expecting %d - got %d.",
                            expectedNumberOfPerformances,
                            events.get(i).getPerformances().size());
            checkpoint(String.format("Got %d performance(s) in the list!", expectedNumberOfPerformances));
        }
    }

    private void checkpoint(String message)
    {
        System.out.println((g+"CHECKPOINT - "+message+rst));
    }

    private void listPerformances(Event e)
    {
        for(EventPerformance p : e.getPerformances())
        {
            System.out.println("    "
                    +p.getPerformanceNumber()
                    +": Start: "
                    +p.getStartDateTime()
                    +" End: "
                    +p.getEndDateTime());
        }
    }
}
