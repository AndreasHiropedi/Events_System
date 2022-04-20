package model;

import java.time.LocalDateTime;

public class Booking
{
    private long bookingNumber;
    private Consumer booker;
    private EventPerformance performance;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;

    public Booking (long bookingNumber,
                    Consumer booker,
                    EventPerformance performance,
                    int numTickets,
                    double amountPaid,
                    LocalDateTime bookingDateTime)
    {
        this.bookingNumber = bookingNumber;
        this.booker = booker;
        this.performance = performance;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid;
        this.bookingDateTime = bookingDateTime;
        this.status = BookingStatus.ACTIVE;
    }

    public LocalDateTime getBookingDateTime()
    {
        return bookingDateTime;
    }

    public long getBookingNumber()
    {
        return bookingNumber;
    }

    public Consumer getBooker()
    {
        return booker;
    }

    public EventPerformance getEventPerformance()
    {
        return performance;
    }

    public double getAmountPaid()
    {
        return amountPaid;
    }

    public BookingStatus getStatus()
    {
        return status;
    }
    
    public int getNumTickets() 
    {
        return numTickets;
    }

    public void cancelByConsumer()
    {
        setStatus(BookingStatus.CANCELLEDBYCONSUMER);
    }

    public void cancelByProvider()
    {
        setStatus(BookingStatus.CANCELLEDBYPROVIDER);
    }

    public void cancelPaymentFailed()
    {
        setStatus(BookingStatus.PAYMENTFAILED);
    }

    public void setBookingNumber(long bookingNumber) 
    {
        this.bookingNumber = bookingNumber;
    }

    public void setBooker(Consumer booker) 
    {
        this.booker = booker;
    }

    public void setAmountPaid(double amountPaid) 
    {
        this.amountPaid = amountPaid;
    }

    public void setStatus(BookingStatus status) 
    {
        this.status = status;
    }

    public String toString()
    {
        return "This booking's booking number is: " + getBookingNumber() + "."
                + "The corresponding consumer for the booking is: " + getBooker().toString() + "."
                + "The booking's event performance is: " + performance.toString() + "."
                + "The number of tickets is: " + numTickets + "."
                + "The amount paid for the booking is: " + getAmountPaid() + "."
                + "The booking's date and time is: " + bookingDateTime.toString() + "."
                + "The booking's status is: " + getStatus().toString() + ".";
    }
}
