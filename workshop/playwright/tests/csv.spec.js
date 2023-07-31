const  fs = require( 'fs');
const path = require( 'path');
const { test } = require( '@playwright/test');
const { parse } = require( 'csv-parse/sync');

const { EcFeed } = require('../page-objects/EcFeed');

const source = parse(fs.readFileSync(path.join(__dirname, "../fixtures/source.csv")), {
  columns: true,
  skip_empty_lines: true
});

test.describe('EcFeed @csv', () => {

    source.forEach(async (element, index) => {

        test(`should process an order ${index}`, async ({ page }) => {
            const ecFeed = new EcFeed(page, element);

            await ecFeed.open();
            await ecFeed.fill();
            await ecFeed.send();
            await ecFeed.validate();

            // await page.pause();
        });
    });
});
