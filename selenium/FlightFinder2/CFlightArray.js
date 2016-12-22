var CFlightArray = function() {

    this.createAvailableFlightArray = function(airportCodeFrom, airportCodeTo, departureDate, ticketClass, priceRange) {
        var flightArray = [];
        var departureHoursArray = [ "09.45", "11:00", "14:00"];
        var arrivalHoursArray =      [ "10.45", "12:10", "15:25"];
        var durationArray =           [ "1 h 0 min", "1 h 10 min", "1h 25 min"];
        var pricesArray =               [ "250", "280", "290"];        
        
        var flightCount = this.createRandom(1, 3);
        var availableFlight;
        
        for(var counter = 0; counter < flightCount; counter++) {
            var departureDateStr = CDateHelper.convertDateToStr(departureDate) + " " + departureHoursArray[counter];
            var arrivalDateStr = CDateHelper.convertDateToStr(departureDate) + " " + arrivalHoursArray[counter];
            availableFlight = new CAvailableFlight(departureDateStr, arrivalDateStr, durationArray[counter], pricesArray[counter]);
            flightArray.push(availableFlight);
            priceRange.calculateMinMax(pricesArray[counter]);
        }        
        
        return flightArray;
    }    

    this.createRandom = function(lowerLimit, upperLimit) {
        return Math.floor((Math.random() * upperLimit) + lowerLimit);
    }
};