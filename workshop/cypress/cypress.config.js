const webpackPreprocessor = require('@cypress/webpack-preprocessor');
const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    projectId: "wuzirn",
    baseUrl: 'http://www.workshop-2021-december.ecfeed.com?mode=error',
    viewportWidth: 1024,
    viewportHeight: 910,
    supportFile: false,
    setupNodeEvents(on, config) {
      const webpackDefaults = webpackPreprocessor.defaultOptions;
      webpackDefaults.webpackOptions.module.rules.push({
        test: /\.csv$/,
        loader: 'csv-loader',
        options: {
          dynamicTyping: true,
          header: true,
          skipEmptyLines: true,
          delimiter: ',',
        },
      });
      on('file:preprocessor', webpackPreprocessor(webpackDefaults));
    },
  },
});
