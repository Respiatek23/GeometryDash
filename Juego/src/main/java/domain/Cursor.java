package domain;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Una clase que sirver para usar el raton en el creador de niveles y con el cual creo los objetos
 */
public class Cursor extends MouseAdapter {
    private int x,y;
    private int d_x,d_y;
    @Override
    public void mousePressed(MouseEvent e) {
        this.x=e.getX();
        this.y=e.getY();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
         d_x = e.getX() - x;
         d_y = e.getY() - y;
        if (!(d_x == 0 || d_y == 0)) {
            if (d_x < 0)
                x = e.getX();
            if (d_y < 0)
                y = e.getY();
            if (CreadorNiveles.bloques.getSelectedItem() == "Bloque") {

                GameObject g = new GameObject("Bloque", new Posicion(new Vector(x - 5, y - 64)),CreadorNiveles.x);
                g.addComponent(new Bloque(Math.abs(d_x), Math.abs(d_y)));
                CreadorNiveles.panelCN.getGameObjects().add(g);
            }
            if (CreadorNiveles.bloques.getSelectedItem() == "Seleccionar") {
                Rectangle area = new Rectangle(x, y, Math.abs(d_x), Math.abs(d_y));
                CreadorNiveles.panelCN.contiene(area);
            }
        }
    }
}