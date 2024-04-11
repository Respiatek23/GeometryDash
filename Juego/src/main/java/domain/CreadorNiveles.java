package domain;



import ui.Pmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

/**
 * Este JFrame es el que crea los niveles
 */
public class CreadorNiveles extends JFrame {
    public static PanelCN panelCN;
    public static int WIDTH=900;
    public static int HEIGHT=600;
    public static domain.Cursor mouseListener=new Cursor();
    public static JComboBox bloques;
    public static int x;
    private Pmenu menu;
    public static HashSet<Integer> teclasPulsadas;
    public CreadorNiveles(Pmenu menu){
        //Aqui se crean los bloques y se exportan a un fichero
        this.menu=menu;
        teclasPulsadas=new HashSet<>();
        panelCN=new PanelCN(this);
        this.add(panelCN);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        class MyAdjustmentListener implements AdjustmentListener {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int d=e.getValue()-x;
                x=e.getValue();
                panelCN.readjust(d);
            }
        }
        JLabel archivo=new JLabel("Archivo");

        JComboBox niveles=new JComboBox();
        for(int d=1;d<10;d++)
        {
            niveles.addItem(d);
        }
        JPanel pnlNorth= new JPanel();
        JButton Clear=new JButton("Borrar");
        Clear.addActionListener((ActionEvent e) -> {
            panelCN.eliminarObjetosSelecionados();
        });
        pnlNorth.add(Clear);
        JButton Exportar= new JButton("Exportar Archivo");
        bloques=new JComboBox();
        bloques.addItem("Bloque");
        bloques.addItem("Seleccionar");
        Exportar.addActionListener(e -> {
            panelCN.export(niveles.getSelectedItem().toString());
            menu.setVisible(true);
            this.dispose();
        });
        this.addMouseListener(CreadorNiveles.mouseListener);
        this.addMouseMotionListener(CreadorNiveles.mouseListener);
        pnlNorth.add(archivo);
        pnlNorth.add(niveles);
        pnlNorth.add(Exportar);
        pnlNorth.add(bloques);
        this.add(pnlNorth,BorderLayout.NORTH);
        this.setSize(this.WIDTH,this.HEIGHT);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                teclasPulsadas.add(e.getKeyCode());
            }
            @Override
            public void keyReleased(KeyEvent e){
                teclasPulsadas.remove(e.getKeyCode());}
        }
        );
        JScrollBar hbar=new JScrollBar(JScrollBar.HORIZONTAL, 0, 400, 0, 5000);
        x=hbar.getValue();
        hbar.addAdjustmentListener(new MyAdjustmentListener());
        this.getContentPane().add(hbar, BorderLayout.SOUTH);
        this.setVisible(true);
        this.setResizable(true);
    }
}