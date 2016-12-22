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
