package model;

import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class User
{
    private String email, passwordHash, paymentAccountEmail;

    protected User (String email, String password, String paymentAccountEmail)
    {
        this.email = email;
        this.passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.paymentAccountEmail = paymentAccountEmail;
    }

    public boolean checkPasswordMatch(String password)
    {
        if (password == null)
        {
            return false;
        }
        return BCrypt.verifyer().verify(password.toCharArray(), passwordHash).verified;
    }

    public void updatePassword(String newPassword)
    {
        passwordHash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPaymentAccountEmail()
    {
        return paymentAccountEmail;
    }

    public void setPaymentAccountEmail(String paymentAccountEmail)
    {
        this.paymentAccountEmail = paymentAccountEmail;
    }

    public String toString()
    {
        return "The user's email address is: " + email + "."
                + "The payment account email is: " + paymentAccountEmail + ".";
    }
}
