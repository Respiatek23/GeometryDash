package domain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Este boton es el que permite ir al juego desde el menu
 */
public class BotonJuego extends JButton {
    private int numero;
    public JFrame j;
    public BotonJuego(String text, int numero,JFrame j)
    {
        super(text);
        this.j=j;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Juego(numero,j);
                j.setVisible(false);
            }
        });
        this.numero=numero;
    }

}
