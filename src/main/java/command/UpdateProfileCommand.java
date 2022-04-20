package command;

import controller.Context;
import model.User;

public abstract class UpdateProfileCommand implements ICommand
{
    protected Boolean successResult = false;

    public UpdateProfileCommand() {}

    protected boolean isProfileUpdateValid(Context context, String oldPassword, String newEmail)
    {
        User user = context.getUserState().getCurrentUser();
        boolean passwordsMatch = user.checkPasswordMatch(oldPassword);
        String currentEmail = user.getEmail();

        if (passwordsMatch && !currentEmail.equals(newEmail))
        {
            successResult = true;
            return true;
        }
        return false;
    }

    public Boolean getResult()
    {
        return this.successResult;
    }
}
