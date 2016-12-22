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
