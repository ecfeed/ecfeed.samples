"use strict";

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
