package model;

import java.util.*;


public class Consumer extends User
{

    private String name, phoneNumber;
    private List<Booking> bookings;
    private ConsumerPreferences preferences;

    public Consumer (String name, String email, String phoneNumber, String password, String paymentAccountEmail)
    {
        super(email, password, paymentAccountEmail);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking)
    {
        bookings.add(booking);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    public List<Booking> getBookings()
    {
        return bookings;
    }

    public ConsumerPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(ConsumerPreferences preferences)
    {
        this.preferences = preferences;
    }

    public void notify(String message)
    {
        StdOut.println(message);
    }

    public String toString()
    {
        // Set strPreferences depending on whether preferences are null or not. If not null,
        // set strPreferences to a string list of preferences, other wise set to "None".
        String strPreferences = preferences != null ? preferences.toString() : "None";

        return "User Type: " + this.getClass().getSimpleName() + "\n"
                + "Name: "+ name + "\n"
                + "Phone Number: " + getPhoneNumber() + "\n"
                + "Preferences: " + strPreferences + "\n"
                + "Email: " + getEmail() + "\n"
                + "Payment Account: " + getPaymentAccountEmail() + "\n";
    }
}
