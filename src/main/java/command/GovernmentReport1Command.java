package command;

import controller.Context;
import model.*;
import state.BookingState;
import state.EventState;
import state.UserState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GovernmentReport1Command implements ICommand
{

    private final LocalDateTime intervalStartInclusive, intervalEndInclusive;
    private final List<Booking> bookings = new ArrayList<>();

    public GovernmentReport1Command(LocalDateTime intervalStartInclusive, LocalDateTime intervalEndInclusive)
    {
        this.intervalStartInclusive = intervalStartInclusive;
        this.intervalEndInclusive = intervalEndInclusive;
    }

    @Override
    public void execute(Context context)
    {
        if (validUser(context))
        {
            EventState eventState = (EventState) context.getEventState();
            Collection<Event> allEvents = eventState.getAllEvents();

            List<Event> allValidEvents = getAllValidEvents(allEvents);

            ArrayList<Booking> allBookingsForValidEvents =
                    getBookingsForAllValidEvents(context, allValidEvents);

            if (intervalEndInclusive.isBefore(intervalStartInclusive))
            {
                StdOut.println("The time interval provided was invalid!");
                return;
            }
            // Of those bookings, create a list of all bookings that fall in the given time interval
            for (Booking booking: allBookingsForValidEvents)
            {
                EventPerformance eventPerformance = booking.getEventPerformance();
                LocalDateTime performanceStart = eventPerformance.getStartDateTime();
                LocalDateTime performanceEnd = eventPerformance.getEndDateTime();
                if (!performanceStart.isBefore(intervalStartInclusive) && !performanceEnd.isAfter(intervalEndInclusive)
                && performanceStart.isAfter(LocalDateTime.now()))
                {
                    bookings.add(booking);
                }
            }
        }
    }

    /**
     * Ensures that the current user is logged in
     * and is also a government representative
     * @param context the current context with all the states up to date
     * @return true if the current user is logged in and a government rep,
     * false otherwise
     */
    private boolean validUser(Context context)
    {
        UserState userState = (UserState) context.getUserState();
        User user = userState.getCurrentUser();
        if (user == null)
        {
            StdOut.println("User must be logged in!");
            return false;
        }
        else if (!(user instanceof GovernmentRepresentative))
        {
            StdOut.println("User is not a government representative!");
            return false;
        }
        return true;
    }

    /**
     * Filter out all events that have been cancelled from
     * all the events on our system
     * @param allEvents list of all events on our system
     * @return list of all events still active on our system
     */
    private List<Event> getAllValidEvents(Collection<Event> allEvents)
    {
        List<Event> allValidEvents = new ArrayList<>();
        // Create list of all active events
        for (Event event: allEvents)
        {
            if (event instanceof TicketedEvent)
            {
                if (((TicketedEvent) event).isSponsored() && event.getStatus().equals(EventStatus.ACTIVE))
                {
                    allValidEvents.add(event);
                }
            }
        }
        return allValidEvents;
    }

    /**
     * For each of all the active events found, retrieve all bookings
     * for that event
     * @param context the current context with all states up to date
     * @param allValidEvents list of all events still active on our system
     * @return a list of all the bookings for all the active events on
     * our system
     */
    private ArrayList<Booking> getBookingsForAllValidEvents(Context context,
                                                            List<Event> allValidEvents)
    {
        ArrayList<Booking> allBookingsForValidEvents = new ArrayList<>();
        BookingState bookingState = (BookingState) context.getBookingState();
        // Create list of all bookings for those active events
        for (Event event: allValidEvents)
        {
            long eventNumber = event.getEventNumber();
            List<Booking> eventBookings = bookingState.findBookingsByEventNumber(eventNumber);
            allBookingsForValidEvents.addAll(eventBookings);
        }
        return allBookingsForValidEvents;
    }

    @Override
    public List<Booking> getResult()
    {
        if (bookings.size() > 0)
        {
            return bookings;
        }
        return null;
    }
}
