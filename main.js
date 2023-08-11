const { app, BrowserWindow, ipcMain } = require('electron')
const path = require('path'); // Make sure this line is present
const url = require('url');

const createWindow = () => {
    mainWindow = new BrowserWindow({
        width: 800,
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

    ipcMain.on('form-data', (event, formData) => {
        // Handle the form data received from the renderer process
        console.log('Form data received:', formData);

        // Perform your file copying logic here using the formData
    });

    ipcMain.on('dropped-files', (event, filePaths) => {
        // Handle the uncensored dropped file paths here
        console.log('Dropped files:', filePaths);
    });
}

app.whenReady().then(() => {
    createWindow();
});