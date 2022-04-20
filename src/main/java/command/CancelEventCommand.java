package command;

import controller.Context;
import external.MockEntertainmentProviderSystem;
import external.MockPaymentSystem;
import model.*;
import state.BookingState;
import state.EventState;
import state.UserState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class CancelEventCommand implements ICommand 
{

    protected long eventNumber;
    protected String organiserMessage;
    protected Boolean cancelSuccess = false;

    public CancelEventCommand(long eventNumber, String organiserMessage) 
    {
        this.eventNumber = eventNumber;
        this.organiserMessage = organiserMessage;
    }

    @Override
    public void execute(Context context) 
    {
        if (validOrganiserMessage())
        {
            UserState userState = (UserState) context.getUserState();
            User currentUser = userState.getCurrentUser();
            if (validUser(currentUser))
            {
                EventState eventState = (EventState) context.getEventState();
                Event givenEvent = eventState.findEventByNumber(eventNumber);
                if (validEventDetails(givenEvent, currentUser))
                {
                    if (!performanceAlreadyStarted(givenEvent))
                    {
                        // Refund the event if it is ticketed
                        if (givenEvent instanceof TicketedEvent && ((TicketedEvent) givenEvent).isSponsored())
                        {
                            refundGovernmentSponsorshipMoney(context, givenEvent);
                            refundConsumers(context, true);
                            StdOut.println("Sponsored event cancelled successfully, everyone was refunded!");
                        }
                        else if (givenEvent instanceof TicketedEvent)
                        {
                            refundConsumers(context, false);
                            StdOut.println("Non-sponsored event cancelled successfully, everyone was refunded!");
                        }
                        else
                        {
                            StdOut.println("Non-ticketed event cancelled successfully!");
                        }
                        givenEvent.cancel();
                        EntertainmentProvider eventOrganiser = givenEvent.getOrganiser();
                        MockEntertainmentProviderSystem providerSystem =
                                (MockEntertainmentProviderSystem) eventOrganiser.getProviderSystem();
                        providerSystem.cancelEvent(eventNumber, organiserMessage);
                        cancelSuccess = true;
                    }
                    else
                    {
                        StdOut.println("At least one performance has already started or ended!");
                    }
                }
            }
        }
    }

    /**
     * Take care of all customer refunds in case an event is successfully cancelled
     * @param context the current context, with all the states up to date
     * @param sponsored flag indicating if the event is sponsored or not
     */
    private void refundConsumers(Context context, Boolean sponsored) 
    {
        BookingState bookingState = (BookingState) context.getBookingState();
        EventState eventState = (EventState) context.getEventState();
        TicketedEvent givenEvent = (TicketedEvent) eventState.findEventByNumber(eventNumber);
        String sellerEmail = givenEvent.getOrganiser().getPaymentAccountEmail();
        MockPaymentSystem paymentSystem = (MockPaymentSystem) context.getPaymentSystem();
        List<Booking> allEventBookings = bookingState.findBookingsByEventNumber(givenEvent.getEventNumber());
        double amountToRefund;
        if (sponsored) 
        {
            amountToRefund = givenEvent.getDiscountedTicketPrice();
        } 
        else 
        {
            amountToRefund = givenEvent.getOriginalTicketPrice();
        }
        for (Booking booking : allEventBookings)
        {
            String buyerEmail = booking.getBooker().getEmail();
            booking.setStatus(BookingStatus.CANCELLEDBYPROVIDER);
            paymentSystem.processRefund(buyerEmail, sellerEmail, amountToRefund);
        }
    }

    /**
     * Check if the current user is logged in, and
     * also if they are an entertainment provider
     * @param currentUser the currently logged-in user
     * @return true if the user is logged-in and is also
     * an entertainment provider
     */
    private boolean validUser(User currentUser)
    {
        if (currentUser == null)
        {
            StdOut.println("User is not logged in!");
            return false;
        }
        else if (!(currentUser instanceof EntertainmentProvider))
        {
            StdOut.println("User is not an entertainment provider!");
            return false;
        }
        return true;
    }

    /**
     * Ensure that the event details are valid (event is not null,
     * the event is still active, and the current user is the
     * event's organiser)
     * @param givenEvent the currently searched event
     * @param currentUser the currently logged-in user
     * @return true if all details are valid
     */
    private boolean validEventDetails(Event givenEvent, User currentUser)
    {
        if (givenEvent == null)
        {
            StdOut.println("The event number provided is invalid!");
            return false;
        }
        else if (!givenEvent.getStatus().equals(EventStatus.ACTIVE))
        {
            StdOut.println("The selected event is not active!");
            return false;
        }
        else if (!givenEvent.getOrganiser().equals(currentUser))
        {
            StdOut.println("The logged-in user is not the organiser of the event!");
            return false;
        }
        return true;
    }

    /**
     * Checks if any performance of the currently searched
     * event has already started
     * @param givenEvent the currently searched event
     * @return true if no performance has already started
     */
    private boolean performanceAlreadyStarted(Event givenEvent)
    {
        Collection<EventPerformance> allEventPerformances = givenEvent.getPerformances();
        for (EventPerformance performance : allEventPerformances)
        {
            LocalDateTime performanceStartTime = performance.getStartDateTime();
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isAfter(performanceStartTime))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * In case a sponsored event is cancelled, refund the sponsorship
     * money to the government
     * @param context the current context, with all states up to date
     * @param givenEvent the currently searched event
     */
    private void refundGovernmentSponsorshipMoney(Context context, Event givenEvent)
    {
        MockPaymentSystem paymentSystem = (MockPaymentSystem) context.getPaymentSystem();
        String buyerEmailAccount = ((TicketedEvent) givenEvent).getSponsorAccountEmail();
        List<MockPaymentSystem.Transaction> governmentPayments =
                paymentSystem.findTransactionByBuyerEmail(buyerEmailAccount);
        String sellerEmail = givenEvent.getOrganiser().getPaymentAccountEmail();
        MockPaymentSystem.Transaction governmentSponsorshipMoney;
        double sponsorshipMoney = 0;
        for (MockPaymentSystem.Transaction transaction: governmentPayments)
        {
            if (transaction.getSellerEmailAccount().equals(sellerEmail))
            {
                governmentSponsorshipMoney = transaction;
                sponsorshipMoney = governmentSponsorshipMoney.getAmount();
                break;
            }
        }
        paymentSystem.processRefund(buyerEmailAccount, sellerEmail, sponsorshipMoney);
    }

    /**
     * Ensure the entertainment provider cancellation message
     * is valid (it is not null or an empty string)
     * @return true if a valid message is provided
     */
    private boolean validOrganiserMessage()
    {
        if (organiserMessage == null || organiserMessage.length() <= 0)
        {
            StdOut.println("No valid message from the event organiser was provided!");
            return false;
        }
        return true;
    }

    @Override
    public Boolean getResult() 
    {
        return cancelSuccess;
    }
}
