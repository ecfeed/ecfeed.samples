const regexMail = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
const regexPhone = /^[+][0-9]{2}[0-9]{7,12}$/i;
const regexNumber = /^[-]*[0-9]*$/i;

//------------------------------------------------

export const validate = (test) => {
    const response = {
        errorInput: [],
        errorOutput: [],
        structure: test
    }

    for (const value in test) {
        const check = validateField[value](test[value].trim().toLowerCase(), test);
        
        if (check.startsWith('[F]')) { 
            response.errorOutput.push(check.substring(4)) 
        } else if (check.startsWith('[M]')) {
            response.errorInput.push(check.substring(4));
        } 
        
    }

    return response;
}

//------------------------------------------------

const validateField = {
    country: (country) => {
        if (country === 'norway' || country === 'poland' || country === 'other') {
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
        if (product === 't_shirt' || product === 'hoodie') {
            return '';
        } 
        return "[M] We do not have this product in our offer. Please select a value from the list: 'tshirt', 'hoodie'.";
    },
    color: (color) => {
        if (color === 'black' || color === 'white' || color === 'red' || color === 'green' || color === 'blue') {
            return '';
        } 
        return "[M] The selected color is not available for this product.";
    },
    size: (size) => {
        if (size === 'xs' || size === 's' || size === 'm' || size === 'l' || size === 'xl') {
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

        const quantityParsed = parseInt(quantity);

        if (quantityParsed == 0) {
            return "[F] No, the quantity should be at least 1."
        } else if (quantityParsed < 0) {
            return `[F] No, the quantity is incorrect. Do you want to send us ${-quantityParsed} item(s)?`;
        } else if (quantityParsed >= 100000) {
            return `[F] No, we do not even know how to deliver ${quantityParsed} items! Please be reasonable and set the number below 100000!`;
        }

        return '';
    },
    payment: (payment) => {
        if (payment === 'visa' || payment === 'mastercard' || payment.replace(/ /g, '_') === 'bank_transfer' || payment.replace(/ /g, '_') === 'cash_on_delivery') {
            return '';
        } 
        return "[M] The payment method is not supported. Please select a value from the list: 'VISA', 'MASTERCARD', 'bank transfer', 'cash on delivery'.";
    },
    delivery: (delivery) => {
        if (delivery === 'standard' || delivery === 'express') {
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
