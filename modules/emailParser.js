const simpleParser = require('mailparser').simpleParser

async function parseEmlFile(emlContent) {
    const parsed = await simpleParser(emlContent);

    const from = parsed.from?.value?.[0]?.address;
    const to = parsed.to?.value?.[0]?.address;
    const date = parsed.date;
    const subject = parsed.subject;

    if (!from || !to || !date || !subject) {
        throw new Error('One or more required fields are missing in the EML file. We are probably not parsing an eml file.');
    }

    return { from, to, date, subject };
}

module.exports = {
    parseEmlFile,
}