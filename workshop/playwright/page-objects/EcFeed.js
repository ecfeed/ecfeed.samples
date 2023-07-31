const { expect } = require('@playwright/test');

export class EcFeed {
    constructor(page, source) {
        this.page = page;
        this.source = source;
    }

    open = async () => {
        await this.page.goto('?mode=error');
    }

    fill = async () => {
        
        await this.type('Quantity');
        await this.type('Name');
        await this.type('Address');
        await this.type('Phone');
        await this.type('Email');

        await this.select('Product');
        await this.select('Color');
        await this.select('Size');
        await this.select('Country');
        await this.select('Payment');
        await this.select('Delivery');
    }

    type = async (name) => {
        const element = this.page.locator(`#${name}`.toLowerCase());

        await element.waitFor();
        await element.fill(this.source[name]);
    }

    select = async (name) => {
        const element = this.page.locator(`#${name}`.toLowerCase());

        await element.waitFor();
        await element.selectOption(this.source[name]);
    }

    send = async () => {
        const element = this.page.locator('#submit');

        await element.waitFor();
        await element.click();
    }

    validate = async () => {
        const element = this.page.locator('#response');

        await element.waitFor();

        await expect(element).toHaveValue(/^Order processed without errors/, { timeout: 10000 });
    }
}