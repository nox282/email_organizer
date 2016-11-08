package emlorg.email;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailFactory {
    public static String headersToBeFound[] = {"From", "Date", "To", "Subject"};
    private static EmailFactory instance;
    
    private ArrayList<String> refHeaders;
    
    public static EmailFactory getInstance(){
        if(instance == null) instance = new EmailFactory();
        return instance;
    }
    
    public Email construct(MimeMessage message) throws MessagingException{
        HashMap<String, String> values = extractFromSource(message);
        return instantiateEmail(values);
    }
    
    
    
    
    private HashMap<String, String> extractFromSource(MimeMessage message) throws MessagingException{
        HashMap<String, String> values = new HashMap<>();
        message.getAllHeaderLines();
        for(Enumeration<Header> e = message.getAllHeaders(); e.hasMoreElements();){
            Header h = e.nextElement();
            if(refHeaders.contains(h.getName()))
                values.put(h.getName(), h.getValue());
        }
        return values;
    }
    
    private Email instantiateEmail(HashMap<String, String> values){
        Email email = new Email();
        for(Iterator it = values.entrySet().iterator(); it.hasNext();){
            Map.Entry pair = (Map.Entry) it.next();
            email = setEmailValues((String)pair.getKey(), (String)pair.getValue(), email);
        }
        return email;
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
