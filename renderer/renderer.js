let dataPayload = {
    inputFilePaths: [],
    outputFolderPath: [],
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

const checkMark = document.getElementById('checkMark');

const progressBar = document.getElementById('progressBar');

// Request initial data when the DOM content is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.api.send('request-initial-data');
});

// Listen for the initial-data event and update your form fields
window.api.onInitialData((event, initialData) => {
    if (!initialData) {
        return; // Exit if initialData is undefined
    }

    if (initialData.email !== undefined) {
        email.value = initialData.email;
    }

    if (initialData.outputFolderPath !== undefined) {
        updateOutputFolderPath(initialData.outputFolderPath);
    }

    // Update other fields as needed
});

window.api.onOpenDirectory((event, path) => {
    if (path) {
        updateOutputFolderPath(path);
    }
});

window.api.onFormDataHandled((event) => {
    checkMark.classList.add('show');
    // Wait for a few seconds
    new Promise(resolve => setTimeout(resolve, 1500)).then(() => {
        // Hide the check mark
        checkMark.classList.remove('show');
    });
});

window.api.onProgress((event, progress) => {
    progressBar.value = progress * 100;
});

document.addEventListener('DOMContentLoaded', function () {
    // Listen for file input changes
    inputFilePaths.addEventListener('change', (event) => {
        updateInputFilePaths(event.target.files);
        event.target.value = '';
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

    outputFolderPath.addEventListener('click', () => {
        window.api.send('dialog:open-directory');
    });

    // logic for the Go button
    goButton.addEventListener('click', () => {
        dataPayload.email = email.value;

        // Send message to the main process with the form data
        window.api.send('form-data', dataPayload);
    });
});

// Remove a file path from the list
function removeFilePath(filePath) {
    dataPayload.inputFilePaths = dataPayload.inputFilePaths.filter(path => path !== filePath);
    updateInputFilePathsDisplay(); // Update the display after removing
}

// Remove all selected file paths
function removeAllFilePaths() {
    dataPayload.inputFilePaths = [];
    updateInputFilePathsDisplay(); // Update the display after removing all
}

// Update the display of selected input file paths
function updateInputFilePathsDisplay() {
    inputFilePathsDisplay.innerHTML = ''; // Clear the existing display

    for (const filePath of dataPayload.inputFilePaths) {
        const entry = document.createElement('div');
        entry.classList.add('file-path-entry');
    
        const pathText = document.createElement('span');
        pathText.textContent = filePath;
    
        const removeIcon = document.createElement('i');
        removeIcon.classList.add('remove-icon');
        removeIcon.textContent = 'x';
        removeIcon.addEventListener('click', () => removeFilePath(filePath)); // Add click event to remove
    
        entry.appendChild(pathText);
        entry.appendChild(removeIcon);

        inputFilePathsDisplay.appendChild(entry);
    }

    if (dataPayload.inputFilePaths.length > 0) {
        const removeAllButton = document.createElement('button');
        removeAllButton.classList.add('btn');
        removeAllButton.classList.add('btn-primary');
        removeAllButton.textContent = 'Remove All';
        removeAllButton.addEventListener('click', removeAllFilePaths);

        inputFilePathsDisplay.appendChild(document.createElement('hr')); // Add a separator
        inputFilePathsDisplay.appendChild(removeAllButton);
    }
}

// Update the dataPayload and display with new file paths
function updateInputFilePaths(files) {
    let fileArray = Array.from(files); // Convert FileList to array
    const validatedFilePaths = fileArray.map(file => file.path)
        .filter(filePath => filePath && !dataPayload.inputFilePaths.includes(filePath));

    dataPayload.inputFilePaths = [...dataPayload.inputFilePaths, ...validatedFilePaths];
    updateInputFilePathsDisplay(); // Update the display with the new data
}

// path: string
function updateOutputFolderPath(path) {
    dataPayload.outputFolderPath = path;
    outputFolderPathDisplay.innerHTML = `Selected Folder: ${path}`;
}