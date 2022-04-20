package command;

import controller.Context;
import model.EntertainmentProvider;
import model.StdOut;
import model.User;
import state.UserState;

import java.util.List;
import java.util.Map;

public class RegisterEntertainmentProviderCommand implements ICommand
{
    private String orgName, orgAddress, paymentAccountEmail, mainRepName, mainRepEmail, password;
    private List<String> otherRepNames, otherRepEmails;
    private EntertainmentProvider entertainmentProvider = null;

    public RegisterEntertainmentProviderCommand(String orgName,
                                                String orgAddress,
                                                String paymentAccountEmail,
                                                String mainRepName,
                                                String mainRepEmail,
                                                String password,
                                                List<String> otherRepNames,
                                                List<String> otherRepEmails)
    {
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.paymentAccountEmail = paymentAccountEmail;
        this.mainRepName = mainRepName;
        this.mainRepEmail = mainRepEmail;
        this.password = password;
        this.otherRepNames = otherRepNames;
        this.otherRepEmails = otherRepEmails;
    }

    /**
     * the purpose of this method is to check if all the details
     * for an entertainment provider have suitable values
     * (are not null or empty strings)
     * @return true if all details pass our validation, false otherwise
     */
    private boolean validDetails() 
    {
        if (orgName == null || orgName.equals(""))
        {
            StdOut.println("Invalid organisation name!");
            return false;
        }
        else if (orgAddress == null || orgAddress.equals(""))
        {
            StdOut.println("Invalid organisation address!");
            return false;
        }
        else if (paymentAccountEmail == null || paymentAccountEmail.equals(""))
        {
            StdOut.println("Invalid organisation payment account email!");
            return false;
        }
        else if (mainRepName == null || mainRepName.equals(""))
        {
            StdOut.println("Invalid main representative name!");
            return false;
        }
        else if (mainRepEmail == null || mainRepEmail.equals(""))
        {
            StdOut.println("Invalid main representative email address!");
            return false;
        }
        else if (password == null || password.equals(""))
        {
            StdOut.println("Invalid password!");
            return false;
        }
        else if (otherRepNames == null)
        {
            StdOut.println("Issue with the other representatives' names!");
            return false;
        }
        else if (otherRepEmails == null)
        {
            StdOut.println("Issue with the other representatives' emails!");
            return false;
        }
        return true;
    }

    /**
     * Checks if an organisation with the same name and address
     * was already registered on our system
     * @param allUsers the list of all users registered on our system
     * @return true if a match for the organisation name and address
     * is found on our system (the organisation is already registered)
     */
    private boolean organisationAlreadyRegistered(Map<String, User> allUsers)
    {
        for (User user: allUsers.values())
        {
            if (user instanceof EntertainmentProvider)
            {
                String userOrgAddress = ((EntertainmentProvider) user).getOrgAddress();
                String userOrgName = ((EntertainmentProvider) user).getOrgName();
                if (userOrgAddress.equals(orgAddress) && userOrgName.equals(orgName))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void execute(Context context)
    {
        if(validDetails())
        {
            UserState userState = (UserState) context.getUserState();
            Map<String, User> allUsers = userState.getAllUsers();
            boolean accountAlreadyExists = allUsers.containsKey(mainRepEmail);
            if (accountAlreadyExists)
            {
                StdOut.println("An account with the given main representative email address already exists!");
            }
            else
            {
                if (organisationAlreadyRegistered(allUsers))
                {
                    StdOut.println("Organisations has already been registered by someone else!");
                }
                else
                {
                    // Register new entertainment provider after passing all checks
                    entertainmentProvider = new EntertainmentProvider(orgName, orgAddress,
                            paymentAccountEmail, mainRepName, mainRepEmail, password,
                            otherRepNames, otherRepEmails);
                    userState.setCurrentUser(entertainmentProvider);
                    context.setUserState(userState);
                    userState.addUser(entertainmentProvider);
                    StdOut.println("Entertainment provider registered successfully!");
                }
            }
        }
    }

    @Override
    public EntertainmentProvider getResult()
    {
        return entertainmentProvider;
    }
}
