package model;

import java.time.LocalDateTime;
import java.util.List;

public class EventPerformance
{
    private long performanceNumber;
    private Event event;
    private String venueAddress;
    private LocalDateTime startDateTime, endDateTime;
    private List<String> performerNames;
    private boolean hasSocialDistancing, hasAirFiltration, isOutdoors;
    private int capacityLimit, venueSize;

    public EventPerformance(long performanceNumber, Event event, String venueAddress,
                            LocalDateTime startDateTime, LocalDateTime endDateTime,
                            List<String> performerNames, boolean hasSocialDistancing,
                            boolean hasAirFiltration, boolean isOutdoors, int capacityLimit,
                            int venueSize)
    {
        this.venueAddress = venueAddress;
        this.performerNames = performerNames;
        this.hasSocialDistancing = hasSocialDistancing;
        this.hasAirFiltration = hasAirFiltration;
        this.isOutdoors = isOutdoors;
        this.performanceNumber = performanceNumber;
        this.event = event;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.capacityLimit = capacityLimit;
        this.venueSize = venueSize;
    }

    public long getPerformanceNumber()
    {
        return performanceNumber;
    }

    public void setPerformanceNumber(long performanceNumber)
    {
        this.performanceNumber = performanceNumber;
    }

    public Event getEvent()
    {
        return event;
    }

    public void setEvent(Event event)
    {
        this.event = event;
    }

    public String getVenueAddress()
    {
        return venueAddress;
    }

    public LocalDateTime getStartDateTime()
    {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime)
    {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime()
    {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime)
    {
        this.endDateTime = endDateTime;
    }

    public List<String> getPerformerNames()
    {
        return performerNames;
    }

    public boolean hasSocialDistancing()
    {
        return hasSocialDistancing;
    }

    public boolean hasAirFiltration()
    {
        return hasAirFiltration;
    }

    public boolean isOutdoors()
    {
        return isOutdoors;
    }

    public int getCapacityLimit()
    {
        return capacityLimit;
    }

    public void setCapacityLimit(int capacityLimit)
    {
        this.capacityLimit = capacityLimit;
    }

    public int getVenueSize()
    {
        return venueSize;
    }

    public void setVenueSize(int venueSize)
    {
        this.venueSize = venueSize;
    }

    public String toString()
    {
        return "The event's performance number is: " + performanceNumber + "."
                + "The corresponding event is: " + event.toString() + "."
                + "The event's venue address is: " + venueSize + "."
                + "The start of the event is: " + startDateTime.toString() + "."
                + "The end of the event is: " + endDateTime.toString() + "."
                + "The performers' names are: " + performerNames.toString() + "."
                + "Social distancing is in place for the performance: " + hasSocialDistancing + "."
                + "Air filtration is in place for the performance: " + hasAirFiltration + "."
                + "The performance will be outdoors: " + isOutdoors + "."
                + "The maximum number of people who will be allowed to attend the performance is: " + capacityLimit + "."
                + "The maximum number of people who could legally be allowed in the venue is: " + venueSize + ".";
    }
}
