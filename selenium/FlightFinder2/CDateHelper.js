var CDateHelper = function() {

};

CDateHelper.createIncrementalDateStr = function(incrementalDateStr) {
    var daysStr = CStringHelper.getTheFirstItem(incrementalDateStr, " ");
    var daysIncrement = parseInt(daysStr);

    var newDate = new Date();
    newDate.setDate(newDate.getDate() + daysIncrement);

    var newDateStr = CDateHelper.convertDateToStr(newDate);
    var newTimeStr = CStringHelper.getTheLastItem(incrementalDateStr," ");
    return newDateStr + " " + newTimeStr;
}

CDateHelper.convertDateToStr = function(date) {
         var monthStr = CStringHelper.createZeroFormattedString(date.getMonth() + 1, 2)
         var dayStr = CStringHelper.createZeroFormattedString(date.getDate(), 2)
         return date.getFullYear().toString() + "-" + monthStr + "-" + dayStr;
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
