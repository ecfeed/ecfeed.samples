"use strict";

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
