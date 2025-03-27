# Flight Reservation System
(A flight booking webapp)

A web application for searching, booking, and managing flight reservations built with Java.


## Description

The **Flight Reservation System** is a Java-based Dynamic Web Project designed to streamline flight bookings. It allows users to search for available flights, reserve seats, and manage their bookings through a web interface. The system is built using Java Servlets, JSP, and MySQL, deployed on Apache Tomcat. Key features include user authentication, flight search by destination, and booking management.



## Prerequisites

Before running the project, ensure you have the following installed:
- **Java**: JDK 17 or higher
- **Apache Tomcat**: 10.0.27
- **Maven**: 3.8.x (for dependency management and build)
- **MySQL**: 8.0 or higher (database)
- **IDE**: Eclipse IDE for Enterprise Java Developers (recommended) or IntelliJ IDEA
- **Git**: For cloning the repository

## Installation

Follow these steps to set up the project locally:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Strangr7/Flight_Reservation_System.git
   cd Flight_Reservation_System
   
2. **Set up the database**:
- create MySQL database
	```bash
	CREATE DATABASE <db_name>;
- Import the schema and sample data from `src/main/resources/db/setup.sql
	```bash
	mysql -u <your_username> -p<password> <db_name> < src/main/resources/db/setup.sql
3. **Configure Database Connection**:
- Add .env file in the root directory with your database details:
	```bash
	DB_URL=jdbc:mysql://localhost:3306/<db_name>
	DB_USERNAME=your_username
    DB_PASSWORD=your_password
4. **Configure Hibernate config file**:
- Edit src\main\resources\hibernate.cfg.xml with your database details in **Database Connection Settings**

5. **Build the project**:
```bash
	mvn clean install
	or
	Right click the project > Run As > Maven Clean
	Right click the project > Run As > Maven Install

