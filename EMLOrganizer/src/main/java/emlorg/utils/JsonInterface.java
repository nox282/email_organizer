package emlorg.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonInterface {
    
    
    private static JsonInterface instance;
    
    public static JsonInterface getInstance(){
        if(instance == null) instance = new JsonInterface();
        return instance;
    }
    
    public void outputWriter(JSONObject output, String fileName) throws IOException {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(output.toJSONString());
        }
    }
    
    /**
     * Reads JSON files.
     * @param output JSONObject to be written
     * @throws IOException
     */ 
    public Object parseJson(String toParse) throws  IOException {
        Object ret = null;
        JSONParser parser = new JSONParser();        
        try {
            ret = parser.parse(new FileReader(toParse));
        } catch (org.json.simple.parser.ParseException e)
            {System.out.println("Failed to parse the JSON file.");}
        return ret;
    }

    //Private constructor so compiler yields an error if you try to make an instance out of it.
    private JsonInterface(){}
}
