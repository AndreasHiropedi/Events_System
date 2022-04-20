package controller;

import external.*;
import state.*;

public class Context
{

    private PaymentSystem paymentSystem;
    private IUserState userState;
    private IEventState eventState;
    private IBookingState bookingState;
    private ISponsorshipState sponsorshipState;

    public Context()
    {
        paymentSystem = new MockPaymentSystem();
        userState = new UserState();
        eventState = new EventState();
        bookingState = new BookingState();
        sponsorshipState = new SponsorshipState();
    }

    public Context(Context other)
    {
        paymentSystem = new MockPaymentSystem();
        userState = new UserState(other.getUserState());
        eventState = new EventState(other.getEventState());
        bookingState = new BookingState(other.getBookingState());
        sponsorshipState = new SponsorshipState(getSponsorshipState());
    }

    public PaymentSystem getPaymentSystem()
    {
        return paymentSystem;
    }

    public IUserState getUserState()
    {
        return userState;
    }

    public void setUserState(IUserState newUserState)
    {
        this.userState = newUserState;
    }

    public void setEventState(IEventState newEventState)
    {
        this.eventState = newEventState;
    }

    public IEventState getEventState()
    {
        return eventState;
    }

    public IBookingState getBookingState()
    {
        return bookingState;
    }

    public ISponsorshipState getSponsorshipState()
    {
        return sponsorshipState;
    }
}
