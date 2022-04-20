package tests;

import command.LoginCommand;
import command.LogoutCommand;
import command.RegisterConsumerCommand;
import command.RegisterEntertainmentProviderCommand;
import controller.*;
import model.Consumer;
import model.EntertainmentProvider;
import model.GovernmentRepresentative;
import model.User;
import org.junit.jupiter.api.*;
import state.UserState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogInSystemTest
{
    Context context = new Context();

    // Output colors - for flagging errors, successes and warnings.
    private final String
            g = "\u001B[32m",       // GREEN
            y = "\u001B[33m",       // YELLOW
            r = "\u001B[31m",       // RED
            rst   = "\u001B[0m";    // DEFAULT

    @BeforeEach
    void displayTestName(TestInfo testInfo)
    {
        System.out.println(testInfo.getDisplayName());
    }

    private void registerOneConsumer(String name, String email,
                                     String phoneNumber, String password, String paymentAccountEmail)
    {
        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                name,
                email,
                phoneNumber,
                password,
                paymentAccountEmail
        );

        cmd.execute(context);
    }

    private void highlightPass(String message)
    {
        System.out.println(g+message+rst);
    }

    private void highlightWarning(String message)
    {
        System.out.println(y+message+rst);
    }

    // ================================================================================
    //                                  REGISTRATIONS
    // ================================================================================

    private EntertainmentProvider registerOneEntertainmentProvider()
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

        cmd.execute(context);
        return cmd.getResult();
    }

    private void registerTwoEntertainmentProvidersWithDifferentEmails()
    {
        RegisterEntertainmentProviderCommand cmd1 = new RegisterEntertainmentProviderCommand(
                "Awesome Volleyball",
                "Meadows, Edinburgh",
                "rgb@rbs.co.uk",
                "Matty",
                "matty@hotmail.com",
                "pWord",
                List.of("One Dude", "Other Dude"),
                List.of("oneDude@hotmail.com", "otherDude@gmail.com")
        );

        cmd1.execute(context);
        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        RegisterEntertainmentProviderCommand cmd2 = new RegisterEntertainmentProviderCommand(
                "Cirque d'Edimbourg",
                "Meadows, Edinburgh",
                "rgb@rbs.com",
                "CdE",
                "CdE@gamil.com",
                "pWord",
                List.of("Oui", "Non"),
                List.of("oui@hotmail.com", "non@yahoo.com")
        );
        cmd2.execute(context);
        logoutCmd.execute(context);
    }

    // ================================================================================

    // ================================================================================
    //                                      TESTS
    // ================================================================================

    @Test
    @DisplayName("Test for one valid login for consumer")
    void loginConsumerValid()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand cmd = new LoginCommand(email, password);
        cmd.execute(context);

        User user = cmd.getResult();

        // Ensure User is a consumer.
        assertTrue(user instanceof Consumer, "Consumer expected! "
                + "Got " + user.getClass().getSimpleName() + ".");

        Consumer currentConsumer = (Consumer) user;
        UserState state = (UserState) context.getUserState();

        // Check matching phone numbers.
        assertEquals (currentConsumer.getPhoneNumber(), phoneNumber,
                "Given phone number does not match the consumer's phone number! Expecting "
                        + phoneNumber + ", but got " + currentConsumer.getPhoneNumber() + ".");

        // Check matching names.
        assertEquals (currentConsumer.getName(), name,
                "Given name does not match the consumer's name! Expecting "
                        + name + ", but got " + currentConsumer.getName() + ".");

        // Check matching emails.
        assertEquals (currentConsumer.getEmail(), email,
                "Given email does not match the consumer's email! Expecting "
                        + name + ", but got " + currentConsumer.getName() + ".");

        // Check matching payment accounts.
        assertEquals (currentConsumer.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the consumer's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + currentConsumer.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(currentConsumer.checkPasswordMatch("SecretPassword"),
                "Given password does not match the consumer's password!");

        // Check matching user instances.
        assertEquals(state.getCurrentUser(), currentConsumer, "User instances do not match match!");

        highlightPass("loginConsumerValid passes!");
    }

    @Test
    @DisplayName("Test for missing login email for consumer")
    void loginConsumerMissingEmail()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(null, password);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/consumer after attempting to log in without an email.
        assertNull(user, "Not null consumer found!"
                + "Consumer should not have been logged in, given no email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no email");

        highlightPass("loginConsumerMissingEmail passes missing email test!");
    }

    @Test
    @DisplayName("Test for missing password for consumer")
    void loginConsumerMissingPassword()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(email, null);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/consumer after attempting to log in without a password.
        assertNull(user, "Not null consumer found!"
                + "Consumer should not have been logged in, given no password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no password!");

        highlightPass("loginConsumerMissingPassword passes missing password test!");
    }

    @Test
    @DisplayName("Test for missing email and password for consumer")
    void loginConsumerMissingBoth()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(null, null);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/consumer after attempting to log in without neither a password or email.
        assertNull(user, "Not null consumer found!"
                + "Consumer should not have been logged in, given no password and email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no password and email!");

        highlightPass("loginConsumerMissingBoth passes missing email and password test!");
        highlightWarning("Note: should only say that email is incorrect!");
    }

    @Test
    @DisplayName("Test for wrong email for consumer")
    void loginConsumerWrongEmail()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand("john@gmail.com", password);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/consumer after attempting to log in with an incorrect email.
        assertNull(user, "Not null consumer found!"
                + "Consumer should not have been logged in, given incorrect email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect email!");

        highlightPass("loginConsumerWrongEmail passes wrong email test!");
    }

    @Test
    @DisplayName("Test for wrong password for consumer")
    void loginConsumerWrongPassword()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(email, "12345");
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/consumer after attempting to log in with an incorrect password.
        assertNull(user, "Not null consumer found!"
                + "Consumer should not have been logged in, given incorrect password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect password!");

        highlightPass("loginConsumerWrongPassword passes wrong password test!");
    }

    @Test
    @DisplayName("Test for both wrong email and password for consumer")
    void loginConsumerWrongBoth()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand("john@gmail.com", "12345");
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/consumer after attempting to log in with both incorrect password and email.
        assertNull(user, "Not null consumer found!"
                + "Consumer should not have been logged in, given incorrect email and password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect email and password!");

        highlightPass("loginConsumerWrongBoth passes wrong details test!");
        highlightWarning("Note: it should output that email is incorrect!");
    }

    @Test
    @DisplayName("Test for one valid login with multiple registered consumers")
    void loginWithMultipleRegisteredConsumerValid()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        String name2="John Smith II",
                email2="johnsmith2@gmail.com",
                phoneNumber2="+1234567892",
                password2="SecretPassword2",
                paymentAccountEmail2="johnsmith2@money.com";

        // ------> Consumer 1
        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        // ------> Consumer 2
        registerOneConsumer(name2, email2, phoneNumber2, password2, paymentAccountEmail2);
        logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand cmd = new LoginCommand(email, password);
        cmd.execute(context);

        User user = cmd.getResult();

        highlightWarning("Asserting for second consumer...");

        // Ensure User is a consumer.
        assertTrue(user instanceof Consumer, "Consumer expected! "
                + "Got " + user.getClass().getSimpleName() + ".");

        Consumer currentConsumer = (Consumer) user;
        UserState state = (UserState) context.getUserState();

        // Check matching phone numbers.
        assertEquals (currentConsumer.getPhoneNumber(), phoneNumber,
                "Given phone number does not match the consumer's phone number! Expecting "
                        + phoneNumber + ", but got " + currentConsumer.getPhoneNumber() + ".");

        // Check matching names.
        assertEquals (currentConsumer.getName(), name,
                "Given name does not match the consumer's name! Expecting "
                        + name + ", but got " + currentConsumer.getName() + ".");

        // Check matching emails.
        assertEquals (currentConsumer.getEmail(), email,
                "Given email does not match the consumer's email! Expecting "
                        + name + ", but got " + currentConsumer.getName() + ".");

        // Check matching payment accounts.
        assertEquals (currentConsumer.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the consumer's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + currentConsumer.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(currentConsumer.checkPasswordMatch("SecretPassword"),
                "Given password does not match the consumer's password!");

        // Check matching consumer instance.
        assertEquals(state.getCurrentUser(), currentConsumer, "Consumer instances do not match match!");

        highlightPass("loginWithMultipleRegisteredConsumerValid passes!");
    }

    @Test
    @DisplayName("Test for one valid login for provider")
    void loginProviderValid()
    {
        EntertainmentProvider provider = registerOneEntertainmentProvider();
        String email = provider.getEmail();
        String password = "pWord";
        String name = provider.getOrgName();
        String paymentAccountEmail = provider.getPaymentAccountEmail();
        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand cmd = new LoginCommand(email, password);
        cmd.execute(context);

        User user = cmd.getResult();

        // Ensure user is an entertainment provider.
        assertTrue(user instanceof EntertainmentProvider, "EntertainmentProvider expected! "
                + "Got " + user.getClass().getSimpleName() + ".");

        UserState state = (UserState) context.getUserState();
        EntertainmentProvider loggedInProvider = (EntertainmentProvider) user;

        // Checking matching organisation names.
        assertEquals (loggedInProvider.getOrgName(), name,
                "Given organisation name does not match the provider's organisation name! Expecting "
                        + name + ", but got " + loggedInProvider.getOrgName() + ".");

        // Check matching organisation addresses.
        assertEquals(loggedInProvider.getOrgAddress(), provider.getOrgAddress(),
                "Given provider's organisation address does not match the logged in provider's organisation address! Expecting "
                        + provider.getOrgAddress() + ", but got " + loggedInProvider.getOrgAddress() + ".");

        // Check matching emails.
        assertEquals (loggedInProvider.getEmail(), email,
                "Given email does not match the provider's email! Expecting "
                        + email + ", but got " + loggedInProvider.getEmail() + ".");

        // Check matching payment accounts.
        assertEquals (loggedInProvider.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the provider's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + loggedInProvider.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(loggedInProvider.checkPasswordMatch(password),
                "Given password does not match the provider's password!");

        // Check matching user instances.
        assertEquals(state.getCurrentUser(), loggedInProvider, "User instances do not match match!");

        highlightPass("loginProviderValid passes!");
    }

    @Test
    @DisplayName("Test for missing login email for provider")
    void loginProviderMissingEmail()
    {
        registerOneEntertainmentProvider();
        String password = "pWord";

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(null, password);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/provider after attempting to log in without an email.
        assertNull(user, "Not null entertainment provider found!"
                + "Entertainment provider should not have been logged in, given no email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no email");

        highlightPass("loginProviderMissingEmail passes missing email test!");
    }

    @Test
    @DisplayName("Test for missing login password for provider")
    void loginProviderMissingPassword()
    {
        EntertainmentProvider provider = registerOneEntertainmentProvider();

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(provider.getEmail(), null);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/provider after attempting to log in without a password.
        assertNull(user, "Not null entertainment provider found!"
                + "Entertainment provider should not have been logged in, given no password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no password!");

        highlightPass("loginProviderMissingPassword passes missing password test!");
    }

    @Test
    @DisplayName("Test for missing email and password for provider")
    void loginProviderMissingBoth()
    {
        registerOneEntertainmentProvider();

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(null, null);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/provider after attempting to log in without neither a password or email.
        assertNull(user, "Not null entertainment provider found!"
                + "Entertainment provider should not have been logged in, given no password and email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no password and email!");

        highlightPass("loginProviderMissingBoth passes missing email and password test!");
        highlightWarning("Note: should only say that email is incorrect!");
    }

    @Test
    @DisplayName("Test for wrong email for provider")
    void loginProviderWrongEmail()
    {
        registerOneEntertainmentProvider();
        String password = "pWord";

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand("john@gmail.com", password);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/provider after attempting to log in with an incorrect email.
        assertNull(user, "Not null entertainment provider found!"
                + "Entertainment provider should not have been logged in, given incorrect email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect email!");

        highlightPass("loginProviderWrongEmail passes wrong email test!");
    }

    @Test
    @DisplayName("Test for wrong password for provider")
    void loginProviderWrongPassword()
    {
        EntertainmentProvider provider = registerOneEntertainmentProvider();

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(provider.getEmail(), "12345");
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/provider after attempting to log in with an incorrect password.
        assertNull(user, "Not null entertainment provider found!"
                + "Entertainment provider should not have been logged in, given incorrect password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect password!");

        highlightPass("loginProviderWrongPassword passes wrong password test!");
    }

    @Test
    @DisplayName("Test for both wrong email and password for provider")
    void loginProviderWrongBoth()
    {
        registerOneEntertainmentProvider();

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand("john@gmail.com", "12345");
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/provider after attempting to log in with both incorrect password and email.
        assertNull(user, "Not null entertainment provider found!"
                + "Entertainment provider should not have been logged in, given incorrect email and password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect email and password!");

        highlightPass("loginProviderWrongBoth passes wrong details test!");
        highlightWarning("Note: it should output that email is incorrect!");
    }

    @Test
    @DisplayName("Test for one valid login with multiple registered providers")
    void loginWithMultipleRegisteredProvidersValid()
    {
        // ------> Subject provider
        EntertainmentProvider provider = registerOneEntertainmentProvider();
        String email = provider.getEmail();
        String password = "pWord";
        String name = provider.getOrgName();
        String paymentAccountEmail = provider.getPaymentAccountEmail();
        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        // ------> Two other providers
        registerTwoEntertainmentProvidersWithDifferentEmails();

        // ------> Log in as subject provider
        LoginCommand loginCmd = new LoginCommand(email, password);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        highlightWarning("Asserting for subject entertainment provider...");

        // Ensure user is an entertainment provider.
        assertTrue(user instanceof EntertainmentProvider, "EntertainmentProvider expected! "
                + "Got " + user.getClass().getSimpleName() + ".");

        UserState state = (UserState) context.getUserState();
        EntertainmentProvider loggedInProvider = (EntertainmentProvider) user;

        // Checking matching organisation names.
        assertEquals (loggedInProvider.getOrgName(), name,
                "Given organisation name does not match the provider's organisation name! Expecting "
                        + name + ", but got " + loggedInProvider.getOrgName() + ".");

        // Check matching organisation addresses.
        assertEquals(loggedInProvider.getOrgAddress(), provider.getOrgAddress(),
                "Given provider's organisation address does not match the logged in provider's organisation address! Expecting "
                        + provider.getOrgAddress() + ", but got " + loggedInProvider.getOrgAddress() + ".");

        // Check matching emails.
        assertEquals (loggedInProvider.getEmail(), email,
                "Given email does not match the provider's email! Expecting "
                        + email + ", but got " + loggedInProvider.getEmail() + ".");

        // Check matching payment accounts.
        assertEquals (loggedInProvider.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the provider's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + loggedInProvider.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(loggedInProvider.checkPasswordMatch(password),
                "Given password does not match the consumer's password!");

        // Check matching provider instances.
        assertEquals(state.getCurrentUser(), loggedInProvider, "Consumer instances do not match match!");

        highlightPass("loginWithMultipleRegisteredProvidersValid passes!");
    }

    @Test
    @DisplayName("Test for valid login for government representative")
    void loginGovernmentRepresentative()
    {
        String email = "margaret.thatcher@gov.uk";
        String paymentAccountEmail = "payment@gov.uk";
        String password = "The Good times  ";

        LoginCommand cmd = new LoginCommand(email, password);
        cmd.execute(context);

        User user = cmd.getResult();

        // Ensure user is a government representative.
        assertTrue(user instanceof GovernmentRepresentative, "User should be a government representative!");

        UserState state = (UserState) context.getUserState();
        GovernmentRepresentative loggedInUser = (GovernmentRepresentative) user;

        // Check matching emails.
        assertEquals (loggedInUser.getEmail(), email,
                "Given email does not match the representative's email! Expecting "
                        + email + ", but got " + loggedInUser.getEmail() + ".");

        // Check matching payment accounts.
        assertEquals (loggedInUser.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the representative's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + loggedInUser.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(loggedInUser.checkPasswordMatch(password),
                "Given password does not match the representative's password!");

        // Check matching user instances.
        assertEquals(state.getCurrentUser(), loggedInUser, "User instances do not match match!");

        highlightPass("loginGovernmentRepresentative passes!");
    }

    @Test
    @DisplayName("Test for missing login email for government rep")
    void loginGovRepMissingEmail()
    {
        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd.execute(context);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(null, "The Good times  ");
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/representative after attempting to log in without an email.
        assertNull(user, "Not null government representative found!"
                + "Government representative should not have been logged in, given no email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no email");

        highlightPass("loginGovRepMissingEmail passes missing email test!");
    }

    @Test
    @DisplayName("Test for missing login password for government rep")
    void loginGovRepMissingPassword()
    {
        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd.execute(context);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand("margaret.thatcher@gov.uk", null);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/representative after attempting to log in without a password.
        assertNull(user, "Not null representative found!"
                + "Government representative should not have been logged in, given no password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no password!");

        highlightPass("loginGovRepMissingPassword passes missing password test!");
    }

    @Test
    @DisplayName("Test for missing email and password for government rep")
    void loginGovRepMissingBoth()
    {
        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd.execute(context);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand(null, null);
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/representative after attempting to log in without neither a password or email.
        assertNull(user, "Not null government representative found!"
                + "Government representative should not have been logged in, given no password and email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given no password and email!");

        highlightPass("loginGovRepMissingBoth passes missing email and password test!");
        highlightWarning("Note: should only say that email is incorrect!");
    }

    @Test
    @DisplayName("Test for wrong email for government rep")
    void loginGovRepWrongEmail()
    {
        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd.execute(context);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand("john@gmail.com", "The Good times  ");
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/representative after attempting to log in with an incorrect email.
        assertNull(user, "Not null government representative found!"
                + "Government representative should not have been logged in, given incorrect email!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect email!");

        highlightPass("loginGovRepWrongEmail passes wrong email test!");
    }

    @Test
    @DisplayName("Test for wrong password for government rep")
    void loginGovRepWrongPassword()
    {
        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd.execute(context);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand("margaret.thatcher@gov.uk", "12345");
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/representative after attempting to log in with an incorrect password.
        assertNull(user, "Not null government representative found!"
                + "Government representative should not have been logged in, given incorrect password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect password!");

        highlightPass("loginGovRepWrongPassword passes wrong password test!");
    }

    @Test
    @DisplayName("Test for both wrong email and password for government rep")
    void loginGovRepWrongBoth()
    {
        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd.execute(context);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        LoginCommand loginCmd = new LoginCommand("john@gmail.com", "12345");
        loginCmd.execute(context);

        User user = loginCmd.getResult();

        // Expect a null user/representative after attempting to log in with both incorrect password and email.
        assertNull(user, "Not null government representative found!"
                + "Government representative should not have been logged in, given incorrect email and password!");

        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        assertNull(currentUser, "Not null user found!"
                + "No user should have been logged in, given incorrect email and password!");

        highlightPass("loginGovRepWrongBoth passes wrong details test!");
        highlightWarning("Note: it should output that email is incorrect!");
    }

    @Test
    @DisplayName("Test for multiple different types of consumers (just consumers and government rep)")
    void loginSeveralDifferentUsersTest1()
    {
        String name="John Smith",
                email="johnsmith@gmail.com",
                phoneNumber="+123456789",
                password="SecretPassword",
                paymentAccountEmail="johnsmith@money.com";

        // ------> Register consumer John Smith.
        registerOneConsumer(name, email, phoneNumber, password, paymentAccountEmail);

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        // ------> Log in as government representative.
        LoginCommand cmd1 = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd1.execute(context);

        logoutCmd.execute(context);

        // ------> Log in as John Smith.
        LoginCommand cmd = new LoginCommand(email, password);
        cmd.execute(context);

        User user = cmd.getResult();

        highlightWarning("Asserting for Consumer, John Smith...");

        // Ensure User is a consumer.
        assertTrue(user instanceof Consumer, "Consumer expected! "
                + "Got " + user.getClass().getSimpleName() + ".");

        Consumer currentConsumer = (Consumer) user;
        UserState state = (UserState) context.getUserState();

        // Check matching phone numbers.
        assertEquals (currentConsumer.getPhoneNumber(), phoneNumber,
                "Given phone number does not match the consumer's phone number! Expecting "
                        + phoneNumber + ", but got " + currentConsumer.getPhoneNumber() + ".");

        // Check matching names.
        assertEquals (currentConsumer.getName(), name,
                "Given name does not match the consumer's name! Expecting "
                        + name + ", but got " + currentConsumer.getName() + ".");

        // Check matching emails.
        assertEquals (currentConsumer.getEmail(), email,
                "Given email does not match the consumer's email! Expecting "
                        + name + ", but got " + currentConsumer.getName() + ".");

        // Check matching payment accounts.
        assertEquals (currentConsumer.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the consumer's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + currentConsumer.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(currentConsumer.checkPasswordMatch("SecretPassword"),
                "Given password does not match the consumer's password!");

        // Check matching user instances.
        assertEquals(state.getCurrentUser(), currentConsumer, "User instances do not match match!");

        highlightPass("loginSeveralDifferentUsers test 1 passes!");
    }

    @Test
    @DisplayName("Test for multiple different types of consumers (just providers and government rep)")
    void loginSeveralDifferentUsersTest2()
    {
        // ------> Register subject entertainment provider.
        EntertainmentProvider provider = registerOneEntertainmentProvider();
        String email = provider.getEmail();
        String password = "pWord";
        String name = provider.getOrgName();
        String paymentAccountEmail = provider.getPaymentAccountEmail();

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        // ------> Log in as government representative.
        LoginCommand cmd1 = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd1.execute(context);

        logoutCmd.execute(context);

        // ------> Log in subject entertainment provider.
        LoginCommand cmd = new LoginCommand(email, password);
        cmd.execute(context);

        User user = cmd.getResult();

        highlightWarning("Asserting for subject entertainment provider...");

        // Ensure user is an entertainment provider.
        assertTrue(user instanceof EntertainmentProvider, "EntertainmentProvider expected! "
                + "Got " + user.getClass().getSimpleName() + ".");

        UserState state = (UserState) context.getUserState();
        EntertainmentProvider loggedInProvider = (EntertainmentProvider) user;

        // Checking matching organisation names.
        assertEquals (loggedInProvider.getOrgName(), name,
                "Given organisation name does not match the provider's organisation name! Expecting "
                        + name + ", but got " + loggedInProvider.getOrgName() + ".");

        // Check matching organisation addresses.
        assertEquals(loggedInProvider.getOrgAddress(), provider.getOrgAddress(),
                "Given provider's organisation address does not match the logged in provider's organisation address! Expecting "
                        + provider.getOrgAddress() + ", but got " + loggedInProvider.getOrgAddress() + ".");

        // Check matching emails.
        assertEquals (loggedInProvider.getEmail(), email,
                "Given email does not match the provider's email! Expecting "
                        + email + ", but got " + loggedInProvider.getEmail() + ".");

        // Check matching payment accounts.
        assertEquals (loggedInProvider.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the provider's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + loggedInProvider.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(loggedInProvider.checkPasswordMatch(password),
                "Given password does not match the provider's password!");

        // Check matching user instances.
        assertEquals(state.getCurrentUser(), loggedInProvider, "User instances do not match match!");

        highlightPass("loginSeveralDifferentUsers test 2 passes!");
    }

    @Test
    @DisplayName("Test for multiple different types of consumers (just consumers and providers)")
    void loginSeveralDifferentUsersTest3()
    {
        // ------> Register and log in subject entertainment provider.
        EntertainmentProvider provider = registerOneEntertainmentProvider();
        String email = provider.getEmail();
        String password = "pWord";
        String name = provider.getOrgName();
        String paymentAccountEmail = provider.getPaymentAccountEmail();

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        // ------> Register and log in as consumer, John Smith.
        String consumerName="John Smith",
                consumerEmail="johnsmith@gmail.com",
                phoneNumber="+123456789",
                consumerPassword="SecretPassword",
                consumerPaymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(consumerName, consumerEmail, phoneNumber, consumerPassword, consumerPaymentAccountEmail);

        logoutCmd.execute(context);

        // ------> Log in as subject entertainment provider.
        LoginCommand cmd = new LoginCommand(email, password);
        cmd.execute(context);

        User user = cmd.getResult();

        highlightWarning("Asserting for subject entertainment provider...");

        // Ensure user is an entertainment provider.
        assertTrue(user instanceof EntertainmentProvider, "EntertainmentProvider expected! "
                + "Got " + user.getClass().getSimpleName() + ".");

        UserState state = (UserState) context.getUserState();
        EntertainmentProvider loggedInProvider = (EntertainmentProvider) user;

        // Checking matching organisation names.
        assertEquals (loggedInProvider.getOrgName(), name,
                "Given organisation name does not match the provider's organisation name! Expecting "
                        + name + ", but got " + loggedInProvider.getOrgName() + ".");

        // Check matching organisation addresses.
        assertEquals(loggedInProvider.getOrgAddress(), provider.getOrgAddress(),
                "Given provider's organisation address does not match the logged in provider's organisation address! Expecting "
                        + provider.getOrgAddress() + ", but got " + loggedInProvider.getOrgAddress() + ".");

        // Check matching emails.
        assertEquals (loggedInProvider.getEmail(), email,
                "Given email does not match the provider's email! Expecting "
                        + email + ", but got " + loggedInProvider.getEmail() + ".");

        // Check matching payment accounts.
        assertEquals (loggedInProvider.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the provider's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + loggedInProvider.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(loggedInProvider.checkPasswordMatch(password),
                "Given password does not match the provider's password!");

        // Check matching user instances.
        assertEquals(state.getCurrentUser(), loggedInProvider, "User instances do not match match!");

        highlightPass("loginSeveralDifferentUsers test 3 passes!");
    }

    @Test
    @DisplayName("Test for all different types of consumers")
    void loginSeveralDifferentUsersTest4()
    {
        // ------> Register and log in subject provider
        EntertainmentProvider provider = registerOneEntertainmentProvider();
        String email = provider.getEmail();
        String password = "pWord";
        String name = provider.getOrgName();
        String paymentAccountEmail = provider.getPaymentAccountEmail();

        LogoutCommand logoutCmd = new LogoutCommand();
        logoutCmd.execute(context);

        // ------> Register and log in consumer, John Smith.
        String consumerName="John Smith",
                consumerEmail="johnsmith@gmail.com",
                phoneNumber="+123456789",
                consumerPassword="SecretPassword",
                consumerPaymentAccountEmail="johnsmith@money.com";

        registerOneConsumer(consumerName, consumerEmail, phoneNumber, consumerPassword, consumerPaymentAccountEmail);

        logoutCmd.execute(context);

        // ------> register and log in as a representative.
        LoginCommand cmd1 = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        cmd1.execute(context);

        logoutCmd.execute(context);

        LoginCommand cmd = new LoginCommand(email, password);
        cmd.execute(context);

        User user = cmd.getResult();

        highlightWarning("Asserting for subject entertainment provider...");

        // Ensure user is an entertainment provider.
        assertTrue(user instanceof EntertainmentProvider, "EntertainmentProvider expected! "
                + "Got " + user.getClass().getSimpleName() + ".");

        UserState state = (UserState) context.getUserState();
        EntertainmentProvider loggedInProvider = (EntertainmentProvider) user;

        // Checking matching organisation names.
        assertEquals (loggedInProvider.getOrgName(), name,
                "Given organisation name does not match the provider's organisation name! Expecting "
                        + name + ", but got " + loggedInProvider.getOrgName() + ".");

        // Check matching organisation addresses.
        assertEquals(loggedInProvider.getOrgAddress(), provider.getOrgAddress(),
                "Given provider's organisation address does not match the logged in provider's organisation address! Expecting "
                        + provider.getOrgAddress() + ", but got " + loggedInProvider.getOrgAddress() + ".");

        // Check matching emails.
        assertEquals (loggedInProvider.getEmail(), email,
                "Given email does not match the provider's email! Expecting "
                        + email + ", but got " + loggedInProvider.getEmail() + ".");

        // Check matching payment accounts.
        assertEquals (loggedInProvider.getPaymentAccountEmail(), paymentAccountEmail,
                "Given payment account does not match the provider's payment account! Expecting "
                        + paymentAccountEmail + ", but got " + loggedInProvider.getPaymentAccountEmail() + ".");

        // Check matching passwords.
        assertTrue(loggedInProvider.checkPasswordMatch(password),
                "Given password does not match the provider's password!");

        // Check matching user instances.
        assertEquals(state.getCurrentUser(), loggedInProvider, "User instances do not match match!");

        System.out.println("loginSeveralDifferentUsers test 4 passes!");
    }
    
    @Test
    @DisplayName("Test for logging in while already logged in")
    void logInWhileLoggedIn()
    {
        Consumer consumer1, consumer2;
        Controller ctrl = new Controller();

        // ------> Register as consumer, John Smith.
        String consumerName="John Smith",
                consumerEmail="johnsmith@gmail.com",
                phoneNumber="+123456789",
                consumerPassword="SecretPassword",
                consumerPaymentAccountEmail="johnsmith@money.com";

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                consumerName,
                consumerEmail,
                phoneNumber,
                consumerPassword,
                consumerPaymentAccountEmail
        );
        ctrl.runCommand(cmd);
        consumer1 = cmd.getResult();

        // ------> Register as consumer, Mike Schmidt.
        consumerName="Mike Schmidt";
        consumerEmail="mikeschmidt@gmail.com";
        phoneNumber="+987654321";
        consumerPassword="SuperSecretPassword";
        consumerPaymentAccountEmail="mikeschmidt@money.com";

        cmd = new RegisterConsumerCommand(
                consumerName,
                consumerEmail,
                phoneNumber,
                consumerPassword,
                consumerPaymentAccountEmail
        );
        ctrl.runCommand(cmd);
        consumer2 = cmd.getResult();

        // ------> Log in as John Smith.
        LoginCommand cmd2 = new LoginCommand(consumer1.getEmail(),"SecretPassword");
        ctrl.runCommand(cmd2);

        highlightWarning("Current User: "+ctrl.getContext().getUserState().getCurrentUser().getEmail());

        // Check matching first consumers.
        assertEquals(consumer1, cmd2.getResult(),
                "Logged in consumer does not match the expected consumer!");

        // ------> Log in as John Smith.
        cmd2 = new LoginCommand(consumer2.getEmail(), "SuperSecretPassword");
        ctrl.runCommand(cmd2);

        highlightWarning("Current User: "+ctrl.getContext().getUserState().getCurrentUser().getEmail());

        // Check matching first consumers.
        assertEquals(consumer2, ctrl.getContext().getUserState().getCurrentUser(),
                "Logged in consumer does not match the expected consumer!");

        // Check whether the current user has been overwritten.
        assertNotEquals(consumer1, cmd2.getResult(),
                "Logged in consumer is the first consumer! Expecting overwrite to the second consumer!");

        highlightPass("logInWhileLoggedIn passes!");
    }
}
