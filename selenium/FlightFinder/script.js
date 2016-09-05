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
        CFormHelper.initializeDateFields("dateFlyOutYear", "dateFlyOutMonth", "dateFlyOutDay", 1);
        CFormHelper.initializeDateFields("dateReturnYear", "dateReturnMonth", "dateReturnDay", 2);
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
            messageTag.innerHTML = "Please fill 'Class' field.";
            return false;
        }

        return true;
    }

    this.checkFromToFields = function(messageTag) {
        if (CFormHelper.isSelectEmpty("airportFrom")) {
            messageTag.innerHTML = "Please fill 'From' field.";
            return false;
        }

        if (CFormHelper.isSelectEmpty("airportTo")) {
            messageTag.innerHTML = "Please fill 'To' field.";
            return false;
        }

        if (CFormHelper.twoSelectTextsEqual("airportFrom", "airportTo")) {
            messageTag.innerHTML = "Field 'From' and 'To' must not be the same.";
            return false;
        }

        return true;
    }

    this.checkFlyOutDate = function(messageTag) {
        if (CFormHelper.isSelectEmpty("dateFlyOutYear")) {
            messageTag.innerHTML = "Please fill 'Fly out date' (year).";
            return false;
        }

        if (CFormHelper.isSelectEmpty("dateFlyOutMonth")) {
            messageTag.innerHTML = "Please fill 'Fly out date' (month).";
            return false;
        }

        if (CFormHelper.isSelectEmpty("dateFlyOutDay")) {
            messageTag.innerHTML = "Please fill 'Fly out date' (day).";
            return false;
        }

        return true;
    }

    this.checkReturnDate = function(messageTag) {
        if (CFormHelper.isSelectEmpty("dateReturnYear")) {
            messageTag.innerHTML = "Please fill 'Return date' (year).";
            return false;
        }

        if (CFormHelper.isSelectEmpty("dateReturnMonth")) {
            messageTag.innerHTML = "Please fill 'Return date' (month).";
            return false;
        }

        if (CFormHelper.isSelectEmpty("dateReturnDay")) {
            messageTag.innerHTML = "Please fill 'Return date' (day).";
            return false;
        }

        return true;
    }
    
    this.checkFlyOutReturn = function(messageTag) {
        var flyOutDate = CFormHelper.createDate("dateFlyOutYear", "dateFlyOutMonth", "dateFlyOutDay");
        var returnDate = CFormHelper.createDate("dateReturnYear", "dateReturnMonth", "dateReturnDay");
        
        if (returnDate.getTime() < flyOutDate.getTime()) {
            messageTag.innerHTML = "'Return date' is too early.";
            return false;
        }
        
        return true;
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
        var date1 = CFormHelper.createDate("dateFlyOutYear", "dateFlyOutMonth", "dateFlyOutDay");
        var availableFlightsArray1 = flightArray.createAvailableFlightArray(airportCodeFrom, airportCodeTo, date1, ticketClass, priceRange);        
        
        if (availableFlightsArray1.length > 0) {
            this.appendRowWithJourneyTable(date1, airportCodeFrom, airportCodeTo, ticketClass, table, availableFlightsArray1, priceRange);
        } else {
            this.appendRowNoFlights(airportCodeFrom, airportCodeTo, table);
        }
        
        if (CFormHelper.isRadioChecked("radioReturn")) {
            var date2 = CFormHelper.createDate("dateReturnYear", "dateReturnMonth", "dateReturnDay");
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

// ****************************************************************************

var CPriceRange = function() {
    var initialized = false;
    var minPrice = 0;
    var maxPrice = 0;
    
    this.calculateMinMax = function(number) {
        
        if (!this.initialized) {
            this.minPrice = number;
            this.maxPrice = number;
            this.initialized = true;
        }
        
        this.minPrice = Math.min(this.minPrice, number);
        this.maxPrice = Math.max(this.maxPrice, number);
    }
};

// ****************************************************************************

var CFormHelper = function() {
};

CFormHelper.htmlToElement = function(html) {
    var div = document.createElement('div');
    div.innerHTML = html;
    return div.firstChild;
}
    
CFormHelper.addOneSelectOption = function(selectTag, optionText) {
    selectTag.options[selectTag.options.length] = new Option(optionText, optionText);
}

CFormHelper.fillComboWithNumbers = function(selectTag, startNumber, howMany, width) {
    var endNumber = howMany + startNumber;
    var formattedStr;

    for (var number = startNumber; number < endNumber; number++) {
        formattedStr = CStringHelper.createZeroFormattedString(number, width);
        CFormHelper.addOneSelectOption(selectTag, formattedStr);
    }
}

CFormHelper.initializeDateFields = function(yearTagId, monthTagId, dayTagId, dayIncrement) {
    var date = new Date();
    date.setDate(date.getDate() + dayIncrement);

    CFormHelper.fillComboWithNumbers(document.getElementById(yearTagId), date.getFullYear(), 3, 4);
    CFormHelper.setSelectText(yearTagId, date.getFullYear().toString());

    CFormHelper.fillComboWithNumbers(document.getElementById(monthTagId), 1, 12, 2);
    var monthStr = CStringHelper.createZeroFormattedString(date.getMonth()+1, 2);
    CFormHelper.setSelectText(monthTagId, monthStr);

    CFormHelper.fillComboWithNumbers(document.getElementById(dayTagId), 1, 31, 2);
    var dayStr = CStringHelper.createZeroFormattedString(date.getDate(), 2);
    CFormHelper.setSelectText(dayTagId, dayStr);
}

CFormHelper.deleteAllChildren = function(parentTag) {
    var childrenCount = parentTag.children.length;
    var index;
    var lastChild;

    for (index = childrenCount - 1; index >= 0; index--) {
        lastChild = parentTag.children[0];
        parentTag.removeChild(lastChild);
    }
}

CFormHelper.appendRowWithOneCell = function(tableElement, cellElement) {
    var tableRow = document.createElement("tr");
    var tableData = document.createElement("td");
    tableData.appendChild(cellElement);
    tableRow.appendChild(tableData);
    tableElement.appendChild(tableRow);
}

CFormHelper.appendTableStandardRowWithCells = function(tableElement, cellContentArray) {
    CFormHelper.appendTableRowWithCells(tableElement, "td", cellContentArray);
}

CFormHelper.appendTableHeaderWithCells = function(tableElement, cellContentArray) {
    CFormHelper.appendTableRowWithCells(tableElement, "th", cellContentArray);
}

CFormHelper.appendTableRowWithCells = function(tableElement, cellTag, cellContentArray) {
    var tableRow = document.createElement("tr");
    CFormHelper.appendCells(tableRow, cellTag, cellContentArray);
    tableElement.appendChild(tableRow);
}

CFormHelper.appendCells = function(tableRow, cellTag, contentTable) {
    var length = contentTable.length;
    var index;
    var tableData;
    var tableText;

    for (index = 0; index < length; index++) {
        tableData = document.createElement(cellTag);
        tableText = document.createTextNode(contentTable[index]);
        tableData.appendChild(tableText);
        tableRow.appendChild(tableData);
    }
}

CFormHelper.isSelectEmpty = function(elementId) {
    var text = CFormHelper.getSelectedText(elementId);

    if(CStringHelper.isStringEmptyOrBlank(text)) {
        return true;
    }

    return false;
}

CFormHelper.twoSelectTextsEqual = function(id1, id2) {
    if (CFormHelper.getSelectedText(id1) === CFormHelper.getSelectedText(id2)) {
        return true;
    }

    return false;
}

CFormHelper.setSelectText = function(elementId, value) {
    var selectTag = document.getElementById(elementId);
    selectTag.value = value;
}

CFormHelper.getSelectedText = function(elementId) {
    var selectTag = document.getElementById(elementId);
    return selectTag.options[selectTag.selectedIndex].text;
}

CFormHelper.isRadioChecked = function(radioTag) {
    if (document.getElementById(radioTag).checked) {
        return true;
    }
    return false;
}

CFormHelper.createDate = function(yearId, monthId, dayId) {
     var yearStr =  CFormHelper.getSelectedText(yearId);
     var monthStr =  CFormHelper.getSelectedText(monthId);
     var dayStr =  CFormHelper.getSelectedText(dayId);

    return new Date(yearStr + "-" + monthStr + "-" + dayStr);
}

// ****************************************************************************

var CDateHelper = function() {

};

CDateHelper.createIncrementalDateStr = function(incrementalDateStr) {
    var daysStr = CStringHelper.getTheFirstItem(incrementalDateStr, " ");
    var daysIncrement = parseInt(daysStr);

    var newDate = new Date();
    newDate.setDate(newDate.getDate() + daysIncrement);

    var fullYear = newDate.getFullYear();
    var month = newDate.getMonth() + 1;
    var day = newDate.getDate();

    var newDateStr =
        fullYear.toString() + "-" +
        CStringHelper.createZeroFormattedString(month, 2) + "-" +
        CStringHelper.createZeroFormattedString(day, 2);

    var newTimeStr = CStringHelper.getTheLastItem(incrementalDateStr," ");

    return newDateStr + " " + newTimeStr;
}

CDateHelper.createIncrementalDate = function(incrementalDateStr) {
    return new Date(CDateHelper.createIncrementalDateStr(incrementalDateStr));
}

CDateHelper.differenceInHoursAndMinutes = function(earlierDate, laterDate) {
    var difference = laterDate - earlierDate;

    var oneMinute = 1000 * 60;
    var diffTotalMinutes = difference / oneMinute;

    var hours = Math.floor(diffTotalMinutes / 60);
    var minutes = diffTotalMinutes % 60;

    return hours.toString() + " h " + minutes.toString() + " min";
}

// ****************************************************************************

var CAvailableFlight = function(departure, arrival, duration, price) {
    this.departure = departure;
    this.arrival = arrival;
    this.duration = duration;
    this.price = price;
};

// ****************************************************************************

var CAvailableFlightHelper = function() {

};

CAvailableFlightHelper.flightToTextArray = function(availableFlight) {
    var array = [];

    array.push(availableFlight.departure);
    array.push(availableFlight.arrival);
    array.push(availableFlight.duration);
    array.push(availableFlight.price);

    return array;
}

// ****************************************************************************

var CFlightArray = function() {

    this.flights = [
        {airportCodeFrom:"JFK", airportCodeTo:"ATL", departure:"1 9:30", arrival:"1 10:30", ticketClass:"Economy", freePlaces:"5", price:"280"},
        {airportCodeFrom:"JFK", airportCodeTo:"ATL", departure:"1 9:30", arrival:"1 10:30", ticketClass:"Business class", freePlaces:"2", price:"800"},
        {airportCodeFrom:"JFK", airportCodeTo:"ATL", departure:"1 19:30", arrival:"1 21:30", ticketClass:"Economy", freePlaces:"5", price:"300"},
        {airportCodeFrom:"JFK", airportCodeTo:"ATL", departure:"1 19:30", arrival:"1 21:30", ticketClass:"Business class", freePlaces:"2", price:"800"},
        {airportCodeFrom:"ATL", airportCodeTo:"JFK", departure:"1 10:30", arrival:"1 11:30", ticketClass:"Economy", freePlaces:"8", price:"310"},
        {airportCodeFrom:"ATL", airportCodeTo:"JFK", departure:"1 10:30", arrival:"1 11:30", ticketClass:"Business class", freePlaces:"3", price:"800"},
        {airportCodeFrom:"ATL", airportCodeTo:"JFK", departure:"1 20:30", arrival:"1 22:30", ticketClass:"Economy", freePlaces:"8", price:"330"},
        {airportCodeFrom:"ATL", airportCodeTo:"JFK", departure:"1 20:30", arrival:"1 22:30", ticketClass:"Business class", freePlaces:"3", price:"800"},

        {airportCodeFrom:"JFK", airportCodeTo:"ATL", departure:"2 9:45", arrival:"2 10:45", ticketClass:"Economy", freePlaces:"5", price:"300"},
        {airportCodeFrom:"JFK", airportCodeTo:"ATL", departure:"2 9:45", arrival:"2 10:45", ticketClass:"Business class", freePlaces:"2", price:"800"},
        {airportCodeFrom:"ATL", airportCodeTo:"JFK", departure:"2 10:30", arrival:"2 11:45", ticketClass:"Economy", freePlaces:"8", price:"305"},
        {airportCodeFrom:"ATL", airportCodeTo:"JFK", departure:"2 10:30", arrival:"2 11:45", ticketClass:"Business class", freePlaces:"3", price:"800"},

        {airportCodeFrom:"CDG", airportCodeTo:"FRA", departure:"1 08:30", arrival:"1 10:30", ticketClass:"Economy", freePlaces:"51", price:"200" },
        {airportCodeFrom:"CDG", airportCodeTo:"FRA", departure:"1 08:30", arrival:"1 10:30", ticketClass:"Business class", freePlaces:"3", price:"600" },
        {airportCodeFrom:"FRA", airportCodeTo:"CDG", departure:"1 08:50", arrival:"1 10:36", ticketClass:"Economy", freePlaces:"7", price:"200" },
        {airportCodeFrom:"FRA", airportCodeTo:"CDG", departure:"1 08:50", arrival:"1 10:36", ticketClass:"Business class", freePlaces:"7", price:"600" },

        {airportCodeFrom:"CDG", airportCodeTo:"FRA", departure:"2 08:30", arrival:"2 10:30", ticketClass:"Economy", freePlaces:"51", price:"200" },
        {airportCodeFrom:"CDG", airportCodeTo:"FRA", departure:"2 08:30", arrival:"2 10:30", ticketClass:"Business class", freePlaces:"3", price:"600" },
        {airportCodeFrom:"FRA", airportCodeTo:"CDG", departure:"2 08:50", arrival:"2 10:36", ticketClass:"Economy", freePlaces:"7", price:"200" },
        {airportCodeFrom:"FRA", airportCodeTo:"CDG", departure:"2 08:50", arrival:"2 10:36", ticketClass:"Business class", freePlaces:"7", price:"600" },

        {airportCodeFrom:"JFK", airportCodeTo:"FRA", departure:"1 9:30", arrival:"2 10:30", ticketClass:"Economy", freePlaces:"5", price:"1500"},
        {airportCodeFrom:"JFK", airportCodeTo:"FRA", departure:"1 9:30", arrival:"2 10:30", ticketClass:"Business class", freePlaces:"2", price:"4000"},

        ];

    this.createAvailableFlightArray = function(airportCodeFrom, airportCodeTo, departureDate, ticketClass, priceRange) {
        var flightArray = [];
        var flight;
        var availableFlight;
        var index;
        var departureDateStr;
        var arrivalDateStr;
        var departureDate;
        var arrivalDate;
        var durationStr;

        for (index = 0; index < this.flights.length; index++) {
            flight = this.flights[index];

            if (this.isFlightSelected(flight, airportCodeFrom, airportCodeTo, departureDate, ticketClass)) {
                
                departureDateStr = CDateHelper.createIncrementalDateStr(flight.departure);
                arrivalDateStr = CDateHelper.createIncrementalDateStr(flight.arrival);

                departureDate = new Date(departureDateStr);
                arrivalDate = new Date(arrivalDateStr);

                durationStr = CDateHelper.differenceInHoursAndMinutes(departureDate, arrivalDate);
                availableFlight = new CAvailableFlight(departureDateStr, arrivalDateStr, durationStr, flight.price);
                flightArray.push(availableFlight);
                
                 priceRange.calculateMinMax(flight.price);
            }
        }

        return flightArray;
    }

    this.isFlightSelected = function(flight, airportCodeFrom, airportCodeTo, departureDate, ticketClass) {

        if (flight.airportCodeFrom !== airportCodeFrom) {
            return false;
        }
        if (flight.airportCodeTo !== airportCodeTo) {
            return false;
        }

        var departureDateTime = CDateHelper.createIncrementalDate(flight.departure);

        if (departureDate.getFullYear() !== departureDateTime.getFullYear()) {
            return false;
        }
        if (departureDate.getMonth() !== departureDateTime.getMonth()) {
            return false;
        }
        if (departureDate.getDate() !== departureDateTime.getDate()) {
            return false;
        }

        if (flight.ticketClass !== ticketClass) {
            return false;
        }

        return true;
    }

};

// ****************************************************************************

var CAirportArray = function() {
    this.airports = [
        {city:"Atlanta", code:"ATL"},    
        {city:"New York", code:"JFK"},
        {city:"Paris", code:"CDG"},
        {city:"Frankfurt am Main", code:"FRA"},
        {city:"Delhi", code:"DEL" },
        {city:"Tokyo, Narita", code:"NRT"},
        
        ];

    this.count = function() {
        return this.airports.length;
    }

    this.airportAt = function(index) {
        return this.airports[index];
    }
};

// ****************************************************************************

var CTicketClassArray = function() {
    this.classes = [ "Economy", "Premium economy", "Business", "First" ];

    this.count = function() {
        return this.classes.length;
    }

    this.classAt = function(index) {
        return this.classes[index];
    }
};

// ****************************************************************************

var CStringHelper = function() {
};

CStringHelper.createZeroFormattedString = function(number, width) {
    return CStringHelper.addLeadingZeros(number.toString(), width);
}

CStringHelper.addLeadingZeros = function(numStr, width) {
    var str = "0";
    var cnt = width - numStr.length;

    if (cnt <= 0) {
        return numStr;
    }

    return str.repeat(cnt) + numStr;
}

CStringHelper.isStringEmptyOrBlank = function(str) {
    if (CStringHelper.isStringEmpty(str)) {
        return true;
    }

    if (/^\s*$/.test(str)) {
        return true;
    }

    return false;
}

CStringHelper.isStringEmpty = function(str) {
    if(!str) {
        return true;
    }

    if (0 === str.length) {
        return true;
    }

    return false;
}

CStringHelper.getTheFirstItem = function(str, separator) {
    var index = str.indexOf(separator);

    if (index == -1) {
        return "";
    }

    return (str.slice(0, index)).trim();
}

CStringHelper.getTheSecondItem = function(str, separator) {
    var index = str.indexOf(separator);

    if (index == -1) {
        return "";
    }

    return (str.slice(index + 1)).trim();
}

CStringHelper.getTheLastItem = function(str, separator) {
    var index = str.lastIndexOf(separator);

    if (index == -1) {
        return "";
    }

    return (str.slice(index + 1)).trim();
}

// ****************************************************************************