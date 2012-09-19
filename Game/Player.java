/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Benedikt
 */
public class Player extends Block{
   public int posX = 0;
   public int posY = 0;
   public int posZ = 0;
   private int absCoordX;
   private int absCoordY;
   public int coordZ;
   private int relCoordX;
   private int relCoordY;
   private Sound fallsound;
   private int timetillmove;
   
    /**Creates a player
     *
     * @param X Absolute X-Pos
     * @param Y Absolute Y-Pos
     * @param Z Absolute Z-Pos
     * @throws SlickException
     */
    public Player(int X, int Y, int Z) throws SlickException{
        name="Player";
        fallsound = new Sound("Game/Sounds/wind.wav");
        setAbsCoords(X,Y,Z);
        //if Z is too high set to highes possible position
        if (coordZ > Chunk.BlocksZ-2) coordZ = Chunk.BlocksZ -2;
        
        Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
        Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,1);
    }

    
    private void setAbsCoordX(int X){
        absCoordX = X;
        setRelCoordX(X - Controller.map.coordlistX[4]*Chunk.BlocksX);
    }
    
    private void setAbsCoordY(int Y){
        absCoordY = Y;
        //da das Map Coordinatensystem in y-Richtung in zwei unterschiedliche Richtungen geht
        setRelCoordY(Y + Controller.map.coordlistY[4]*Chunk.BlocksY);
    }    
   
    private void setAbsCoords(int X, int Y, int Z){
        setAbsCoordX(X);
        setAbsCoordY(Y);
        coordZ = Z;
    }
    
    public void setRelCoordX(int X){
        if (X < Chunk.BlocksX*3){
            relCoordX = X;
        } else {
            this.relCoordX = 3*Chunk.BlocksX-1;
            GameplayState.iglog.add("RelativeCoordX ist too high:"+X);
            System.out.println("RelativeCoordX ist too high:"+X);
        }
        
        if (X >= 0) {
            relCoordX = X;
        } else {
            relCoordX = 0;
            GameplayState.iglog.add("RelativeCoordX ist too low:"+X);
            System.out.println("RelativeCoordX ist too low:"+X);
        }
    }
    
    public int getRelCoordX(){
        return relCoordX;
    }
    
    public void setRelCoordY(int Y){
        if (Y < Chunk.BlocksY*3){
            relCoordY = Y;
        }else {
            relCoordY = 3*Chunk.BlocksY-1;
            GameplayState.iglog.add("RelativeCoordY ist too high:"+Y);
            System.out.println("RelativeCoordY ist too high:"+Y);
        }
        
        if (Y >= 0) {
            relCoordY = Y;
        }else {
            relCoordY = 0;
            GameplayState.iglog.add("RelativeCoordY ist too low:"+Y);
            System.out.println("RelativeCoordY ist too low:"+Y);
        }
    }
    
    public int getRelCoordY(){
        return relCoordY;
    }
    
       
    public void walk(int dir, int delta) throws SlickException{
        timetillmove += delta;
        if (timetillmove > 50){
            switch (dir) {
                case 1://up
                    posY--;
                break;

                case 3: //left
                    posX--;
                break;

                case 5: //right
                    posX++;
                break;        

                case 7://down
                    posY++;
                break;
            }
            
            if (posY >= Block.height /2 && relCoordY < Chunk.BlocksY*3-1) {
                    posY = 0;
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                    setAbsCoordY(absCoordY+1);

                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                    Controller.map.changes = true;
            } else {
                if (posY < 0 && relCoordY > 0) {
                    posY = Block.height;
                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                    setAbsCoordY(absCoordY-1);

                    Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                    Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);  
                    Controller.map.changes = true;
                } else {
                    if (posX >= Block.width /2 && relCoordX < Chunk.BlocksX*3-1) {
                        posX = 0;        
                        Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                        Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                        setAbsCoordX(absCoordX+1);

                        Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                        Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                        Controller.map.changes = true;
                    } else {
                        if (posX > 0 && relCoordX > 0) {
                            posX = Block.width;
                            Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
                            Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
                            setAbsCoordX(absCoordX-1);
                            Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
                            Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
                            Controller.map.changes = true;
                        }
                    }
                }
            }
            offsetX = posX / 2;
            offsetY = posY / 2;   
                 
           timetillmove = 0;
       }
       //GameplayState.iglog.add(relCoordX+":"+relCoordY+":"+coordZ);
        //System.out.println(relCoordX+":"+relCoordY+":"+coordZ);
               
   }
   
   public void jump(){
       if (coordZ<Chunk.BlocksZ-2){
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
           coordZ++;
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
           
           Controller.map.changes = true;
       }
       //GameplayState.iglog.add(relCoordX+":"+relCoordY+":"+coordZ);
   }
   
   public void update(int delta){
       
       //Gravitation
       if (Controller.map.data[relCoordX][relCoordY][coordZ-1].obstacle==false && coordZ>1){
           if (! fallsound.playing()) fallsound.play();
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(0,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(0,0);
           coordZ--;
           Controller.map.data[relCoordX][relCoordY][coordZ] = new Block(40,0);
           Controller.map.data[relCoordX][relCoordY][coordZ+1] = new Block(40,1);
           //GameplayState.iglog.add(relCoordX+":"+relCoordY+":"+coordZ);
       } else{
            fallsound.stop();
       }
   }
           
}