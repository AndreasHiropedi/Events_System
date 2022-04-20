package external;

import model.StdOut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockEntertainmentProviderSystem implements EntertainmentProviderSystem
{
    private String orgName, orgAddress;
    // stores a pairing of the event number and corresponding number of available tickets
    private Map<Long, Integer>  remainingTicketsPerEvent = new HashMap<>();
    // stores a pairing of the event number and corresponding list of all bookings
    private Map<Long, ArrayList<Long>> eventBookings = new HashMap<>();
    // stores a pairing of the booking number and corresponding number of booked tickets
    private Map<Long, Integer> bookingTickets = new HashMap<>();
    // stores a pairing of the event number and corresponding performance number
    private Map<Long, List<Long>> eventPerformances = new HashMap<>();
    // stores a pairing of the event number and corresponding sponsorship percentage (0 if the event is not sponsored)
    private Map<Long, Integer> sponsorshipStatus = new HashMap<>();
    // stores a pairing of the booking number and corresponding event number
    // NOTE: this may be a bit redundant, given there already is eventBookings, but it makes searching and updating easier
    private Map<Long, Long> bookingEvent = new HashMap<>();

    public MockEntertainmentProviderSystem(String orgName, String orgAddress)
    {
        this.orgName = orgName;
        this.orgAddress = orgAddress;
    }

    @Override
    public void cancelBooking(long bookingNumber)
    {
        // check if the booking number is valid
        boolean isBookingValid = bookingEvent.containsKey(bookingNumber);

        if (!isBookingValid)
        {
            StdOut.println("The booking number was invalid!");
        }
        else
        {
            // retrieve the event for which the booking was made
            long eventNumber = bookingEvent.get(bookingNumber);
            // update the available tickets for that event
            int bookedTickets = bookingTickets.get(bookingNumber);
            int remainingEventTickets = remainingTicketsPerEvent.get(eventNumber);
            int updatedRemainingTickets = remainingEventTickets + bookedTickets;
            remainingTicketsPerEvent.put(eventNumber, updatedRemainingTickets);
            // remove the booking from the provider's system
            bookingTickets.remove(bookingNumber);
            bookingEvent.remove(bookingNumber);
            // print the appropriate message
            StdOut.println("The booking has been successfully cancelled.");
        }
    }

    @Override
    public void cancelEvent(long eventNumber, String message)
    {
        // check if the event number is valid
        boolean isEventValid = remainingTicketsPerEvent.containsKey(eventNumber);

        if (!isEventValid)
        {
            StdOut.println("The event number was invalid!");
        }
        else
        {
            // discard all tickets for the event
            remainingTicketsPerEvent.remove(eventNumber);
            // remove all bookings for the corresponding event
            ArrayList<Long> bookingsForEvent = eventBookings.get(eventNumber);
            for (Long booking: bookingsForEvent)
            {
                bookingEvent.remove(booking);
                bookingTickets.remove(booking);
            }
            // remove the event from the system
            eventBookings.remove(eventNumber);
            eventPerformances.remove(eventNumber);
            // print the appropriate message
            StdOut.println(message);
        }
    }

    @Override
    public int getNumTicketsLeft(long eventNumber, long performanceNumber)
    {
        // check if the event number is valid
        boolean isEventValid = remainingTicketsPerEvent.containsKey(eventNumber);

        if (!isEventValid)
        {
            StdOut.println("The event number was invalid!");
            return -1;
        }
        else
        {
            // if the event number is valid, check if the performance number is valid
            List<Long> performanceNumbers = eventPerformances.get(eventNumber);
            boolean isPerformanceValid = performanceNumbers.contains(performanceNumber);

            if (!isPerformanceValid)
            {
                StdOut.println("The performance number was invalid!");
                return -1;
            }
            else
            {
                return remainingTicketsPerEvent.get(eventNumber);
            }
        }
    }

    @Override
    public void recordNewBooking(long eventNumber, long performanceNumber, long bookingNumber, String consumerName, String consumerEmail, int bookedTickets)
    {
        // check if the event number is valid
        boolean isEventValid = remainingTicketsPerEvent.containsKey(eventNumber);

        if (!isEventValid)
        {
            StdOut.println("The event number was invalid!");
        }
        else
        {
            // if the event number is valid, check if the performance number is valid
            List<Long> performanceNumbers = eventPerformances.get(eventNumber);
            boolean isPerformanceValid = performanceNumbers.contains(performanceNumber);

            if (!isPerformanceValid)
            {
                StdOut.println("The performance number was invalid!");
            }
            else
            {
                // record the booking in the entertainment system
                bookingEvent.put(bookingNumber, eventNumber);
                bookingTickets.put(bookingNumber, bookedTickets);
                // update the number of tickets based on the new booking
                int remainingTickets = remainingTicketsPerEvent.get(eventNumber);
                int updatedRemainingTickets = remainingTickets - bookedTickets;
                remainingTicketsPerEvent.put(eventNumber, updatedRemainingTickets);
                // print the appropriate message
                StdOut.println("Booking successfully made!");
            }
        }
    }

    @Override
    public void recordNewEvent(long eventNumber, String title, int numTickets)
    {
        // check if the event number is valid
        boolean eventExists = remainingTicketsPerEvent.containsKey(eventNumber);

        if (eventExists)
        {
            StdOut.println("An event with this number already exists!");
        }
        else
        {
            // add the new event with the number of available tickets
            remainingTicketsPerEvent.put(eventNumber, numTickets);
            // and initialise the list of bookings for that event
            eventBookings.put(eventNumber, new ArrayList<>());
            eventPerformances.put(eventNumber, new ArrayList<>());
            StdOut.println("Event successfully created!");
        }
    }

    @Override
    public void recordNewPerformance(long eventNumber, long performanceNumber, LocalDateTime startDateTime, LocalDateTime endDateTime)
    {
        // check if the event number is valid
        boolean isEventValid = remainingTicketsPerEvent.containsKey(eventNumber);

        if (!isEventValid)
        {
            StdOut.println("The event number was invalid!");
        }
        else
        {
            // if the event number is valid, check if the performance number is valid
            List<Long> performanceNumbers = eventPerformances.get(eventNumber);
            boolean isPerformanceValid = performanceNumbers.contains(performanceNumber);

            if (isPerformanceValid)
            {
                StdOut.println("A performance with this number already exists!");
            }
            else
            {
                performanceNumbers.add(performanceNumber);
                eventPerformances.put(eventNumber, performanceNumbers);
                StdOut.println("Performance successfully added!");
            }
        }
    }

    @Override
    public void recordSponsorshipAcceptance(long eventNumber, int sponsoredPricePercent)
    {
        // check if the event number is valid
        boolean isEventValid = remainingTicketsPerEvent.containsKey(eventNumber);

        if (!isEventValid)
        {
            StdOut.println("The event number was invalid!");
        }
        else
        {
            // if the event number is valid, check if a decision has already been made
            boolean alreadyDecided = sponsorshipStatus.containsKey(eventNumber);

            if (alreadyDecided)
            {
                StdOut.println("A decision has already been made!");
            }
            else
            {
                sponsorshipStatus.put(eventNumber, sponsoredPricePercent);
                StdOut.println("The sponsorship request for the event with event number of " + eventNumber +
                        " has been accepted by a government representative.");
            }
        }
    }

    @Override
    public void recordSponsorshipRejection(long eventNumber)
    {
        // check if the event number is valid
        boolean isEventValid = remainingTicketsPerEvent.containsKey(eventNumber);

        if (!isEventValid)
        {
            StdOut.println("The event number was invalid!");
        }
        else
        {
            // if the event number is valid, check if a decision has already been made
            boolean alreadyDecided = sponsorshipStatus.containsKey(eventNumber);

            if (alreadyDecided)
            {
                StdOut.println("A decision has already been made!");
            }
            else
            {
                sponsorshipStatus.put(eventNumber, 0);
                StdOut.println("The sponsorship request for the event with event number of " +
                        eventNumber + " has been rejected by a government representative.");
            }
        }
    }
}
