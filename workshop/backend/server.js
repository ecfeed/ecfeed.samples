import * as validate from '../frontend/resources/js/model/testCaseValidate.js';
const express = require('express');
const url = require('url');

const app = express();
const port = 55555;

app.use(express.json());

app.get('/*', (req, res) => {
    return res.send('Available!');
});

app.post('/*', (req, res) => {
    const mode = url.parse(req.url, true).query.mode;console.log(mode)
    return res.send(validate.validate(req.query, mode ? mode : 'standard', true));
});

app.listen(port, () => console.log(`Application listening on port ${port}!\n`));
