package command;

import controller.Context;
import external.MockEntertainmentProviderSystem;
import model.*;
import state.EventState;
import state.SponsorshipState;
import state.UserState;

public class CreateTicketedEventCommand extends CreateEventCommand
{
    private int numTickets;
    private double ticketPrice;
    private boolean requestSponsorship;

    public CreateTicketedEventCommand(String title, EventType type,
                                      int numTickets, double ticketPrice,
                                      boolean requestSponsorship)
    {
        super(title, type);
        this.numTickets = numTickets;
        this.ticketPrice = ticketPrice;
        this.requestSponsorship = requestSponsorship;
    }

    @Override
    public void execute(Context context)
    {
        if (isUserAllowedToCreateEvent(context))
        {
            // Get the information to create a ticketed event and add it to the system
            UserState userState = (UserState) context.getUserState();
            EventState eventState = (EventState) context.getEventState();
            EntertainmentProvider organiser = (EntertainmentProvider) userState.getCurrentUser();
            TicketedEvent newEvent = eventState.createTicketedEvent(organiser, title, type, ticketPrice, numTickets);
            if (requestSponsorship)
            {
                SponsorshipState sponsorshipState = (SponsorshipState) context.getSponsorshipState();
                sponsorshipState.addSponsorshipRequest(newEvent);
            }
            eventNumberResult = newEvent.getEventNumber();
            MockEntertainmentProviderSystem providerSystem = (MockEntertainmentProviderSystem) organiser.getProviderSystem();
            providerSystem.recordNewEvent(eventNumberResult, title, numTickets);
            organiser.addEvent(newEvent);
            StdOut.println("Ticketed event created successfully!");
        }
        else
        {
            StdOut.println("User is not allowed to create an event!");
        }
    }
}
