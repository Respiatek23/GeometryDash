package domain;

import Util.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * La clase de los bloques
 */
public class Bloque extends Componente {
    private double width, height;
    private Vector velocidad;
    private boolean isSelected;
    private Image image;
    private Image img;


    public  double getWidth()
    {
        return width;
    }

    public Bloque(double width, double height)
    {
        setHeight(height);
        setWidth(width);
        this.velocidad = new Vector(-200, 0);
        try {
            BufferedImage im=new imgUtils().scaleImage((int) this.width, (int) this.height, "Resources\\Bloque2.jpg");
            this.img=im;
            BufferedImage image= new imgUtils().scaleImage((int)this.width,(int)this.height,"Resources\\Bloque1.png");
            this.image=image;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Vector getVelocidad() {
        return velocidad;
    }
    public void setVelocidad(Vector v)
    {
        this.velocidad=v;
    }
    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean b)
    {
        isSelected=b;
    }
    public void setHeight(double height) {
        if(height<80)
            this.height=height;
        else
            this.height=45;}
    public void setWidth(double width)
    {
        if(width<500)
            this.width=width;
        else
        {
            System.out.println("Ha alcanzado la anchura maxima para un bloque");
            this.width=500;
        }
    }
    @Override
    public void draw(Graphics g) {
        if(isSelected) {
            g.drawImage(img, (int) parent.conversion.position.x, (int) parent.conversion.position.y, null);
        }else
            g.drawImage(this.image,(int)parent.conversion.position.x,(int)parent.conversion.position.y,null);

    }

    @Override
    public void start() {
        this.velocidad = new Vector(-200, 0);
    }
    @Override
    public void update(double tiempo) {

        parent.conversion.position.x += this.velocidad.x / 60;
        parent.conversion.position.y += this.velocidad.y / 60;
    }

    /**
     * Una funcion que me da el area del objeto para poder usarlo luego y decir si el objeto choca
     * @return Rectangle
     */
    public Rectangle getRectangle()
    {
        return new Rectangle((int)(parent.conversion.position.x),(int)(parent.conversion.position.y),(int)this.width,(int)this.height);
    }
    /**
     * Una funcion que se inicializa cada vez que se comienze o reinicie el juego
     */
    public void reset()
    {
        setVelocidad(new Vector(-200,0));
    }

    /**
     * Funcion necesaria para crear el JSON
     * @param espacios
     * @return
     */
    @Override
    public String serialize(int espacios) {
        StringBuilder b = new StringBuilder();

        b.append(beginObjectProperty("Bloque", espacios));
        b.append(addDoubleProperty("width", width, espacios + 1, true, true));
        b.append(addDoubleProperty("height", height, espacios + 1, true, false));
        b.append(closeObjectProperty(espacios));

        return b.toString();
    }

    /**
     * Funcion para crear un bloque a partir el JSON
     * @return
     */
    public static Bloque deserialize() {
        Parser.consumeBeginObjectProperty();

        double width = Parser.consumeDoubleProperty("width");
        Parser.consume(',');
        double height = Parser.consumeDoubleProperty("height");;
        return new Bloque(width, height);
    }
}

