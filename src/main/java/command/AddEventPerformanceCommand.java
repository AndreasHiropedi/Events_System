package command;

import controller.Context;
import external.MockEntertainmentProviderSystem;
import model.*;
import state.EventState;
import state.UserState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class AddEventPerformanceCommand implements ICommand
{
    private long eventNumber;
    private String venueAddress;
    private LocalDateTime startDateTime, endDateTime;
    private List<String> performerNames;
    private boolean hasSocialDistancing, hasAirFiltration, isOutdoors;
    private int capacityLimit, venueSize;
    private EventPerformance latestEventPerformance;

    public AddEventPerformanceCommand(long eventNumber,
                                      String venueAddress,
                                      LocalDateTime startDateTime,
                                      LocalDateTime endDateTime,
                                      List<String> performerNames,
                                      boolean hasSocialDistancing,
                                      boolean hasAirFiltration,
                                      boolean isOutdoors,
                                      int capacityLimit,
                                      int venueSize)
    {
        this.eventNumber = eventNumber;
        this.venueAddress = venueAddress;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.hasSocialDistancing = hasSocialDistancing;
        this.hasAirFiltration = hasAirFiltration;
        this.isOutdoors = isOutdoors;
        this.capacityLimit = capacityLimit;
        this.venueSize = venueSize;
        latestEventPerformance = null;
    }

    @Override
    public void execute(Context context)
    {
        if (validDetails())
        {
            UserState userState = (UserState) context.getUserState();
            User currentUser = userState.getCurrentUser();
            EventState eventState = (EventState) context.getEventState();
            Event givenEvent = eventState.findEventByNumber(eventNumber);
            if (givenEvent == null)
            {
                StdOut.println("The event number is invalid!");
                return;
            }
            EntertainmentProvider eventOrganiser = givenEvent.getOrganiser();
            String givenEventTitle = givenEvent.getTitle();
            if (validProvider(currentUser, eventOrganiser))
            {
                if (noSameTimePerformanceForSameEventTitle(eventState, givenEventTitle))
                {
                    // Add the event performance on our system and record it on the provider's system
                    latestEventPerformance = eventState.createEventPerformance(
                            givenEvent, venueAddress, startDateTime, endDateTime,
                            performerNames, hasSocialDistancing, hasAirFiltration,
                            isOutdoors, capacityLimit, venueSize);
                    long performanceNumber = latestEventPerformance.getPerformanceNumber();
                    LocalDateTime performanceStartTime = latestEventPerformance.getStartDateTime();
                    LocalDateTime performanceEndTime = latestEventPerformance.getEndDateTime();
                    MockEntertainmentProviderSystem providerSystem = (MockEntertainmentProviderSystem) eventOrganiser.getProviderSystem();
                    providerSystem.recordNewPerformance(eventNumber, performanceNumber, performanceStartTime, performanceEndTime);
                    StdOut.println("Event performance added successfully!");
                }
                else
                {
                    StdOut.println("An event with the same title has a performance with the same start and end times!");
                }
            }
        }
    }

    /**
     * the purpose of this method is to check if all the details
     * for an event performance have suitable values
     * @return true if all details pass our validation, false otherwise
     */
    private boolean validDetails()
    {
        if (startDateTime.isAfter(endDateTime))
        {
            StdOut.println("Performance start time is after end time!");
            return false;
        }
        else if (capacityLimit < 1)
        {
            StdOut.println("The capacity limit is less than 1!");
            return false;
        }
        else if (venueSize < 1)
        {
            StdOut.println("The venue size is less than 1!");
            return false;
        }
        return true;
    }

    /**
     * Check if the currently-logged in user is an entertainment provider,
     * and if so, for a given event, check if the currently-logged in user
     * is also the event's organiser
     * @param currentUser the currently logged-in user
     * @param eventOrganiser the given event's organiser
     * @return true if the currently logged-in user is an entertainment provider
     * and the event organiser for a given event
     */
    private boolean validProvider(User currentUser, EntertainmentProvider eventOrganiser)
    {
        if (currentUser == null)
        {
            StdOut.println("User is not logged in!");
            return false;
        }
        if (!(currentUser instanceof EntertainmentProvider))
        {
            StdOut.println("User is not an entertainment provider!");
            return false;
        }
        else if (!eventOrganiser.equals(currentUser))
        {
            StdOut.println("The current entertainment provider is not the organiser of the given event!");
            return false;
        }
        return true;
    }

    /**
     * Given a valid event, check if, for any other events with the same title,
     * there exists a performance with the same start and end times
     * as the ones provided for creating a new performance
     * @param eventState the state of all the events in our system
     * @param givenEventTitle the title of the searched event
     * @return true if there is no performance with the same start and end times
     * for any event with the same title as the searched event
     */
    private boolean noSameTimePerformanceForSameEventTitle(EventState eventState, String givenEventTitle)
    {
        List<Event> allEvents = eventState.getAllEvents();
        // Check all events, and for each event, check all performances
        // to see if a match is found
        for (Event event : allEvents)
        {
            String eventTitle = event.getTitle();
            if (eventTitle.equals(givenEventTitle))
            {
                Collection<EventPerformance> eventPerformances = event.getPerformances();
                for (EventPerformance performance : eventPerformances)
                {
                    LocalDateTime performanceStartTime = performance.getStartDateTime();
                    LocalDateTime performanceEndTime = performance.getEndDateTime();
                    if (performanceStartTime.isEqual(startDateTime) && performanceEndTime.isEqual(endDateTime)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public EventPerformance getResult()
    {
        return latestEventPerformance;
    }
}
