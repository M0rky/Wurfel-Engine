import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;


public class OutThere2 extends javax.swing.JFrame {
    private static final int FRAME_DELAY = 20; // 20ms. implies 50fps (1000/20) = 50
    final int tilewidth = 80; //alernativ tile.getWidth(null);
    final int tileheight = 40; //alternativ tile.getHeight(null);
    public Chunk chunklist[] = new Chunk[9];
    Insets insets;
    int clickstartX,clickstartY;
    int tilesizeX = 80;
    int tilesizeY = 40;
    public JButton b1; 
    public JButton b2; 
    JFrame window = new JFrame(); 
    BufferStrategy bufferStrategy;
    final ExecutorService executorService = Executors.newFixedThreadPool(1);
    final static Random RANDOM = new Random();
   
    /*Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
 
            while (!shouldExitGame) {
                //processKeys();
                //updateGameState();
                //drawScene();
                try {
                    TimeUnit.MILLISECONDS.sleep(15L);
                } catch (InterruptedException e) {
                }
            }
            //shutdown();
            
            
        }
    };*/
    
    public static void main(String[] args) {
        OutThere2 outThere2 = new OutThere2();
    }
         
    public OutThere2() {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        setUndecorated(true);
        setIgnoreRepaint(true);
        GraphicsDevice graphicsDevice = getGraphicsConfiguration().getDevice();
        //graphicsDevice.setFullScreenWindow(this);
        //graphicsDevice.setDisplayMode(new DisplayMode(640, 480, 16, 60));
        window.setSize( 800, 400 );
        
        //alternative. Lässt startposition vom System verwalten
        //window.setLocationByPlatform(true);
        
        window.setLocationRelativeTo(null);
       
        window.setUndecorated(true);
        window.getContentPane().setLayout(new FlowLayout());
        
        Canvas gui = new Canvas();
        //window.getContentPane().add(gui);
        
        b1 = new JButton("Beenden");
        b1.addActionListener(
            new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                executorService.shutdown();
                System.exit(0);  
                }
            }
        );
        window.getContentPane().add(b1);
        
        b2 = new JButton("Draw Chunk 4");
        b2.addActionListener(
            new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                chunklist[4].drawChunk(); 
                }
            }
        );
        
        window.getContentPane().add(b2);
        class mouseEventHandler extends MouseAdapter{         
            @Override
             public void mousePressed(MouseEvent e){
                clickstartX = e.getX();
                clickstartY = e.getY();
            }
        } 

        class mouseMovedEventHandler extends MouseMotionAdapter{           
            @Override
            public void mouseDragged(MouseEvent e) {
                for (int i=0;i<9;i++){
                    chunklist[i].posX -= clickstartX - e.getX();
                    chunklist[i].posY -= clickstartY - e.getY();
                }
                clickstartX = e.getX();
                clickstartY = e.getY();
                //draw_all_chunks();
                chunklist[4].drawChunk();
            }
        } 
        
        window.addMouseListener(new mouseEventHandler()); 
        window.addMouseMotionListener(new mouseMovedEventHandler());
       
        window.setVisible(true);
        window.createBufferStrategy(2);
        bufferStrategy = window.getBufferStrategy();
        
        //dynamisch erstellen in Zukunft mit verschachtelter for-Schleife
        //i und j fängt bei i/2+0,5 an und geht bis i/2-0,5
        //k von 0 bis feldgröße^2-1
        //chunklist[k] = new Chunk(i,j);
        
        chunklist[0] = new Chunk(-1,1);
        chunklist[1] = new Chunk(0,1);
        chunklist[2] = new Chunk(1,1);
        chunklist[3] = new Chunk(-1,0);
        chunklist[4] = new Chunk(0,0);
        chunklist[5] = new Chunk(1,0);
        chunklist[6] = new Chunk(-1,-1);
        chunklist[7] = new Chunk(0,-1);
        chunklist[8] = new Chunk(1,-1); 
    }

    public class Chunk {
        public int coordX, coordY, posX, posY;

        //Konstruktor
        public Chunk(int X, int Y){
            //die größe der Chunks, hängt von der Auflösung ab
            coordX = X;
            coordY = Y;
            posX = X*window_width();
            posY = -Y*window_height();    
        }
        
       /* public static void draw(){
             hier hätte ich gerne eine Methode die mit einem Aufruf von Chunk.daw()
             in einer Schleife neun mal die drawChunk ausführt. Im moment in draw_all_Chunks zu finden
         }*/
        
        //draw the chunk
        public void drawChunk() {
            Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
            //Graphics2D g2d = bimage.createGraphics();            
            //Graphics2D g2d = (Graphics2D) window.getGraphics();
            
            BasicStroke stokestyle = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
            g2d.setStroke(stokestyle);

            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            // Create an image that supports transparent pixels
            //BufferedImage bimage = gc.createCompatibleImage(window_width(), window_height(), Transparency.BITMASK);
            
            //Zeile wird irgendwie ignoriert
            //bimage.getGraphics().setColor(Color.red);
            //bimage.getGraphics().fillRect(0, 0, bimage.getWidth(), bimage.getHeight());
            //g2d.drawImage(bimage, null, posX, posY);
            
            
            //g2d.fillRect(posX,posY,window_width(),window_height());            
            g2d.setColor(Color.blue);
            
            Image tilefile = new ImageIcon(this.getClass().getResource("grastile.png")).getImage();
            BufferedImage tile = gc.createCompatibleImage(tilesizeX, tilesizeY, Transparency.BITMASK);
            tile.getGraphics().drawImage(tilefile,
                    0,//dx1
                    0,//dy1
                    tilesizeX,//width
                    tilesizeY,//height
                    null);
            
            for (int i=-1; i*tilesizeY/2 < window_height()+tilesizeY/2; i++)//vertikal
                for (int j=-1; j*tilesizeX < window_width(); j++)//horizontal
                  g2d.drawImage(tilefile,
                          j*tilesizeX + (i%2 * (tilesizeX/2)) + tilesizeX/2 + posX,//dx1
                          i*tilesizeY/2 + posY,//dy1
                          j*tilesizeX + (i%2 * (tilesizeX/2)) + 3*tilesizeX/2 + posX,//dx2
                          i*tilesizeY/2 + posY + tilesizeY,//dy2
                          0,//sx1
                          0,//sy2
                          tilesizeX,//sx2
                          tilesizeY,//sy2
                          null);

            //26.56° Steigung der Pixel da m=0,5 und atan(0,5) = 26,565° 
            //int startpunkt = (int) (Math.sin(Math.atan(0.5))*window_width);
            //int gegenkathe = (int)(window_width/Math.tan(1.571-Math.atan(0.5)));

            
            //Linien zeichen
            
            //Komponenten der Punkte
            int start1X = posX;
            int start1Y = posY;
            int end1X = posX;
            int end1Y = posY;
            int start2X = posX;
            int start2Y = posY;
            int end2X = posX + window_width();
            int end2Y = posY;
            
            //zählervariablen
            int s1x = 0;int s1y = 0;int e1x = 0;int e1y = 0;
            int s2x = 0;int s2y = 0;int e2x = 0;int e2y = 0;
            
            for (int i=0; i < window_width()/tilesizeX+window_height()/tilesizeY; i++){
                //g2d.setColor(new Color(i*5+20,i*5+20,0));
                
                //Linie 1
                if (s1y*tilesizeY > window_height()){
                    s1x++;
                    start1X = posX + s1x*tilesizeX;                    
                } else {
                    s1y++;
                    start1Y = posY + s1y*tilesizeY;       
                }
                
                if (e1x*tilesizeX > window_width()){
                   e1y++;
                   end1Y = posY + e1y*tilesizeY;
                } else{
                   e1x++; 
                   end1X = posX + e1x*tilesizeX;                    
                }
                
                g2d.drawLine(start1X,start1Y,end1X,end1Y);
                
                //Linie 2          
                if (s2x*tilesizeX < window_width()){
                    s2x++;
                    start2X = posX + window_width() - s2x*tilesizeX;
                } else {
                    s2y++;
                    start2Y = posY + s2y*tilesizeY;
                }
                
                if (e2y*tilesizeY < window_height()){
                    e2y++;
                    end2Y = posY + e2y*tilesizeY;                    
                } else {
                    e2x++;
                    end2X = posX + window_width() - e2x*tilesizeX;                   
                }
                               
                g2d.drawLine(start2X,start2Y,end2X,end2Y);
            }
            
            g2d.setFont(new Font("Helvetica", Font.PLAIN, 50));
            g2d.drawString(String.valueOf(coordX)+","+String.valueOf(coordY), posX+window_width()/2-40, posY+window_height()/2-40);
           
            
            //wohl wie flush(), irgendwie für den garbage kolleltor
            g2d.dispose();
            //anzeigen
            bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();
        }
    }      
            
    public void draw_all_chunks(){
       for (Chunk chunk : chunklist) chunk.drawChunk();
   };
   
    public int window_width(){
        Dimension size = window.getSize();
        insets = window.getInsets();
        return size.width - insets.left - insets.right;
   }
   
    public int window_height(){
        Dimension size = window.getSize();
        insets = window.getInsets();
        return size.height - insets.top - insets.bottom;
   }
    
}
