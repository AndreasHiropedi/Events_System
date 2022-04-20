package command;

import controller.Context;
import external.MockEntertainmentProviderSystem;
import model.EntertainmentProvider;
import model.EventType;
import model.NonTicketedEvent;
import model.StdOut;
import state.EventState;
import state.UserState;

public class CreateNonTicketedEventCommand extends CreateEventCommand
{

    public CreateNonTicketedEventCommand(String title, EventType type)
    {
        super(title, type);
    }

    @Override
    public void execute(Context context)
    {
        if (isUserAllowedToCreateEvent(context))
        {
            // Get the information to create a non ticketed event and add it to the system
            UserState userState = (UserState) context.getUserState();
            EventState eventState = (EventState) context.getEventState();
            EntertainmentProvider organiser = (EntertainmentProvider) userState.getCurrentUser();
            NonTicketedEvent newEvent = eventState.createNonTicketedEvent(organiser, title, type);
            eventNumberResult = newEvent.getEventNumber();
            MockEntertainmentProviderSystem providerSystem = (MockEntertainmentProviderSystem) organiser.getProviderSystem();
            providerSystem.recordNewEvent(eventNumberResult, title, 0);
            organiser.addEvent(newEvent);
            StdOut.println("Non ticketed event created successfully!");
        }
        else
        {
            StdOut.println("User is not allowed to create an event!");
        }
    }
}
