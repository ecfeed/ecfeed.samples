const { test } = require('@playwright/test');
const { EcFeed } = require('../page-objects/EcFeed');

test.describe('EcFeed @json', () => {

    require("../fixtures/source.json").testCases.forEach(async (element, index) => {

        test(`should process an order ${index}`, async ({ page }) => {
            const ecFeed = new EcFeed(page, element);

            await ecFeed.open();
            await ecFeed.fill();
            await ecFeed.send();
            await ecFeed.validate();

            // await page.pause();
        });
    })
})

