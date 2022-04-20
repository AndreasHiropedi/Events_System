package command;

import controller.Context;
import model.EntertainmentProvider;
import model.StdOut;
import model.User;

import java.util.List;
import java.util.Map;

public class UpdateEntertainmentProviderProfileCommand extends UpdateProfileCommand
{
    private String oldPassword, newOrgName, newOrgAddress, newPaymentAccountEmail, newMainRepName, newMainRepEmail;
    private String newPassword;
    private List<String> newOtherRepNames, newOtherRepEmails;

    public UpdateEntertainmentProviderProfileCommand(String oldPassword,
                                                     String newOrgName,
                                                     String newOrgAddress,
                                                     String newPaymentAccountEmail,
                                                     String newMainRepName,
                                                     String newMainRepEmail,
                                                     String newPassword,
                                                     List<String> newOtherRepNames,
                                                     List<String> newOtherRepEmails)
    {
        this.oldPassword = oldPassword;
        this.newOrgName = newOrgName;
        this.newOrgAddress = newOrgAddress;
        this.newPaymentAccountEmail = newPaymentAccountEmail;
        this.newMainRepName = newMainRepName;
        this.newMainRepEmail = newMainRepEmail;
        this.newPassword = newPassword;
        this.newOtherRepNames = newOtherRepNames;
        this.newOtherRepEmails = newOtherRepEmails;
    }

    @Override
    public void execute(Context context)
    {
        if (validDetails())
        {
            // Check whether we are allowed to update the details
            User currentUser = context.getUserState().getCurrentUser();
            if (validUser(currentUser))
            {
                boolean passwordsMatch = currentUser.checkPasswordMatch(oldPassword);
                if (!passwordsMatch)
                {
                    StdOut.println("The provided old password does not match the current user's password!");
                    return;
                }
                Map<String, User> allUsers = context.getUserState().getAllUsers();
                boolean userAlreadyExists = allUsers.containsKey(newMainRepEmail);
                if (userAlreadyExists)
                {
                    StdOut.println("A user with the new email provided already exists!");
                }
                else
                {
                    if (!organisationAlreadyExists(allUsers))
                    {
                        successfullyUpdatePreferences(currentUser);
                    }
                }
            }
        }
    }

    /**
     * the purpose of this method is to check if all the new details
     * (and old password) for an entertainment provider have suitable values
     * (are not null or empty strings)
     * @return true if all details pass our validation, false otherwise
     */
    private boolean validDetails()
    {
        if (oldPassword == null || oldPassword.equals(""))
        {
            StdOut.println("The provided old password is invalid!");
            return false;
        }
        else if (newOrgName == null || newOrgName.equals(""))
        {
            StdOut.println("The provided new organisation name is invalid!");
            return false;
        }
        else if (newOrgAddress == null || newOrgAddress.equals(""))
        {
            StdOut.println("The provided new organisation address is invalid!");
            return false;
        }
        else if (newPaymentAccountEmail == null || newPaymentAccountEmail.equals(""))
        {
            StdOut.println("The provided new payment account email is invalid!");
            return false;
        }
        else if (newMainRepName == null || newMainRepName.equals(""))
        {
            StdOut.println("The provided new main representative name is invalid!");
            return false;
        }
        else if (newMainRepEmail == null || newMainRepEmail.equals(""))
        {
            StdOut.println("The provided new main representative email is invalid!");
            return false;
        }
        else if (newPassword == null || newPassword.equals(""))
        {
            StdOut.println("The provided new password is invalid!");
            return false;
        }
        else if (newOtherRepNames == null)
        {
            StdOut.println("The provided list of new representatives' names is invalid!");
            return false;
        }
        else if (newOtherRepEmails == null)
        {
            StdOut.println("The provided list of new representatives' emails is invalid!");
            return false;
        }
        return true;
    }

    /**
     * If all the checks are passed, update the entertainment
     * provider's profile with the new details
     * @param currentUser the currently-logged in user
     */
    private void successfullyUpdatePreferences(User currentUser)
    {
        // Update preferences
        ((EntertainmentProvider) currentUser).setOrgName(newOrgName);
        ((EntertainmentProvider) currentUser).setOrgAddress(newOrgAddress);
        currentUser.setPaymentAccountEmail(newPaymentAccountEmail);
        ((EntertainmentProvider) currentUser).setMainRepName(newMainRepName);
        ((EntertainmentProvider) currentUser).setMainRepEmail(newMainRepEmail);
        currentUser.updatePassword(newPassword);
        ((EntertainmentProvider) currentUser).setOtherRepNames(newOtherRepNames);
        ((EntertainmentProvider) currentUser).setOtherRepEmails(newOtherRepEmails);
        StdOut.println("Entertainment provider profile was successfully updated!");
    }

    /**
     * Check if the current user is logged in and
     * is also an entertainment provider
     * @param currentUser the currently logged-in user
     * @return true if the user is logged in and
     * an entertainment provider
     */
    private boolean validUser(User currentUser)
    {
        if (currentUser == null)
        {
            StdOut.println("User is currently not logged in!");
            return false;
        }
        if (!(currentUser instanceof EntertainmentProvider))
        {
            StdOut.println("The current user is not an entertainment provider!");
            return false;
        }
        return true;
    }

    /**
     * Check if the organisation has already
     * been registered by someone else
     * @param allUsers the list of all users
     *                 registered on our system
     * @return true if the organisation is already
     * registered on our system
     */
    private boolean organisationAlreadyExists(Map<String, User> allUsers)
    {
        // Check the user list for a match
        for (User user : allUsers.values())
        {
            if (user instanceof EntertainmentProvider)
            {
                String userOrgAddress = ((EntertainmentProvider) user).getOrgAddress();
                String userOrgName = ((EntertainmentProvider) user).getOrgName();
                if (userOrgAddress.equals(newOrgAddress) && userOrgName.equals(newOrgName))
                {
                    StdOut.println("Organisations has already been registered by someone else!");
                    return true;
                }
            }
        }
        return false;
    }
}
