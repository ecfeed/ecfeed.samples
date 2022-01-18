const TestProvider = require('ecfeed').TestProvider; 
const forEach = require('mocha-each');
const assert = require('assert');
const axios = require('axios');
const {Builder, By, Key, until, Browser} = require('selenium-webdriver');

const testProvider = new TestProvider("6EG2-YL4S-LMAK-Y5VW-VPV9");
const testMethod = 'com.example.test.Demo.typeString';
const webpage = 'http://www.workshop-2021-december.ecfeed.com?mode=error';

let data = [] 
let driver = null;

pageFormInput = [["name", "address", "quantity", "phone", "email"], ["country", "product", "color", "size", "payment", "delivery"]];
pageFormOutput = ["status", "response"];
pageFormControl = ["submit"];

before(async () => {
    for await (const response of testProvider.generateNWise(testMethod, { feedback: true, label:'Selenium', constraints: "ALL"})) { data.push(response) }
    driver = await new Builder().forBrowser('firefox').build();
    await driver.get(webpage);
})

after(async () => {
    await driver.quit();
})

setFormText = async (values) => {

    for (let i = 0 ; i < pageFormInput[0].length ; i++) {
        const element = await driver.findElement(By.id(pageFormInput[0][i]));
        await element.clear();
        await element.sendKeys(values[i]);
    }
}

setFormSelect = async (values) =>  {
    let missingFields = [];

    field:
    for (let i = 0 ; i < pageFormInput[1].length ; i++) {
        const element = await driver.findElement(By.id(pageFormInput[1][i]));
        const options = await element.findElements(By.tagName('option'));

        for (let j = 0 ; j < options.length ; j++) {
            if (await options[j].getText() === values[i]) {
                await options[j].click();
                continue field;
            }
        }

        missingFields.push(`- Missing field: ${values[i]}.`);
    }

    return missingFields;
}

execute = async () => {

    for (let i = 0 ; i < pageFormControl.length ; i++) {
        const element = await driver.findElement(By.id(pageFormControl[i]));
        await element.click();
    }
}

getResponse = async () => {
    let results = [];

    const status = await driver.findElement(By.id(pageFormOutput[0]));
    const statusValue = await status.getAttribute("value")

    if (statusValue.includes('rejected')) {
        const response = await driver.findElement(By.id(pageFormOutput[1]));
        const responseValue = await response.getAttribute("value")

        results.push(responseValue); 
    }

    return results;
}

it('API tests', () => {
    describe('API', async () => {
        forEach(data).it('API', async (country, name, address, product, color, size, quantity, payment, delivery, phone, email, testHandle) => {

            await setFormText([name, address, quantity, phone, email]);
            let inputError = await setFormSelect([country, product, color, size, payment, delivery]);
            await execute();
            let outputError = await getResponse();

            let index = 1;
            let custom = {};

            if (inputError.length > 0) {
                inputError.forEach(e => { custom[`${index++}`] = e.substring(2); })
                testHandle.addFeedback(false, { custom, comment: "Input" });
                assert.fail();
            }

            if (outputError.length > 0) {
                outputError.forEach(e => { custom[`${index++}`] = e.substring(2); })
                testHandle.addFeedback(false, { custom, comment: "Output" });
                assert.fail();
            }

            testHandle.addFeedback(true);
        });
    });
})

