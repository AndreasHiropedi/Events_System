package tests;

import command.RegisterEntertainmentProviderCommand;
import controller.Controller;
import model.EntertainmentProvider;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import state.UserState;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterEntertainmentProviderSystemTest
{
    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    // ================================================================================

    // ================================================================================
    //                                      TESTS
    // ================================================================================

    @Test
    @DisplayName("Registering multiple different entertainment providers")
    void registerTwoEntertainmentProvidersWithDifferentEmails()
    {
        Controller ctrl = new Controller();

        // provider 1
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows, Edinburgh",
                "rgb@rbs.co.uk",
                "Matty",
                "matty@gmail.com",
                "pWord",
                List.of("One Dude","Other Dude"),
                List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );

        ctrl.runCommand(cmd1);
        EntertainmentProvider provider1 = cmd1.getResult();
        String email1 = provider1.getEmail();

        // provider 2
        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                "Cirque d'Edimbourg",
                "Meadows, Edinburgh",
                "rgb@rbs.com",
                "CdE",
                "CdE@gamil.com",
                "pWord",
                List.of("Oui","Non"),
                List.of("oui@hotmail.com", "non@yahoo.com")
        );

        ctrl.runCommand(cmd2);
        EntertainmentProvider provider2 = cmd2.getResult();
        String email2 = provider2.getEmail();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check they were both recorded successfully
        assertEquals(3, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNotEquals(email1, email2, "The two emails should not match!");
        assertNotNull(provider1, "Entertainment provider should have been registered!");
        assertNotNull(provider2, "Entertainment provider should have been registered!");
        System.out.println("Registering multiple independent providers test passed!");
        System.out.println("Validation for different organisation name but same address passed!");
    }

    @Test
    @DisplayName("Registering multiple different entertainment providers with the same email")
    void registerTwoEntertainmentProvidersWithSameEmails()
    {
        Controller ctrl = new Controller();

        String repeatEmail = "matty@gmail.com";

        // provider 1
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
            "Awesome Volleyball Rec",
            "Meadows, Edinburgh",
            "rgb@rbs.co.uk",
            "Matty",
            repeatEmail,
            "pWord",
            List.of("One Dude","Other Dude"),
            List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );
        ctrl.runCommand(cmd1);
        EntertainmentProvider providerResult1 = cmd1.getResult();
        String email1 = providerResult1.getEmail();

        // provider 2
        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
            "Cirque d'Edimbourg",
            "Meadows, Edinburgh",
            "rgb@rbs.com",
            "CdE",
            repeatEmail,
            "pWord",
            List.of("Oui","Non"),
            List.of("oui@hotmail.com", "non@yahoo.com")
        );
        ctrl.runCommand(cmd2);
        EntertainmentProvider providerResult2 = cmd2.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check only one was recorded, since the email was already in use
        assertEquals(2, allUsers.size(), "Entertainment provider should not have been registered!");
        assertEquals(repeatEmail, email1, "Emails should match!");
        assertNotNull(providerResult1, "Provider should have been registered!");
        assertNull(providerResult2, "Provider should not be registered, as email is already used!");
        System.out.println("Validation for registered email passed!");
    }

    @Test
    @DisplayName("Registering one valid entertainment provider")
    void registerOneEntertainmentProvider()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();
        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check the provider was successfully registered
        assertEquals(2, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNotNull(provider, "Entertainment provider should have been registered!");
        assertEquals(orgName, provider.getOrgName(), "Invalid organisation name registered!");
        assertEquals(orgAddress, provider.getOrgAddress(), "Invalid organisation address registered!");
        assertEquals(paymentAccount, provider.getPaymentAccountEmail(), "Invalid payment account registered!");
        assertEquals(mainRepEmail, provider.getEmail(), "Invalid email registered!");
        assertTrue(provider.checkPasswordMatch(password), "Invalid password registered!");
        System.out.println("Registering one provider test passed!");
    }

    @Test
    @DisplayName("Registering multiple different entertainment providers with the same organisation name and address")
    void registerTwoEntertainmentProvidersWithSameOrganisationNameAndAddress()
    {
        Controller ctrl = new Controller();

        // provider 1
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows, Edinburgh",
                "rgb@rbs.co.uk",
                "Matty",
                "email@org1",
                "pWord",
                List.of("One Dude","Other Dude"),
                List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );
        ctrl.runCommand(cmd1);
        EntertainmentProvider providerResult1 = cmd1.getResult();

        // provider 2
        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows, Edinburgh",
                "rgb@rbs.com",
                "CdE",
                "email@org2",
                "pWord",
                List.of("Oui","Non"),
                List.of("oui@hotmail.com", "non@yahoo.com")
        );
        ctrl.runCommand(cmd2);
        EntertainmentProvider providerResult2 = cmd2.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // since organisation was already registered, check only one provider was recorded
        assertEquals(2, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNotNull(providerResult1, "Provider should have been registered!");
        assertNull(providerResult2, "Provider should not be registered, as organisation is already registered!");
        System.out.println("Validation for registered organisation passed!");
    }

    @Test
    @DisplayName("Registering multiple different entertainment providers with just the same organisation name")
    void registerTwoEntertainmentProvidersWithSameOrganisationName()
    {
        Controller ctrl = new Controller();

        // provider 1
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows, Edinburgh",
                "rgb@rbs.co.uk",
                "Matty",
                "email@org1",
                "pWord",
                List.of("One Dude","Other Dude"),
                List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );
        ctrl.runCommand(cmd1);
        EntertainmentProvider providerResult1 = cmd1.getResult();

        // provider 2
        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows",
                "rgb@rbs.com",
                "CdE",
                "email@org2",
                "pWord",
                List.of("Oui","Non"),
                List.of("oui@hotmail.com", "non@yahoo.com")
        );
        ctrl.runCommand(cmd2);
        EntertainmentProvider providerResult2 = cmd2.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check both were recorded, since we assume the organisation is only the same
        // if both the name and address match
        assertEquals(3, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNotNull(providerResult1, "Provider should have been registered!");
        assertNotNull(providerResult2, "Provider should have been registered!");
        System.out.println("Validation for different organisation address but same name passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid organisation name")
    void registerInvalidProviderTest1()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName=null, orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check null name is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid organisation name test 1 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid organisation name")
    void registerInvalidProviderTest2()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check empty string name is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid organisation name test 2 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid organisation address")
    void registerInvalidProviderTest3()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress=null,
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check null address is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid organisation address test 1 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid organisation address")
    void registerInvalidProviderTest4()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check empty string address is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid organisation address test 2 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid payment account")
    void registerInvalidProviderTest5()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount=null, mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check null payment account email is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid payment account test 1 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid payment account")
    void registerInvalidProviderTest6()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check empty string payment account email is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid payment account test 2 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid main rep name")
    void registerInvalidProviderTest7()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName=null,
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check null main rep name is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid main rep name test 1 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid main rep name")
    void registerInvalidProviderTest8()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check empty string main rep name is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid main rep name test 2 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid main rep email")
    void registerInvalidProviderTest9()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail=null, password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check null main rep email is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid main rep email test 1 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid main rep email")
    void registerInvalidProviderTest10()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check empty string main rep email is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid main rep email test 2 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid password")
    void registerInvalidProviderTest11()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password=null;

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check null password is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid password test 1 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid password")
    void registerInvalidProviderTest12()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check empty string password is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid password test 2 passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid list of other rep names")
    void registerInvalidProviderTest13()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = null,
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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check null list of other rep's names is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid list of other rep names test passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with invalid list of other rep emails")
    void registerInvalidProviderTest14()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", orgAddress="Meadows",
                paymentAccount="rgb@rbs.co.uk", mainRepName="Matty",
                mainRepEmail="matty@gmail.com", password="pWord";

        List<String> otherRepNames = List.of("One Dude","Other Dude"),
                otherRepEmails = null;

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
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check null list of other rep's emails is rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid list of other rep emails test passed!");
    }

    @Test
    @DisplayName("Registering entertainment provider with several invalid details")
    void registerInvalidProviderTest15()
    {
        Controller ctrl = new Controller();

        // provider details
        String orgName="Awesome Volleyball Rec", paymentAccount="rgb@rbs.co.uk",
                mainRepEmail="matty@gmail.com";

        List<String> otherRepNames = List.of("One Dude","Other Dude");


        RegisterEntertainmentProviderCommand cmd = new RegisterEntertainmentProviderCommand(
                orgName,
                null,
                paymentAccount,
                null,
                mainRepEmail,
                null,
                otherRepNames,
                null
        );

        ctrl.runCommand(cmd);
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check several invalid details are rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("Invalid list of other rep emails test passed!");
        System.out.println("Note: command should only alert the user for first invalid detail");
    }

    @Test
    @DisplayName("Registering entertainment provider with all invalid details")
    void registerInvalidProviderTest16()
    {
        Controller ctrl = new Controller();

        // provider details
        RegisterEntertainmentProviderCommand cmd = new RegisterEntertainmentProviderCommand(
                null, null, null, null,
                null, null, null, null
        );

        ctrl.runCommand(cmd);
        EntertainmentProvider provider = cmd.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check all invalid details are rejected
        assertEquals(1, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNull(provider, "Entertainment provider should not have been registered!");
        System.out.println("All invalid details test passed!");
        System.out.println("Note: command should only alert the user for first invalid detail");
    }

    @Test
    @DisplayName("Registering multiple valid and one invalid entertainment provider")
    void registerMultipleValidOneInvalidEntertainmentProvider()
    {
        Controller ctrl = new Controller();

        // valid provider 1
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows, Edinburgh",
                "rgb@rbs.co.uk",
                "Matty",
                "matty@gmail.com",
                "pWord",
                List.of("One Dude","Other Dude"),
                List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );

        ctrl.runCommand(cmd1);
        EntertainmentProvider provider1 = cmd1.getResult();

        // valid provider 2
        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                "Cirque d'Edimbourg",
                "Meadows, Edinburgh",
                "rgb@rbs.com",
                "CdE",
                "CdE@gamil.com",
                "pWord",
                List.of("Oui","Non"),
                List.of("oui@hotmail.com", "non@yahoo.com")
        );

        ctrl.runCommand(cmd2);
        EntertainmentProvider provider2 = cmd2.getResult();

        // invalid provider
        RegisterEntertainmentProviderCommand cmd3 = new RegisterEntertainmentProviderCommand(
                null, null, null, null,
                null, null, null, null
        );

        ctrl.runCommand(cmd3);
        EntertainmentProvider provider3 = cmd3.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check all valid providers are recorded
        assertEquals(3, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNotNull(provider1, "Entertainment provider should have been registered!");
        assertNotNull(provider2, "Entertainment provider should have been registered!");
        assertNull(provider3,"Entertainment provider should not have been registered!");
        System.out.println("Registering multiple valid and one invalid provider test passed!");
    }

    @Test
    @DisplayName("Registering multiple valid and multiple invalid entertainment providers")
    void registerMultipleValidMultipleInvalidEntertainmentProviders()
    {
        Controller ctrl = new Controller();

        // valid provider 1
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows, Edinburgh",
                "rgb@rbs.co.uk",
                "Matty",
                "matty@gmail.com",
                "pWord",
                List.of("One Dude","Other Dude"),
                List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );

        ctrl.runCommand(cmd1);
        EntertainmentProvider provider1 = cmd1.getResult();

        // valid provider 2
        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                "Cirque d'Edimbourg",
                "Meadows, Edinburgh",
                "rgb@rbs.com",
                "CdE",
                "CdE@gamil.com",
                "pWord",
                List.of("Oui","Non"),
                List.of("oui@hotmail.com", "non@yahoo.com")
        );

        ctrl.runCommand(cmd2);
        EntertainmentProvider provider2 = cmd2.getResult();

        // invalid provider 1
        RegisterEntertainmentProviderCommand cmd3 = new RegisterEntertainmentProviderCommand(
                null, null, null, null,
                null, null, null, null
        );

        ctrl.runCommand(cmd3);
        EntertainmentProvider provider3 = cmd3.getResult();

        // invalid provider 2
        RegisterEntertainmentProviderCommand cmd4 = new RegisterEntertainmentProviderCommand(
                "", null, "bhr@pay", null,
                null, "fhkjd", null, null
        );

        ctrl.runCommand(cmd4);
        EntertainmentProvider provider4 = cmd4.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check all valid providers are recorded
        assertEquals(3, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNotNull(provider1, "Entertainment provider should have been registered!");
        assertNotNull(provider2, "Entertainment provider should have been registered!");
        assertNull(provider3,"Entertainment provider should not have been registered!");
        assertNull(provider4,"Entertainment provider should not have been registered!");
        System.out.println("Registering multiple valid and multiple invalid providers test passed!");
    }

    @Test
    @DisplayName("Registering one valid and multiple invalid entertainment providers")
    void registerOneValidMultipleInvalidEntertainmentProviders()
    {
        Controller ctrl = new Controller();

        // valid provider
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows, Edinburgh",
                "rgb@rbs.co.uk",
                "Matty",
                "matty@gmail.com",
                "pWord",
                List.of("One Dude","Other Dude"),
                List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );

        ctrl.runCommand(cmd1);
        EntertainmentProvider provider1 = cmd1.getResult();

        // invalid provider 1
        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                null, null, null, null,
                null, null, null, null
        );

        ctrl.runCommand(cmd2);
        EntertainmentProvider provider2 = cmd2.getResult();

        // invalid provider 2
        RegisterEntertainmentProviderCommand cmd3 = new RegisterEntertainmentProviderCommand(
                "", null, "bhr@pay", null,
                null, "fhkjd", null, null
        );

        ctrl.runCommand(cmd3);
        EntertainmentProvider provider3 = cmd3.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check only valid provider is recorded
        assertEquals(2, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNotNull(provider1, "Entertainment provider should have been registered!");
        assertNull(provider2,"Entertainment provider should not have been registered!");
        assertNull(provider3,"Entertainment provider should not have been registered!");
        System.out.println("Registering one valid and multiple invalid providers test passed!");
    }

    @Test
    @DisplayName("Registering one valid and one invalid entertainment provider")
    void registerOneValidAndOneInvalidEntertainmentProvider()
    {
        Controller ctrl = new Controller();

        // valid provider
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball Rec",
                "Meadows, Edinburgh",
                "rgb@rbs.co.uk",
                "Matty",
                "matty@gmail.com",
                "pWord",
                List.of("One Dude","Other Dude"),
                List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );

        ctrl.runCommand(cmd1);
        EntertainmentProvider provider1 = cmd1.getResult();

        // invalid provider
        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                null, null, null, null,
                null, null, null, null
        );

        ctrl.runCommand(cmd2);
        EntertainmentProvider provider2 = cmd2.getResult();

        UserState userState = (UserState) ctrl.getContext().getUserState();
        Map<String, User> allUsers = userState.getAllUsers();

        // check only valid provider is recorded
        assertEquals(2, allUsers.size(), "Entertainment provider should not have been registered!");
        assertNotNull(provider1, "Entertainment provider should have been registered!");
        assertNull(provider2,"Entertainment provider should not have been registered!");
        System.out.println("Registering one valid and one invalid provider test passed!");
    }
}
