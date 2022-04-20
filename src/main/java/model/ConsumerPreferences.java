package model;

public class ConsumerPreferences
{
    public boolean preferSocialDistancing, preferAirFiltration, preferOutdoorsOnly;
    public int preferredMaxCapacity, preferredMaxVenueSize;

    public ConsumerPreferences()
    {
        this.preferSocialDistancing = false;
        this.preferAirFiltration = false;
        this.preferOutdoorsOnly = false;
        this.preferredMaxCapacity = Integer.MAX_VALUE;
        this.preferredMaxVenueSize = Integer.MAX_VALUE;
    }
}
