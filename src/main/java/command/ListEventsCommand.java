package command;

import controller.Context;
import model.*;
import state.EventState;
import state.UserState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListEventsCommand implements ICommand
{

    private boolean userEventsOnly, activeEventsOnly;
    private List<Event> result = null;

    public ListEventsCommand(boolean userEventsOnly, boolean activeEventsOnly)
    {
        this.userEventsOnly = userEventsOnly;
        this.activeEventsOnly = activeEventsOnly;
    }

    @Override
    public void execute(Context context)
    {
        EventState eventState = (EventState) context.getEventState();
        result = eventState.getAllEvents();

        if (userEventsOnly)
        {
            UserState userState = (UserState) context.getUserState();
            User currentUser = userState.getCurrentUser();

            if (currentUser != null)
            {
                handleUserOnlyEvents(eventState, currentUser);
            }
            else
            {
                StdOut.println("Current user is not logged in!");
                result = null;
            }
        }
        if (activeEventsOnly)
        {
            handleActiveEvents();
        }
    }

    /**
     * this method updates result in the case that userEventsOnly was set to true
     * checks which type of user is logged in, and returns the appropriate list
     * @param eventState the current state for all events
     * @param currentUser the currently logged-in user
     */
    private void handleUserOnlyEvents(EventState eventState, User currentUser)
    {
        if (currentUser instanceof Consumer)
        {
            // Create the filters for the events to be listed
            ConsumerPreferences currentUserPreferences = ((Consumer) currentUser).getPreferences();
            boolean userPreferredSocialDistancing = currentUserPreferences.preferSocialDistancing;
            boolean userPreferredAirFiltration = currentUserPreferences.preferAirFiltration;
            boolean userPreferredOutdoorsOnly = currentUserPreferences.preferOutdoorsOnly;
            int userPreferredMaxCapacity = currentUserPreferences.preferredMaxCapacity;
            int userPreferredMaxVenueSize = currentUserPreferences.preferredMaxVenueSize;
            result = new ArrayList<>();
            // Go through events and check whether all the filters apply to all the performances
            for (Event event: eventState.getAllEvents())
            {
                Collection<EventPerformance> eventPerformances = event.getPerformances();
                for (EventPerformance performance: eventPerformances)
                {
                    LocalDateTime start = performance.getStartDateTime();
                    if (performance.hasSocialDistancing() == userPreferredSocialDistancing
                            && performance.hasAirFiltration() == userPreferredAirFiltration
                            && performance.isOutdoors() == userPreferredOutdoorsOnly
                            && performance.getCapacityLimit() <= userPreferredMaxCapacity
                            && performance.getVenueSize() <= userPreferredMaxVenueSize
                            && start.isAfter(LocalDateTime.now()))
                    {
                        result.add(event);
                        break;
                    }
                }
            }
        }
        else if (currentUser instanceof EntertainmentProvider)
        {
            result = ((EntertainmentProvider) currentUser).getEvents();
        }
        else
        {
            StdOut.println("The current user is not a consumer or an entertainment provider," +
                    " all events will be returned!");
        }
    }

    /**
     * based on whether userEventsOnly was true or false
     * get all active events from result, and update result afterwards
     */
    private void handleActiveEvents()
    {
        // set list of all active events as result
        List<Event> resultCopy = new ArrayList<>();
        if (result != null)
        {
            for (Event event: result)
            {
                if (event.getStatus().equals(EventStatus.ACTIVE))
                {
                    resultCopy.add(event);
                }
            }
            result = resultCopy;
        }
    }

    @Override
    public List<Event> getResult()
    {
        return result;
    }
}
