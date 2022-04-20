package command;

import controller.Context;
import model.StdOut;
import model.User;
import state.UserState;

import java.util.Map;

public class LoginCommand implements ICommand
{

    private String email, password;
    private User user = null;

    public LoginCommand(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    @Override
    public void execute(Context context)
    {
        UserState userState = (UserState) context.getUserState();
        Map<String, User> allUsers = userState.getAllUsers();
        boolean userRegistered = allUsers.containsKey(email);
        if (userRegistered)
        {
            User registeredUser = allUsers.get(email);
            if (registeredUser.checkPasswordMatch(password))
            {
                user = registeredUser;
                userState.setCurrentUser(user);
                context.setUserState(userState);
                StdOut.println("User logged in successfully!");
            }
            else
            {
                StdOut.println("The password provided does not match the saved password!");
            }
        }
        else
        {
            StdOut.println("The account email is not registered on the system!");
        }
    }

    @Override
    public User getResult()
    {
        return user;
    }
}
