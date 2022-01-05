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
    const parsedProduct = formElements.product.value.toLowerCase();
    
    optionRemove(formElements.color, formElements.color_red, formElements.color_green, formElements.color_blue);

    if (parsedProduct === 't-shirt') {
        optionAdd(formElements.color, formElements.color_red, formElements.color_green, formElements.color_blue);
    } 
}

export const updateCountry = () => {
    const parsedCountry = formElements.country.value.toLowerCase();

    optionRemove(formElements.payment, formElements.payment_cash_on_delivery);
    optionRemove(formElements.delivery, formElements.delivery_express);
    optionRemove(formElements.delivery, formElements.delivery_postnl);

    if (parsedCountry === 'norway' || parsedCountry === 'poland') {
        optionAdd(formElements.payment, formElements.payment_cash_on_delivery);
        optionAdd(formElements.delivery, formElements.delivery_express);
    } 

    if (parsedCountry === 'belgium' || parsedCountry === 'netherlands' || parsedCountry === 'luxembourg') {
        optionAdd(formElements.payment, formElements.payment_cash_on_delivery);
        optionAdd(formElements.delivery, formElements.delivery_express);
        optionAdd(formElements.delivery, formElements.delivery_postnl);
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