package state;

import model.SponsorshipRequest;
import model.TicketedEvent;

import java.util.List;

public interface ISponsorshipState
{
    public SponsorshipRequest addSponsorshipRequest(TicketedEvent event);

    public List<SponsorshipRequest> getAllSponsorshipRequests();

    public List<SponsorshipRequest> getPendingSponsorshipRequests();

    public SponsorshipRequest findRequestByNumber(long requestNumber);
}
