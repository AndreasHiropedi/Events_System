package model;

import external.EntertainmentProviderSystem;
import external.MockEntertainmentProviderSystem;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentProvider extends User
{

    private String orgName, orgAddress, mainRepName, mainRepEmail;
    private List<String> otherRepNames, otherRepEmails;
    private List<Event> events = new ArrayList<>();
    private EntertainmentProviderSystem system;

    public EntertainmentProvider(String orgName, String orgAddress,
                                 String paymentAccountEmail, String mainRepName,
                                 String mainRepEmail, String password, List<String> otherRepNames,
                                 List<String> otherRepEmails)
    {
        super(mainRepEmail, password, paymentAccountEmail);

        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.mainRepName = mainRepName;
        this.mainRepEmail = mainRepEmail;
        this.otherRepNames = otherRepNames;
        this.otherRepEmails = otherRepEmails;

        this.system = new MockEntertainmentProviderSystem(this.orgName, this.orgAddress);
    }

    public void addEvent(Event event)
    {
        events.add(event);
    }

    public List<Event> getEvents()
    {
        return events;
    }

    public String getOrgAddress() 
    {
        return orgAddress;
    }

    public String getOrgName() 
    {
        return orgName;
    }

    public EntertainmentProviderSystem getProviderSystem()
    {
        return system;
    }

    public void setMainRepEmail(String mainRepEmail)
    {
        this.mainRepEmail = mainRepEmail;
    }

    public void setMainRepName(String mainRepName) 
    {
        this.mainRepName = mainRepName;
    }

    public void setOrgAddress(String orgAddress) 
    {
        this.orgAddress = orgAddress;
    }

    public void setOrgName(String orgName) 
    {
        this.orgName = orgName;
    }

    public void setOtherRepEmails(List<String> otherRepEmails) 
    {
        this.otherRepEmails = otherRepEmails;
    }

    public void setOtherRepNames(List<String> otherRepNames) 
    {
        this.otherRepNames = otherRepNames;
    }

    @Override
    public String toString()
    {
        return "User Type: " + this.getClass().getSimpleName() + "\n"
                + "Name: " + mainRepName + "\n"
                + "Email: " + mainRepEmail + "\n"
                + "Organisation: " + orgName + "\n"
                + "Address: " + orgAddress + "\n"
                + "Other Reps: " + otherRepNames.toString() + "\n"
                + "Emails: " + otherRepEmails.toString() + "\n"
                + "Payment Account: " + getPaymentAccountEmail() + "\n";
    }
}
