const { app } = require('electron');
const path = require('path');
const fs = require('fs');

const userDataPath = app.getPath('userData');
const userDataFilePath = path.join(userDataPath, 'data.json');

function getDataPayload() {
    try {
        const userDataFile = fs.readFileSync(userDataFilePath, 'utf-8');
        const userData = JSON.parse(userDataFile);
        return userData.dataPayload || {};
    } catch (error) {
        return {};
    }
}

function setDataPayload(dataPayload) {
    try {
        const existingData = getDataPayload();
        const sanitizedDataPayload = { ...dataPayload, inputFilePaths: [] }; // Exclude input files
        const updatedData = { ...existingData, dataPayload: sanitizedDataPayload };
        const dataToWrite = JSON.stringify(updatedData, null, 2);
        fs.writeFileSync(userDataFilePath, dataToWrite, 'utf-8');
    } catch (error) {
        console.error('Error writing data payload to userData:', error);
    }
}

module.exports = {
    getDataPayload,
    setDataPayload,
};