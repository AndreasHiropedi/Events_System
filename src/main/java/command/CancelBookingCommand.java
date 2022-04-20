package command;

import controller.Context;
import external.MockEntertainmentProviderSystem;
import external.MockPaymentSystem;
import model.*;
import state.BookingState;
import state.UserState;

import java.time.LocalDateTime;


public class CancelBookingCommand implements ICommand
{
    long bookingNumber;
    boolean result = false;

    public CancelBookingCommand(long bookingNumber)
    {
        this.bookingNumber = bookingNumber;
    }

    @Override
    public void execute(Context context)
    {
        this.result = false;
        UserState userState = (UserState) context.getUserState();
        User currentUser = userState.getCurrentUser();
        if (validUser(currentUser))
        {
            BookingState bookingState = (BookingState) context.getBookingState();
            Booking currentBooking = bookingState.findBookingByNumber(bookingNumber);
            if (validBooking(currentBooking, currentUser))
            {
                EventPerformance bookedPerformance = currentBooking.getEventPerformance();
                LocalDateTime performanceStartTime = bookedPerformance.getStartDateTime();
                LocalDateTime exactly24HoursAwayFromNow = LocalDateTime.now().plusDays(1);
                if (validPerformanceStartTime(performanceStartTime, exactly24HoursAwayFromNow))
                {
                    Event bookedEvent = bookedPerformance.getEvent();
                    EntertainmentProvider bookedEventOrganiser = bookedEvent.getOrganiser();
                    if (refundsHandled(context, currentUser, bookedEvent, bookedEventOrganiser))
                    {
                        result = true;
                        MockEntertainmentProviderSystem providerSystem =
                                (MockEntertainmentProviderSystem) bookedEventOrganiser.getProviderSystem();
                        providerSystem.cancelBooking(bookingNumber);
                        currentBooking.cancelByConsumer();
                        StdOut.println("Booking cancelled successfully!");
                    }
                    else
                    {
                        StdOut.println("Refund payment was unsuccessful!");
                    }
                }
            }
        }
    }

    /**
     * If the booking can be cancelled, check that the refund is successful
     * @param context the current context with all states up to date
     * @param currentUser the currently logged-in user
     * @param bookedEvent the event for which the booking was made
     * @param bookedEventOrganiser the organiser for
     *                             the event for which the booking was made
     * @return true if the refund payment was successful
     */
    private boolean refundsHandled(Context context, User currentUser,
                                   Event bookedEvent,
                                   EntertainmentProvider bookedEventOrganiser)
    {
        MockPaymentSystem paymentSystem = (MockPaymentSystem) context.getPaymentSystem();
        String userPaymentAccount = currentUser.getPaymentAccountEmail();
        String organiserPaymentEmail = bookedEventOrganiser.getPaymentAccountEmail();
        double amountPaid = ((TicketedEvent) bookedEvent).getDiscountedTicketPrice();
        return paymentSystem.processRefund(userPaymentAccount, organiserPaymentEmail, amountPaid);
    }

    /**
     * Check that the user is logged-in, and is also a consumer
     * @param currentUser the currently logged-in user
     * @return true if the user is logged-in and
     * that currently logged-in user is a consumer
     */
    private boolean validUser(User currentUser)
    {
        if (currentUser == null)
        {
            StdOut.println("Current user is not logged in!");
            return false;
        }
        else if (!(currentUser instanceof Consumer))
        {
            StdOut.println("Currently logged-in user is not a consumer!");
            return false;
        }
        return true;
    }

    /**
     * Check that all booking details are valid (the booking
     * is not null, the current user is the one who made the booking,
     * and the booking is still active)
     * @param currentBooking the given booking for the event
     * @param currentUser the currently logged-in user
     * @return true if all the booking's details are valid
     */
    private boolean validBooking(Booking currentBooking, User currentUser)
    {
        if (currentBooking == null)
        {
            StdOut.println("The booking number provided is invalid!");
            return false;
        }
        else if (!currentBooking.getBooker().equals(currentUser))
        {
            StdOut.println("Current user is not the booking owner!");
            return false;
        }
        else if (!currentBooking.getStatus().equals(BookingStatus.ACTIVE))
        {
            StdOut.println("Booking is not active!");
            return false;
        }
        return true;
    }

    /**
     * Check if the performance start time is at least 24 hours away
     * from the time of cancellation
     * @param performanceStartTime the start time of the performance
     * @param exactly24HoursAway the current time 24 hours away from now
     * @return true if the performance start time is at least 24 hours away
     */
    private boolean validPerformanceStartTime(LocalDateTime performanceStartTime,
                                              LocalDateTime exactly24HoursAway)
    {
        if (performanceStartTime.isBefore(exactly24HoursAway))
        {
            StdOut.println("Booking cannot be cancelled since the performance is less than 24 hours away!");
            return false;
        }
        return true;
    }

    @Override
    public Boolean getResult()
    {
        return result;
    }
}
