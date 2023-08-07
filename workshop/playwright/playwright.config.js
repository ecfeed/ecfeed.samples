const { defineConfig, devices } = require('@playwright/test');

module.exports = defineConfig({
  testDir: './tests',
  fullyParallel: false,
  forbidOnly: false,
  retries: 1,
  workers: 1,
  reporter: 'html',
  
  use: {
    baseURL: 'http://www.workshop-2021-december.ecfeed.com',
    trace: 'on-first-retry',
    video: 'on-first-retry',
    screenshot: 'only-on-failure',
  },

  expect: {
    timeout: 4000
  },

  projects: [
    // {
    //   name: 'chromium',
    //   use: { 
    //     ...devices['Desktop Chrome'],
    //     viewport: {
    //       width: 1536,
    //       height: 900
    //     }
    //   }
    // },

    {
      name: 'firefox',
      use: { 
        ...devices['Desktop Firefox'],
        viewport: {
          width: 1536,
          height: 900
        }
      }
    },

    // {
    //   name: 'webkit',
    //   use: { ...devices['Desktop Safari'] },
    // },

    /* Test against mobile viewports. */
    // {
    //   name: 'Mobile Chrome',
    //   use: { ...devices['Pixel 5'] },
    // },

    // {
    //   name: 'Mobile Safari',
    //   use: { ...devices['iPhone 12'] },
    // },

    /* Test against branded browsers. */
    // {
    //   name: 'Microsoft Edge',
    //   use: { ...devices['Desktop Edge'], channel: 'msedge' },
    // },

    // {
    //   name: 'Google Chrome',
    //   use: { ...devices['Desktop Chrome'], channel: 'chrome' },
    // },
  ],
});
