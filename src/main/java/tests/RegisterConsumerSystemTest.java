package tests;

import command.RegisterConsumerCommand;
import controller.Context;
import model.Consumer;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.UserState;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterConsumerSystemTest
{
    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    // setting up the context ahead of testing
    Context context = new Context();

    // ================================================================================

    // ================================================================================
    //                                      TESTS
    // ================================================================================

    @Test
    @DisplayName("Test for one valid consumer")
    void validConsumerTest()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // check all consumer details are correct
        assertNotNull(registeredConsumer, "Consumer should have been registered");
        assertEquals("Johnny", registeredConsumer.getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", registeredConsumer.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", registeredConsumer.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", registeredConsumer.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(2, allUsers.size(), "Consumer should have been registered");

        User currentUser = userState.getCurrentUser();
        // check that the currently logged-in user has been set to the newly registered consumer
        assertTrue(currentUser instanceof Consumer, "Logged in user should be a consumer");
        assertEquals("Johnny", ((Consumer) currentUser).getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", currentUser.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", ((Consumer) currentUser).getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", currentUser.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(currentUser.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");
        System.out.println("Valid consumer test passed!");
    }

    @Test
    @DisplayName("Test for invalid name")
    void invalidNameTest1()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                null, "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject null name
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid name test 1 passed!");
    }

    @Test
    @DisplayName("Test for invalid name")
    void invalidNameTest2()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject empty string name
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid name test passed!");
    }

    @Test
    @DisplayName("Test for invalid email")
    void invalidEmailTest1()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", null, "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject null email
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid email test 1 passed!");
    }

    @Test
    @DisplayName("Test for invalid email")
    void invalidEmailTest2()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject empty string email
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid email test 2 passed!");
    }

    @Test
    @DisplayName("Test for invalid phone number")
    void invalidPhoneTest1()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", null,
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject null phone number
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid phone number test 1 passed!");
    }

    @Test
    @DisplayName("Test for invalid phone number")
    void invalidPhoneTest2()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject empty string phone number
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid phone number test 2 passed!");
    }

    @Test
    @DisplayName("Test for invalid password")
    void invalidPasswordTest1()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                null, "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject null password
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid password test 1 passed!");
    }

    @Test
    @DisplayName("Test for invalid password")
    void invalidPasswordTest2()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject empty string password
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid password test 2 passed!");
    }

    @Test
    @DisplayName("Test for invalid payment account email")
    void invalidPaymentAccountTest1()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", null);
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject null payment account email
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid payment account email test 1 passed!");
    }

    @Test
    @DisplayName("Test for invalid payment account email")
    void invalidPaymentAccountTest2()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // reject empty string email
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid payment account email test 2 passed!");
    }

    @Test
    @DisplayName("Test for not adding one valid consumer already registered")
    void validConsumerAlreadyRegisteredTest()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        RegisterConsumerCommand command2 = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command2.execute(context);
        Consumer registeredConsumer = command2.getResult();
        // check already registered user isn't registered again
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(2, allUsers.size(), "Consumer should have been registered");

        User currentUser = userState.getCurrentUser();
        // current user should still be the registered consumer, despite new attempt to re-register
        assertTrue(currentUser instanceof Consumer, "Logged in user should be a consumer");
        assertEquals("Johnny", ((Consumer) currentUser).getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", currentUser.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", ((Consumer) currentUser).getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", currentUser.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(currentUser.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");
        System.out.println("Already registered consumer test passes for one registered consumer!");
    }

    @Test
    @DisplayName("Test for several invalid details")
    void multipleInvalidDetailsTest()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                null, "johnny@gmail.com", "",
                "myNameIsJohnny", null);
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // if more than just one detail is null or empty string, reject registration
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Several invalid details test passed!");
        System.out.println("Note: command should only alert the user for first invalid detail");
    }

    @Test
    @DisplayName("Test for all invalid details")
    void allInvalidDetailsTest()
    {
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                null, "", null,
                "", null);
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        // if all details are null or empty, reject registration
        assertNull(registeredConsumer, "Consumer should not have been registered");

        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // and hence don't log anyone in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("All invalid details test passed!");
        System.out.println("Note: command should only alert the user for first invalid detail");
    }

    @Test
    @DisplayName("Test for just multiple valid consumers")
    void validConsumersTest()
    {
        // consumer 1 registered successfully
        RegisterConsumerCommand command1 = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command1.execute(context);
        Consumer registeredConsumer1 = command1.getResult();
        assertNotNull(registeredConsumer1, "Consumer should have been registered");
        assertEquals("Johnny", registeredConsumer1.getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", registeredConsumer1.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", registeredConsumer1.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", registeredConsumer1.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer1.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");

        // consumer 2 registered successfully
        RegisterConsumerCommand command2 = new RegisterConsumerCommand(
                "Will", "will@gmail.com", "0756532298",
                "myNameIsWill", "will@payments.com");
        command2.execute(context);
        Consumer registeredConsumer2 = command2.getResult();
        assertNotNull(registeredConsumer1, "Consumer should have been registered");
        assertEquals("Will", registeredConsumer2.getName(),
                "Invalid name was registered");
        assertEquals("will@gmail.com", registeredConsumer2.getEmail(),
                "Invalid email was registered");
        assertEquals("0756532298", registeredConsumer2.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("will@payments.com", registeredConsumer2.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer2.checkPasswordMatch("myNameIsWill"),
                "Invalid password was registered");

        // check they were recorded on the system
        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(3, allUsers.size(), "Consumers should have been registered");

        User currentUser = userState.getCurrentUser();
        // check current user is the last consumer to be registered
        assertTrue(currentUser instanceof Consumer, "Logged in user should be a consumer");
        assertEquals("Will", ((Consumer) currentUser).getName(),
                "Invalid name was registered");
        assertEquals("will@gmail.com", currentUser.getEmail(),
                "Invalid email was registered");
        assertEquals("0756532298", ((Consumer) currentUser).getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("will@payments.com", currentUser.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(currentUser.checkPasswordMatch("myNameIsWill"),
                "Invalid password was registered");
        System.out.println("Valid consumers test passed!");
    }

    @Test
    @DisplayName("Test for just multiple invalid consumers")
    void invalidConsumersTest()
    {
        // invalid consumer 1 should be rejected
        RegisterConsumerCommand command1 = new RegisterConsumerCommand(
                null, "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "");
        command1.execute(context);
        Consumer registeredConsumer1 = command1.getResult();
        assertNull(registeredConsumer1, "Consumer should not have been registered");

        // invalid consumer 2 should be rejected
        RegisterConsumerCommand command2 = new RegisterConsumerCommand(
                "Will", "", "0756532298",
                "myNameIsWill", null);
        command2.execute(context);
        Consumer registeredConsumer2 = command2.getResult();
        assertNull(registeredConsumer2, "Consumer should not have been registered");

        // check that they weren't recorded on the system
        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(1, allUsers.size(), "Consumer should not have been registered");

        User currentUser = userState.getCurrentUser();
        // check that nobody was logged in
        assertNull(currentUser, "No user should have been logged in");
        System.out.println("Invalid consumers test passed!");
    }

    @Test
    @DisplayName("Test for one valid and one invalid consumer")
    void oneValidOneInvalidConsumerTest()
    {
        // valid consumer
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        assertNotNull(registeredConsumer, "Consumer should have been registered");
        assertEquals("Johnny", registeredConsumer.getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", registeredConsumer.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", registeredConsumer.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", registeredConsumer.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");

        // invalid consumer
        RegisterConsumerCommand command2 = new RegisterConsumerCommand(
                "Will", "", "0756532298",
                "myNameIsWill", null);
        command2.execute(context);
        Consumer registeredConsumer2 = command2.getResult();
        assertNull(registeredConsumer2, "Consumer should not have been registered");

        // check only the valid consumer was recorded
        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(2, allUsers.size(), "Consumer should have been registered");

        User currentUser = userState.getCurrentUser();
        // check current user is the valid consumer
        assertTrue(currentUser instanceof Consumer, "Logged in user should be a consumer");
        assertEquals("Johnny", ((Consumer) currentUser).getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", currentUser.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", ((Consumer) currentUser).getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", currentUser.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(currentUser.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");
        System.out.println("One valid and one invalid consumer test passed!");
    }

    @Test
    @DisplayName("Test for one valid and multiple invalid consumers")
    void oneValidMultipleInvalidConsumerTest()
    {
        // valid consumer
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        assertNotNull(registeredConsumer, "Consumer should have been registered");
        assertEquals("Johnny", registeredConsumer.getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", registeredConsumer.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", registeredConsumer.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", registeredConsumer.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");

        // invalid consumer 1
        RegisterConsumerCommand command2 = new RegisterConsumerCommand(
                "Will", "", "0756532298",
                "myNameIsWill", null);
        command2.execute(context);
        Consumer registeredConsumer2 = command2.getResult();
        assertNull(registeredConsumer2, "Consumer should not have been registered");

        // invalid consumer 2
        RegisterConsumerCommand command3 = new RegisterConsumerCommand(
                null, "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "");
        command3.execute(context);
        Consumer registeredConsumer3 = command3.getResult();
        assertNull(registeredConsumer3, "Consumer should not have been registered");

        // check only the valid consumer was recorded
        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(2, allUsers.size(), "Consumer should have been registered");

        User currentUser = userState.getCurrentUser();
        // check current user is the valid consumer
        assertTrue(currentUser instanceof Consumer, "Logged in user should be a consumer");
        assertEquals("Johnny", ((Consumer) currentUser).getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", currentUser.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", ((Consumer) currentUser).getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", currentUser.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(currentUser.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");
        System.out.println("One valid and multiple invalid consumers test passed!");
    }

    @Test
    @DisplayName("Test for multiple valid and one invalid consumer")
    void multipleValidOneInvalidConsumerTest()
    {
        // valid consumer 1
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        assertNotNull(registeredConsumer, "Consumer should have been registered");
        assertEquals("Johnny", registeredConsumer.getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", registeredConsumer.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", registeredConsumer.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", registeredConsumer.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");

        // invalid consumer
        RegisterConsumerCommand command2 = new RegisterConsumerCommand(
                null, "", "",
                "", null);
        command2.execute(context);
        Consumer registeredConsumer2 = command2.getResult();
        assertNull(registeredConsumer2, "Consumer should not have been registered");

        // valid consumer 2
        RegisterConsumerCommand command3 = new RegisterConsumerCommand(
                "Will", "will@gmail.com", "0756532298",
                "myNameIsWill", "will@payments.com");
        command3.execute(context);
        Consumer registeredConsumer3 = command3.getResult();
        assertNotNull(registeredConsumer3, "Consumer should have been registered");
        assertEquals("Will", registeredConsumer3.getName(),
                "Invalid name was registered");
        assertEquals("will@gmail.com", registeredConsumer3.getEmail(),
                "Invalid email was registered");
        assertEquals("0756532298", registeredConsumer3.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("will@payments.com", registeredConsumer3.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer3.checkPasswordMatch("myNameIsWill"),
                "Invalid password was registered");

        // check the invalid consumer was not registered
        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(3, allUsers.size(), "Consumer should have been registered");

        User currentUser = userState.getCurrentUser();
        // check current user is the last valid consumer
        assertTrue(currentUser instanceof Consumer, "Logged in user should be a consumer");
        assertEquals("Will", ((Consumer) currentUser).getName(),
                "Invalid name was registered");
        assertEquals("will@gmail.com", currentUser.getEmail(),
                "Invalid email was registered");
        assertEquals("0756532298", ((Consumer) currentUser).getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("will@payments.com", currentUser.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(currentUser.checkPasswordMatch("myNameIsWill"),
                "Invalid password was registered");
        System.out.println("Multiple valid and one invalid consumers test passed!");
    }

    @Test
    @DisplayName("Test for multiple valid and multiple invalid consumers")
    void multipleValidMultipleInvalidConsumerTest()
    {
        // valid consumer 1
        RegisterConsumerCommand command = new RegisterConsumerCommand(
                "Johnny", "johnny@gmail.com", "0756432198",
                "myNameIsJohnny", "johhny@payments.com");
        command.execute(context);
        Consumer registeredConsumer = command.getResult();
        assertNotNull(registeredConsumer, "Consumer should have been registered");
        assertEquals("Johnny", registeredConsumer.getName(),
                "Invalid name was registered");
        assertEquals("johnny@gmail.com", registeredConsumer.getEmail(),
                "Invalid email was registered");
        assertEquals("0756432198", registeredConsumer.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("johhny@payments.com", registeredConsumer.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer.checkPasswordMatch("myNameIsJohnny"),
                "Invalid password was registered");

        // invalid consumer 1
        RegisterConsumerCommand command2 = new RegisterConsumerCommand(
                null, "", "",
                "", null);
        command2.execute(context);
        Consumer registeredConsumer2 = command2.getResult();
        assertNull(registeredConsumer2, "Consumer should not have been registered");

        // valid consumer 2
        RegisterConsumerCommand command3 = new RegisterConsumerCommand(
                "Will", "will@gmail.com", "0756532298",
                "myNameIsWill", "will@payments.com");
        command3.execute(context);
        Consumer registeredConsumer3 = command3.getResult();
        assertNotNull(registeredConsumer3, "Consumer should have been registered");
        assertEquals("Will", registeredConsumer3.getName(),
                "Invalid name was registered");
        assertEquals("will@gmail.com", registeredConsumer3.getEmail(),
                "Invalid email was registered");
        assertEquals("0756532298", registeredConsumer3.getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("will@payments.com", registeredConsumer3.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(registeredConsumer3.checkPasswordMatch("myNameIsWill"),
                "Invalid password was registered");

        // invalid consumer 2
        RegisterConsumerCommand command4 = new RegisterConsumerCommand(
                null, null, null,
                null, null);
        command4.execute(context);
        Consumer registeredConsumer4 = command4.getResult();
        assertNull(registeredConsumer4, "Consumer should not have been registered");

        // add all valid consumers, and reject invalid ones
        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        assertEquals(3, allUsers.size(), "Consumer should have been registered");

        User currentUser = userState.getCurrentUser();
        // check current user is the last valid consumer
        assertTrue(currentUser instanceof Consumer, "Logged in user should be a consumer");
        assertEquals("Will", ((Consumer) currentUser).getName(),
                "Invalid name was registered");
        assertEquals("will@gmail.com", currentUser.getEmail(),
                "Invalid email was registered");
        assertEquals("0756532298", ((Consumer) currentUser).getPhoneNumber(),
                "Invalid phone number was registered");
        assertEquals("will@payments.com", currentUser.getPaymentAccountEmail(),
                "Invalid payment account email was registered");
        assertTrue(currentUser.checkPasswordMatch("myNameIsWill"),
                "Invalid password was registered");
        System.out.println("Multiple valid and multiple invalid consumers test passed!");
    }
}
