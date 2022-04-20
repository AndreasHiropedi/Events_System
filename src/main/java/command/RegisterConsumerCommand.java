package command;

import controller.Context;
import model.Consumer;
import model.StdOut;
import model.User;
import state.UserState;

import java.util.Map;

public class RegisterConsumerCommand implements ICommand
{
    private String name, email, phoneNumber, password, paymentAccountEmail;
    private Consumer newConsumer = null;

    public RegisterConsumerCommand(String name,
                                   String email,
                                   String phoneNumber,
                                   String password,
                                   String paymentAccountEmail)
    {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.paymentAccountEmail = paymentAccountEmail;
    }

    @Override
    public void execute(Context context)
    {
        if (validDetails())
        {
            UserState userState = (UserState) context.getUserState();
            Map<String, User> allUsers = userState.getAllUsers();
            boolean userAlreadyExists = allUsers.containsKey(email);
            if (userAlreadyExists)
            {
                StdOut.println("There already exists a consumer with that email address!");
            }
            else
            {
                // Register the new consumer
                this.newConsumer = new Consumer(name, email, phoneNumber, password, paymentAccountEmail);
                userState.setCurrentUser(newConsumer);
                context.setUserState(userState);
                userState.addUser(newConsumer);
                StdOut.println("Consumer added successfully!");
            }
        }
    }

    /**
     * the purpose of this method is to check if all the details
     * for a consumer have suitable values
     * (are not null or empty strings)
     * @return true if all details pass our validation, false otherwise
     */
    private boolean validDetails()
    {
        if (name == null || name.equals(""))
        {
            StdOut.println("The name provided was invalid!");
            return false;
        }
        else if (email == null || email.equals(""))
        {
            StdOut.println("The email address provided was invalid!");
            return false;
        }
        else if (phoneNumber == null || phoneNumber.equals(""))
        {
            StdOut.println("The phone number provided was invalid!");
            return false;
        }
        else if (password == null || password.equals(""))
        {
            StdOut.println("The password provided was invalid!");
            return false;
        }
        else if (paymentAccountEmail == null || paymentAccountEmail.equals(""))
        {
            StdOut.println("The payment account email address provided was invalid!");
            return false;
        }
        return true;
    }

    @Override
    public Consumer getResult()
    {
        return newConsumer;
    }
}
