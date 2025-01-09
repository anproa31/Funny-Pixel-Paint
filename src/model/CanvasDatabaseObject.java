package model;

public class CanvasDatabaseObject {
    private final int id;
    private final String name;
    private final String date;

    public CanvasDatabaseObject(int id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

}
