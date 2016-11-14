package emlorg.utils;

import java.io.IOException;
import org.json.simple.JSONObject;

public class State {
    
    private static String stateFileName = "state.json";
    private static String emailKey = "email";
    private static String dateKey = "date";
    private static String secondsKey = "seconds";
    private static String nameKey = "name";
    private static String subjectKey = "subject";
    private static String destinationFolderKey = "destinationFolder";
    private static String timezoneKey = "timezone";
    
    private String email;
    private boolean date;
    private boolean seconds;
    private boolean name;
    private boolean subject;
    private String destinationFolder;
    private long timezone;
    
    private State(){
        email = "Enter your email adress";
        date = false;
        seconds = false;
        name = false;
        subject = false;
        destinationFolder = "Destination folder path.";
        timezone = 0;
    }
    
    public State (JSONObject data){
        this.email = (String) data.get(emailKey);
        this.date = (boolean) data.get(dateKey);
        this.seconds = (boolean) data.get(secondsKey);
        this.name = (boolean) data.get(nameKey);
        this.subject = (boolean) data.get(subjectKey);
        this.destinationFolder = (String) data.get(destinationFolderKey);
        this.timezone = (long) data.get(timezoneKey);
    }
    
    public void writeState() throws IOException{
        JsonInterface jsonWriter = JsonInterface.getInstance();
        jsonWriter.outputWriter(this.toJsonObject(), this.stateFileName);
    }
    
    private JSONObject toJsonObject(){
        JSONObject ret = new JSONObject();
        ret.put(emailKey, email);
        ret.put(dateKey, date);
        ret.put(secondsKey, seconds);
        ret.put(nameKey, name);
        ret.put(subjectKey, subject);
        ret.put(destinationFolderKey, destinationFolder);
        ret.put(timezoneKey, timezone);
        return ret;
    }

    public String getEmail() {return email;}
    public boolean isDate() {return date;}
    public boolean isSeconds() {return seconds;}
    public boolean isName() {return name;}
    public boolean isSubject() {return subject;}
    public String getDestinationFolder() {return destinationFolder;}
    public long getTimezone() {return timezone;}
    
    
    public void setEmail(String email) {this.email = email;}
    public void setDate(boolean date) {this.date = date;}
    public void setSeconds(boolean seconds) {this.seconds = seconds;}
    public void setName(boolean name) {this.name = name;}
    public void setSubject(boolean subject) {this.subject = subject;}
    public void setDestinationFolder(String destinationFolder) {this.destinationFolder = destinationFolder;}
    public void setTimezone(int timezone) {this.timezone = timezone;}

    
    
    public static State getState() throws IOException{
        try{
            JsonInterface jsonReader = JsonInterface.getInstance();
            return new State((JSONObject) jsonReader.parseJson(stateFileName));
        } catch (IOException ex){
            return new State();
        }
    }
}
