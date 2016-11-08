package emlorg.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class EmailReader {
    private static EmailReader instance;
    
    public static EmailReader getInstance(){
        if(instance == null) instance = new EmailReader();
        return instance;
    }
    
    public MimeMessage parseMail(String path) throws MessagingException, IOException{
        try{
            File file = new File(path);
            String s = FileUtils.readFileToString(file);
            Session session = Session.getDefaultInstance(new Properties());
            InputStream is = new ByteArrayInputStream(s.getBytes());
            return new MimeMessage(session, is);
        } 
        catch (IOException e) {return null;}
        catch (MessagingException e) {return null;}
    }
}
