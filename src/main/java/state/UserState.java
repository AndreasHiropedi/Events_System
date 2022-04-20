package state;

import model.*;

import java.util.HashMap;
import java.util.Map;

public class UserState implements IUserState, Cloneable
{
    private HashMap<String, User> users;
    User currentUser;

    public UserState()
    {
        users = new HashMap<>();
        currentUser = null;
        registerGovernmentRepresentatives();
    }

    // Overloaded constructor for deep copying
    public UserState(IUserState other)
    {
        UserState otherState = null;
        /*
            Attempt to clone the instance of the other object. In the case
            of a CloneNotSupportedException error, do nothing. In this case,
            otherState will be left null, leading to the execution of the
            else statement below.
         */
        try
        {
            otherState = (UserState) ((UserState)other).clone();
        }
        catch (CloneNotSupportedException ignored){ }

        if(otherState != null)
        {
            this.currentUser = otherState.getCurrentUser();
            this.users = new HashMap<>();

            for (String key: otherState.users.keySet())
            {
                User user = otherState.users.get(key);
                this.users.put(key, user);
            }
        }
        else
        {
            StdOut.println("Failed to copy otherState of type UserState...");
        }
    }

    private void registerGovernmentRepresentatives()
    {
        GovernmentRepresentative govRep1 = new GovernmentRepresentative("margaret.thatcher@gov.uk", "The Good times  ", "payment@gov.uk");
        users.put("margaret.thatcher@gov.uk", govRep1);
    }

    public void addUser(User user)
    {
        if (user != null)
        {
            users.put(user.getEmail(), user);
        }
    }

    public Map<String, User> getAllUsers()
    {
        return users;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(User user)
    {
        currentUser = user;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
