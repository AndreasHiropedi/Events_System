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

public class BookEventCommand implements ICommand
{
    private Long bookingNumber = null;
    private final long eventNumber, performanceNumber;
    private final int numTicketsRequested;

    public BookEventCommand(long eventNumber, long performanceNumber, int numTicketsRequested)
    {
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
        this.numTicketsRequested = numTicketsRequested;
    }

    @Override
    public void execute(Context context)
    {
        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        if (validUser(currentUser))
        {
            EventState eventState = (EventState) context.getEventState();
            Event currentEvent = eventState.findEventByNumber(eventNumber);
            if (validEventAndRequestedTickets(currentEvent))
            {
                EventPerformance currentPerformance = validPerformance(currentEvent);
                if (currentPerformance != null)
                {
                    LocalDateTime performanceEndTime = currentPerformance.getEndDateTime();
                    LocalDateTime currentTime = LocalDateTime.now();
                    if (hasNotEnded(currentTime, performanceEndTime))
                    {
                        EntertainmentProvider eventOrganiser = currentEvent.getOrganiser();
                        MockEntertainmentProviderSystem providerSystem =
                                (MockEntertainmentProviderSystem) eventOrganiser.getProviderSystem();
                        double ticketsLeft = providerSystem.getNumTicketsLeft(eventNumber, performanceNumber);
                        if (enoughTicketsLeft(ticketsLeft))
                        {
                            double amountToPay =
                                    ((TicketedEvent) currentEvent).getDiscountedTicketPrice() * numTicketsRequested;
                            if (handlePayment(context, currentUser, eventOrganiser, amountToPay))
                            {
                                bookingSuccessful(context, currentUser, currentPerformance, amountToPay, providerSystem);
                            }
                            else
                            {
                                StdOut.println("Booking payment was unsuccessful!");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Check that the user is logged in, and that
     * the logged-in user is a consumer
     * @param currentUser currently logged-in user
     * @return true if the user is logged in and
     * also a consumer
     */
    private boolean validUser(User currentUser)
    {
        if (currentUser == null)
        {
            StdOut.println("User is not logged in!");
            return false;
        }
        else if (!(currentUser instanceof Consumer))
        {
            StdOut.println("Currently logged in user is not a consumer!");
            return false;
        }
        return true;
    }

    /**
     * Check if all the event details are valid, and the
     * number of requested tickets is also valid
     * @param currentEvent the event for which a booking is made
     * @return true if all the event details are valid (the event
     * exists and is ticketed) and the requested number of tickets
     * is also valid
     */
    private boolean validEventAndRequestedTickets(Event currentEvent)
    {
        if (currentEvent == null)
        {
            StdOut.println("The event number provided was invalid!");
            return false;
        }
        else if (!(currentEvent instanceof TicketedEvent))
        {
            StdOut.println("The event is non ticketed!");
            return false;
        }
        else if (numTicketsRequested < 1)
        {
            StdOut.println("The number of requested tickets is invalid!");
            return false;
        }
        return true;
    }

    /**
     * Check if the performance which the uswr is trying to book
     * exists on our system
     * @param currentEvent the event for which the booking is made
     * @return true if the performance is valid (the performance exists
     * on our system)
     */
    private EventPerformance validPerformance(Event currentEvent)
    {
        Collection<EventPerformance> performances = currentEvent.getPerformances();
        EventPerformance currentPerformance = null;
        for (EventPerformance performance : performances)
        {
            if (performance.getPerformanceNumber() == performanceNumber)
            {
                currentPerformance = performance;
                break;
            }
        }
        if (currentPerformance == null)
        {
            StdOut.println("The performance number was not valid!");
        }
        return currentPerformance;
    }

    /**
     * If all checks are passed, process the payment for the booking
     * @param context the current context with all states up to date
     * @param currentUser the currently logged-in user
     * @param eventOrganiser the organiser of the event the
     *                       booking is made for
     * @param amountToPay total amount to pay for the booking
     * @return true if the booking payment was successful
     */
    private boolean handlePayment(Context context, User currentUser,
                                  EntertainmentProvider eventOrganiser,
                                  double amountToPay)
    {
        MockPaymentSystem paymentSystem = (MockPaymentSystem) context.getPaymentSystem();
        String userPaymentAccountEmail = currentUser.getPaymentAccountEmail();
        String organiserPaymentAccountEmail = eventOrganiser.getPaymentAccountEmail();
        return paymentSystem.processPayment(userPaymentAccountEmail,
                organiserPaymentAccountEmail, amountToPay);
    }

    /**
     * In case the payment was successful, update our system
     * and the provider's system as well
     * @param context the current context with all states up to date
     * @param currentUser the currently logged-in user
     * @param currentPerformance the performance for which the
     *                           booking was made
     * @param amountToPay total amount to pay for the booking
     * @param providerSystem the event organiser's system
     */
    private void bookingSuccessful(Context context, User currentUser,
                                   EventPerformance currentPerformance,
                                   double amountToPay,
                                   MockEntertainmentProviderSystem providerSystem)
    {
        BookingState bookingState = (BookingState) context.getBookingState();
        Booking newBooking = bookingState.createBooking((Consumer) currentUser,
                currentPerformance, numTicketsRequested, amountToPay);
        long bookingNumber = newBooking.getBookingNumber();
        String consumerName = ((Consumer) currentUser).getName();
        String consumerEmail = currentUser.getEmail();
        providerSystem.recordNewBooking(eventNumber, performanceNumber,
                bookingNumber, consumerName, consumerEmail, numTicketsRequested);
        this.bookingNumber = bookingNumber;
        StdOut.println("Booking was successful!");
    }

    /**
     * Check if the end time of the performance the consumer
     * is trying to book is before the current time of booking
     * @param currentTime the current time right now
     * @param performanceEndTime the end time of the performance
     * @return true if the end time of the performance is after
     * the current time
     */
    private boolean hasNotEnded(LocalDateTime currentTime,
                                  LocalDateTime performanceEndTime)
    {
        if (!currentTime.isBefore(performanceEndTime))
        {
            StdOut.println("Performance has already ended!");
            return false;
        }
        return true;
    }

    /**
     * Check if the event still has enough tickets left
     * to allow the booking to be made
     * @param ticketsLeft number of tickets left for the event
     * @return true if the number of tickets the consumer is
     * trying to buy does not exceed the remaining number of tickets
     * available for the event
     */
    private boolean enoughTicketsLeft(double ticketsLeft)
    {
        if (ticketsLeft < numTicketsRequested)
        {
            StdOut.println("Not enough tickets left to process booking!");
            return false;
        }
        return true;
    }

    @Override
    public Long getResult()
    {
        return bookingNumber;
    }
}
