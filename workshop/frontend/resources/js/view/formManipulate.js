import * as formElements from './formElements.js';
import * as formRelations from './formRelations.js';

//------------------------------------------------

const footerDefaultMessage = [
`To use the emdedded test execution driver, instead of JUnit5/Selenium, export test cases to the CSV format and click on 'EXECUTE'.
 Note, that during the test execution phase the main panel is not responsive (to stop the process, reload the web page). 
 The default delay between test cases is 1 second.`
]

//------------------------------------------------

export const getValues = () => {

    return ({
        country: formElements.country.value,
        name: formElements.name.value,
        address: formElements.address.value,
        product: formElements.product.value,
        color: formElements.color.value,
        size: formElements.size.value,
        quantity: formElements.quantity.value,
        payment: formElements.payment.value,
        delivery: formElements.delivery.value,
        phone: formElements.phone.value,
        email: formElements.email.value
    })

}

export const clearForm = () => {
        formElements.name.value = "";
        formElements.address.value = "";
        formElements.quantity.value = "";
        formElements.phone.value = "";
        formElements.email.value = "";
}

export const fillForm = (
        _country = 'norway', _name = 'Hanna Larsen', _address = 'Holme 153,\n1675 Kråkerøy',
        _product = 't-shirt', _color = 'white', _size = 'm', _quantity = '1',
        _payment = 'visa', _delivery = 'standard', _phone = '+47123456789', _email = 'h.larsen@data.no'
    ) => {
        formElements.country.value = _country.replace(/[ -]/g, '_');
        formElements.name.value = _name;
        formElements.address.value = _address;
        formElements.product.value = _product.replace(/[ -]/g, '_');
        formElements.color.value = _color.replace(/[ -]/g, '_');
        formElements.size.value = _size.replace(/[ -]/g, '_');
        formElements.quantity.value = _quantity;
        formElements.payment.value = _payment.replace(/[ -]/g, '_');
        formElements.delivery.value = _delivery.replace(/[ -]/g, '_');
        formElements.phone.value = _phone;
        formElements.email.value = _email;
        formRelations.updateCountry();
        formRelations.updateProduct();
}

export const fillFormJSON = (json) => {
        formElements.country.value = json.country.replace(/[ -]/g, '_');
        formElements.name.value = json.name;
        formElements.address.value = json.address;
        formElements.product.value = json.product.replace(/[ -]/g, '_');
        formElements.color.value = json.color.replace(/[ -]/g, '_');
        formElements.size.value = json.size.replace(/[ -]/g, '_');
        formElements.quantity.value = json.quantity;
        formElements.payment.value = json.payment.replace(/[ -]/g, '_');
        formElements.delivery.value = json.delivery.replace(/[ -]/g, '_');
        formElements.phone.value = json.phone;
        formElements.email.value = json.email;
        formRelations.updateCountry();
        formRelations.updateProduct();
}

export const updateResponse = (status = '', message = '') => {
    formElements.status.value = status;
    formElements.response.textContent = message;
}

export const updateFooter = (message = footerDefaultMessage) => {
    formElements.footer_message.innerHTML = message.join('<br/>');
}

export const updateTime = () => {
    const date = new Date()
    const dateFormatted = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " 
        + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + ":" + date.getMilliseconds();
    formElements.time.value = dateFormatted;
}