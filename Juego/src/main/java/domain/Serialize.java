package domain;

/**
 *  Otra clase necesaria para poder usar JSON
 */
public abstract class Serialize {
    public abstract String serialize(int tabSize);
    public String addTabs(int tabSize) {
        String str = "";
        for (int i=0; i < tabSize; i++) {
            str += "\t";
        }
        return str;
    }
    public String addStringProperty(String name, String value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + "\"" + value + "\"" + addEnding(newline, comma);
    }

    public String addIntProperty(String name, int value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + value + addEnding(newline, comma);
    }

    public String addFloatProperty(String name, double value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + value + "f" + addEnding(newline, comma);
    }

    public String addDoubleProperty(String name, double value, int tabSize, boolean newline, boolean comma) {
        return addTabs(tabSize) + "\"" + name + "\": " + value + addEnding(newline, comma);
    }


    public String addIntArrayProperty(String name, int[] data, int tabSize, boolean newline, boolean comma) {
        StringBuilder builder = new StringBuilder();
        builder.append(addTabs(tabSize) + "\"" + name + "\": [");
        for (int i=0; i < data.length; i++) {
            if (i != data.length - 1)
                builder.append("" + data[i] + ", ");
            else
                builder.append("" + data[i]);
        }
        builder.append("]");
        builder.append(addEnding(newline, comma));

        return builder.toString();
    }
    public String beginObjectProperty(String name, int tabSize) {
        return addTabs(tabSize) + "\"" + name + "\": {" + addEnding(true, false);
    }

    public String closeObjectProperty(int tabSize) {
        return addTabs(tabSize) + "}";
    }

    public String addEnding(boolean newline, boolean comma) {
        String str = "";
        if (comma) str += ",";
        if (newline) str += "\n";
        return str;
    }
}

