package emlorg.run;

import emlorg.email.Email;
import emlorg.email.EmailFactory;
import emlorg.utils.EmailReader;
import java.io.IOException;
import javax.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException, IOException{
        EmailReader emlReader = EmailReader.getInstance();
        EmailFactory emailFactory = EmailFactory.getInstance();
        
        Email email = emailFactory.construct(emlReader.parseMail(args[0]));
        System.out.println(email.toString());
    }
}
