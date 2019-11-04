import * as internalDriver from './testDriver.js';

//------------------------------------------------

export const results = () => {
    const resultsTestSuite = internalDriver.getResultsTestSuite();

    let resultsDetails = [];
    
    resultsDetails.push('TEST EXECUTION DETAILS');

    for (const val of resultsTestSuite.details) {
        resultsDetails.push('');
        resultsDetails.push(`Test - ${val.resultsTestId}`);
        resultsDetails.push(JSON.stringify(val.response.structure));

        for (const error of val.response.errorInput) {
            resultsDetails.push(`Input ${error}`);
        }

        for (const error of val.response.errorOutput) {
            resultsDetails.push(`Output ${error}`);
        }
    }

    const windowResults = window.open("", "_blank");
    windowResults.document.write(resultsDetails.join('<br/>'));
}