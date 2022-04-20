package state;

import model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventState implements IEventState, Cloneable
{
    List<Event> events;
    private long nextEventNumber, nextPerformanceNumber;

    public EventState()
    {
        events = new ArrayList<>();
        nextEventNumber = 1;
        nextPerformanceNumber = 1;
    }

    public EventState(IEventState other)
    {
        EventState otherState = null;

        /*
            Attempt to clone the instance of the other object. In the case
            of a CloneNotSupportedException error, do nothing. In this case,
            otherState will be left null, leading to the execution of the
            else statement below.
         */
        try
        {
            otherState = (EventState) ((EventState)other).clone();
        }
        catch (CloneNotSupportedException ignored){ }

        if(otherState != null)
        {
            this.nextPerformanceNumber = otherState.nextPerformanceNumber;
            this.nextEventNumber = otherState.nextEventNumber;
            this.events = new ArrayList<>();

            for (Event event: otherState.events)
            {
                if (event instanceof TicketedEvent)
                {
                    TicketedEvent currentEvent = (TicketedEvent) event;
                    TicketedEvent eventToAdd = new TicketedEvent(currentEvent.getEventNumber(),
                            currentEvent.getOrganiser(), currentEvent.getTitle(), currentEvent.getType(),
                            currentEvent.getOriginalTicketPrice(), currentEvent.getNumTickets());
                    this.events.add(eventToAdd);
                }
                else
                {
                    NonTicketedEvent currentEvent = (NonTicketedEvent) event;
                    NonTicketedEvent eventToAdd = new NonTicketedEvent(currentEvent.getEventNumber(),
                            currentEvent.getOrganiser(), currentEvent.getTitle(), currentEvent.getType());
                    this.events.add(eventToAdd);
                }
            }
        }
        else
        {
            StdOut.println("Failed to copy otherState of type EventState...");
        }
    }

    public List<Event> getAllEvents()
    {
        return events;
    }

    public void setAllEvents(List<Event> updatedEvents)
    {
        events = updatedEvents;
    }

    public Event findEventByNumber(long eventNumber)
    {
        for (Event event: events)
        {
            if (event.getEventNumber() == eventNumber)
            {
                return event;
            }
        }
        return null;
    }

    public NonTicketedEvent createNonTicketedEvent(EntertainmentProvider organiser,
                                                   String title,
                                                   EventType type)
    {
        if (organiser != null)
        {
            NonTicketedEvent newEvent = new NonTicketedEvent(nextEventNumber++,
                    organiser, title, type);
            events.add(newEvent);
            return newEvent;
        }
        return null;
    }

    public TicketedEvent createTicketedEvent(EntertainmentProvider organiser,
                                             String title,
                                             EventType type,
                                             double ticketPrice,
                                             int numTickets)
    {
        TicketedEvent newEvent = new TicketedEvent(nextEventNumber++,
                organiser, title, type, ticketPrice, numTickets);
        events.add(newEvent);
        return newEvent;
    }

    public EventPerformance createEventPerformance(Event event,
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
        if (event != null)
        {
            EventPerformance newPerformance = new EventPerformance(nextPerformanceNumber++,
                    event, venueAddress, startDateTime, endDateTime, performerNames,
                    hasSocialDistancing, hasAirFiltration, isOutdoors, capacityLimit,
                    venueSize);
            event.addPerformance(newPerformance);
            return newPerformance;
        }
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
