"use strict";

var CTicketClassArray = function() {
    this.classes = [ "Economy", "Premium economy", "Business", "First" ];

    this.count = function() {
        return this.classes.length;
    }

    this.classAt = function(index) {
        return this.classes[index];
    }
};
