package domain;

public class Vector extends Serialize {
    public double x;
    public double y;
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public static Vector add(Vector value1, Vector value2) {
        return new Vector(value1.x + value2.x, value1.y + value2.y);
    }
    @Override
    public String toString() {
        return "Vec<" + this.x + ", " + this.y + ">";
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("Vector", tabSize));
        builder.append(addFloatProperty("x", x, tabSize + 1, true, true));
        builder.append(addFloatProperty("y", y, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }
    public static Vector deserialize() {
        String title = Parser.parseString();
        Parser.checkString("Vector", title);
        Parser.consume(':');
        Parser.consume('{');

        String xTitle = Parser.parseString();
        Parser.checkString("x", xTitle);
        Parser.consume(':');
        float x = Parser.parseFloat();
        Parser.consume(',');
        String yTitle = Parser.parseString();
        Parser.checkString("y", yTitle);
        Parser.consume(':');
        float y = Parser.parseFloat();
        Parser.consume('}');
        return new Vector(x, y);
    }
}
