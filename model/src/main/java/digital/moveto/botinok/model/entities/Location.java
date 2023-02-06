package digital.moveto.botinok.model.entities;

import java.util.ArrayList;
import java.util.List;

public enum Location {
    ISRAEL("Israel", "101620260");

    private final String linkedinId;
    private final String name;

    Location(String name, String linkedinId) {
        this.name = name;
        this.linkedinId = linkedinId;
    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public String getName(){
        return name;
    }

    public static List<String> getAllNames() {
        List<String> result = new ArrayList<>(values().length);
        for (Location location : values()) {
            result.add(location.name);
        }
        return result;
    }


    public static Location getByName(String name) {
        for (Location location : values()) {
            if (location.name.equalsIgnoreCase(name)) {
                return location;
            }
        }
        return null;
    }

    public String toString() {
        return name;
    }
}
