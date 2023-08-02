import  fs from "fs";
import path from "path";
import { parse } from "csv-parse/sync";

const pathCSV = "../source/source.csv";
const pathJSON = "../source/source.json";

export const parseCSV = () => {
        return parse(fs.readFileSync(path.join(__dirname, pathCSV)), {
            columns: true,
            skip_empty_lines: true
        });
};

export const parseJSON = () => {
    return require(pathJSON).testCases;
};
