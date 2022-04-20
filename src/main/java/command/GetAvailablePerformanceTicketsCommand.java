package command;

import controller.Context;
import model.Event;
import model.EventPerformance;
import model.TicketedEvent;

public class GetAvailablePerformanceTicketsCommand implements ICommand
{
    private long eventNumber, performanceNumber;
    private Integer numTicketsResult;

    public GetAvailablePerformanceTicketsCommand(long eventNumber,
                                                 long performanceNumber)
    {
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
        numTicketsResult = null;
    }

    @Override
    public void execute(Context context)
    {
        // Find the event and the number of tickets for the performance nunber associated with it
        Event event = context.getEventState().findEventByNumber(eventNumber);
        if (event instanceof TicketedEvent)
        {
            EventPerformance eventPerformance =
                    event.getPerformanceByNumber(performanceNumber);
            if (eventPerformance != null)
            {
                TicketedEvent ticketedEvent = (TicketedEvent) event;
                numTicketsResult = ticketedEvent.getNumTickets();
            }
        }
    }

    @Override
    public Integer getResult()
    {
        return numTicketsResult;
    }
}
