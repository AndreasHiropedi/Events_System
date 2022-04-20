This project was done as part of a group coursework.

## SDK Requirements
- InteliJ IDEA
- Gradle 6.6
- JDK 14.0.1

## Software Specification
###### **_Aim_**
To implement and test the events application requested by the Scottish Government

###### **_About the Application_**
The events application will enable members of the public to find and book entertainment events, which are organised by registered entertainment providers. Certain events may also be funded by the Scottish Government.

## Simplified Requirements
###### General
- Identifying numbers must be unique across the entire system.
  - A new object of a class must be incremented from the previous object instance.
- Upon registration, ALL users must confirm registration to a payment provider and provide their email, which is attached to the payment system.
- API is simulated through interfaces within the `external` package, since we do not actually connect with the servers through the network.
- Booking states:
  - `ACTIVE` - booking is paid
  - `PAYMENTFAILED` - error with the payment
  - `CANCELLEDBYCONSUMER` - event cancelled by the consumer
  - `CANCELLEDBYPROVIDER` - event cancelled by the provider
- Emails should be unique
- Emails cannot be changed after registration
- For any action, the system must check
  - that the action was requested by the correct type of user (given email).
  - that the user has legitamate rights to to any requested booking (using booking numbers).
  - that the event/booking number corresponds to an existing event/booking.

###### Event
- Performence is uniquely identified by its date and time
- Can have multiple occurances at different times
- Consumer must provide
  - performance number
  - event number
  - date and time
- Only one performence can be booked at a time
- `ACTIVE` or `CANCELLED`status
- Different events can be scheduled at the same address at overlapping times.

###### Entertainment Providers
- Upon event/performence creation, the system should should send a notification to the providers, including
  - event name
  - identification numbers
  - total ticket number
- There can only be one entertainment provider instance on the system. Other providers should have their points of contact included (email, LinkedIn, Twitter, etc.).

###### Government Representative
- Pre-registered onto the system
- Able to log in via email and password
- Already has a payemnt provider email
- Accesses/requests booking records
- Upon record request, the representative must provide the entertainment provider's name.
  - System will return information that have active bookings for that entertainment provider's events that are active and ticketed
- Representative cannot change their sponsorship decision after making one.

###### Consumer


###### Booking
- Event booking confirmation should be provided to the consumer for the whole, individual booking.
- Booking record should contain the price paid per ticket and the entertainment provider's payment system username.
  - Ensure correct refunds in events of cancellations.

###### Collaborators
- Mateusz Pasternak
- Andreas Hiropedi
- Alex Murphy
- Ignas Kleveckas
