package emlorg.run;

import emlorg.ui.MainUI;
import java.io.IOException;
import javax.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException, IOException{
        MainUI ui = MainUI.getInstance();
        ui.mainUI();
    }
}
