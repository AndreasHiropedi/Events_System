package model;

public class NonTicketedEvent extends Event
{
    public NonTicketedEvent(long eventNumber, EntertainmentProvider organiser, String title, EventType type)
    {
        super(eventNumber, organiser, title, type);
    }

    public String toString()
    {
        return "This event's number is: " + getEventNumber() + "."
                + "This event's organiser is: " + getOrganiser().toString() + "."
                + "This event's title is: " + getTitle() + "."
                + "This event's type is: " + getType().toString() + ".";
    }
}
