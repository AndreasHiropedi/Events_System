package command;

import controller.Context;
import model.Booking;
import model.Consumer;
import model.StdOut;
import model.User;

import java.util.List;

public class ListConsumerBookingsCommand implements ICommand
{
    private List<Booking> bookingListResult;

    public ListConsumerBookingsCommand()
    {
        bookingListResult = null;
    }

    @Override
    public void execute(Context context)
    {
        User currentUser = context.getUserState().getCurrentUser();
        if (currentUser == null)
        {
            StdOut.println("No user is logged in!");
            return;
        }
        if (currentUser instanceof Consumer)
        {
            Consumer consumer = (Consumer) currentUser;
            bookingListResult = consumer.getBookings();
        }
        else
        {
            StdOut.println("Current user is not a consumer!");
        }
    }

    @Override
    public List<Booking> getResult()
    {
        return bookingListResult;
    }
}
