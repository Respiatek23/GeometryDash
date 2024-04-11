package domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase que comprende a los objetos
 */
public  class GameObject extends Serialize implements Comparable<GameObject> {
    public List<Componente> getComponents() {
        return components;
    }
    private List<Componente> components;
    private List<Componente> componentsToRemove = new ArrayList<>();
    private List<Componente> componentsToAdd = new ArrayList<>();
    public List<Componente> getComponentsSelected() {
        return componentsSelected;
    }
    private List<Componente> componentsSelected= new ArrayList<>();
    private GameObject parent;
    private String nombre;
    private int Index = 0;
    public Posicion conversion;
    final double xInicial,yInicial;

    public GameObject(String nombre, Posicion c) {
        components = new ArrayList<Componente>();
        this.nombre = nombre;
        this.parent = null;
        this.conversion = c;
        xInicial=c.position.x;
        yInicial=c.position.y;
    }
    public GameObject(String nombre, Posicion c,int distancia) {
        components = new ArrayList<Componente>();
        this.nombre = nombre;
        this.parent = null;
        this.conversion = c;
        xInicial=c.position.x+distancia;
        yInicial=c.position.y;

    }
    public void setIndex(int newIndex) {
        this.Index = newIndex;
    }
    public <T extends Componente> T getComponent(Class<T> com) {
        for (Componente c : components) {
            if (com.isAssignableFrom(c.getClass())) {
                try {
                    return com.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return null;
    }

    /**
     * Clase para reiniciar al objeto
     */
    public void reset()
    {
        conversion.position.x=xInicial;
        conversion.position.y=yInicial;
        for(Componente c :components)
        {
            if(c instanceof Bloque)
            {
                Bloque b=(Bloque)c;
                b.reset();
            }
        }
    }
    public void draw(Graphics g2) {
        for (Componente c : components) {
            if(c instanceof Bloque)
            {
                Bloque b=(Bloque)c;
                b.draw(g2);
            }
            if(c instanceof Character)
            {
                Character d=(Character)c;
                c.draw(g2);
            }
            else
                c.draw(g2);
        }
    }
    public <T extends Componente> void removeComponent(Componente c) {
        for (Componente co : components) {
            if (co == c) {
                components.remove(co);
                return;
            }
        }
    }

    /**
     * Metodo para saber si un bloque esta siendo contenido en un area
     * @param a
     */
    public void contiene(Rectangle a)
    {
        for(Componente co: components)
        {
            if(co instanceof Bloque)
            {
                Bloque b=(Bloque)co;
                if(b.getRectangle().intersects(a))
                {
                    componentsSelected.add(b);
                    b.setSelected(true);
                }
                else
                {
                    componentsSelected.remove(b);
                    b.setSelected(false);
                }
            }
        }
    }
    public void addComponent(Componente c) {
        c.parent = this;
        components.add(c);
        c.start();
    }

    /**
     * Actualiza el objeto
     * @param dt
     */
    public void update(double dt) {
        for (Componente c : components) {
            if(c instanceof Bloque)
            {
                Bloque b=(Bloque)c;
                b.update(dt);
            }
            else if(c instanceof Character)
            {
                Character d= (Character)c;
                d.update(dt);
            }
        }

        if (componentsToAdd.size() > 0) {
            for (Componente c : componentsToAdd) {
                addComponent(c);
            }
            componentsToAdd.clear();
        }
        if (componentsToRemove.size() > 0) {
            for (Componente c : componentsToRemove) {
                removeComponent(c);
            }
            componentsToRemove.clear();
        }
    }
    public String getNombre()
    {
        return this.nombre;
    }
    @Override
    public int compareTo(GameObject g) {
        if (g.conversion.position.y != this.conversion.position.y)
            return this.conversion.position.y > g.conversion.position.y ? 1 : -1;

        if (g.conversion.position.x == this.conversion.position.x) return 0;
        return this.conversion.position.x > g.conversion.position.x ? 1 : -1;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    /**
     * Clase para crear el JSON del objeto
     * @param espacios
     * @return
     */
    @Override
    public String serialize(int espacios) {  //Para poder convertir a JSON
        StringBuilder s = new StringBuilder();
        s.append(beginObjectProperty("GameObject", espacios));
        conversion.position.x=xInicial;
        conversion.position.y=yInicial;
        s.append(conversion.serialize(espacios + 1) + addEnding(true, true));
        s.append(addIntProperty("Index", Index, espacios + 1, true, true));
        if (components.size() > 0)
            s.append(addStringProperty("nombre", nombre, espacios + 1, true, true));
        else
            s.append(addStringProperty("nombre", nombre, espacios + 1, true, false));
        //Añade los componentes
        int i = 0;
        if (components.size() > 0)
            s.append(beginObjectProperty("Components", espacios + 1));
        for (Componente c : components) {
            String str = c.serialize(espacios + 2);
            if (str.compareTo("") != 0) s.append(str);
            if (str.compareTo("") != 0 && i != components.size() - 1)
                s.append(addEnding(true, true));
            else if (str.compareTo("") != 0)
                s.append(addEnding(true, false));
            i++;
        }
        //Añade la coma
        if (components.size() > 0)
            s.append(closeObjectProperty(espacios + 1));

        // Añade GameObjects
        i = 0;
        s.append(closeObjectProperty(espacios));

        return s.toString();
    }

    /**
     * Clase para crear un objeto a partir del JSON
     * @return GameObject
     */
    public static GameObject deserialize() {
        String gameObjectTitle = Parser.parseString();
        Parser.checkString("GameObject", gameObjectTitle);

        Parser.consume(':');
        Parser.consume('{');

        Posicion conv = Posicion.deserialize();
        Parser.consume(',');

        int Index = Parser.consumeIntProperty("Index");
        Parser.consume(',');

        String nameTitle = Parser.parseString();
        Parser.checkString("nombre", nameTitle);
        Parser.consume(':');
        String name = Parser.parseString();

        GameObject objeto = new GameObject(name,conv);

        String title = "";
        if (Parser.peek() == ',') {
            Parser.consume(',');
            title = Parser.parseString();
            if (title.compareTo("Components") == 0) {
                Parser.consume(':');
                Parser.consume('{');
                Componente c = Parser.parseComponent();
                objeto.addComponent(c);

                while (Parser.peek() == ',') {
                    Parser.consume(',');
                    c = Parser.parseComponent();
                    objeto.addComponent(c);
                }
                Parser.consume('}');
                Parser.consume('}');
            }
        }
        Parser.consume('}');
        objeto.setIndex(Index);
        return objeto;
    }
}