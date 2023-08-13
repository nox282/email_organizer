const { app, BrowserWindow, ipcMain, dialog, shell } = require('electron')
const path = require('path');
const url = require('url');
const fs = require('fs');
const chardet = require('chardet');
const iconv = require('iconv-lite');

const { sanitizeDataPayload } = require('./modules/inputSanitizer');
const { formatFileName } = require('./modules/fileNameFormatter');
const userData = require('./modules/userData');
const { parseEmlFile } = require('./modules/emailParser');

const createWindow = () => {
    mainWindow = new BrowserWindow({
        width: 764,
        height: 1024,
        webPreferences: {
            preload: path.join(__dirname, 'preload.js'),
            nodeIntegration: false // Disable Node.js integration in the renderer process
        }
    });

    // Load your HTML file
    mainWindow.loadURL(url.format({
        pathname: path.join(__dirname, 'renderer/index.html'),
        protocol: 'file:',
        slashes: true
    }));

    ipcMain.on('request-initial-data', (event) => {
        // Send the saved dataPayload to the renderer process
        const savedDataPayload = userData.getDataPayload();
        event.reply('initial-data', savedDataPayload);
    });    

    ipcMain.on('form-data', (event, dataPayload) => {
        // Handle the form data received from the renderer process
        console.log('Form data received:', dataPayload);
        processFormData(dataPayload).then(() => {
            console.log('Finished processing files.');
            event.reply('form-data-handled');
        })
        .catch((reason) => {
            console.log(reason);
        });

        userData.setDataPayload(dataPayload);
    });

    ipcMain.on('dialog:open-directory', (event) => {
        dialog.showOpenDialog(mainWindow, {
            properties: ['openDirectory']
        }).then((result) => {
            const { canceled, filePaths } = result;
            
            if (!canceled) {
                event.reply('open-directory', filePaths[0]);
            }
        })
    });

    ipcMain.on('show-in-folder', (event, filePath) => {
        shell.showItemInFolder(filePath);
    })
}

app.whenReady().then(() => {
    createWindow();
});


// This function processes the form data by sanitizing it, parsing email files,
// formatting filenames, and copying files to the output folder.
// The dataPayload object is expected to have the following fields:
// - inputFilePaths: Array of input file paths
// - outputFolderPath: Path of the output folder
// - GMToffset: GMT offset for timezone configuration
// - email: User's email address
async function processFormData(dataPayload) {
    const sanitizedDataPayload = sanitizeDataPayload(dataPayload);

    if (sanitizedDataPayload.inputFilePaths.length === 0 || sanitizedDataPayload.outputFolderPath.length === 0) {
        console.log('No input file paths or output folder path provided. Nothing to process.');
        return;
    }

    for (const inputFilePath of sanitizedDataPayload.inputFilePaths) {        
        // Detect the encoding of the file
        const encoding = chardet.detectFileSync(inputFilePath);

        try {
            const emailObject = await openEmlFile(inputFilePath, encoding);
            const fileName = formatFileName(sanitizedDataPayload, emailObject, encoding);
    
            console.log(`Processing file: ${inputFilePath}, Formatted filename: ${fileName}`);
            
            copyFileWithNewName(inputFilePath, sanitizedDataPayload.outputFolderPath, fileName);
        } catch (error) {
            console.log(error);
        }
    }
}

function copyFileWithNewName(sourceFilePath, destinationFolderPath, newName) {
    const extension = path.extname(sourceFilePath); // Get the file extension
    const newFilePath = path.join(destinationFolderPath, `${newName}${extension}`);

    // Check if the destination file already exists
    if (fs.existsSync(newFilePath)) {
        console.log('Destination file already exists. Skipping copy.');
        return;
    }

    try {
        fs.copyFileSync(sourceFilePath, newFilePath);
    } catch (error) {
        console.error('Error copying file:', error);
    }
}

function openEmlFile(filePath, encoding) {
    // Validate the file path (you can add more validation if needed)
    if (typeof filePath !== 'string' || filePath.trim() === '') {
        throw new Error('Invalid file path');
    }

    try {
        // Read the file contents using the detected encoding
        const buffer = fs.readFileSync(filePath);
        const fileContents = iconv.decode(buffer, encoding);

        // Parse the email contents using the simulated parsing function
        return parseEmlFile(fileContents);
    } catch (error) {
        throw new Error('Error reading or parsing the email file: ' + error.message);
    }
}