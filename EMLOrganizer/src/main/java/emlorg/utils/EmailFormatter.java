package emlorg.utils;

import emlorg.email.Email;

public class EmailFormatter {
    private String keywords[] = {"date", "from", "to", "subject"};
    private String displayName = "name";
    private String displayEmail = "email";
    private String[] formats;
    
    public EmailFormatter(String format){
        format = format.toLowerCase();
        validateFormat(format);
        this.formats = format.split(" ");
    }
    
    private void validateFormat(String format){
        boolean valid = false;
        for (String keyword : keywords)
            valid = valid || format.contains(keyword);
        
        if(!valid) throw new ClassFormatError("Invalid Formatter input");    
    }
    
    public String formatEmail(Email email){
        String ret = "";
        for (String format : formats) {
            ret += formatHelper(format, email);
        }
        ret = ret.substring(0, ret.length()-1);
        return ret;
    }
    
    private String formatHelper(String key, Email email){
        if(key.contains(keywords[0])) return email.getDate()+"-";
        else if(key.contains(keywords[1])) return email.getFrom()+"-";
        else if(key.contains(keywords[2])) return email.getTo()+"-";
        else return email.getSubject()+"-";
    }
    
    public void cleanUpValues(Email email){
        String from = email.getFrom();
        String to = email.getTo();
        email.setFrom(cleanUp(from, keywords[1]));
        email.setTo(cleanUp(to, keywords[2]));
    }
    
    private String cleanUp(String s, String key){
        String format = getFormats(key);
        if(format.contains(displayName)) s = s.replaceAll("<.*>", "");
        else {
            s = s.replaceAll(".*<", "");
            s = s.replaceAll(">", "");
        }
        s = s.trim();
        return s;
    }
    
    private String getFormats(String key){
        for(int i = 0; i < formats.length; i++)
            if(formats[i].contains(key)) return formats[i];
        return "";
    }
}
