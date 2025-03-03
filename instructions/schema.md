
Final Schema

1. Airlines

Airlines (Done)
- airline_id (Primary Key)
- airline_name
- airline_code
- logo_url

2. Airports  (Done)

Airports
- airport_id (Primary Key)
- airport_code (Unique)
- airport_name
- city
- country

3. Aircraft (Done)

Aircraft
- aircraft_id (Primary Key)
- aircraft_model
- manufacturer
- seating_capacity

4. Flights (Done)

Flights
- flight_id (Primary Key)
- airline_id (Foreign Key referencing Airlines)
- flight_number
- departure_airport_id (Foreign Key referencing Airports)
- destination_airport_id (Foreign Key referencing Airports)
- departure_time
- arrival_time
- aircraft_id (Foreign Key referencing Aircraft)
- seats_available
- status (Enum: Scheduled, Delayed, Cancelled, Departed, Arrived)
- delay_minutes
- trip_duration

5. Stops (Done)

Stops
- stop_id (Primary Key)
- flight_id (Foreign Key referencing Flights)
- stop_number
- stop_airport_id (Foreign Key referencing Airports)
- stop_duration

6. Classes (Done)

Classes
- class_id (Primary Key)
- class_name (Enum: Economy, Business, First Class)

7. Seats (Done)

Seats
- seat_id (Primary Key)
- flight_id (Foreign Key referencing Flights)
- seat_number
- class_id (Foreign Key referencing Classes)
- is_available

8. FlightPrices (Done)

FlightPrices
- flight_id (Foreign Key referencing Flights)
- class_id (Foreign Key referencing Classes)
- base_price
- dynamic_price
- Primary Key: (flight_id, class_id)

9. Meals    (Done)

Meals
- meal_id (Primary Key)
- meal_name
- description

10. Users  (Partially Done)

Users
- user_id (Primary Key)
- name
- email
- phone
- password_hash
- role (Enum: Traveler, Admin)

11. Passengers (New Table) (Partially Done)

Passengers
- passenger_id (Primary Key)
- booking_id (Foreign Key referencing Bookings)
- name
- age
- passport_number

12. Bookings (Partially Done)

Bookings
- booking_id (Primary Key)
- user_id (Foreign Key referencing Users)
- flight_id (Foreign Key referencing Flights)
- return_flight_id (Foreign Key referencing Flights, nullable)
- seat_id (Foreign Key referencing Seats)
- meal_id (Foreign Key referencing Meals, nullable)
- pnr (Unique)
- status (Enum: Confirmed, Cancelled, Checked-in)

13. BookingFlights (New Table) (Not Sure)

BookingFlights
- booking_flight_id (Primary Key)
- booking_id (Foreign Key referencing Bookings)
- flight_id (Foreign Key referencing Flights)
- is_return_flight (Boolean)

14. CheckIns (not sure)

CheckIns
- checkin_id (Primary Key)
- booking_id (Foreign Key referencing Bookings)
- checkin_time
- seat_id (Foreign Key referencing Seats)

15. BoardingPasses (New Table) (Not Sure)

BoardingPasses
- boarding_pass_id (Primary Key)
- booking_id (Foreign Key referencing Bookings)
- flight_id (Foreign Key referencing Flights)
- boarding_pass_url

16. BaggageRules (Done)

BaggageRules
- baggage_rule_id (Primary Key)
- flight_id (Foreign Key referencing Flights)
- allowed_checked_bags
- allowed_carry_ons
- checked_bag_weight_limit
- carry_on_weight_limit

17. BaggageAllocations (Partially Done)

BaggageAllocations
- baggage_allocation_id (Primary Key)
- booking_id (Foreign Key referencing Bookings)
- checked_bags
- carry_ons
- extra_baggage_fee

18. Payments

Payments
- payment_id (Primary Key)
- booking_id (Foreign Key referencing Bookings)
- amount
- payment_status (Enum: Pending, Completed, Failed)
- payment_date



Relationships Between Tables

Airlines to Flights: One-to-Many

One airline can have many flights.

Airports to Flights: One-to-Many

One airport can be the departure or destination for many flights.

Aircraft to Flights: One-to-Many

One aircraft can be used for many flights.

Flights to Stops: One-to-Many

One flight can have multiple stops.

Flights to Seats: One-to-Many

One flight can have many seats.

Classes to Seats: One-to-Many

One class can have many seats.

Flights to FlightPrices: One-to-Many

One flight can have multiple prices for different classes.

Users to Bookings: One-to-Many

One user can make many bookings.

Flights to Bookings: One-to-Many

One flight can have multiple bookings.

Meals to Bookings: One-to-Many

One meal can be associated with multiple bookings.

Bookings to Passengers: One-to-Many

One booking can have multiple passengers.

Bookings to BookingFlights: One-to-Many

One booking can have multiple flights (for multi-leg bookings).

Bookings to CheckIns: One-to-One

One booking can have one check-in.

Bookings to BoardingPasses: One-to-Many

One booking can have multiple boarding passes (for multi-leg flights).

Flights to BaggageRules: One-to-Many

One flight can have baggage rules.

Bookings to BaggageAllocations: One-to-One

One booking can have one baggage allocation.

Bookings to Payments: One-to-One

One booking can have one payment.