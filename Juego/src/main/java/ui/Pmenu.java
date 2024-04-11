package ui;


import Util.imgUtils;
import domain.BotonCN;
import domain.BotonJuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Menu de inicio en el cual se pasa al Juego o al creador de niveles
 */
public class Pmenu extends JFrame {
    private ImageIcon img;
    public static void main (String[] args) {
        new Pmenu();
    }
    public Pmenu(){
        this.setSize(900,600);
        BufferedImage q= new imgUtils().scaleImage(this.getWidth(),this.getHeight(),"resources/menu.jpg");
        img=new ImageIcon(q);
        JLabel j= new JLabel(img);
        j.setOpaque(true);
        j.setBounds(0,0,img.getIconWidth(),img.getIconHeight());
        BotonJuego[] botoness=new BotonJuego[10];
        JPanel panelBotones=new JPanel();
        JLabel texto=new JLabel("Que archivo quiere usar?");
        panelBotones.add(texto);
        for(int i=1;i<10;i++) {
            botoness[i]= new BotonJuego(""+i,i,this);
            panelBotones.add(botoness[i]);
        }
        JPanel panel=new JPanel();
        add(panel,BorderLayout.NORTH);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        BotonCN botonCN= new BotonCN("Creador de Niveles",this);
        this.add(botonCN,BorderLayout.SOUTH);
        j.setOpaque(true);
        this.add(j);
        add(panelBotones, BorderLayout.BEFORE_FIRST_LINE);
        setVisible(true);
    }
}
