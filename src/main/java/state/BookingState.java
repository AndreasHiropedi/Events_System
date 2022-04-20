package state;

import model.Booking;
import model.Consumer;
import model.EventPerformance;
import model.StdOut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingState implements IBookingState, Cloneable
{
    private long nextBookingNumber;
    private List<Booking> bookings;

    public BookingState()
    {
        nextBookingNumber = 1;
        bookings = new ArrayList<>();
    }

    public BookingState(IBookingState other)
    {
        BookingState otherState = null;

        /*
            Attempt to clone the instance of the other object. In the case
            of a CloneNotSupportedException error, do nothing. In this case,
            otherState will be left null, leading to the execution of the
            else statement below.
         */
        try
        {
            otherState = (BookingState) ((BookingState)other).clone();
        }
        catch (CloneNotSupportedException ignored) { }

        if(otherState != null)
        {
            this.nextBookingNumber = otherState.nextBookingNumber;
            this.bookings = new ArrayList<>();

            for (Booking booking: otherState.bookings)
            {
                Booking bookingCopy = new Booking(booking.getBookingNumber(), booking.getBooker(),
                        booking.getEventPerformance(), booking.getNumTickets(), booking.getAmountPaid(),
                        booking.getBookingDateTime());
                this.bookings.add(bookingCopy);
            }
        }
        else
        {
            StdOut.println("Failed to copy otherState of type BookingState...");
        }
    }

    public Booking createBooking(Consumer booker, EventPerformance performance, int numTickets, double amountPaid)
    {
        if (booker != null && performance != null && numTickets > 0)
        {
            Booking newBooking = new Booking(nextBookingNumber++, booker, performance,
                    numTickets, amountPaid, LocalDateTime.now());
            bookings.add(newBooking);
            booker.addBooking(newBooking);
            return newBooking;
        }
        return null;
    }

    public Booking findBookingByNumber(long bookingNumber)
    {
        int i = 0;
        while (i < bookings.size())
        {
            Booking booking = bookings.get(i);
            if (bookingNumber == booking.getBookingNumber())
            {
                return booking;
            }
            i++;
        }
        return null;
    }

    public List<Booking> findBookingsByEventNumber(long eventNumber)
    {
        List<Booking> bookingsWithNumber = new ArrayList<>();
        for (Booking booking : bookings)
        {
            if (booking.getEventPerformance().getEvent().getEventNumber() == eventNumber)
            {
                bookingsWithNumber.add(booking);
            }
        }
        return bookingsWithNumber;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
