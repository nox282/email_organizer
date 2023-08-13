function sanitizeDataPayload(dataPayload) {
    // Apply sanitization to dataPayload
    dataPayload.email = sanitizeEmail(dataPayload.email);
    dataPayload.GMToffset = sanitizeGMToffset(dataPayload.GMToffset);
    dataPayload.inputFilePaths = sanitizeFilePaths(dataPayload.inputFilePaths);
    dataPayload.outputFolderPath = sanitizeOutputFolderPath(dataPayload.outputFolderPath);
    
    return dataPayload;
}

// Sanitize and validate email
function sanitizeEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (typeof email === 'string' && emailRegex.test(email)) {
        return email;
    }
    return '';
}

// Sanitize and validate GMToffset
function sanitizeGMToffset(GMToffset) {
    const parsedOffset = parseInt(GMToffset);
    if (!isNaN(parsedOffset)) {
        return parsedOffset;
    }
    return 0; // Default value if not valid
}

// Sanitize and validate file paths
function sanitizeFilePaths(filePaths) {
    if (Array.isArray(filePaths)) {
        // Perform additional validation if needed
        return filePaths;
    }
    return [];
}

// Sanitize and validate output folder path
function sanitizeOutputFolderPath(outputFolderPath) {
    // Perform additional validation if needed
    return outputFolderPath;
}


module.exports = {
    sanitizeDataPayload,
}