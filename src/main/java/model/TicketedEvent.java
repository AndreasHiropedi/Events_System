package model;

public class TicketedEvent extends Event
{
    private double ticketPrice;
    private int numTickets;
    private SponsorshipRequest sponsorshipRequest;

    public TicketedEvent (long eventNumber, EntertainmentProvider organiser,
                          String title, EventType type, double ticketPrice, int numTickets)
    {
        super(eventNumber, organiser, title, type);
        this.ticketPrice = ticketPrice;
        this.numTickets = numTickets;
    }

    public int getNumTickets()
    {
        return numTickets;
    }

    public void setNumTickets(int numTickets)
    {
        this.numTickets = numTickets;
    }

    public SponsorshipRequest getSponsorshipRequest()
    {
        return sponsorshipRequest;
    }

    public void setSponsorshipRequest(SponsorshipRequest sponsorshipRequest)
    {
        this.sponsorshipRequest = sponsorshipRequest;
    }

    public double getOriginalTicketPrice()
    {
        return ticketPrice;
    }

    public boolean isSponsored()
    {
        if (sponsorshipRequest == null)
        {
            return false;
        }
        return sponsorshipRequest.getStatus().equals(SponsorshipStatus.ACCEPTED);
    }

    public double getDiscountedTicketPrice()
    {
        if (isSponsored())
        {
            double discount = ticketPrice * sponsorshipRequest.getSponsoredPricePercent() / 100;
            return ticketPrice - discount;
        }
        return ticketPrice;
    }

    public String getSponsorAccountEmail()
    {
        if (isSponsored())
        {
            return sponsorshipRequest.getSponsorAccountEmail();
        }
        return null;
    }

    public String toString()
    {
        return "The event's ticket price is: " + ticketPrice + "."
                + "The event's number of available tickets is: " + numTickets + "." 
                + "The event's sponsorship request is: " + sponsorshipRequest.toString() + "."
                + "The event's number is: " + getEventNumber() + "."
                + "The event's organiser is: " + getOrganiser().toString() + "."
                + "The event's title is: " + getTitle() + "."
                + "The event's type is: " + getType().toString() + ".";
    }
}
