package domain;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * Esta es la clase del JFrame del juego
 */
public class Juego extends JFrame {
    static PanelJuego panelJuego;
    private static int WIDTH = 900;
    public static Juego juego = null;
    private int archivo;
    private JFrame menu;//Arrastro el menu para poder hacerlo visible cuando acabe la partida
    private static int HEIGHT = 600;
    private static HashSet<Integer> teclasPulsadas= new HashSet<>();

    public Juego(int archivo,JFrame j) {
        super("juego");
        this.menu=j;
        this.archivo=archivo;
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setVisible(true);
        this.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            teclasPulsadas.add(e.getKeyCode());
        }
        @Override
        public void keyReleased(KeyEvent e){
            teclasPulsadas.remove(e.getKeyCode());
        };
    });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        init();
    }
    public JFrame getMenu()
    {
        return menu;
    }
    public void end()
    {
        this.dispose();
    }
    public static HashSet<Integer> getTeclasPulsadas() {
        return teclasPulsadas;
    }
    public int getWIDTH()
    {
        return WIDTH;
    }
    public int getHEIGHT()
    {
        return HEIGHT;
    }
    public void init()
    {
        panelJuego = new PanelJuego(this,archivo,getMenu());
        this.add(panelJuego);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
