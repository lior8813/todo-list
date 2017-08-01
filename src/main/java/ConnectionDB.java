
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class ConnectionDB {

    Connection connection = null;
    static final String URL = JDBC.PREFIX + "todo.db";
    static final String TABLE_NAME = "TASKS";
    static final String COUNT = "count(*)";
    static final String ID = "SELECT max(ID) from " + TABLE_NAME;
    private int index = 0;

    public void connect() {
        Statement stmt = null;

        try {

            connection = DriverManager.getConnection(URL);
            System.out.println("Opened database successfully");
            stmt = connection.createStatement();
            String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_NAME + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.isClosed() || !rs.getString("name").equals(TABLE_NAME)) {
                stmt = connection.createStatement();
                sql = "CREATE TABLE " + TABLE_NAME + " " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " NAME           TEXT    NOT NULL," +
                        " DONE           BOOL    NOT NULL )";
                stmt.executeUpdate(sql);
                stmt.close();
            }
            //connection.close();
        } catch(Exception e ){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");

    }

    public void insert(String recordName) {
        Statement stmt = null;
        try {
            //connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(ID);
            //if (this.index == 0){ this.index = rs.getInt(1);}
            this.index = rs.getInt(1) + 1;
            stmt = connection.createStatement();
            String sql = "INSERT INTO " + TABLE_NAME + " (ID,NAME,DONE) " +
                    "VALUES (" + this.index + ", '" + recordName + "', '0');";
            stmt.executeUpdate(sql);
            //this.index++;
            stmt.close();
            connection.commit();
            //connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }


    public void update(String recordName, String recordId){
        Statement stmt = null;
        try {
            //connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connection.createStatement();
            String sql = "UPDATE TASKS SET NAME = \'" + recordName +  "\' WHERE ID = "+ Integer.valueOf(recordId) +";";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            //connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records updated successfully");

    }

    public void delete(String taskId) {
        Statement stmt = null;
        try {
            //connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connection.createStatement();
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID = "  + taskId;
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            //connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records deleted successfully");
    }

    public void changeTaskStatus(String taskId){ // todo: check this method.
        Statement stmt = null;

        try {
           // connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(true);
            stmt = connection.createStatement();
            String sql = "UPDATE " + TABLE_NAME +
                    " SET DONE= NOT DONE WHERE ID=" + taskId;
            stmt.executeUpdate(sql);
            stmt.close();
            //connection.commit();
            //connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("task id " + taskId + " changed successfully");
    }

    public String retrieveRecords() {
        Statement stmt = null;
        String answerJson = "";
        try {
            //connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connection.createStatement();
            String sql = "SELECT * FROM " + TABLE_NAME;
            answerJson += "[";
            ResultSet rs = stmt.executeQuery(sql);
            boolean firstRecord = true;
            while(rs.next()){
                if(firstRecord){
                    answerJson += "{";
                    firstRecord = false;
                } else {
                    answerJson += ", {";
                }
                int id = rs.getInt(1);
                String name = rs.getString(2);
                boolean done = rs.getBoolean(3);
                answerJson += "\"id\": " + id + ", \"name\": \"" + name + "\", \"done\": " + done + "}";
            }
            answerJson += "]";
            stmt.close();
            connection.commit();
            //connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records retrieved successfully");
        return answerJson;
    }



    public int getIndex() {
        Statement stmt = null;
        try {
            //connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(ID);
            this.index = rs.getInt(1) + 1;
            stmt.close();
            //connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return this.index + 1;
        }

        return this.index;
    }

}
