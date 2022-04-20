package state;

import model.SponsorshipRequest;
import model.SponsorshipStatus;
import model.StdOut;
import model.TicketedEvent;
import java.util.ArrayList;
import java.util.List;

public class SponsorshipState implements ISponsorshipState, Cloneable
{
    private long nextRequestNumber;
    private List<SponsorshipRequest> sponsorshipRequests;

    public SponsorshipState()
    {
        sponsorshipRequests = new ArrayList<>();
        nextRequestNumber = 1;
    }

    public SponsorshipState(ISponsorshipState other)
    {
        SponsorshipState otherState = null;

        /*
            Attempt to clone the instance of the other object. In the case
            of a CloneNotSupportedException error, do nothing. In this case,
            otherState will be left null, leading to the execution of the
            else statement below.
         */
        try
        {
            otherState = (SponsorshipState) ((SponsorshipState)other).clone();
        }
        catch (CloneNotSupportedException ignored){ }

        if(otherState != null)
        {
            this.nextRequestNumber = otherState.nextRequestNumber;
            this.sponsorshipRequests = new ArrayList<>();

            for (SponsorshipRequest request: otherState.sponsorshipRequests)
            {
                SponsorshipRequest requestCopy = new SponsorshipRequest(request.getRequestNumber(), request.getEvent());
                sponsorshipRequests.add(requestCopy);
            }
        }
        else
        {
            StdOut.println("Failed to copy otherState of type SponsorshipState...");
        }
    }

    public SponsorshipRequest addSponsorshipRequest(TicketedEvent event)
    {
        if (event != null)
        {
            SponsorshipRequest newRequest =
                    new SponsorshipRequest(nextRequestNumber++, event);
            event.setSponsorshipRequest(newRequest);
            sponsorshipRequests.add(newRequest);
            return newRequest;
        }
        return null;
    }

    public List<SponsorshipRequest> getAllSponsorshipRequests()
    {
        return sponsorshipRequests;
    }

    public List<SponsorshipRequest> getPendingSponsorshipRequests()
    {
        List<SponsorshipRequest> pendingList = new ArrayList<>();
        for (SponsorshipRequest request : sponsorshipRequests)
        {
            if (request.getStatus() == SponsorshipStatus.PENDING)
            {
                pendingList.add(request);
            }
        }
        return pendingList;
    }

    public SponsorshipRequest findRequestByNumber(long requestNumber)
    {
        int i = 0;
        while (i < sponsorshipRequests.size())
        {
            SponsorshipRequest request = sponsorshipRequests.get(i);
            if (request.getRequestNumber() == requestNumber) {
                return request;
            }
            i++;
        }
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
