package command;

import controller.Context;
import model.Event;
import model.EventPerformance;
import model.StdOut;
import model.User;
import state.UserState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListEventsOnGivenDateCommand extends ListEventsCommand
{
    boolean userEventsOnly, activeEventsOnly;
    LocalDateTime searchDateTime;
    List<Event> eventsOnGivenDate = new ArrayList<>();

    public ListEventsOnGivenDateCommand(boolean userEventsOnly, boolean activeEventsOnly, LocalDateTime searchDateTime)
    {
        super(userEventsOnly, activeEventsOnly);
        this.userEventsOnly = userEventsOnly;
        this.activeEventsOnly = activeEventsOnly;
        this.searchDateTime = searchDateTime;
    }

    @Override
    public void execute(Context context)
    {
        // ensure null is returned if no user is logged in
        if (userIsLoggedIn(context))
        {
            ListEventsCommand cmd = new ListEventsCommand(userEventsOnly, activeEventsOnly);
            cmd.execute(context);
            List<Event> requestedEvents = cmd.getResult();
            // Loop through the events to check whether they fit the given date
            for (Event event: requestedEvents)
            {
                Collection<EventPerformance> performances = event.getPerformances();
                for (EventPerformance performance: performances)
                {
                    // Create date times and check whether event is within the acceptable range
                    LocalDateTime startTime = performance.getStartDateTime();
                    LocalDateTime endTime = performance.getEndDateTime();
                    LocalDateTime maxStartTime = searchDateTime.minusDays(1);
                    LocalDateTime maxEndTime = searchDateTime.plusDays(1);
                    if (startTime.isAfter(maxStartTime) && endTime.isBefore(maxEndTime)
                            && !startTime.isEqual(maxStartTime) && !endTime.isEqual(maxEndTime))
                    {
                        eventsOnGivenDate.add(event);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Ensures that the current user is logged in
     * @param context the current context with all the states up to date
     * @return true if the current user is logged in
     */
    private boolean userIsLoggedIn(Context context)
    {
        UserState userState = (UserState) context.getUserState();
        User user = userState.getCurrentUser();
        if (user == null)
        {
            StdOut.println("User must be logged in!");
            return false;
        }
        return true;
    }

    @Override
    public List<Event> getResult()
    {
        if (eventsOnGivenDate.isEmpty())
        {
            return null;
        }
        return eventsOnGivenDate;
    }
}
