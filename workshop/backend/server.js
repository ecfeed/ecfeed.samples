import * as validate from '../frontend/resources/js/model/testCaseValidate.js';
const express = require('express');

const app = express();
const port = 55555;

let present = function(req) {
    console.log(new Date());
    console.log(`query: ${JSON.stringify(req.query)}\nparameter: ${JSON.stringify(req.params)}\nbody: ${JSON.stringify(req.body)}`);
    console.log('\n');
}

app.use(express.json());

app.get('/*', (req, res) => {
    present(req);
    return res.send('Available');
});

app.post('/*', (req, res) => {
    return res.send(validate.validate(req.query, true));
});

app.listen(port, () => console.log(`Application listening on port ${port}!\n`));
