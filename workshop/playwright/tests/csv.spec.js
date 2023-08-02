import { test } from "@playwright/test";
import { parseCSV } from "../helpers/parser";
import { EcFeed } from "../page-objects/EcFeed";

test.describe('EcFeed @csv', () => {

    parseCSV().forEach(async (element, index) => {

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
