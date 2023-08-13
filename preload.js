const { contextBridge, ipcRenderer } = require('electron');

// Use contextBridge to expose functions to the renderer process
contextBridge.exposeInMainWorld('api', {
  // Expose a function called 'send' that takes a channel and data
  send: (channel, data) => {
    // When the 'send' function is called, use ipcRenderer to send a message to the main process
    ipcRenderer.send(channel, data);
  },

  onOpenDirectory: (callback) => ipcRenderer.on('open-directory', callback),

  onInitialData: (callback) => ipcRenderer.on('initial-data', callback),

  onFormDataHandled: (callback) => ipcRenderer.on('form-data-handled', callback),
});
