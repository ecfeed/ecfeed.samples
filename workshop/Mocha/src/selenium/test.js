const TestProvider = require('../../../TestProvider').TestProvider;
const forEach = require('mocha-each');
const assert = require('assert');

const data = {}    

const testProvider = new TestProvider("IMHL-K0DU-2U0I-J532-25J9");
const f10x10 = 'com.example.test.Playground.size_10x10';
const f100x2 = 'com.example.test.Playground.size_100x2';
const fTest = 'QuickStart.test';

before(async () => {
    await assemble("10x10 / Random / Quantity - Single", testProvider.generateRandom(f10x10, { feedback:true, length:1, label:'Random / Quantity - Single'}));
    await assemble("10x10 / Random / Quantity - Short", testProvider.generateRandom(f10x10, { feedback:true, length:(100 + Math.floor(Math.random() * 400)), label:'Random / Quantity - Short'}));
    await assemble("10x10 / Random / Quantity - Long", testProvider.generateRandom(f10x10, { feedback:true, length:(1000 + Math.floor(Math.random() * 4000)), label:'Random / Quantity - Long'}));
    await assemble("10x10 / Random / Custom", testProvider.generateRandom(f10x10, { feedback:true, length:1, custom:{ key1: 'value1', key2: 'value2' }, label:'Random / Custom'}));
    await assemble("10x10 / NWise", testProvider.generateNWise(f10x10, { feedback:true, label:'NWise'}));

    await assemble("100x2 / Random / Quantity - Single", testProvider.generateRandom(f100x2, { feedback:true, length:1, label:'Random / Quantity - Single'}));
    await assemble("100x2 / Random / Quantity - Short", testProvider.generateRandom(f100x2, { feedback:true, length:(100 + Math.floor(Math.random() * 400)), label:'Random / Quantity - Short'}));
    await assemble("100x2 / Random / Quantity - Long", testProvider.generateRandom(f100x2, { feedback:true, length:(1000 + Math.floor(Math.random() * 4000)), label:'Random / Quantity - Long'}));
    await assemble("100x2 / Random / Custom", testProvider.generateRandom(f100x2, { feedback: true,length:1, custom:{ key1: 'value1', key2: 'value2' }, label:'Random / Custom'}));
    await assemble("100x2 / NWise", testProvider.generateNWise(f100x2, { feedback:true, label:'NWise'}));

    await assemble("fTest / Random / Quantity - Single", testProvider.generateRandom(fTest, { feedback:true, length:1, label:'Random / Quantity - Single'}));
    await assemble("fTest / Random / Quantity - Short", testProvider.generateRandom(fTest, { feedback:true, length:(100 + Math.floor(Math.random() * 400)), label:'Random / Quantity - Short'}));
    await assemble("fTest / Random / Quantity - Long", testProvider.generateRandom(fTest, { feedback:true, length:(1000 + Math.floor(Math.random() * 4000)), label:'Random / Quantity - Long'}));
    await assemble("fTest / Random", testProvider.generateRandom(fTest, { feedback:true, label:'Random'}));
    await assemble("fTest / Random - Adaptive", testProvider.generateRandom(fTest, { feedback:true, length:10, adaptive:false, label:'Random - Adaptive'}));
    await assemble("fTest / Random - Duplicates", testProvider.generateRandom(fTest, { feedback:true, length:10, duplicates:true, label:'Random - Duplicates'}));
    await assemble("fTest / NWise", testProvider.generateNWise(fTest, { feedback:true, label:'NWise'}));
    await assemble("fTest / NWise - N", testProvider.generateNWise(fTest, { feedback:true, n:3, label:'NWise - N'}));
    await assemble("fTest / NWise - Coverage", testProvider.generateNWise(fTest, { feedback:true, coverage:50, label:'NWise - Coverage'}));
    await assemble("fTest / NWise / Constraints - None", testProvider.generateNWise(fTest, { feedback:true, constraints:"NONE", label:'NWise / Constraints - None'}));
    await assemble("fTest / NWise / Constraints - Selected", testProvider.generateNWise(fTest, { feedback:true, constraints:['constraint1', 'constraint2'], label:'NWise / Constraints - Selected'}));
    await assemble("fTest / NWise / Choices - Selected", testProvider.generateNWise(fTest, { feedback:true, choices:{arg1: ['choice1', 'choice2'], arg2: ['choice2', 'choice3']}, label:'NWise / Choices - Selected'}));
    await assemble("fTest / Cartesian", testProvider.generateCartesian(fTest, { feedback:true, label:'Cartesian'}));
    await assemble("fTest / Static", testProvider.generateStatic(fTest, { feedback:true, label:'Static'}));
    await assemble("fTest / Static - All", testProvider.generateStatic(fTest, { feedback:true, testSuites:"ALL", label:'Static - All'}));
    await assemble("fTest / Static - Selected", testProvider.generateStatic(fTest, { feedback:true, testSuites:['suite1'], label:'Static - Selected'}));
})

it('Mocha tests', () => {
    describe('f10x10', async () => {
        forEach(data['10x10 / Random / Quantity - Single']).it("Random / Quantity - Single", (a, b, c, d, e, f, g, h, i, j, testHandle) => { validateF10x10(a, b, c, d, e, f, g, h, i, j, testHandle) });
        forEach(data['10x10 / Random / Quantity - Short']).it("Random / Quantity - Short", (a, b, c, d, e, f, g, h, i, j, testHandle) => { validateF10x10(a, b, c, d, e, f, g, h, i, j, testHandle) });
        forEach(data['10x10 / Random / Quantity - Long']).it("Random / Quantity - Long", (a, b, c, d, e, f, g, h, i, j, testHandle) => { validateF10x10(a, b, c, d, e, f, g, h, i, j, testHandle) });
        forEach(data['10x10 / Random / Custom']).it("Random / Custom", (a, b, c, d, e, f, g, h, i, j, testHandle) => { validateF10x10(a, b, c, d, e, f, g, h, i, j, testHandle) });
        forEach(data['10x10 / NWise']).it("NWise", (a, b, c, d, e, f, g, h, i, j, testHandle) => { validateF10x10(a, b, c, d, e, f, g, h, i, j, testHandle) });
    });
    describe('f100x2', async () => {
        forEach(data['100x2 / Random / Quantity - Single']).it("Random / Quantity - Single", (a, b, testHandle) => { validateF100x2(a, b, testHandle) });
        forEach(data['100x2 / Random / Quantity - Short']).it("Random / Quantity - Short", (a, b, testHandle) => { validateF100x2(a, b, testHandle) });
        forEach(data['100x2 / Random / Quantity - Long']).it("Random / Quantity - Long", (a, b, testHandle) => { validateF100x2(a, b, testHandle) });
        forEach(data['100x2 / Random / Custom']).it("Random / Custom", (a, b, testHandle) => { validateF100x2(a, b, testHandle) });
        forEach(data['100x2 / NWise']).it("NWise", (a, b, testHandle) => { validateF100x2(a, b, testHandle) });
    });
    describe('fTest', async () => {
        forEach(data['fTest / Random / Quantity - Single']).it("Quantity - Single", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Random / Quantity - Short']).it("Quantity - Short", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Random / Quantity - Long']).it("Quantity - Long", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Random']).it("Random", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Random - Adaptive']).it("Random - Adaptive", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Random - Duplicates']).it("Random - Duplicates", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / NWise']).it("NWise", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / NWise - N']).it("NWise - N", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / NWise - Coverage']).it("NWise - Coverage", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / NWise / Constraints - None']).it("NWise / Constraints - None", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / NWise / Constraints - Selected']).it("NWise / Constraints - Selected", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / NWise / Choices - Selected']).it("NWise / Choices - Selected", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Cartesian']).it("Cartesian", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Static']).it("Static", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Static - All']).it("Static - All", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
        forEach(data['fTest / Static - Selected']).it("Static - Selected", (arg1, arg2, arg3, testHandle) => { validateFTest(arg1, arg2, arg3, testHandle) });
    })
})

async function assemble(name, generator) {
    data[name] = [];

    for await (const response of generator) { data[name].push(response) }
}

function validateF10x10(a, b, c, d, e, f, g, h, i, j, testHandle) {
    let result = [];

    if (a === 'a0') {
        result.push("Incorrect A");
    }

    if (b === 'b1') {
        result.push("Incorrect B");
    }

    if (h === 'h6') {
        result.push("Incorrect H");
    }

    if (result.length > 0) {
        testHandle.addFeedback(false, { duration: getDuration(), comment: result.join(", "), custom: { Severity: `${ result.length }` } });
        assert.fail();
    } else {
        testHandle.addFeedback(true, { duration: getDuration() });
    }
}

function validateF100x2(a, b, testHandle) {
    let result = [];

    if (a === 'a00') {
        result.push("Incorrect A");
    }

    if (b === 'b00') {
        result.push("Incorrect B");
    }

    if (result.length > 0) {
        testHandle.addFeedback(false, { duration: getDuration(), comment: result.join(", "), custom: { Severity: `${ result.length }` } });
        assert.fail();
    } else {
        testHandle.addFeedback(true, { duration: getDuration() });
    }
}

function validateFTest(arg1, arg2, arg3, testHandle) {
    let result = [];

    if (arg1 >= 2) {
        result.push("Incorrect 'arg1'");
    }

    if (result.length > 0) {
        testHandle.addFeedback(false, { duration: getDuration(), comment: result.join(", "), custom: { Severity: `${ result.length }` } });
        assert.fail();
    } else {
        testHandle.addFeedback(true, { duration: getDuration() });
    }
}

function getDuration() {

    return 100 + Math.floor(Math.random() * 400);
}

