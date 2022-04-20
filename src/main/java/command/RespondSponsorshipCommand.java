package command;

import controller.Context;
import external.MockEntertainmentProviderSystem;
import external.MockPaymentSystem;
import model.*;

public class RespondSponsorshipCommand implements ICommand
{
    private long requestNumber;
    private int percentToSponsor;
    private boolean commandSuccessful = false;

    public RespondSponsorshipCommand(long requestNumber, int percentToSponsor)
    {
        this.requestNumber = requestNumber;
        this.percentToSponsor = percentToSponsor;
    }

    @Override
    public void execute(Context context)
    {
        User user = context.getUserState().getCurrentUser();
        SponsorshipRequest request =
                context.getSponsorshipState().findRequestByNumber(requestNumber);
        SponsorshipStatus requestStatus = request.getStatus();
        if(validUserAndRequestDetails(user, requestStatus))
        {
            TicketedEvent event = request.getEvent();
            long eventNumber = event.getEventNumber();
            EntertainmentProvider organiser = event.getOrganiser();
            MockEntertainmentProviderSystem providerSystem =
                    (MockEntertainmentProviderSystem) organiser.getProviderSystem();
            if (percentToSponsor == 0)
            {
                commandSuccessful = true;
                request.reject();
                providerSystem.recordSponsorshipRejection(eventNumber);
                StdOut.println("Request has been rejected successfully!");
            }
            else
            {
                String sponsorAccountEmail = user.getPaymentAccountEmail();
                // Check payment was successful
                if (handleSponsorshipPayment(context, organiser,
                        event, sponsorAccountEmail))
                {
                    commandSuccessful = true;
                    request.accept(percentToSponsor, sponsorAccountEmail);
                    providerSystem.recordSponsorshipAcceptance(eventNumber, percentToSponsor);
                    StdOut.println("Request has been accepted successfully!");
                }
                else
                {
                    StdOut.println("Sponsorship payment was unsuccessful!");
                }
            }
        }
    }

    /**
     * Checks if the current user is logged in and a government rep,
     * and also that the given request is pending and has a suitable sponsorship percentage
     * (in the range 0-100)
     * @param user currently logged-in user
     * @param requestStatus status of a given sponsorship request
     * @return true if the user was logged in and a government rep,
     * and the request details were valid, false otherwise
     */
    private boolean validUserAndRequestDetails(User user, SponsorshipStatus requestStatus)
    {
        if (user == null)
        {
            StdOut.println("User not logged in!");
            return false;
        }
        else if (!(user instanceof GovernmentRepresentative))
        {
            StdOut.println("User is not a government representative and can't respond to sponsorship requests!");
            return false;
        }
        else if (!requestStatus.equals(SponsorshipStatus.PENDING))
        {
            StdOut.println("Sponsorship decision has already been made!");
            return false;
        }
        else if (percentToSponsor < 0 || percentToSponsor > 100)
        {
            StdOut.println("Specified percentage not within the acceptable range!");
            return false;
        }
        return true;
    }

    /**
     * This method processes the payment for sponsorship money
     * in the case that the sponsorship request for the event was
     * accepted
     * @param context the current context with all states yp to date
     * @param organiser the organiser of the event
     * @param event the event for which the sponsorship request was made
     * @param sponsorAccountEmail the payment account email for the sponsor
     * @return true if the payment was successful
     */
    private boolean handleSponsorshipPayment(Context context,
                                             EntertainmentProvider organiser,
                                             TicketedEvent event,
                                             String sponsorAccountEmail)
    {
        // Check payment was successful
        MockPaymentSystem paymentSystem = (MockPaymentSystem) context.getPaymentSystem();
        String organiserEmail = organiser.getPaymentAccountEmail();
        int totalTickets = event.getNumTickets();
        double ticketPrice = event.getOriginalTicketPrice();
        double amountToPay = totalTickets * ticketPrice * percentToSponsor;
        return paymentSystem.processPayment(sponsorAccountEmail, organiserEmail, amountToPay);
    }

    @Override
    public Boolean getResult()
    {
        return commandSuccessful;
    }
}
