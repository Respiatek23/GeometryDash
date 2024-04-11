package domain;

/**
 * Clase para definir la posicion de los objetos
 */
public class Posicion extends Serialize {

    public Vector position;
    public Posicion(Vector vector) {
        this.position = vector;
    }

    /**
     * Metodo para pasar al JSON la posicion del objeto
     * @param tabSize
     * @return
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();
        builder.append(beginObjectProperty("Posicion", tabSize));
        builder.append(position.serialize(tabSize + 1) + addEnding(true, false));
        builder.append(closeObjectProperty(tabSize));
        return builder.toString();
    }

    @Override
    public String toString() {
        return "Vec (" + position.x + ", " + position.y + ")";
    }

    public static Posicion deserialize(){
        Parser.skipWhiteSpace();
        String transformTitle = Parser.parseString();
        Parser.checkString("Posicion", transformTitle);
        Parser.consume(':');
        Parser.consume('{');

        Vector position = Vector.deserialize();

        Parser.consume('}');
        return new Posicion(position);
    }
}

