package command;

import controller.Context;
import model.Consumer;
import model.ConsumerPreferences;
import model.StdOut;
import model.User;

import java.util.Map;

public class UpdateConsumerProfileCommand extends UpdateProfileCommand
{
    private String oldPassword, newName, newEmail, newPhoneNumber, newPassword, newPaymentAccountEmail;
    private ConsumerPreferences newPreferences;

    public UpdateConsumerProfileCommand(String oldPassword,
                                        String newName,
                                        String newEmail,
                                        String newPhoneNumber,
                                        String newPassword,
                                        String newPaymentAccountEmail,
                                        ConsumerPreferences newPreferences)
    {
        this.oldPassword = oldPassword;
        this.newName = newName;
        this.newEmail = newEmail;
        this.newPhoneNumber = newPhoneNumber;
        this.newPassword = newPassword;
        this.newPaymentAccountEmail = newPaymentAccountEmail;
        this.newPreferences = newPreferences;
    }

    @Override
    public void execute(Context context)
    {
        if (validDetails())
        {
            User currentUser = context.getUserState().getCurrentUser();
            if (currentUser == null)
            {
                StdOut.println("User not currently logged in!");
                return;
            }
            if (!(currentUser instanceof Consumer))
            {
                StdOut.println("User is not a consumer!");
                return;
            }
            boolean passwordsMatch = currentUser.checkPasswordMatch(oldPassword);
            if (!passwordsMatch)
            {
                StdOut.println("Old password provided does not match current password!");
            }
            else
            {
                Map<String, User> allUsers = context.getUserState().getAllUsers();
                boolean userAlreadyExists = allUsers.containsKey(newEmail);
                if (userAlreadyExists)
                {
                    StdOut.println("A user with the new email provided already exists!");
                }
                else
                {
                    currentUser.setEmail(newEmail);
                    ((Consumer) currentUser).setName(newName);
                    ((Consumer) currentUser).setPhoneNumber(newPhoneNumber);
                    currentUser.setPaymentAccountEmail(newPaymentAccountEmail);
                    currentUser.updatePassword(newPassword);
                    ((Consumer) currentUser).setPreferences(newPreferences);
                    StdOut.println("Consumer profile updated successfully!");
                }
            }
        }
    }

    /**
     * the purpose of this method is to check if all the new details
     * (and old password) for a consumer have suitable values
     * (are not null or empty strings)
     * @return true if all details pass our validation, false otherwise
     */
    private boolean validDetails()
    {
        if (oldPassword == null || oldPassword.equals(""))
        {
            StdOut.println("The old password provided was invalid!");
            return false;
        }
        else if (newName == null || newName.equals(""))
        {
            StdOut.println("The new name provided was invalid!");
            return false;
        }
        else if (newEmail == null || newEmail.equals(""))
        {
            StdOut.println("The new email provided was invalid!");
            return false;
        }
        else if (newPhoneNumber == null || newPhoneNumber.equals(""))
        {
            StdOut.println("The new phone number provided was invalid!");
            return false;
        }
        else if (newPassword == null || newPassword.equals(""))
        {
            StdOut.println("The new password provided was invalid!");
            return false;
        }
        else if (newPaymentAccountEmail == null || newPaymentAccountEmail.equals(""))
        {
            StdOut.println("The new payment account email provided was invalid!");
            return false;
        }
        else if (newPreferences == null)
        {
            StdOut.println("The new preferences provided were invalid!");
            return false;
        }
        return true;
    }
}
