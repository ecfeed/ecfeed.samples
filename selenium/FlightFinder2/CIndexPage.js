"use strict";

//****************************************************************************

function initializeForm() {
    var form = new CIndexPage();
    form.initialize();
}

function onDirectionOneWayClicked() {
    document.getElementById("labelReturn").style.display = "none";
    document.getElementById("returnDate").style.display = "none";
}

function onDirectionReturnClicked() {
    document.getElementById("labelReturn").style.display = "block";
    document.getElementById("returnDate").style.display = "block";
}

function onSearchFlightsClicked() {
    var form = new CIndexPage();
    form.searchAndDisplayFlights();
}

// ****************************************************************************

var CIndexPage = function() {

    this.initialize = function() {
        
        this.initializeRadio();
        this.initilizeAirportFields();
        this.initializeTicketClass();
        
        CFormHelper.setSelectText("airportFrom", "ATL");
        CFormHelper.setSelectText("airportTo", "JFK");
        
        document.getElementById("flyOutDate").value = "2016-12-31";
        // document.getElementById("returnDate").value = "2017-02-15";
        
        CFormHelper.setSelectText("ticketClass", "Economy");
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
        
        if (this.performBasicChecks()) {
            this.goToResultsPage();
            }
    }

    this.performBasicChecks = function() {

        if(!this.checkFromToFields()) {
            return false;
        }

        if (!this.checkFlyOutDate()) {
            return false;
        }

        if (CFormHelper.isRadioChecked("radioReturn")) {
            if (!this.checkReturnDate()) {
                return false;
            }
            
            if (!this.checkFlyOutReturn()) {
                return false;
            }
                
        }

        if (CFormHelper.isSelectEmpty("ticketClass")) {
            this.showErrorMessage("Please fill 'Class' field.");
            return false;
        }

        return true;
    }

    this.checkFromToFields = function() {
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

    this.checkFlyOutDate = function() {
        var date = document.getElementById("flyOutDate").value;
        
        if (CStringHelper.isStringEmpty(date)) {
            this.showErrorMessage("'Fly out date' must not be the empty.");
            return false;
        }
        
        return true;
    }

    this.checkReturnDate = function() {
        var date = document.getElementById("returnDate").value;
        
        if (CStringHelper.isStringEmpty(date)) {
            this.showErrorMessage("'Return date' must not be the empty.");
            return false;
        }
        
        return true;
    }
    
    this.checkFlyOutReturn = function() {
        var flyOutDate = new Date("2016-12-31");
        var returnDate = new Date("2017-02-15");
        
        return true;
    }
    
    this.showErrorMessage = function(errorMessage) {
        window.location.href="error.html?msg=" + errorMessage;
    }

    this.goToResultsPage = function() {
        var airportFrom = document.getElementById("airportFrom").value;
        
        var airportTo = document.getElementById("airportTo").value;
        
        var airportsParam = "airportFrom=" + airportFrom + "&" + "airportTo=" + airportTo;
        
        var isReturn = document.getElementById("radioReturn").checked;
        
        var isReturnParam = "isReturn=" + isReturn;
        
        var flyOutDate = document.getElementById("flyOutDate").value;
        
        var returnDate = document.getElementById("returnDate").value;
        
        var datesParam = "flyOutDate=" + flyOutDate + "&" + "returnDate=" + returnDate;

        var ticketClass = document.getElementById("ticketClass").value;
        
        var ticketClassParam = "ticketClass=" + ticketClass;
       
        window.location.href = "results.html?" + airportsParam + "&" + isReturnParam + "&" + datesParam + "&" + ticketClassParam;
    }

};

