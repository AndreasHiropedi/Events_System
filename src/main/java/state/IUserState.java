package state;

import model.User;
import java.util.Map;

public interface IUserState
{
    public void addUser(User user);

    public Map<String, User> getAllUsers();

    public User getCurrentUser();

    public void setCurrentUser(User user);
}
