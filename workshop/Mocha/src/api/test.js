const TestProvider = require('ecfeed').TestProvider; 
const forEach = require('mocha-each');
const assert = require('assert');
const axios = require('axios');

const testProvider = new TestProvider("6EG2-YL4S-LMAK-Y5VW-VPV9");
const fTest = 'com.example.test.Demo.typeString';

const data = [] 

before(async () => {
    for await (const response of testProvider.generateNWise(fTest, { feedback: true, label:'API', constraints: "NONE"})) { data.push(response) }
})

it('API tests', () => {
    describe('API', async () => {
        forEach(data).it('API', async (country, name, address, product, color, size, quantity, payment, delivery, phone, email, testHandle) => { 

            let params = {
                mode: 'error',
                country, name, address, product, color, size, quantity, payment, delivery, phone, email
            }

            let res = await axios.post('https://api.ecfeed.com', {}, { params });

            let index = 1;
            let custom = {};

            res.data.errorOutput.forEach(e => { custom[`${index++} / Output`] = e; })
            res.data.errorInput.forEach(e => { custom[`${index++} / Input`] = e; })
           
            if (Object.keys(custom).length > 0) {
                let comment = res.data.errorOutput.length > 0 && res.data.errorInput.length > 0 ? "Input/Output" : res.data.errorOutput.length > 0 ? "Output" : "Input";
                testHandle.addFeedback(false, { custom, comment });
                assert.fail();
            }

            testHandle.addFeedback(true);
        });
    });
})

