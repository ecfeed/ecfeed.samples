import * as validate from '../frontend/resources/js/model/testCaseValidate.js';
const express = require('express');

const app = express();
const port = 55555;

app.use(express.json());

app.get('/*', (req, res) => {
    return res.send('Available!');
});

app.post('/*', (req, res) => {
    return res.send(validate.validate(req.query, true));
});

app.listen(port, () => console.log(`Application listening on port ${port}!\n`));
