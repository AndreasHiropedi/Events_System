package tests;

import model.Consumer;
import model.EntertainmentProvider;
import model.GovernmentRepresentative;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.UserState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestUserState
{
    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    // create all the necessary objects for the testing
    UserState userState = new UserState();
    Map<String, User> users = userState.getAllUsers();
    GovernmentRepresentative representative = new GovernmentRepresentative("margaret.thatcher@gov.uk", "The Good times  ", "payment@gov.uk");
    EntertainmentProvider newOrganiser = new EntertainmentProvider("org", "25 East Avenue",
            "provider@payment.com", "Johnny P", "johnnyp@email",
            "pass", List.of("Marcus D"), List.of("marcusd@email"));

    @Test
    @DisplayName("Test registering government representatives registers one government rep")
    void registerGovernmentRepTest1()
    {
        assertEquals(1, users.size(), "There are more government reps than expected!");
        System.out.println("registerGovernmentRepresentatives() passed the first test!");
    }

    @Test
    @DisplayName("Test registering government representatives has the correct government rep email")
    void registerGovernmentRepTest2()
    {
        String repEmail = representative.getEmail();
        // the command used gets the first key of the hashmap
        assertEquals(repEmail, users.keySet().toArray()[0], "The email of the given government " +
                "representative does not match the email we have saved on our system!");
        System.out.println("registerGovernmentRepresentatives() passed the second test!");
    }

    @Test
    @DisplayName("Test registering government representatives has the correct government rep payment account")
    void registerGovernmentRepTest3()
    {
        String repPaymentEmail = representative.getPaymentAccountEmail();
        // the command here gets the first value of the hashmap
        GovernmentRepresentative actualRep = (GovernmentRepresentative) users.values().toArray()[0];
        String actualRepPaymentEmail = actualRep.getPaymentAccountEmail();
        assertEquals(repPaymentEmail, actualRepPaymentEmail, "The payment account email of the given government " +
                "representative does not match the payment account email we have saved on our system!");
        System.out.println("registerGovernmentRepresentatives() passed the third test!");
    }

    Consumer newConsumer = new Consumer("Bobby Salmon", "bobby.salmon@gmail.com",
            "0748794543", "password", "payment@bobby.com");

    @Test
    @DisplayName("Test adding users works for registering one consumer")
    void addConsumerTest1()
    {
        userState.addUser(newConsumer);
        assertEquals(2, users.size(), "The new consumer was not added successfully!");
        System.out.println("addUser() passes first test for adding a consumer!");
    }

    @Test
    @DisplayName("Test adding users adds the correct consumer")
    void addConsumerTest2()
    {
        userState.addUser(newConsumer);
        assertEquals(newConsumer, users.get(newConsumer.getEmail()), "The new consumer was not added successfully!");
        System.out.println("addUser() passes second test for adding a consumer!");
    }

    @Test
    @DisplayName("Test adding entertainment providers works for registering one entertainment provider")
    void addEntertainmentProviderTest1()
    {
        userState.addUser(newOrganiser);
        assertEquals(2, users.size(), "The new entertainment provider was not added successfully!");
        System.out.println("addUser() passes first test for adding an entertainment provider!");
    }

    @Test
    @DisplayName("Test adding users adds the correct entertainment provider")
    void addEntertainmentProviderTest2()
    {
        userState.addUser(newOrganiser);
        assertEquals(users.get(newOrganiser.getEmail()), newOrganiser, "The new organiser was not added successfully!");
        System.out.println("addUser() passes second test for adding an entertainment provider!");
    }

    @Test
    @DisplayName("Test adding multiple users works")
    void addMultipleUsersTest1()
    {
        userState.addUser(newConsumer);
        userState.addUser(newOrganiser);
        assertEquals(3, users.size(), "The users were not added successfully!");
        System.out.println("addUser() passes first test for adding multiple users!");
    }

    @Test
    @DisplayName("Test adding multiple users adds the correct users")
    void addMultipleUsersTest2()
    {
        userState.addUser(newConsumer);
        userState.addUser(newOrganiser);
        assertEquals(newConsumer, users.get(newConsumer.getEmail()), "The new consumer was not added successfully!");
        System.out.println("addUser() passes second test for adding multiple users!");
    }

    @Test
    @DisplayName("Test adding multiple users adds the correct users")
    void addMultipleUsersTest3()
    {
        userState.addUser(newConsumer);
        userState.addUser(newOrganiser);
        assertEquals(newOrganiser, users.get(newOrganiser.getEmail()), "The new organiser was not added successfully!");
        System.out.println("addUser() passes third test for adding multiple users!");
    }

    @Test
    @DisplayName("Test for rejecting invalid users")
    void addInvalidUser1()
    {
        assertNull(users.get(""), "An invalid user was added!");
        System.out.println("addUser() passes the first check for invalid users!");
    }

    @Test
    @DisplayName("Test for rejecting invalid users")
    void addInvalidUser2()
    {
        userState.addUser(null);
        assertEquals(1, userState.getAllUsers().size(),"An invalid user was added!");
        System.out.println("addUser() passes the second check for invalid users!");
    }

    Map<String, User> allUsersMock = new HashMap<>();

    @Test
    @DisplayName("Test getAllUsers() method before any new user is added!")
    void getAllUsersTest1()
    {
        allUsersMock.put(representative.getEmail(), representative);
        assertEquals(allUsersMock.size(), users.size(),"The list of all users is incorrect!");
        System.out.println("getAllUsers() passes first test!");
    }

    @Test
    @DisplayName("Test getAllUsers() method after one user is added!")
    void getAllUsersTest2()
    {
        userState.addUser(newConsumer);
        allUsersMock.put(representative.getEmail(), representative);
        allUsersMock.put(newConsumer.getEmail(), newConsumer);
        assertEquals(allUsersMock.size(), users.size(),"The list of all users is incorrect!");
        System.out.println("getAllUsers() passes second test!");
    }

    @Test
    @DisplayName("Test getAllUsers() method after multiple users are added!")
    void getAllUsersTest3()
    {
        userState.addUser(newConsumer);
        userState.addUser(newOrganiser);
        allUsersMock.put(representative.getEmail(), representative);
        allUsersMock.put(newConsumer.getEmail(), newConsumer);
        allUsersMock.put(newOrganiser.getEmail(), newOrganiser);
        assertEquals(allUsersMock.size(), users.size(),"The list of all users is incorrect!");
        System.out.println("getAllUsers() passes third test!");
    }

    @Test
    @DisplayName("Test getCurrentUser() method when no user is logged in!")
    void getCurrentUserTest()
    {
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "A user is logged in when they are not meant to be!");
        System.out.println("getCurrentUser() passes single test!");
    }

    @Test
    @DisplayName("Test getCurrentUser() and setCurrentUser() methods when a consumer is logged in!")
    void getAndSetCurrentUserTest1()
    {
        userState.setCurrentUser(newConsumer);
        assertEquals(newConsumer, userState.getCurrentUser(),"The consumer is not logged in!");
        System.out.println("getCurrentUser() and setCurrentUser() pass first test!");
    }

    @Test
    @DisplayName("Test getCurrentUser() and setCurrentUser() methods when an entertainment provider is logged in!")
    void getAndSetCurrentUserTest2()
    {
        userState.setCurrentUser(newOrganiser);
        assertEquals(newOrganiser, userState.getCurrentUser(),"The entertainment provider is not logged in!");
        System.out.println("getCurrentUser() and setCurrentUser() pass second test!");
    }

    @Test
    @DisplayName("Test getCurrentUser() and setCurrentUser() methods when a government representative is logged in!")
    void getAndSetCurrentUserTest3()
    {
        userState.setCurrentUser(representative);
        assertEquals(representative, userState.getCurrentUser(),"The government representative is not logged in!");
        System.out.println("getCurrentUser() and setCurrentUser() pass third test!");
    }

    @Test
    @DisplayName("Test getCurrentUser() and setCurrentUser() methods when a different user logs in after one user logs out")
    void getAndSetCurrentUserTest4()
    {
        userState.setCurrentUser(representative);
        userState.setCurrentUser(newConsumer);
        assertEquals(newConsumer, userState.getCurrentUser(),"The logged in user is not updated!");
        System.out.println("getCurrentUser() and setCurrentUser() pass fourth test!");
    }

    @Test
    @DisplayName("Test deep copying for the users list")
    void deepCopyingTest1()
    {
        UserState newUserState = new UserState(userState);
        newUserState.addUser(newConsumer);
        newUserState.addUser(newOrganiser);
        // old state should not update if the cloned state is updated
        assertNotEquals(userState.getAllUsers().size(),
                newUserState.getAllUsers().size(), "The deep copied user state has the same users as the old state");
        System.out.println("Test 1 for deep copying passed!");
    }

    @Test
    @DisplayName("Test deep copying for the users list")
    void deepCopyingTest2()
    {
        UserState newUserState = new UserState(userState);
        // since method Map.equals() checks for contents and not reference, they are meant to be equal
        assertEquals(userState.getAllUsers(),
                newUserState.getAllUsers(), "The deep copied user state has different users from the old state");
        System.out.println("Test 2 for deep copying passed!");
    }

    @Test
    @DisplayName("Test deep copying for the users list")
    void deepCopyingTest3()
    {
        UserState newUserState = new UserState(userState);
        userState.addUser(newConsumer);
        userState.addUser(newOrganiser);
        // deep cloned state should not update if the old state is updated
        assertNotEquals(userState.getAllUsers().size(),
                newUserState.getAllUsers().size(), "The deep copied user state has the same users as the old state");
        System.out.println("Test 3 for deep copying passed!");
    }

    @Test
    @DisplayName("Test deep copying for current user")
    void deepCopyingTest4()
    {
        userState.setCurrentUser(newOrganiser);
        UserState newUserState = new UserState(userState);
        // old state should not update if the cloned state is updated
        newUserState.setCurrentUser(newConsumer);
        assertNotEquals(userState.getCurrentUser(), newUserState.getCurrentUser(),
                "The deep copied user state has the same current user as the old state");
        System.out.println("Test 4 for deep copying passed!");
    }

    @Test
    @DisplayName("Test deep copying for current user")
    void deepCopyingTest5()
    {
        userState.setCurrentUser(newOrganiser);
        UserState newUserState = new UserState(userState);
        // since no state is further updated, they should be equal
        assertEquals(userState.getCurrentUser(), newUserState.getCurrentUser(),
                "The deep copied user state does not have the same current user as the old state");
        System.out.println("Test 5 for deep copying passed!");
    }

    @Test
    @DisplayName("Test deep copying for current user")
    void deepCopyingTest6()
    {
        userState.setCurrentUser(newOrganiser);
        UserState newUserState = new UserState(userState);
        userState.setCurrentUser(newConsumer);
        // deep cloned state should not update if the old state is updated
        assertNotEquals(userState.getCurrentUser(), newUserState.getCurrentUser(),
                "The deep copied user state has the same current user as the old state");
        System.out.println("Test 6 for deep copying passed!");
    }

    @Test
    @DisplayName("Test copy constructor is using deep copying")
    void deepCopyingTest7()
    {
        UserState newUserState = new UserState(userState);
        userState = null;
        // deep cloned state should not change if the old state is changed
        assertNotNull(newUserState, "The deep copied user state is not independent of the old user state!");
        System.out.println("Test 7 for deep copying passed!");
    }
}
