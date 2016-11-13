package emlorg.utils;

import emlorg.email.Email;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class EmailFormatter {
    private String[] keywords = {"date", "seconds", "name", "subject"};
    private HashMap<String, Boolean> format;
    private String userEmail;
    private DateTimeFormatter dateSecondLess;
    private DateTimeFormatter dateSecondFull;
    
    
    public EmailFormatter(boolean date, boolean seconds, boolean name, boolean subject){
        format = new HashMap<>();
        format.put("date", date);
        format.put("seconds", seconds && date);
        format.put("name", name);
        format.put("subject", subject);
        userEmail = "";
        dateSecondFull = DateTimeFormatter.ofPattern("yyyy MM dd.HHmmss");
        dateSecondLess = DateTimeFormatter.ofPattern("yyyy MM dd.HHmm");
    }
    
    public String formatEmail(Email email){
        String ret = "";
        for (String keyword: keywords) {
            ret += formatHelper(keyword, email);
        }
        return ret;
    }
    
    private String formatHelper(String key, Email email){
        if(key.contains(keywords[0])){
            if(format.get(key)){
                if(format.get(keywords[1])) return email.getDate().format(dateSecondFull) + "-";
                else return email.getDate().format(dateSecondLess) + "-";
            }
        }
        else if(key.contains(keywords[2])){
            if(format.get(key)){
                if(email.getFromEmail() == userEmail) return "A "+email.getTo()+ "-";
                else return "De "+email.getFrom()+ "-";
            }
        }
        else if (key.contains(keywords[3])){
            if(format.get(key))
                return email.getSubject();
        }
        return "";
    }
    
    public void cleanUpValues(Email email){
        String from = email.getFrom();
        String to = email.getTo();
        
        email.setFrom(cleanUp(from));
        email.setTo(cleanUp(to));
    }
    
    private String cleanUp(String s){
        s = s.replaceAll("<.*", "");
        s = s.trim();
        return s;
    }
    
    public void setUserEmail(String email){userEmail = email;}
}
