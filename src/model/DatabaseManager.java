package model;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static Connection conn = null;
    private static String path = null;

    private DatabaseManager() {
    }

    public static void connect(String path) {
        DatabaseManager.path = path;
        connect();
    }

    public static void connect() {
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            String url = String.format("jdbc:sqlite:%s", DatabaseManager.path);
            // create a connection to the database
            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // creating the table if It's not already there
        String sql = "CREATE TABLE IF NOT EXISTS RECENT (\n" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    NAME TEXT NOT NULL,\n" +
                "    DATA BLOB NOT NULL,\n" +
                "    RECENTDATE TEXT DEFAULT (datetime('now', 'localtime'))" + // 0 for text, 1 for image
                ");";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        if (conn == null)
            return;
        try {
            conn.close();
        } catch (SQLException ignored) {
        }
    }

    public static void insert(String name, byte[] data) {

        if (data == null)
            throw new IllegalArgumentException("data cannot be null");

        String sql = "INSERT INTO recent(NAME, DATA) VALUES(?,?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setBytes(2, data);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static byte[] get(int id) {
        String sql = "SELECT DATA FROM recent WHERE ID= ?;";
        byte[] data = null;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.isClosed())
                return null;

            data = rs.getBytes(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public static ArrayList<CanvasDatabaseObject> list() {
        if (conn == null) return null;

        String sql = "SELECT ID, NAME, RECENTDATE FROM RECENT ORDER BY datetime(RECENTDATE) DESC LIMIT 15;";

        ArrayList<CanvasDatabaseObject> list = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            if (rs.isClosed())
                return null;

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String date = rs.getString(3);
                list.add(
                        new CanvasDatabaseObject(id, name, date)
                );

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

}