const webpackPreprocessor = require('@cypress/webpack-preprocessor')
const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
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
