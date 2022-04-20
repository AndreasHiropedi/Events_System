package model;

import java.util.*;

public abstract class Event
{
    private long eventNumber;
    private EntertainmentProvider organiser;
    private String title;
    private EventType type;
    private EventStatus status;
    private List<EventPerformance> performances;

    protected Event (long eventNumber, EntertainmentProvider organiser, String title, EventType type)
    {
        this.performances = new ArrayList<>();
        this.eventNumber = eventNumber;
        this.organiser = organiser;
        this.title = title;
        this.type = type;
        this.status = EventStatus.ACTIVE;
    }

    public void addPerformance(EventPerformance performance)
    {
       performances.add(performance);
    }

    public void cancel()
    {
        status = EventStatus.CANCELLED;
    }

    public long getEventNumber()
    {
        return eventNumber;
    }

    public void setEventNumber(long eventNumber)
    {
        this.eventNumber = eventNumber;
    }

    public EntertainmentProvider getOrganiser()
    {
        return organiser;
    }

    public void setOrganiser(EntertainmentProvider organiser)
    {
        this.organiser = organiser;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public EventType getType()
    {
        return type;
    }

    public void setType(EventType type)
    {
        this.type = type;
    }

    public EventStatus getStatus()
    {
        return status;
    }

    public void setStatus(EventStatus status)
    {
        this.status = status;
    }

    public EventPerformance getPerformanceByNumber(long performanceNumber)
    {
        for (EventPerformance performance: performances)
        {
            if (performance.getPerformanceNumber() == performanceNumber)
            {
                return performance;
            }
        }
        return null;
    }

    public Collection<EventPerformance> getPerformances()
    {
        return performances;
    }
}
