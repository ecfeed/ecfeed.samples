const TestProvider = require('ecfeed').TestProvider; 
const forEach = require('mocha-each');
const assert = require('assert');
const axios = require('axios');

const testProvider = new TestProvider("6EG2-YL4S-LMAK-Y5VW-VPV9");
const fTest = 'com.example.test.Demo.typeString';

const data = [] 

before(async () => {
    for await (const response of testProvider.generateNWise(fTest, { feedback: true, label:'API', constraints: "ALL"})) { data.push(response) }
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

            if (res.data.errorInput.length > 0) {
                res.data.errorInput.forEach(e => { custom[`${index++}`] = e; })
                testHandle.addFeedback(false, { custom, comment: "Input" });
                assert.fail();
            }

            if (res.data.errorOutput.length > 0) {
                res.data.errorOutput.forEach(e => { custom[`${index++}`] = e; })
                testHandle.addFeedback(false, { custom, comment: "Output" });
                assert.fail();
            }

            testHandle.addFeedback(true);
        });
    });
})

