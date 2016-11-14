package emlorg.email;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailFactory {
    public static String headersToBeFound[] = {"From", "Received", "To", "Subject"};
    private static EmailFactory instance;
    
    private ArrayList<String> refHeaders;
    
    public static EmailFactory getInstance(){
        if(instance == null) instance = new EmailFactory();
        return instance;
    }
    
    public Email construct(MimeMessage message, int timezone) throws MessagingException{
        HashMap<String, String> values = extractFromSource(message);
        return instantiateEmail(values, timezone);
    }
    
    
    
    
    private HashMap<String, String> extractFromSource(MimeMessage message) throws MessagingException{
        HashMap<String, String> values = new HashMap<>();
        message.getAllHeaderLines();
        for(Enumeration<Header> e = message.getAllHeaders(); e.hasMoreElements();){
            Header h = e.nextElement();
            if(refHeaders.contains(h.getName())){
                if(h.getName().contains(headersToBeFound[1])){
                    if(!values.containsKey(h.getName())) values.put(h.getName(), extractDate(h.getValue()));
                }
                else values.put(h.getName(), h.getValue());
            }
        }
        return values;
    }
    
    private String extractDate(String value){
        Pattern p = Pattern.compile("((Mon)|(Tue)|(Wed)|(Thu)|(Fri)|(Sat)|(Sun))\\,\\s\\d\\s((Jan)|(Feb)|(Mar)|(Apr)|(May)|(Jun)|(Jul)|(Aug)|(Sep)|(Oct)|(Nov)|(Dec))\\s\\d{4}\\s\\d{2}\\:\\d{2}\\:\\d{2}\\s\\-\\d{4}");
        Matcher m = p.matcher(value);
        if(m.find()) return m.group(0);
        else return "";
    }
    private Email instantiateEmail(HashMap<String, String> values, int timezone){
        Email email = new Email();
        for(Iterator it = values.entrySet().iterator(); it.hasNext();){
            Map.Entry pair = (Map.Entry) it.next();
            email = setEmailValues((String)pair.getKey(), (String)pair.getValue(), email);
        }
        
        email.setZoneId(setTimeZone(timezone));
        
        return email;
    }
    
    private String setTimeZone(int timezone){
        String zone = Integer.toString(timezone);
        if(timezone < 1000 && timezone > 0) zone = "+" + "0" + zone;
        if(timezone > -1000 && timezone < 0) zone = zone.charAt(0) + "0" + zone.substring(1);
        return zone;
    }
    
    private Email setEmailValues(String key, String Value, Email email){
        if(key.equals(headersToBeFound[0])) email.setFrom(Value);
        else if(key.equals(headersToBeFound[1])) email.setDate(Value);
        else if(key.equals(headersToBeFound[2])) email.setTo(Value);
        else if(key.equals(headersToBeFound[3])) email.setSubject(Value);
        return email;
    }
    
    private EmailFactory(){
        refHeaders = new ArrayList<>();
        for(int i = 0; i < headersToBeFound.length; i++)
            refHeaders.add(headersToBeFound[i]);
    }
}
