import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    ConnectionDB db = new ConnectionDB();


    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run(){
        db.connect();
        staticFiles.location("/");
        before((request, response) -> response.type("application/json"));
        post("/addElement", "application/json", (req, res) -> {
            saveToDB(req.body());
            return "";
        });
        post("/removeElement",  (req, res) -> {
            removeTask(req.body());
            return "";
        });
        post("/changeTaskStatus",  (req, res) -> {
            changeTaskStatus(req.body());
            return "";
        });
        get("/getIndex", "application/json", (req, res) -> getIndex());
        get("/retrieveRecords", "application/json", (req, res) -> retrieveRecords());
        post("/updateTask", "application/json", (req, res) -> {
            updateTaskName(req.body());
            return "";
        });

    }

    private int getIndex() {
        return db.getIndex();
    }

    private void removeTask(String json) {
        db.delete(getValuesFromJson(json).iterator().next());
    }

    private void updateTaskName(String json) {
        String[] nameAndId = json.split(",");
        db.update(getValuesFromJson(nameAndId[0]).iterator().next(),getValuesFromJson(nameAndId[1]).iterator().next());
    }

    private String retrieveRecords() {
        return db.retrieveRecords();
    }

    private void changeTaskStatus(String json) {
        db.changeTaskStatus(getValuesFromJson(json).iterator().next());
    }

    private void saveToDB(String json){
        db.insert(getValuesFromJson(json).iterator().next());
    }

    private Collection<String> getValuesFromJson(String json){
        Type mapType = new TypeToken<Map<String,String>>(){}.getType();
        Map<String,String> varToName = new Gson().fromJson(json, mapType);
        return varToName.values();
    }

}

