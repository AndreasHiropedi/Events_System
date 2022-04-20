# SEPP Coursework 3 - Group 15
###### Due Date
Friday 8 April 2022 at 16:00 GMT

## SDK Requirements
As specified in the [Development Tools](Inf2_SEPP_Development_Tools.pdf) specification.
- InteliJ IDEA
- Gradle 6.6
- JDK 14.0.1

## Software Specification
###### **_Aim_**
To implement and test the events application requested by the Scottish Government

###### **_About the Application_**
The events application will enable members of the public to find and book entertainment events, which are organised by registered entertainment providers. Certain events may also be funded by the Scottish Government.

###### **_Code Specification_**
We are required to complete a set of constructs that will interact with each another and create a fully functional system. The code specification is given by the [Javadoc](https://mateuszpast12.github.io/SEPP_Cw3_Group15/javadoc/index.html) documentation.

## Other Links
- [Requirements for this coursework](Inf2_SEPP_Coursework_3_21_22.pdf)
- [Requirements for previous coursework](Inf2_SEPP_Coursework_2_21_22_Requirements.pdf)
- [Requirements for first coursework](Inf2_SEPP_Coursework_1_21_22.pdf)

## Accessing Files
To obtain a copy of the repository, navigate to the directory where you want it to reside, then copy and paste the following into your console:
```
git clone https://github.com/mateuszPast12/SEPP_Cw3_Group15
```

Once you have local a copy of the repository, you can contribute into this repository using
```
git add .
git commit -m "messgae"
git push origin main
```

You can retrieve the most up-to-date version of the repository by running
```
git pull origin main
```

If any changes have been made after your last pull, pull the most recent version, which should cause git to ask you whether you want to merge your files, overwrite, or keep the new ones. After that, you can push your contributed version of the repository.

DO NOT try to push into the gh_pages branch, as that is only for rendering the javadoc web pages. It will also cause the inclusion of untracked files. So make sure you are pushing to the main branch.

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

## Other Notes
###### Faults in HTML document
- `PACKAGE` and `CLASS` links in the navigation bar are not working. I tried accessing them from the original HTML document, but it's still faulty. If it works for any of you, then please let me know.

###### Collaborators
- Mateusz Pasternak
- Andreas Hiropedi
- Alex Murphy
- Ignas Kleveckas
