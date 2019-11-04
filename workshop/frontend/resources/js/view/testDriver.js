import * as showModal from './showModal.js';
import * as formEffects from './formEffects.js';
import * as formElements from './formElements.js';
import * as formManipulate from './formManipulate.js';

//------------------------------------------------

let resultsTestId = 1;
let resultsTestMaxId = 0;

const resultsTestSuite = {
    success: 0,
    failureInput: 0,
    failureOutput: 0,
    successId: [],
    failureInputId: [],
    failureOutputId: [],
    details: []
}

const resultsTestSuiteDefault = {
    success: 0,
    failureInput: 0,
    failureOutput: 0,
    successId: [],
    failureInputId: [],
    failureOutputId: [],
    details: []
}

//------------------------------------------------

export const setResultsTestMaxId = (max = 0) => {
    resultsTestMaxId = max;
}

export const getResultsTestSuite = () => {
    return resultsTestSuite;
}

export const setResultsTestSuite = (id = 1, testSuite = resultsTestSuiteDefault) => {
    resultsTestId = id;
    resultsTestSuite.success = testSuite.success;
    resultsTestSuite.failureInput = testSuite.failureInput;
    resultsTestSuite.failureOutput = testSuite.failureOutput;
    resultsTestSuite.successId = [...testSuite.successId];
    resultsTestSuite.failureInputId = [...testSuite.failureInputId];
    resultsTestSuite.failureOutputId = [...testSuite.failureOutputId];
    resultsTestSuite.details = [...testSuite.details];
}

export const footerProgressUpdate = () => {
    if (resultsTestMaxId > 0) {
        formManipulate.updateFooter([
            `Total progress - ${resultsTestId}/${resultsTestMaxId}`, 
            `Success - ${resultsTestSuite.success} | Failed (input) - ${resultsTestSuite.failureInput} | Failed (output) - ${resultsTestSuite.failureOutput}`
        ]);
    }
}

export const processTestSuite = (testSuite) => {
    formEffects.backgroundOverlay(true);
    formEffects.backgroundGrayscale(true);
    setResultsTestSuite();
    setResultsTestMaxId(testSuite.length);

    let index = 1;
    for (const val of testSuite) {
        setTimeout( () => {
            $.modal.close();
            formManipulate.fillFormJSON(val);
            formElements.submit.click();
        }, index * 1000)        

        index++;
    }

    setTimeout( () => {
        $.modal.close();
        formEffects.backgroundOverlay(false);
        showModal.showModalResults();
    }, index * 1000)
}

export const processTestCase = (response) => {
    let executionStatus = 'success';

    footerProgressUpdate();

    if (response.errorInput.length > 0) {
        executionStatus = 'failureInput';
        formManipulate.updateResponse();
        showModal.showModalError(response.errorInput.join('\n'));
    } else if (response.errorOutput.length > 0) {
        executionStatus = 'failureOutput';
        formManipulate.updateResponse('Request rejected', response.errorOutput.join('\n'));
    } else {
        formManipulate.updateResponse('Request accepted', 'Order processsed without errors! Please wait for the delivery...');
    }

    updateResultsTestSuite(resultsTestId, response, executionStatus);
    resultsTestId++;
}

//------------------------------------------------

const updateResultsTestSuite = (number, response, type = 'success') => {
    resultsTestSuite[type]++;
    resultsTestSuite[`${type}Id`].push(number);

    if (response.errorInput.length > 0 || response.errorOutput.length > 0) {
        resultsTestSuite.details.push({
            resultsTestId,
            response
        });
    }
}

//------------------------------------------------

Object.freeze(resultsTestSuiteDefault)