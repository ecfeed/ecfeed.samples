import * as validate from '../frontend/resources/js/model/testCaseValidate.js';
const express = require('express');
const url = require('url');

const app = express();
const port = 55555;

app.use(express.json());

app.get('/*', (req, res) => {
    return res.send('Available!');
});

//---------------------------------------------------------------------------------------------------------

app.post('/fsm/auth/basic', (req, res) => {
    const error = authBasic(req.get('Authorization'));

    if (error !== undefined) {
        res.status(401);
        return res.send(error);
    }

    return res.send('OK');
});

app.post('/fsm/auth/token', (req, res) => {
    const error = authToken(req.get('Authorization'));

    if (error !== undefined) {
        res.status(401);
        return res.send(error);
    }

    return res.send('OK');
});

function authBasic(header) {
    const elements = header.split(" ");

    if (elements.length != 2) {
        return `The basic auth header should be composed of two elements, i.e. the keyword 'Basic' and the payload (base64 encoded).`;
    }

    const credPair = Buffer.from(elements[1], 'base64').toString().split(':');

    if (credPair.length != 2) {
        return `The basic auth payload should contain two elements separated by a colon, i.e. 'user' and 'password'`;
    }

    if (credPair[0] !== 'admin' || credPair[1] !== 'secret') {
        return `Authorization failed (basic).`;
    }
};

function authToken(header) {
    if (header === undefined) {
        return `The authorization header is not provided.`;
    }

    const elements = header.split(" ");

    if (elements.length != 2) {
        return `The token token auth header should be composed of two elements, i.e. the keyword 'Bearer' and the payload.`;
    }

    if (elements[1] !== 'qwerty') {
        return `Authorization failed (bearer token).`;
    }
};

//---------------------------------------------------------------------------------------------------------

app.post('/fsm/m1/success', (req, res) => {
    const params = url.parse(req.url, true);
    const problems = valInputM1(params);

    if (problems.length > 0) {
        res.status(400);

        return res.send(problems);
    }

    return res.send('OK');
});

app.post('/fsm/m1/failure', (req, res) => {
    const params = url.parse(req.url, true);
    const problems = valInputM1(params);

    if (problems.length > 0) {
        res.status(400);

        return res.send(problems);
    }

    if (params.query.p1 === '1' && params.query.p5 === '1') {
        res.status(500);

        return res.send('BROKEN');
    }

    return res.send('OK');
});

app.post('/fsm/m2/success', (req, res) => {
    const params = url.parse(req.url, true);
    const problems = valInputM2(params);

    if (problems.length > 0) {
        res.status(400);

        return res.send(problems);
    }

    return res.send('OK');
});

app.post('/fsm/m2/failure', (req, res) => {
    const params = url.parse(req.url, true);
    const problems = valInputM2(params);

    if (problems.length > 0) {
        res.status(400);

        return res.send(problems);
    }

    if (params.query.p1 === "KRZYSZTOF") {
        res.status(500);

        return res.send('BROKEN');
    }

    return res.send('OK');
});

function valInputM1(params) {
    const problems = [];

    if (Object.keys(params.query).length != 5) {
        problems.push(`The query should consist of exactly 5 parameters, i.e. 'p1', 'p2', 'p3', 'p4', 'p5'.`);
    }

    valInputParam(problems, params, 'p1');
    valInputParam(problems, params, 'p2');
    valInputParam(problems, params, 'p3');
    valInputParam(problems, params, 'p4');
    valInputParam(problems, params, 'p5');

    if (problems.length > 0) {
        return;
    }

    valInputParamInt(problems, params, 'p1');
    valInputParamInt(problems, params, 'p2');
    valInputParamInt(problems, params, 'p3');
    valInputParamInt(problems, params, 'p4');
    valInputParamInt(problems, params, 'p5');

    return problems;
};

function valInputM2(params) {
    const problems = [];

    if (Object.keys(params.query).length != 2) {
        problems.push(`The query should consist of exactly 2 parameters, i.e. 'p1', 'p2'.`);
    }

    valInputParam(problems, params, 'p1');
    valInputParam(problems, params, 'p2');

    return problems;
};

function valInputParam(problems, params, name) {

    if (params.query[name] === undefined) {
        problems.push(`The parameter '${name}' is missing.`);
    }
};

function valInputParamInt(problems, params, name) {

    if (Number.isNaN(Number(params.query[name]))) {
        problems.push(`The parameter '${name}' cannot be parsed to integer.`);
    }
}

//---------------------------------------------------------------------------------------------------------

app.post('/*', (req, res) => {
    const mode = url.parse(req.url, true).query.mode;
    if (mode) {
        delete req.query.mode;
        return res.send(validate.validate(req.query, mode, true));
    } else {
        return res.send(validate.validate(req.query, 'standard', true));
    }

});

app.listen(port, () => console.log(`Application listening on port ${port}!\n`));
