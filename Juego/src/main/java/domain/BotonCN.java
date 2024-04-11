package domain;


import ui.Pmenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Un boton mediante el cual puedo pasar al creador de niveles
 */
public class BotonCN extends JButton {

    public Pmenu menu;
    public BotonCN(String text,Pmenu menu)
    {
        super(text);
        this.menu=menu;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreadorNiveles(menu);
                menu.setVisible(false);
            }
        });
    }
}
