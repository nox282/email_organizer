package emlorg.email;

import emlorg.utils.EmailFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Email {
    private LocalDate date;
    private String from;
    private String to;
    private String subject;
    
    public Email() {}
    
    public String toString(EmailFormatter formatter){
        formatter.cleanUpValues(this);
        return formatter.formatEmail(this);
    }
    
    public void setDate(String date){
        date =  date.substring(0, date.length()-5);
        date = date.trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss");
        this.date = LocalDate.parse(date, formatter);
    }
    
    public void setFrom(String from) {this.from = from;}
    public void setTo(String to) {this.to = to;}
    public void setSubject(String subject) {this.subject = subject;}

    public LocalDate getDate() {return date;}
    public String getFrom() {return from;}
    public String getTo() {return to;}
    public String getSubject() {return subject;}
    
}
