"use strict";

var CResultsPage = function() {

    var fAirportFrom;
    var fAirportTo;
    var fIsReturn;
    var fFlyOutDate;
    var fReturnDate;
    var fTicketClass;


    this.parseParameters = function(parameters) {
        
        var result = this.getFirstItemAndRemainder(parameters);
        fAirportFrom = this.getParameterValue(result.fistItem);
        
        result = this.getFirstItemAndRemainder(result.remainder);
        fAirportTo = this.getParameterValue(result.fistItem);
        
        result = this.getFirstItemAndRemainder(result.remainder);
        fIsReturn = this.getParameterValue(result.fistItem);

        result = this.getFirstItemAndRemainder(result.remainder);
        fFlyOutDate = this.getParameterValue(result.fistItem);
        
        result = this.getFirstItemAndRemainder(result.remainder);
        fReturnDate = this.getParameterValue(result.fistItem);
        
        result = this.getFirstItemAndRemainder(result.remainder);
        fTicketClass = this.getParameterValue(result.fistItem);
    }
    
    this.getFirstItemAndRemainder = function(str) {
        return CStringHelper.getFirstItemAndRemainder(str, "&");
    }
    
    this.getParameterValue = function(complexParameter) {
        return CStringHelper.getTheSecondItem(complexParameter, "=");
    }
    
    this.searchAndDisplayFlights = function(pageParameters) {
        this.parseParameters(pageParameters)
        this.fillResultsTable();
    }

    this.fillResultsTable = function() {
        var table = document.getElementById("mainResultsTable");

        var priceRange = new CPriceRange();
        priceRange.initalized = false;
        
        var flightArray = new CFlightArray();
        var date1 = new Date(fFlyOutDate);
        var availableFlightsArray1 = flightArray.createAvailableFlightArray(fAirportFrom, fAirportTo, date1, fTicketClass, priceRange);        
        
        if (availableFlightsArray1.length > 0) {
            this.appendRowWithJourneyTable(date1, fAirportFrom, fAirportTo, fTicketClass, table, availableFlightsArray1, priceRange);
        } else {
            this.appendRowNoFlights(fAirportFrom, fAirportTo, table);
        }

        if (fIsReturn=="true") {
            var date2 = new Date(fReturnDate);
            var availableFlightsArray2 = flightArray.createAvailableFlightArray(fAirportFrom, fAirportTo, date2, fTicketClass, priceRange);
            
            if (availableFlightsArray2.length > 0) {
                this.appendRowWithJourneyTable(date2, fAirportTo, fAirportFrom, fTicketClass, table, availableFlightsArray2, priceRange);
            } else {
                this.appendRowNoFlights(airportCodeTo, airportCodeFrom, table);
            }
        }

        this.appendRowWithPriceRange(priceRange, table);
    }
    
    this.appendRowWithJourneyTable = function(departureDate, airportCodeFrom, airportCodeTo, ticketClass, table, availableFlightsArray, priceRange) {
        var singleJourneyTable = this.createSingleJourneyTable(airportCodeFrom, airportCodeTo, departureDate, ticketClass, priceRange, availableFlightsArray);
        CFormHelper.appendRowWithOneCell(table, singleJourneyTable); 
    }
    
    this.createSingleJourneyTable = function(airportCodeFrom, airportCodeTo, departureDate, ticketClass, priceRange, availableFlightsArray) {
        var table = document.createElement("table");
        table.className = "single_journey";

        var journeyTitleNode = document.createTextNode("From: \xa0\xa0\xa0" + airportCodeFrom + "\xa0\xa0\xa0 to: \xa0\xa0\xa0" + airportCodeTo); // TODO similar exists
        CFormHelper.appendRowWithOneCell(table, journeyTitleNode);

        var availableFlightsTable = this.createAvailableFlightsTable(airportCodeFrom, airportCodeTo, departureDate, ticketClass, priceRange, availableFlightsArray);
        CFormHelper.appendRowWithOneCell(table, availableFlightsTable);

        return table;
    }

    this.createAvailableFlightsTable = function(airportCodeFrom, airportCodeTo, departureDate, ticketClass, priceRange, availableFlightsArray) {
        var table = document.createElement("table");
        table.className = "available_flights";

        var headerArray = [ "Departure", "Arrival", "Duration", "Price" ];
        CFormHelper.appendTableHeaderWithCells(table, headerArray);

        var cellContentArray;
        var availableFlight;
        var index;

        for (index = 0; index < availableFlightsArray.length; index++) {
            availableFlight = availableFlightsArray[index];
            cellContentArray = CAvailableFlightHelper.flightToTextArray(availableFlight);
            CFormHelper.appendTableStandardRowWithCells(table, cellContentArray);
        }

        return table;
    }
    
    this.appendRowNoFlights = function(airportCodeFrom, airportCodeTo, table) {
        var txtFromTo = "From: \xa0\xa0\xa0" + airportCodeFrom + "\xa0\xa0\xa0 to: \xa0\xa0\xa0" + airportCodeTo;
        var txt = CFormHelper.htmlToElement('<table><tr><td>' + txtFromTo + '</td><td>\xa0\xa0NO FLIGHTS FOUND\xa0\xa0</td></tr></table>'); 
        CFormHelper.appendRowWithOneCell(table, txt);
    }    
    
    this.appendRowWithPriceRange = function(priceRange, table) {
        var txt = CFormHelper.htmlToElement(
                                    '<table class="minMaxPrices"><tr><td><p id="minPriceLabel" >Min price:</p></td><td id="minPrice"></td>' + 
                                    '<td><p id="maxPriceLabel" >Max price:</p></td><td id="maxPrice"></td></tr></table>'); 
        CFormHelper.appendRowWithOneCell(table, txt);
        
        document.getElementById("minPrice").innerHTML = priceRange.minPrice;
        document.getElementById("maxPrice").innerHTML = priceRange.maxPrice;
    }    
};
