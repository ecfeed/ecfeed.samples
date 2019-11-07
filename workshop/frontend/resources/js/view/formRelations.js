import * as formElements from './formElements.js';

//------------------------------------------------

export const initialSetup = () => {
    optionAdd(formElements.color, formElements.color_red, formElements.color_green, formElements.color_blue);
    optionAdd(formElements.payment, formElements.payment_cash_on_delivery);
    optionAdd(formElements.delivery, formElements.delivery_express);

    if (formElements.displayDriver) {
        document.getElementById('driver-buttons').classList.remove('hidden');
    }
}

export const updateProduct = () => {
    if (formElements.product.value.toLowerCase() === 'hoodie') {
        optionRemove(formElements.color, formElements.color_red, formElements.color_green, formElements.color_blue);
    } else {
        optionAdd(formElements.color, formElements.color_red, formElements.color_green, formElements.color_blue);
    } 
}

export const updateCountry = () => {
    if (formElements.country.value.toLowerCase() === 'other') {
        optionRemove(formElements.payment, formElements.payment_cash_on_delivery);
        optionRemove(formElements.delivery, formElements.delivery_express);
    } else {
        optionAdd(formElements.payment, formElements.payment_cash_on_delivery);
        optionAdd(formElements.delivery, formElements.delivery_express);
    } 
}

//------------------------------------------------

const optionAdd = (parameter, ...values) => {
    values.forEach( (value) => {
        if (!document.body.contains(value)) {
            parameter.insertAdjacentElement('beforeend', value);
        }
    })
}

const optionRemove = (parameter, ...values)  => {
    values.forEach( (value) => {
        if (document.body.contains(value)) {
            parameter.removeChild(value);
        }
    })
}