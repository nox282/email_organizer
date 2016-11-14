package emlorg.email;

import emlorg.utils.EmailFormatter;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {
    private ZonedDateTime date;
    private ZoneId zone;
    private String from;
    private String fromEmail;
    private String to;
    private String toEmail;
    private String subject;
    
    public Email() {}


    
    public String toString(EmailFormatter formatter){
        formatter.cleanUpValues(this);
        return formatter.formatEmail(this);
    }
    
    public void setDate(String date ){
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        this.date = ZonedDateTime.parse(date, formatter);
    }
    
    
    public void setFrom(String from) {
        this.from = from;
        Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher m = p.matcher(from);
        if(m.matches()) setFromEmail(m.group());
    }
    public void setTo(String to) {
        this.to = to;
        Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher m = p.matcher(to);
        if(m.matches()) setToEmail(m.group());
    }
    public void setSubject(String subject) {this.subject = subject;}
    public void setFromEmail(String fromEmail) {this.fromEmail = fromEmail;}
    public void setToEmail(String toEmail) {this.toEmail = toEmail;}
    public void setZoneId(String zone){this.zone = ZoneId.of(zone);}

    public ZonedDateTime getDate() {return date.withZoneSameInstant(this.zone);}
    public String getFrom() {return from;}
    public String getTo() {return to;}
    public String getSubject() {return subject;}
    public String getFromEmail() {return fromEmail;}
    public String getToEmail() {return toEmail;}
}
