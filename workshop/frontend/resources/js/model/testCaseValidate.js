const regexMail = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
const regexPhone = /^[+][0-9]{2}[0-9]{7,12}$/i;
const regexNumber = /^[-]*[0-9]*$/i;

//------------------------------------------------

export const validate = (test, api = false) => {
    const response = {
        errorInput: [],
        errorOutput: [],
        structure: test
    }

    if (api) {
        processAPI(test, response);
        if (response.errorInput.length > 0) {
            return response;
        }
    }
   
    for (const value in test) {
        const check = validateField[value](test[value].trim(), test);
        
        if (check.startsWith('[F]')) { 
            response.errorOutput.push(check.substring(4)) 
        } else if (check.startsWith('[M]')) {
            response.errorInput.push(check.substring(4));
        } 
        
    }

    return response;
}

//------------------------------------------------

const processAPI = (test, response) => {
    
    validateFields(test, response);
    
    if (response.errorInput.length > 0) {
        return;
    } 

    updateFields(test);
}

const updateFields = (test) => {
    const parsedCountry = test.country.trim().toLowerCase();
    const parsedProduct = test.product.trim().toLowerCase().replace(/[ -]/g, '_');
    
    
    if (parsedCountry === 'other') {
        const parsedPayment = test.payment.trim().toLowerCase().replace(/[ -]/g, '_');
        const parsedDelivery = test.delivery.trim().toLowerCase();
        
        if (parsedPayment === 'cash_on_delivery') {
            test.payment = '';
        }
        
        if (parsedDelivery === "express") {
            test.delivery = '';
        }
    }

    if (parsedProduct === 'hoodie') {
        const parsedColor = test.color.trim().toLowerCase();
        
        if (parsedColor === 'red' || parsedColor === 'green' || parsedColor === 'blue') {
            test.color = '';
        }
    }
    
}

const validateFields = (test, response) => {
    const empty = [];

    if (!test.country) empty.push('country');
    if (!test.name) empty.push('name');
    if (!test.address) empty.push('address');

    if (!test.product) empty.push('product');
    if (!test.color) empty.push('color');
    if (!test.size) empty.push('size');
    if (!test.quantity) empty.push('quantity');

    if (!test.payment) empty.push('payment');
    if (!test.delivery) empty.push('delivery');
    if (!test.phone) empty.push('phone');
    if (!test.email) empty.push('email');

    if (empty.length > 0) {
        response.errorInput.push(`The order cannot be processed. Missing field(s): ${empty.join(', ')}.`);
    }

}

//------------------------------------------------

const validateField = {
    country: (country) => {
        const parsedCountry = country.toLowerCase();
        if (parsedCountry === 'norway' || parsedCountry === 'poland' || parsedCountry === 'other') {
            return '';
        } 
        return "[M] The name of the country is incorrect. Please select a value from the list: 'Norway', 'Poland', 'other'.";
    },
    name: (name) => {
        if (!name) {
            return "[F] The name of the client is not defined.";
        }

        if (name.split(" ").length < 2) {
            return "[F] The name of the client must consist of at least two elements: the first name, the second name.";
        }

        return '';
    },
    address: (address) => {
        if (!address) {
            return "[F] The address field cannot be empty.";
        }

        if (address.length === 0) {
            return "[F] No, the address cannot consist of whitespaces only. Please stick to the rules!"
        } else if (address.length < 5) {
            return `[F] For obvious reasons we do not believe that the address is correct. It consists of ${address.length} characters!`;
        }

        return '';
    } ,
    product: (product) => {
        const parsedProduct = product.toLowerCase().replace(/[ -]/g, '_');
        if (parsedProduct === 't_shirt' || parsedProduct === 'hoodie') {
            return '';
        } 
        return "[M] We do not have this product in our offer. Please select a value from the list: 't-shirt', 'hoodie'.";
    },
    color: (color) => {
        const parsedColor = color.toLowerCase();
        if (parsedColor === 'black' || parsedColor === 'white' || parsedColor === 'red' || parsedColor === 'green' || parsedColor === 'blue') {
            return '';
        } 
        return "[M] The selected color is not available for this product.";
    },
    size: (size) => {
        const parsedSize = size.toLowerCase();
        if (parsedSize === 'xs' || parsedSize === 's' || parsedSize === 'm' || parsedSize === 'l' || parsedSize === 'xl') {
            return '';
        } 
        return "[M] We do not have this size in our offer. Please select a value from the list: 'XS', 'S', 'M', 'L', 'XL'.";
    },
    quantity: (quantity) => {
        if (!quantity) {
            return "[F] Please define the quantity.";
        }

        if (!regexNumber.test(quantity)) {
            return `[F] The quantity cannot be '${quantity}'. It is not even a number!`
        }  

        const parsedQuantity = parseInt(quantity);

        if (parsedQuantity == 0) {
            return "[F] No, the quantity should be at least 1."
        } else if (parsedQuantity < 0) {
            return `[F] No, the quantity is incorrect. Do you want to send us ${-parsedQuantity} item(s)?`;
        } else if (parsedQuantity >= 100000) {
            return `[F] No, we do not even know how to deliver ${parsedQuantity} items! Please be reasonable and set the number below 100000!`;
        }

        return '';
    },
    payment: (payment) => {
        const parsedPayment = payment.toLowerCase().replace(/[ -]/g, '_');
        if (parsedPayment === 'visa' || parsedPayment === 'mastercard' || parsedPayment === 'bank_transfer' || parsedPayment === 'cash_on_delivery') {
            return '';
        } 
        return "[M] The payment method is not supported. Please select a value from the list: 'VISA', 'MASTERCARD', 'bank transfer', 'cash on delivery'.";
    },
    delivery: (delivery) => {
        const parsedDelivery = delivery.toLowerCase();
        if (parsedDelivery === 'standard' || parsedDelivery === 'express') {
            return '';
        } 
        return "[M] The delivery option is not supported. Please select a value from the list: 'standard', 'express'.";
    },
    phone: (phone, test) => {
        if (!phone) {
            return "[F] Please define the phone number.";
        }

        if (!phone.startsWith('+')) {
            return "[F] The phone number is invalid. It must start with a country prefix, i.e. '+'."
        }

        if (!regexPhone.test(phone)) {
            return `[F] Sorry, the phone number incorrect correct '${phone}'. Please correct it and try again.`
        }  

        switch (test.country) {
            case 'norway':
                if (!phone.startsWith('+47')) {
                    return "[F] The prefix to Norway must be '+47'. Please correct the value."
                }
                break;
            case 'poland': {
                if (!phone.startsWith('+48')) {
                    return "[F] The prefix to Poland must be '+48'. Please correct the value."
                }
                break;
            }
        }

        return '';
    },
    email: (email) => {
        if (!email) {
            return "[F] Please define the email address.";
        }

        if (!regexMail.test(email)) {
            return `[F] Sorry, there is something wrong with the email '${email}'. Please correct it and try again.`
        }    

        return '';
    } 
}

//------------------------------------------------
