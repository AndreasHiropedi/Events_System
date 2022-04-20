package command;

import controller.Context;
import model.*;

import java.util.List;

public class ListEventBookingsCommand implements ICommand
{
    private long eventNumber;
    private List<Booking> bookingListResult;

    public ListEventBookingsCommand(long eventNumber)
    {
        this.eventNumber = eventNumber;
        bookingListResult = null;
    }

    @Override
    public void execute(Context context)
    {
        User currentUser = context.getUserState().getCurrentUser();
        Event event = context.getEventState().findEventByNumber(eventNumber);
        if (event instanceof TicketedEvent)
        {
            if (currentUser instanceof GovernmentRepresentative
                    || event.getOrganiser().equals(currentUser))
            {
                bookingListResult =
                        context.getBookingState().findBookingsByEventNumber(eventNumber);
            }
        }

    }

    @Override
    public List<Booking> getResult()
    {
        return bookingListResult;
    }
}
