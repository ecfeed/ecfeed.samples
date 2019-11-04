export const fileRead = (file) => {
    const reader = new FileReader();
    reader.readAsText(file, 'UTF-8');

    return reader;
}

export const fileParse = (content) => {
    const header = fileParseHeader(content);
    return fileParseTestSuite(header, content);
}

//------------------------------------------------

const fileParseHeader = (body) => {
    const content = body[0].split(',');
    const header = {};

    let index = 0;
    for (const val of content) {
        header[index++] = val.toLowerCase();
    }

    return header;
}

const fileParseTestSuite = (header, body) => {
    const testSuite = [];
    for (let i = 1 ; i < body.length ; i++) {
        const line = body[i].trim();

        if (line !== '') {
            testSuite.push(fileParseTest(header, body[i]));
        }
    }

    return testSuite;
}

const fileParseTest = (header, body) => {
    const content = body.split(',');
    const test = {};

    let index = 0;
    for (const val of content) {
        test[header[index++]] = val.toLowerCase();
    }

    return test;
}