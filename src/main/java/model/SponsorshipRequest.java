package model;

public class SponsorshipRequest
{
    private long requestNumber;
    private TicketedEvent event;
    private SponsorshipStatus status;
    private int sponsoredPricePercent;
    private String sponsorAccountEmail;

    public SponsorshipRequest(long requestNumber, TicketedEvent event)
    {
        this.requestNumber = requestNumber;
        this.event = event;
        this.status = SponsorshipStatus.PENDING;
    }

    public long getRequestNumber()
    {
        return requestNumber;
    }

    public void setRequestNumber(long requestNumber)
    {
        this.requestNumber = requestNumber;
    }

    public TicketedEvent getEvent()
    {
        return event;
    }

    public void setEvent(TicketedEvent event)
    {
        this.event = event;
    }

    public SponsorshipStatus getStatus()
    {
        return status;
    }

    public int getSponsoredPricePercent()
    {
        return sponsoredPricePercent;
    }

    public void setSponsoredPricePercent(int sponsoredPricePercent)
    {
        this.sponsoredPricePercent = sponsoredPricePercent;
    }

    public String getSponsorAccountEmail() {
        return sponsorAccountEmail;
    }

    public void setSponsorAccountEmail(String sponsorAccountEmail)
    {
        this.sponsorAccountEmail = sponsorAccountEmail;
    }

    public void reject()
    {
        status = SponsorshipStatus.REJECTED;
    }

    public void accept(int percent, String sponsorAccountEmail)
    {
        status = SponsorshipStatus.ACCEPTED;
        this.setSponsoredPricePercent(percent);
        this.setSponsorAccountEmail(sponsorAccountEmail);
    }
}
