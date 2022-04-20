package command;

import controller.Context;
import model.GovernmentRepresentative;
import model.SponsorshipRequest;
import model.StdOut;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class ListSponsorshipRequestsCommand implements ICommand
{
    private boolean pendingOnly;
    private List<SponsorshipRequest> requestListResult = new ArrayList<>();

    public ListSponsorshipRequestsCommand(boolean pendingOnly)
    {
        this.pendingOnly = pendingOnly;
    }

    @Override
    public void execute(Context context)
    {
        User currentUser = context.getUserState().getCurrentUser();
        if (currentUser == null)
        {
            StdOut.println("User is not logged in!");
            return;
        }
        if (currentUser instanceof GovernmentRepresentative)
        {
            if (pendingOnly)
            {
                requestListResult =
                        context.getSponsorshipState().getPendingSponsorshipRequests();
            }
            else
            {
                requestListResult =
                        context.getSponsorshipState().getAllSponsorshipRequests();
            }
        }
        else
        {
            StdOut.println("User is not a government representative!");
        }
    }

    @Override
    public List<SponsorshipRequest> getResult()
    {
        return requestListResult;
    }
}
