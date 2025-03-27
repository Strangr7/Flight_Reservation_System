# Flight Booking Project - TODO List

## Flight Search and Results Improvements
### Meals and Seat Selection
- [ ] Add `flightId` in meals fetch to retrieve meals according to flight and class
- [ ] Create UI for seat selection process

### Flight Search Result Enhancements

- [ ] Modify flight result display:
  - Add date above flight results (reference: Skyscanner design)
  - Display flight search parameters on result page
  - Enable search parameter editing
- [ ] Update stops table(optional):
  - Add arrival time and departure time
  - Improve visual timeline representation

### Flight Status
- [ ] Develop flight status servlet to:
  - Fetch complete flight data
  - Search by flight number
  - Include departure date
  - Create specific JSP for flight status display



## Admin Panel Development
### Admin Dashboard Features
- [ ] Create comprehensive admin panel with sections for:
  - Airports
  - Flights
  - Users
  - Analysis

### Airports Management
- [ ] Implement airport admin functionalities:
  - Add new airports
  - Edit existing airport details
  - Remove airports from system

### Flights Management
- [ ] Develop flight administration features:
  - Add new flights
  - Edit existing flight details
  - Delete flights
  - View passenger information
  - Display passenger seat and meal details

### User Management
- [ ] Create user administration section:
  - View all registered users
  - Display user booking history

### Analysis and Reporting
- [ ] Develop analysis dashboard
  - Create comprehensive reporting system
  - Implement data visualization
  - Generate insights from booking data

## Future Considerations
- [ ] Refine UI/UX based on initial user feedback
- [ ] Implement additional security measures
- [ ] Optimize database queries
- [ ] Set up comprehensive logging