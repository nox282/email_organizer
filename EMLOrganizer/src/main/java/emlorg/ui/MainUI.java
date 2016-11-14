package emlorg.ui;

import emlorg.email.Email;
import emlorg.email.EmailFactory;
import emlorg.utils.EmailFormatter;
import emlorg.utils.EmailInterface;
import emlorg.utils.State;
import java.io.File;
import java.io.IOException;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;

public class MainUI {
    private static MainUI instance;
    
    public static MainUI getInstance() {
        if(instance == null) instance = new MainUI();
        return instance;
    }
    
    private File[] files;
    
    public void mainUI(State state){
        Display display = new Display();
        Shell shell = new Shell(display, SWT.TITLE | SWT.CLOSE);
        shell.setSize(500, 650);
        shell.setText("EMLOrganizer");
        
        shell.setLayout(new GridLayout(2, false));
        
        GridData data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
        data.heightHint = 22;
        //------------------------------TIMEZONE--------------------------------
        Label label = new Label(shell, SWT.NONE);
        label.setText("TimeZone");
        label.setLayoutData(data);
        
        Spinner timezone = new Spinner(shell, SWT.LONG);
        timezone.setMinimum(-1200);
        timezone.setMaximum(1400);
        timezone.setSelection((int) state.getTimezone());
        timezone.setLayoutData(data);
        
        //-------------------------------EMAIL----------------------------------
        data = new GridData(SWT.FILL, SWT.TOP, true, false);
        data.heightHint = 22;
        
        label = new Label(shell, SWT.NONE);
        label.setText("Email");
        label.setLayoutData(data);
        
        Text email = new Text(shell, SWT.BORDER);
        email.setText(state.getEmail());
        email.setLayoutData(data);

        //-------------------------------SETUP LABEL----------------------------
        label = new Label(shell, SWT.NONE);
        label.setText("Setup :");        

        data.horizontalSpan = 2;
        label.setLayoutData(data);

        label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        //------------------------------INPUT FILES-----------------------------
        data = new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1);
     
        label = new Label(shell, SWT.NONE);
        label.setLayoutData(data);
        label.setText("Input files");
      
        List list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
        data = new GridData(GridData.FILL_BOTH);
        data.heightHint = 10 * list.getItemHeight();
        list.setLayoutData(data);
        
        data = new GridData(SWT.FILL, SWT.TOP, true, false);
        data.horizontalSpan = 2;
        
        Button inputButton = new Button(shell, SWT.PUSH);
        inputButton.setLayoutData(data);
        inputButton.setText("Open");
        
        inputButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e){
                files = filesSelector(shell);
                list.removeAll();
                for(int i = 0; i < files.length; i++)
                    list.add(files[i].getName());
            }
        });
        //-----------------------------DESTINATION FOLDER-----------------------
        data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
        data.heightHint = 22;
        
        label = new Label(shell, SWT.NONE);
        label.setLayoutData(data);
        label.setText("Destination folder");
        
        Text destPath = new Text(shell, SWT.BORDER);
        destPath.setLayoutData(data);
        destPath.setText(state.getDestinationFolder());
        
        data = new GridData(SWT.FILL, SWT.TOP, true, false);
        data.horizontalSpan = 2;
        
        Button outputButton = new Button(shell, SWT.PUSH);
        outputButton.setLayoutData(data);
        outputButton.setText("Open");
        
        outputButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e){
                destPath.setText(directorySelector(shell));
            }
        });
        //-------------------------TAG SELECTION--------------------------------
        data = new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1);
        
        label = new Label(shell, SWT.NONE);
        label.setLayoutData(data);
        label.setText("Tag Selection");
        
        data = new GridData(SWT.FILL, SWT.TOP, false, false, 4, 1);
        data.horizontalSpan = 4;
        Button checkDate = new Button(shell, SWT.CHECK);
        checkDate.setLayoutData(data);
        checkDate.setSelection(state.isDate());
        checkDate.setText("Date");
        
        Button checkSeconds = new Button(shell, SWT.CHECK);
        checkSeconds.setLayoutData(data);
        checkSeconds.setText("Add Seconds");
        checkSeconds.setSelection(state.isSeconds());
        if(state.isDate()) checkSeconds.setVisible(true);
        else checkSeconds.setVisible(false);
        
        checkDate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e){
                if(checkDate.getSelection())
                    checkSeconds.setVisible(true);
                else{
                    checkSeconds.setSelection(false);
                    checkSeconds.setVisible(false);
                }
            }
        });
        
        Button checkName = new Button(shell, SWT.CHECK);
        checkName.setLayoutData(data);
        checkName.setSelection(state.isName());
        checkName.setText("Display Name");
        
        Button checkSubject = new Button(shell, SWT.CHECK);
        checkSubject.setLayoutData(data);
        checkSubject.setSelection(state.isSubject());
        checkSubject.setText("Subject");
        
        //-----------------------------------GO---------------------------------
       
        Button goButton = new Button(shell, SWT.PUSH);
        goButton.setText("Go!");
        
        goButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e){
                String emailInput = email.getText();
                Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                Matcher m = p.matcher(emailInput);
                if(!m.matches()){
                    MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
                    dialog.setText("Error");
                    dialog.setMessage("Please enter a valid email adress");
                    dialog.open();
                } else {
                    EmailFormatter emlFormatter = new EmailFormatter(checkDate.getSelection(),
                                                checkSeconds.getSelection(), checkName.getSelection(), checkSubject.getSelection());
                    emlFormatter.setUserEmail(email.getText());
                    try {
                        execute(email.getText(), files, destPath.getText(), emlFormatter, timezone.getSelection());
                    } catch (MessagingException ex) {
                        MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                        dialog.setText("Error");
                        dialog.setMessage("Error : Coulnd't read email\n" + ex.getLocalizedMessage());
                        ex.printStackTrace();
                        dialog.open();
                        return;
                    } catch (IOException ex) {
                        MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                        dialog.setText("Error");
                        dialog.setMessage("Error : Coulnd't read file\n" + ex.getLocalizedMessage());
                        ex.printStackTrace();
                        dialog.open();
                        return;
                    } catch (NullPointerException ex){
                        MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
                        dialog.setText("Error");
                        dialog.setMessage("Please fill out the appropriate fields.");
                        ex.printStackTrace();
                        dialog.open();
                        return;
                    } catch (Exception ex){
                        MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                        dialog.setText("Error");
                        dialog.setMessage("Error : " + ex.getLocalizedMessage());
                        ex.printStackTrace();
                        dialog.open();
                        return;
                    }
                    
                    MessageBox dialog = new MessageBox(shell, SWT.OK);
                    dialog.setMessage("Done!");
                    dialog.open();
                }
            }
        });

        shell.open();
        while(!shell.isDisposed()){
            state.setEmail(email.getText());
            state.setDate(checkDate.getSelection());
            state.setSeconds(checkSeconds.getSelection());
            state.setName(checkName.getSelection());
            state.setSubject(checkSubject.getSelection());
            state.setDestinationFolder(destPath.getText());
            state.setTimezone(timezone.getSelection());
            if(display.readAndDispatch()){
                display.sleep();
            }
        }
        display.dispose();
        
    }
    
    //############################################HELPER########################
    
    private File[] filesSelector(Shell shell){
        FileDialog dialog = new FileDialog(shell, SWT.MULTI);
        
        String firstFile = dialog.open();
        String filterPath = dialog.getFilterPath();
        String[] otherFiles = null;
        
        if(firstFile != null)
            otherFiles = dialog.getFileNames();
        
        File[] files = new File[otherFiles.length];
        
        for(int i = 0; i < otherFiles.length; i++){
            if(filterPath != null && filterPath.trim().length() > 0)
                files[i] = new File(filterPath, otherFiles[i]);
            else
                files[i] = new File(otherFiles[i]);
        }
        
        return files;
    }
    
    private String directorySelector(Shell shell){
        DirectoryDialog dialog = new DirectoryDialog(shell);
        return dialog.open();
    }
    
    //######################################################EXECUTION###########
    
    private void execute(String email, File[] files, String destPath, EmailFormatter emailFormatter, int timezone) throws MessagingException, IOException{
        EmailInterface emailInterface = EmailInterface.getInstance();
        EmailFactory emailFactory = EmailFactory.getInstance();
        String fileName = "";
        for(File file: files){
            fileName = executeOneFile(file, emailInterface, emailFactory, emailFormatter, timezone);
            fileName = fileName.replaceAll("/", "");
            fileName = destPath + "/" + fileName + ".eml";
            emailInterface.copyMail(file.getAbsolutePath(), fileName);
        }
    }
    
    private String executeOneFile(File file, EmailInterface emlInterface, EmailFactory emlFactory, EmailFormatter emlFormatter, int timezone) throws MessagingException, IOException{
        Email eml = emlFactory.construct(emlInterface.parseMail(file.getAbsolutePath()), timezone);
        return eml.toString(emlFormatter);
    }
}