import * as fileManipulate from './model/fileManipulate.js';
import * as testCaseValidate from './model/testCaseValidate.js';
import * as internalDriver from './view/testDriver.js';
import * as formRelations from './view/formRelations.js';
import * as formElements from './view/formElements.js';
import * as formManipulate from './view/formManipulate.js';
import * as formEffects from './view/formEffects.js';
import * as showWindow from './view/showWindow.js';

formElements.submit.addEventListener('click', () => {
    formManipulate.updateTime();
    internalDriver.processTestCase(testCaseValidate.validate(formManipulate.getValues(), formElements.errorMode ? 'error' : 'standard'));
})

formElements.product.addEventListener('input', () => {
    formRelations.updateProduct();
})

formElements.country.addEventListener('change', () => {
   formRelations.updateCountry();
})

formElements.submenu_complete.addEventListener('click', () => {
    formManipulate.fillForm();
});

formElements.submenu_clear.addEventListener('click', () => {
    formManipulate.clearForm();
});

formElements.submenu_test.addEventListener('click', () => {
    formElements.submenu_test_file.click();
})

formElements.execution_details.addEventListener('click', () => {
    showWindow.results();
})

$('#modal-error').on($.modal.BEFORE_OPEN, () => {
    formEffects.backgroundBlur(true);
});

$('#modal-result').on($.modal.BEFORE_OPEN, () => {
    formEffects.backgroundBlur(true);
});

$('#modal-error').on($.modal.AFTER_CLOSE, () => {
    formEffects.backgroundBlur(false);
});

$('#modal-result').on($.modal.AFTER_CLOSE, () => {
    formEffects.backgroundBlur(false);
    formEffects.backgroundGrayscale(false);
    internalDriver.setResultsTestMaxId(0);
    formManipulate.updateFooter();
});

formElements.submenu_test_file.addEventListener('change', event => {
   const reader = fileManipulate.fileRead(event.target.files[0]);

   reader.onload = readerEvent => {
        const testSuite = fileManipulate.fileParse(readerEvent.target.result.split('\n'));
        internalDriver.processTestSuite(testSuite);
    }
})

formRelations.initialSetup();
formManipulate.updateFooter();