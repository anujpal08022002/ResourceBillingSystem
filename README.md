# Resource Usage & Billing System

A Java-based console application for managing resource usage, capacity,
usage sessions, and hourly billing.

## Overview

This application allows users to:

- View available resources
- View resource capacity and associated services
- Start a usage session
- Stop a usage session
- Automatically calculate the bill
- View active usage sessions
- View generated bills
- Handle invalid operations through validation and exceptions

The application uses in-memory collections for data storage as permitted
by the assignment.

## Technologies Used

- Java 21
- Object-Oriented Programming
- Java Collections Framework
- Java Time API
- BigDecimal for monetary calculations
- IntelliJ IDEA

No external libraries or frameworks are required.

## Project Structure

```text
src/
├── Main.java
├── BillingTest.java
│
├── exception/
│   └── BusinessException.java
│
├── model/
│   ├── Bill.java
│   ├── Resource.java
│   ├── Service.java
│   ├── UsageSession.java
│   └── User.java
│
└── service/
    ├── BillingService.java
    └── ResourceService.java