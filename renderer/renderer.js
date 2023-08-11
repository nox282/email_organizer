let dataPayload = {
    inputFilePaths: [],
    outputFolderPath: [],
    GMToffset: 0,
    email: "",
};

const inputFilePaths = document.getElementById('inputFilePaths');
const inputFilePathsDropZone = document.getElementById('inputFilePathsDropZone');
const inputFilePathsDisplay = document.getElementById('inputFilePathsDisplay');

const outputFolderPath = document.getElementById('outputFolderPath');
const outputFolderPathDropZone = document.getElementById('outputFolderPathDropZone');
const outputFolderPathDisplay = document.getElementById('outputFolderPathDisplay');

const email = document.getElementById('email');

const goButton = document.getElementById('goButton');

document.addEventListener('DOMContentLoaded', function () {
  // Listen for file input changes
  inputFilePaths.addEventListener('change', (event) => {
    updateInputFilePaths(event.target.files);
  });

  // Enable drag and drop support for input file paths.
  inputFilePathsDropZone.addEventListener('dragover', (event) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = 'copy'; // Enable copy effect
  });

  inputFilePathsDropZone.addEventListener('drop', (event) => {
    event.preventDefault();
      updateInputFilePaths(event.dataTransfer.files);
  });

  outputFolderPath.addEventListener('change', (event) => {
    const file = event.target.files[0];
    updateOutputFolderPath(file.path);
  });

  // Enable drag and drop support for output folder path.
  outputFolderPathDropZone.addEventListener('dragover', (event) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = 'copy'; // Enable copy effect
  });

  outputFolderPathDropZone.addEventListener('drop', (event) => {
    event.preventDefault();
    
    if (event.dataTransfer.items <= 0) {
      return;
    }

    const item = event.dataTransfer.items[0];
    if (item.kind !== 'file') {
      return;
    }

    const file = item.getAsFile();
    updateOutputFolderPath(file.path);
  });

  // logic for the Go button
  goButton.addEventListener('click', () => {
    const timezoneInput = document.getElementById('timezone');
    dataPayload.GMToffset = parseInt(timezoneInput.value);
    dataPayload.email = email.value;
    
    // Send message to the main process with the form data
    window.api.send('form-data', dataPayload);
  });
});

// filePaths: FileList
function updateInputFilePaths(files) {
  // transform FileList into a file array.
  let fileArray = []
  for (const file of files) {
    fileArray.push(file);
  }

  // Validate filePaths to ensure they're non-empty and unique
  const validatedFilePaths = fileArray.map(file => file.path)
                                      .filter(filePath => {
                                          return filePath && !dataPayload.inputFilePaths.includes(filePath);
                                        });
  
  dataPayload.inputFilePaths = [...dataPayload.inputFilePaths, ...validatedFilePaths];

  const paths = dataPayload.inputFilePaths.join('<br>');
  inputFilePathsDisplay.innerHTML = `Selected Paths:<br>${paths}`;
}

// path: string
function updateOutputFolderPath(path) {
  dataPayload.outputFolderPath = path;
  outputFolderPathDisplay.innerHTML = `Selected Folder: ${path}`;
}
