package com.ecfeed.test;

public class FlightFinderTest {
	public void findFlightsTest(String airportFrom, String airportTo, String flyOutDate, 
			boolean isReturnJourney, int daysBetweenFlights, TicketClass ticketClass) {

		System.out.println("findFlightsTest(" + airportFrom + ", " + airportTo + ", " + flyOutDate + ", " 
		                    + isReturnJourney + ", " + daysBetweenFlights + ", " + ticketClass + ")");
	}

	public void findFlightsTest(String airportFrom, String airportTo, String flyOutDate, boolean isReturnJourney, int daysBetweenFlights, TicketClass ticketClass, float maxPrice) {
		// TODO Auto-generated method stub
		System.out.println("findFlightsTest(" + airportFrom + ", " + airportTo + ", " + flyOutDate + ", " + isReturnJourney + ", " + daysBetweenFlights + ", " + ticketClass + ", " + maxPrice + ")");
	}
}