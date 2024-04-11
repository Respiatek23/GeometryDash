package domain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Este es el panel que incorpora todo lo que se hace dentro del Creador de niveles
 */
//Creo un Jpanel que cumpla con la misma funcion que PanelJuego pero que este sea especificamente para el creador de niveles
public class PanelCN extends JPanel implements Runnable {
    private Image imagenFondo;
    private boolean running = true;
    private CreadorNiveles niveles;
    static public int FPS = 40;
    private static double refresh = 1000000000 / FPS;
    private static PanelCN panelCN = null;
    private List<GameObject> gameObjects;
    public List<GameObject> objetosEliminados;
    public PanelCN(CreadorNiveles niveles) {
        this.niveles = niveles;
        gameObjects = new ArrayList<>();
        objetosEliminados=new ArrayList<>();
        setSize(niveles.getSize());
        try {
            imagenFondo = ImageIO.read(new File("Resources\\BlueBackground.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }
    public List<GameObject> getGameObjects()
    {
        return this.gameObjects;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagenFondo, 0, 0, niveles.WIDTH, niveles.HEIGHT, null);
        if (gameObjects != null) {
            for (GameObject objeto : gameObjects) {
                objeto.draw(g);
            }
        }
        g.dispose();
    }
    public void readjust(int distance)
    {
        for(GameObject go :gameObjects)
        {
            go.conversion.position.x-=distance;
        }
        repaint();
    }
    @Override
    public void run() {
        double refreshRate = (double) 1000000000 / FPS;
        double nextPaint = System.nanoTime() + refreshRate;
        while (running) {
            repaint();
            double remainingTime = nextPaint - System.nanoTime();
            if (remainingTime < 0)
                remainingTime = 0;
            try {
                Thread.sleep((long) remainingTime / 1000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            nextPaint += refreshRate;
        }
    }
    public void contiene(Rectangle a)
    {
        for(GameObject g:gameObjects)
        {
            g.contiene(a);
        }
    }
    public void eliminarObjetosSelecionados()
    {
        for(GameObject g:gameObjects)
        {
            g.getComponents().removeAll(g.getComponentsSelected());
            if(g.getComponents().size()<1)
                objetosEliminados.add(g);
        }
        gameObjects.removeAll(objetosEliminados);
        objetosEliminados.clear();
    }

    public void export(String filename) {
        try {
            int number=Integer.parseInt(filename.substring(0,1));
            FileOutputStream fos = new FileOutputStream("Nivel " +number + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.putNextEntry(new ZipEntry("Nivel "+ filename + ".json"));
            int i = 0;
            for (GameObject go : gameObjects) {
                if (go.getNombre() == "Nonserialize") {
                    i++;
                    continue;
                }
                zos.write(go.serialize(0).getBytes());
                if (i != gameObjects.size() - 1)
                    zos.write(",\n".getBytes());
                i++;
            }
            zos.closeEntry();
            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}