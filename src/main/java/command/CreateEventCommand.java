package command;

import controller.Context;
import model.EntertainmentProvider;
import model.EventType;
import model.StdOut;
import model.User;
import state.UserState;

abstract public class CreateEventCommand implements ICommand
{

    protected Long eventNumberResult = null;
    protected String title;
    protected EventType type;

    public CreateEventCommand(String title, EventType type)
    {
        this.title = title;
        this.type = type;
    }

    @Override
    public Long getResult()
    {
        return eventNumberResult;
    }

    protected boolean isUserAllowedToCreateEvent(Context context)
    {
        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();

        if (currentUser != null)
        {
            if (currentUser instanceof EntertainmentProvider)
            {
                return true;
            }
            StdOut.println("User is not an entertainment provider!");
            return false;
        }
        StdOut.println("Current user is not logged in!");
        return false;
    }
}
