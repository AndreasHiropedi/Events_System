package model;

public class GovernmentRepresentative extends User
{
    public GovernmentRepresentative (String email, String password, String paymentAccountEmail)
    {
        super(email, password, paymentAccountEmail);
    }

    public String toString()
    {
        return "The government representative's email is: " + getEmail() + "."
                + "The government representative's payment account email is: " + getPaymentAccountEmail() + ".";
    }
}
