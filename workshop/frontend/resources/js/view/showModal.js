import * as formElements from './formElements.js';
import * as internalDriver from './testDriver.js';

//------------------------------------------------

export const showModalError = (message) => {
    formElements.modal_message.textContent = message;

    $('#modal-error').modal({
        closeExisting: true,
        escapeClose: false,
        clickClose: false,
    })
}

export const showModalResults = () => {
    const resultsTestSuite = internalDriver.getResultsTestSuite();

    formElements.modal_status_success.textContent = resultsTestSuite.success;
    formElements.modal_status_success_id.value = resultsTestSuite.successId.join(',');
    formElements.modal_results_failure_input.textContent = resultsTestSuite.failureInput;
    formElements.modal_results_failure_input_id.value = resultsTestSuite.failureInputId.join(',');
    formElements.modal_results_failure_output.textContent = resultsTestSuite.failureOutput;
    formElements.modal_results_failure_output_id.value = resultsTestSuite.failureOutputId.join(',');

    $('#modal-result').modal({
        closeExisting: true,
        escapeClose: false,
        clickClose: false,
    })

}