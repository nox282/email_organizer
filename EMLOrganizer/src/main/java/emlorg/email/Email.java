package emlorg.email;

public class Email {
    private String date;
    private String from;
    private String to;
    private String subject;

    
    public Email (String date, String from, String to){
        this.date = date;
        this.from = from;
        this.to = to;
    }

    public Email() {}
    
    @Override
    public String toString(){
        return null;
    }
    
    
    
    public void setDate(String date){this.date = date;}
    public void setFrom(String from) {this.from = from;}
    public void setTo(String to) {this.to = to;}
    public void setSubject(String subject) {this.subject = subject;}
}
