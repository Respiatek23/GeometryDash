package domain;

import Util.Music;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Este panel es el que actualiza el juego
 */
public class PanelJuego  extends JPanel implements Runnable  {
    private Juego juego;
    private Image imagenFondo;
    private boolean running = true;
    private Music stereoMadness = new Music("Resources\\06 Slider.wav");;
    static public int FPS = 60;
    private static PanelJuego panelJuego=null;
    boolean isPaused;
    public JFrame menu;
    private static float maxY = 100000;
    private List<GameObject> gameObjects=new ArrayList<>();;
    private List<GameObject> removal=new ArrayList<>();;
    private List<GameObject> removed=new ArrayList<>();;
    private GameObject player;
    private int archivo;
    public PanelJuego(Juego juego,int archivo,JFrame menu) {
        this.juego = juego;
        this.menu=menu;
        this.archivo=archivo;
        init();
    }

    public static float getMaxY() {
        return maxY;
    }

    /**
     * Meto en un metodo todo lo que tiene que hacer al ser creado
     */
    public void init()
    {
        stereoMadness.restartClip();
        isPaused=false;
        maxY = juego.getHeight() - 138;
        player = new GameObject("Player", new Posicion(new Vector(50, 400)));
        player.addComponent(new Bloque(100, 100));
        player.addComponent(new Character());
        setSize(juego.getWIDTH(), juego.getHEIGHT());
        importLevel("Nivel "+archivo);
        cargarObjetos();
        try {
            imagenFondo = ImageIO.read(new File("Resources\\BlueBackground.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagenFondo, 0, 0, juego.getWIDTH(), juego.getHEIGHT(), null);
        if (gameObjects!= null) {
            for (GameObject objeto : gameObjects) {
                objeto.draw(g);
            }
        }
        this.player.draw(g);
        g.dispose();
    }
    @Override
    public void run() {
        double refreshRate = (double) 1000000000 / FPS;
        double nextPaint = System.nanoTime() + refreshRate;
        while (running==true){
            update(refreshRate);
            if(player!=null) {
                if (player.getComponent(Character.class).getIsDead()){
                    reset();
                }
            }
            if(Juego.getTeclasPulsadas().contains(KeyEvent.VK_ESCAPE))
            {
                if(!isPaused)
                {
                    this.isPaused=true;
                    stereoMadness.stop();
                    try {Thread.sleep(300);}
                    catch (InterruptedException e) {e.printStackTrace();}
                }
                else
                {this.isPaused=false;
                    stereoMadness.continuar();
                    try {Thread.sleep(300);}
                    catch (InterruptedException e) {e.printStackTrace();}
                }
            }
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

    public void cargarObjetos()
    {
        player.getComponent(Character.class).setGameObjects(this.gameObjects);
    }

    /**
     * Cuando se tenga que actualizar llama a todos los objetos para que ellos mismo se actualizen
     * @param re
     */
    public void update(double re) {
        if(isPaused)return;
        if (gameObjects!= null)
        {
            for (GameObject go : gameObjects) {
                go.update(re);
                if (go.conversion.position.x < -go.getComponent(Bloque.class).getWidth()) {
                    removal.add(go);
                    removed.add(go);//almaceno todos los eliminados y asi no tengo que tenerlos en la otra
                    // por que provocarian que la otra lista mirase si los tuviese cuando ya se que no
                }
            }
            player.update(re);
            gameObjects.removeAll(removal);
            removal.clear();
        }
        if(gameObjects.size() == 0) {
            end();
        }}

    /**
     * Una clase para cuando el juego acaba
     */
   public void end()
   {
       juego.end();
       stereoMadness.stop();
       running = false;
       JFrame panel = new JFrame();
       panel.setSize(200, 200);
       JTextArea texto = new JTextArea("Felicidades,ha ganado!\n");
       texto.append("Numero de muertes: " +player.getComponent(Character.class).getNumMuertes());
       texto.setSize(100, 100);
       panel.add(texto,BorderLayout.CENTER);
       panel.addWindowListener(new WindowAdapter() {
           @Override
           public void windowClosing(WindowEvent e) {
               menu.setVisible(true);
               panel.dispose();
           }
       });
       panel.setVisible(true);
       panel.setLocationRelativeTo(this);
    }

    /**
     * Un metodo que reinicia el nivel
     */
    public void reset()
    {

        player.reset();
        this.player.getComponent(Bloque.class).setVelocidad(new Vector(0,0));
        this.player.getComponent(Character.class).setIsDead(false);
        gameObjects.addAll(removed);
        removed.clear();
        for (GameObject go : gameObjects) {
            go.reset();
        }
    }

    /**
     * Es el metodo por el cual importamos el JSON
     * @param filename
     */
    private void importLevel(String filename) {
        File tmp = new File( filename + ".zip");
        if (!tmp.exists())
        {
            System.out.println("Ese archivo no contiene informacion, prueba con otro o escribe sobre el");
            System.exit(0);
        }
        try {
            ZipFile zipFile = new ZipFile( filename + ".zip");
            ZipEntry jsonFile = zipFile.getEntry(filename + ".json");
            InputStream stream = zipFile.getInputStream(jsonFile);
            byte[] bytes = stream.readAllBytes();
            Parser.init(0, bytes);
            GameObject go = Parser.getNextGameObject();
            while (go != null) {
                gameObjects.add(go);
                go = Parser.getNextGameObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
