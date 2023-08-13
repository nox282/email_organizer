function formatFileName(dataPayload, emailObject, encoding) {
    const name = dataPayload.email === emailObject.from ?
        `A ${emailObject.to}` :
        `De ${emailObject.from}`;

    const sanitizedName = sanitizeName(name);
    const sanitizedDate = sanitizeName(formatDate(emailObject.date));
    const sanitizedSubject = sanitizeName(emailObject.subject);

    return `${sanitizedDate}-${sanitizedName}-${sanitizedSubject}`;
}

function formatDate(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');

    return `${year} ${month} ${day}.${hours}${minutes}${seconds}`;
}

function sanitizeName(name) {
    // Replace invalid characters with underscores
    const invalidChars = /[\/:*?"<>|]/g;
    const sanitizedName = name.replace(invalidChars, '');

    // Trim leading and trailing spaces
    return sanitizedName.trim();
}

module.exports = {
    formatFileName,
}