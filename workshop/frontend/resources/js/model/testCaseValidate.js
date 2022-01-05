const regexMail = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
const regexPhone = /^[+][0-9]{2}[0-9]{7,12}$/i;
const regexNumber = /^[-]*[0-9]*$/i;

//------------------------------------------------

export const validate = (test, mode = 'standard', api = false) => {
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
        validateField(test, response)[value](test[value].trim());
    }

    if (mode === 'error') {
        processErrorMode(test, response);
    }

    return response;
}

//------------------------------------------------

const processAPI = (test, response) => {
    
    checkConsistency(test, response);
    
    if (response.errorInput.length > 0) {
        return;
    } 

    validateInputErrors(test);
}

const checkConsistency = (test, response) => {
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

const validateInputErrors = (test) => {
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

//------------------------------------------------

const processErrorMode = (test, response) => {
    breakRelationProductColor(test, response);
}

const breakRelationProductColor = (test, response) => {
    const parsedColor = test.color.trim().toLowerCase();
    const parsedSize = test.size.toLowerCase();

    if (parsedColor === 'green' && parsedSize === 'xl') {
        response.errorOutput.push('@#$#@Nope$/7');
    }
}

//------------------------------------------------

const validateField = (test, response) => {
    

    return {
        country: (country) => {
            const parsedCountry = country.toLowerCase();

            if (parsedCountry === 'norway' || parsedCountry === 'poland' || parsedCountry === 'belgium' || 
                parsedCountry === 'netherlands' || parsedCountry === 'luxembourg' || parsedCountry === 'other') {
                return;
            } 

            response.errorInput.push("The name of the country is incorrect. Please select a value from the list: 'Norway', 'Poland', 'Belgium', 'Netherlands', 'Luxembourg', 'other'.");
        },
        name: (name) => {

            if (!name) {
                response.errorOutput.push("The name of the client is not defined.");
                return;
            }

            if (name.split(" ").length < 2) {
                response.errorOutput.push("The name of the client must consist of at least two elements: the first name, the second name.");
            }

        },
        address: (address) => {

            if (!address) {
                response.errorOutput.push("The address field cannot be empty.");
                return;
            }

            if (address.length === 0) {
                response.errorOutput.push("No, the address cannot consist of whitespaces only. Please stick to the rules!");
            } else if (address.length < 5) {
                response.errorOutput.push(`For obvious reasons we do not believe that the address is correct. It consists of ${address.length} characters!`);
            }

        } ,
        product: (product) => {
            const parsedProduct = product.toLowerCase().replace(/[ -]/g, '_');

            if (parsedProduct === 't_shirt' || parsedProduct === 'hoodie') {
                return;
            } 

            response.errorInput.push("We do not have this product in our offer. Please select a value from the list: 't-shirt', 'hoodie'.");
        },
        color: (color) => {
            const parsedColor = color.toLowerCase();

            if (parsedColor === 'black' || parsedColor === 'white' || parsedColor === 'red' || parsedColor === 'green' || parsedColor === 'blue') {
                return;
            } 

            response.errorInput.push("The selected color is not available for this product.");
        },
        size: (size) => {
            const parsedSize = size.toLowerCase();

            if (parsedSize === 'xs' || parsedSize === 's' || parsedSize === 'm' || parsedSize === 'l' || parsedSize === 'xl') {
                return;
            } 

            response.errorInput.push("We do not have this size in our offer. Please select a value from the list: 'XS', 'S', 'M', 'L', 'XL'.");
        },
        quantity: (quantity) => {

            if (!quantity) {
                response.errorOutput.push("Please define the quantity.");
                return;
            }

            if (!regexNumber.test(quantity)) {
                response.errorOutput.push(`The quantity cannot be '${quantity}'. It is not even a number!`);
                return;
            }  

            const parsedQuantity = parseInt(quantity);

            if (parsedQuantity == 0) {
                response.errorOutput.push("No, the quantity should be at least 1.");
            } else if (parsedQuantity < 0) {
                response.errorOutput.push(`No, the quantity is incorrect. Do you want to send us ${-parsedQuantity} item(s)?`);
            } else if (parsedQuantity >= 100000) {
                response.errorOutput.push(`No, we do not even know how to deliver ${parsedQuantity} items! Please be reasonable and set the number below 100000!`);
            }
        },
        payment: (payment) => {
            const parsedPayment = payment.toLowerCase().replace(/[ -]/g, '_');

            if (parsedPayment === 'visa' || parsedPayment === 'mastercard' || parsedPayment === 'bank_transfer' || parsedPayment === 'cash_on_delivery') {
                return;

            } 
            response.errorInput.push("The payment method is not supported. Please select a value from the list: 'VISA', 'MASTERCARD', 'bank transfer', 'cash on delivery'.");
        },
        delivery: (delivery) => {
            const parsedDelivery = delivery.toLowerCase();
            const parsedCountry = test.country.toLowerCase();

            if (parsedDelivery === 'standard' || parsedDelivery === 'express' || parsedDelivery === 'postnl') {
                return;
            } 

            if (parsedDelivery === 'postnl') {
                if (parsedCountry !== 'belgium' && parsedCountry !== 'netherlands' || parsedCountry !== 'luxembourg') {
                    response.errorInput.push("The requested delivery option is not available in the selected country.")
                }
            }

            response.errorInput.push("The delivery option is not supported. Please select a value from the list: 'standard', 'express'.");
        },
        phone: (phone) => {

            if (!phone) {
                response.errorOutput.push("Please define the phone number.");
                return;
            }

            if (!phone.startsWith('+')) {
                response.errorOutput.push("The phone number is invalid. It must start with a country prefix, i.e. '+'.");
                return;
            }

            if (!regexPhone.test(phone)) {
                response.errorOutput.push(`Sorry, the phone number incorrect correct '${phone}'. Please correct it and try again.`);
                return;
            }  

            switch (test.country.toLowerCase()) {
                case 'norway':
                    if (!phone.startsWith('+47')) {
                        response.errorOutput.push("The prefix to Norway must be '+47'. Please correct the value.");
                    }
                    break;
                case 'poland': {
                    if (!phone.startsWith('+48')) {
                        response.errorOutput.push("The prefix to Poland must be '+48'. Please correct the value.");
                    }
                    break;
                }
            }
        },
        email: (email) => {
            
            if (!email) {
                response.errorOutput.push("Please define the email address.");
                return;
            }

            if (!regexMail.test(email)) {
                response.errorOutput.push(`Sorry, there is something wrong with the email '${email}'. Please correct it and try again.`);
            }    
        } 
    }
}

//------------------------------------------------
