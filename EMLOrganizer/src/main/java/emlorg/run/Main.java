package emlorg.run;

import emlorg.ui.MainUI;
import emlorg.utils.State;
import java.io.IOException;
import javax.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException, IOException{
        State state = State.getState();
        MainUI ui = MainUI.getInstance();
        ui.mainUI(state);
        state.writeState();
    }
}
