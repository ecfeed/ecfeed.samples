import * as formElements from './formElements.js';
import * as formRelations from './formRelations.js';

//------------------------------------------------

const footerDefaultMessage = [
`&nbsp;<br/>&nbsp;`
];

const footerDriverMessage = [
`To use the embedded test execution driver, instead of JUnit5/Selenium, export test cases to the CSV format and click on 'EXECUTE'.
 Note, that during the test execution phase the main panel is not responsive (to stop the process, reload the web page). 
 The default delay between test cases is 1 second.`
];

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
        formElements.country.value = _country.toLowerCase().replace(/[ -]/g, '_');
        formRelations.updateCountry();
        formElements.product.value = _product.toLowerCase().replace(/[ -]/g, '_');
        formRelations.updateProduct();
        formElements.color.value = _color.toLowerCase().replace(/[ -]/g, '_');
        formElements.size.value = _size.toLowerCase().replace(/[ -]/g, '_');
        formElements.quantity.value = _quantity.toLowerCase();
        formElements.payment.value = _payment.toLowerCase().replace(/[ -]/g, '_');
        formElements.delivery.value = _delivery.toLowerCase().replace(/[ -]/g, '_');
        formElements.name.value = _name;
        formElements.address.value = _address;
        formElements.phone.value = _phone;
        formElements.email.value = _email;
}

export const fillFormJSON = (json) => {
        formElements.country.value = json.country.toLowerCase().replace(/[ -]/g, '_');
        formRelations.updateCountry();
        formElements.product.value = json.product.toLowerCase().replace(/[ -]/g, '_');
        formRelations.updateProduct();
        formElements.color.value = json.color.toLowerCase().replace(/[ -]/g, '_');
        formElements.size.value = json.size.toLowerCase().replace(/[ -]/g, '_');
        formElements.quantity.value = json.quantity.toLowerCase();
        formElements.payment.value = json.payment.toLowerCase().replace(/[ -]/g, '_');
        formElements.delivery.value = json.delivery.toLowerCase().replace(/[ -]/g, '_');
        formElements.name.value = json.name;
        formElements.address.value = json.address;
        formElements.phone.value = json.phone;
        formElements.email.value = json.email; 
}

export const updateResponse = (status = '', message = '') => {
    formElements.status.value = ` ${status}`;
    formElements.response.textContent = message;
}

export const updateResponseValid = (status) => {
    formElements.status.classList.add(status ? 'foreground-green' : 'foreground-red');
    formElements.status.classList.remove(status ? 'foreground-red' : 'foreground-green');
}

export const updateFooter = (message) => {
    if (message) {
        formElements.footer_message.innerHTML = message.join('<br/>');
    } else {
        if (formElements.displayDriver) {
            formElements.footer_message.innerHTML = footerDriverMessage.join('<br/>');
        } else {
            formElements.footer_message.innerHTML = footerDefaultMessage.join('<br/>');
        }
    }
    
}

export const updateTime = () => {
    const date = new Date()
    const dateFormatted = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " 
        + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + ":" + date.getMilliseconds();
    formElements.time.value = ` ${dateFormatted}`;
}