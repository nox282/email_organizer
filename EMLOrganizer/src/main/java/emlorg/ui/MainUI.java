package emlorg.ui;

import emlorg.email.EmailFactory;
import emlorg.utils.EmailFormatter;
import emlorg.utils.EmailReader;
import java.io.File;
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

public class MainUI {
    private static MainUI instance;
    
    public static MainUI getInstance() {
        if(instance == null) instance = new MainUI();
        return instance;
    }
    
    private File[] files;
    private String destinationPath;
    
    public void mainUI(){
        Display display = new Display();
        Shell shell = new Shell(display, SWT.TITLE | SWT.CLOSE);
        shell.setSize(500, 650);
        shell.setText("EMLOrganizer");
        
        shell.setLayout(new GridLayout(2, false));
        
        GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
        
        //-------------------------------EMAIL----------------------------------
    
        Label label = new Label(shell, SWT.NONE);
        label.setText("Email");
        label.setLayoutData(data);
        
        Text email = new Text(shell, SWT.BORDER);
        email.setText("Enter your email adress");
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
        data = new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1);
        
        label = new Label(shell, SWT.NONE);
        label.setLayoutData(data);
        label.setText("Destination folder");
        
        Text destPath = new Text(shell, SWT.BORDER);
        destPath.setLayoutData(data);
        destPath.setText("Destination folder path.");
        
        data = new GridData(SWT.FILL, SWT.TOP, true, false);
        data.horizontalSpan = 2;
        
        Button outputButton = new Button(shell, SWT.PUSH);
        outputButton.setLayoutData(data);
        outputButton.setText("Open");
        
        outputButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e){
                destinationPath = directorySelector(shell);
                destPath.setText(destinationPath);
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
        checkDate.setText("Date");
        
        Button checkSeconds = new Button(shell, SWT.CHECK);
        checkSeconds.setLayoutData(data);
        checkSeconds.setText("Add Seconds");
        checkSeconds.setVisible(false);
        
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
        checkName.setText("Display Name");
        
        Button checkSubject = new Button(shell, SWT.CHECK);
        checkSubject.setLayoutData(data);
        checkSubject.setText("Subject");
        
        //-----------------------------------GO---------------------------------
       
        Button goButton = new Button(shell, SWT.PUSH);
        goButton.setText("Go!");
        
        goButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e){
                String format = processCheckboxes(checkDate.getSelection(), checkSeconds.getSelection(), checkName.getSelection(), checkSubject.getSelection());
                execute(email.getText(), files, destinationPath);
            }
        });

        shell.open();
        while(!shell.isDisposed()){
            if(display.readAndDispatch()){
                display.sleep();
            }
        }
        display.dispose();
        
    }
    
    private String processCheckboxes(boolean date, boolean seconds, boolean name, boolean subject){
        String ret = "";
        if(date) ret += "Date";
        if(seconds) ret += ":seconds";
        if(name) ret += " From:name";
        if(subject) ret +=" subject";
        return ret;
    }
    
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
    
    private void displayFileList(){
        
    }
    
    private void execute(String email, File[] files, String destPath){
        EmailReader emailReader = EmailReader.getInstance();
        EmailFactory emailFactory = EmailFactory.getInstance();
        EmailFormatter emailFormatter = new EmailFormatter("");
        
        for(File file: files){
            
        }
    }
    
    private void executeOneFile(File file, EmailReader emlReader, EmailFactory emlFactory){

    }
}