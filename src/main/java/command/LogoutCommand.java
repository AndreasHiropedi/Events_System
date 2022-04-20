package command;

import controller.Context;
import model.StdOut;
import model.User;
import state.UserState;

public class LogoutCommand implements ICommand
{

    public LogoutCommand() {}

    @Override
    public void execute(Context context)
    {
        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        if (currentUser != null)
        {
            userState.setCurrentUser(null);
            StdOut.println("User successfully logged out!");
        }
        else
        {
            StdOut.println("Can't log out since user was not logged in!");
        }
    }

    @Override
    public Object getResult()
    {
        return null;
    }
}
