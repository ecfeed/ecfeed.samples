"use strict";

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

CStringHelper.getFirstItemAndRemainder = function(str, separator) {
    
    if (CStringHelper.isStringEmpty(str)) {
        return { fistItem: null, remainder: null };
    }
    
    var index = str.indexOf(separator);
    
    if (index == -1) {
        return { fistItem: str, remainder: null };
    }

    var firstItem = (str.slice(0, index)).trim();
    var remainder = (str.slice(index+1)).trim();
    
    return { fistItem: firstItem, remainder: remainder };
}