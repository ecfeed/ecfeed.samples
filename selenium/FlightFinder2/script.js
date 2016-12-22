"use strict";

//****************************************************************************

function initializeForm() {
    var form = new CForm();
    form.initialize();
}

function onDirectionOneWayClicked() {
    document.getElementById("labelReturn").style.display = "none";
    document.getElementById("dateReturn").style.display = "none";
}

function onDirectionReturnClicked() {
    document.getElementById("labelReturn").style.display = "block";
    document.getElementById("dateReturn").style.display = "block";
}

function onSearchFlightsClicked() {
    var form = new CForm();
    form.searchAndDisplayFlights();
}

// ****************************************************************************

var CForm = function() {

    this.initialize = function() {
        this.initializeRadio();
        this.initilizeAirportFields();
        this.initializeFlyOutReturnDates();
        this.initializeTicketClass();
    }

    this.initializeRadio = function() {
        document.getElementById("radioReturn").checked = "true";
    }

    this.initilizeAirportFields = function() {
        this.fillAirportsCombo(document.getElementById("airportFrom"));
        this.fillAirportsCombo(document.getElementById("airportTo"));
    }

    this.fillAirportsCombo = function(selectTag) {
        var airportArray = new CAirportArray();
        var airportCount = airportArray.count();
        var airportIndex = 0;
        var airportDescription;
        var option;

        for (airportIndex = 0; airportIndex < airportCount; airportIndex++) {
            airportDescription = airportArray.airportAt(airportIndex);
            option = airportDescription.code;
            CFormHelper.addOneSelectOption(selectTag, option);
        }
    }

    this.initializeFlyOutReturnDates = function() {
        // XYX
    }

    this.initializeTicketClass = function() {
        var ticketClass = new CTicketClassArray();

        var selectTag = document.getElementById("ticketClass");
        var classCount = ticketClass.count();
        var index;

        for (index = 0; index < classCount; index++) {
            CFormHelper.addOneSelectOption(selectTag, ticketClass.classAt(index));
        }
    }

    this.searchAndDisplayFlights = function() {
        
    var mainTable = document.getElementById("mainResultsTable");
    mainTable.style.display = "none";
        
    CFormHelper.deleteAllChildren(mainTable);
        
    if (this.performBasicChecks()) {
        this.generateResultsTable();
        }
    }

    this.performBasicChecks = function() {
        var messageTag = document.getElementById("message");
        messageTag.innerHTML = "&nbsp";

        if(!this.checkFromToFields(messageTag)) {
            return false;
        }

        if (!this.checkFlyOutDate(messageTag)) {
            return false;
        }

        if (CFormHelper.isRadioChecked("radioReturn")) {
            if (!this.checkReturnDate(messageTag)) {
                return false;
            }
            
            if (!this.checkFlyOutReturn(messageTag)) {
                return false;
            }
                
        }

        if (CFormHelper.isSelectEmpty("ticketClass")) {
            this.showErrorMessage("Please fill 'Class' field.");
            return false;
        }

        return true;
    }

    this.checkFromToFields = function(messageTag) {
        if (CFormHelper.isSelectEmpty("airportFrom")) {
            this.showErrorMessage("Please fill 'From' field.");
            return false;
        }

        if (CFormHelper.isSelectEmpty("airportTo")) {
            this.showErrorMessage("Please fill 'To' field.");
            return false;
        }

        if (CFormHelper.twoSelectTextsEqual("airportFrom", "airportTo")) {
            this.showErrorMessage("Field 'From' and 'To' must not be the same.");
            return false;
        }

        return true;
    }

    this.checkFlyOutDate = function(messageTag) {
        // XYX
        return true;
    }

    this.checkReturnDate = function(messageTag) {
        // XYX

        return true;
    }
    
    this.checkFlyOutReturn = function(messageTag) {
        var flyOutDate = null; // XYX
        var returnDate = null; // XYX 
        
        if (returnDate.getTime() < flyOutDate.getTime()) {
            this.showErrorMessage("'Return date' is too early.");
            return false;
        }
        
        return true;
    }
    
    this.showErrorMessage = function(errorMessage) {
        window.location.href="error.html?msg=" + errorMessage;
    }

    this.generateResultsTable = function() {
        var mainTable = document.getElementById("mainResultsTable");
        mainTable.style.display = "block";

        this.fillResultsTable();
    }

    this.fillResultsTable = function() {
        var table = document.getElementById("mainResultsTable");
        var airportCodeFrom = CFormHelper.getSelectedText("airportFrom");
        var airportCodeTo = CFormHelper.getSelectedText("airportTo");
        var ticketClass = CFormHelper.getSelectedText("ticketClass");

        var priceRange = new CPriceRange();
        priceRange.initalized = false;
        
        var flightArray = new CFlightArray();
        var date1 = CFormHelper.createDate("dateFlyOutYear", "dateFlyOutMonth", "dateFlyOutDay"); // XYX
        var availableFlightsArray1 = flightArray.createAvailableFlightArray(airportCodeFrom, airportCodeTo, date1, ticketClass, priceRange);        
        
        if (availableFlightsArray1.length > 0) {
            this.appendRowWithJourneyTable(date1, airportCodeFrom, airportCodeTo, ticketClass, table, availableFlightsArray1, priceRange);
        } else {
            this.appendRowNoFlights(airportCodeFrom, airportCodeTo, table);
        }
        
        if (CFormHelper.isRadioChecked("radioReturn")) {
            var date2 = CFormHelper.createDate("dateReturnYear", "dateReturnMonth", "dateReturnDay"); // XYX
            var availableFlightsArray2 = flightArray.createAvailableFlightArray(airportCodeFrom, airportCodeTo, date2, ticketClass, priceRange);
            
            if (availableFlightsArray2.length > 0) {
                this.appendRowWithJourneyTable(date2, airportCodeTo, airportCodeFrom, ticketClass, table, availableFlightsArray2, priceRange);
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
