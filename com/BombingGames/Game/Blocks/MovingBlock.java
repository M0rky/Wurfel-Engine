package com.BombingGames.Game.Blocks;

import com.BombingGames.Game.Chunk;
import com.BombingGames.Game.Controller;
import com.BombingGames.Game.Map;
import org.newdawn.slick.SlickException;

/**
 *A Block which can move himself around the map, therefore it must also be a SelfAwareBlock.
 * @author Benedikt
 */
public abstract class MovingBlock extends SelfAwareBlock {
    /**
     * Value in pixels
     */
   private float[] pos = {Block.MIDDLEWIDTH / 2, Block.MIDDLEWIDTH / 2, 0};
   
   /* Always one of them must be 1 to prevent a division with 0.*/
   private float[] dir = {1,0,0};
   
   /**
    * provides a factor for the vector
    */
   private float speed;
   
   /**
     * These method should define what happens when the object  jumps. Shoudl call jump(int velo)
     */
    abstract void jump();
   
    MovingBlock(){
        super();
    }
    
    MovingBlock(int id){
        super(id);
    }
   
    MovingBlock(int id,int value){
        super(id, value);
    }
    
    
    /**
     * Returns the side of the current position.
     * @return
     * @see com.BombingGames.Game.Blocks.Block#getSideNumb(int, int) 
     */
    protected int getSideNumb() {
        return getSideNumb((int) pos[0],(int) pos[1]);
    }
        
    /**
     * Lets the player walk
     * @param up
     * @param down
     * @param left
     * @param right
     * @param walkingspeed the higher the speed the bigger the steps
     * @param delta time which has passed since last call
     * @throws SlickException 
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta) throws SlickException {
        walk(up, down, left, right, walkingspeed, delta, null);
    }
    
    
   /**
     * Lets the player walk with a second block on top.
     * @param up 
     * @param down
     * @param left 
     *  @param right 
     * @param walkingspeed the higher the speed the bigger the steps
     *  @param delta time which has passed since last call
     * @param topblock The block who should be on top.
     * @throws SlickException
     */
    public void walk(boolean up, boolean down, boolean left, boolean right, float walkingspeed, int delta, Blockpointer topblock) throws SlickException {
        //if the player is walking then move him
        if (up || down || left || right) {
            speed = walkingspeed;
            
            //update the movement vector
            dir[0] = 0;
            dir[1] = 0;
               
            if (up)    dir[1] = -1;
            if (down)  dir[1] = 1;
            if (left)  dir[0] = -1;
            if (right) dir[0] = 1;
        
            //scale that the velocity vector is always an unit vector
            double vectorLenght = Math.sqrt(dir[0]*dir[0]+dir[1]*dir[1]);
            dir[0] /= vectorLenght;
            dir[1] /= vectorLenght;
            //veloZ /= vectorLenght;
            
            //colision check
            float oldx = pos[0];
            float oldy = pos[1];
            //calculate new position
            float newx = pos[0] + delta * speed * dir[0];
            float newy = pos[1] + delta * speed * dir[1];
            
            //check if position is okay
            boolean validmovement = true;
            
            //check for movement in x
            //top corner
            int neighbourNumber = getSideNumb((int) newx, (int) newy - Block.MIDDLEWIDTH/2); 
            if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber, 0).isObstacle())
                validmovement = false;
            //bottom corner
            neighbourNumber = getSideNumb((int) newx, (int) newy + Block.MIDDLEWIDTH/2); 
            if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber, 0).isObstacle())
                validmovement = false; 
            
            //find out the direction of the movement
            if (oldx-newx > 0) {
                //check left corner
                neighbourNumber = getSideNumb((int) newx - Block.MIDDLEWIDTH/2, (int) newy);
                if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber, 0).isObstacle())
                   validmovement = false;
            } else {
                //check right corner
                neighbourNumber = getSideNumb((int) newx + Block.MIDDLEWIDTH/2, (int) newy);
                if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber, 0).isObstacle())
                   validmovement = false;
            }
            
            //check for movement in y
            //left corner
            neighbourNumber = getSideNumb((int) newx - Block.MIDDLEWIDTH/2, (int) newy); 
            if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber, 0).isObstacle())
                validmovement = false;

            //right corner
            neighbourNumber = getSideNumb((int) newx + Block.MIDDLEWIDTH/2, (int) newy); 
            if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber, 0).isObstacle())
                validmovement = false;  
            
            if (oldy-newy > 0) {
                //check top corner
                neighbourNumber = getSideNumb((int) newx, (int) newy - Block.MIDDLEWIDTH/2);
                if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber, 0).isObstacle())
                   validmovement = false;
            } else {
                //check bottom corner
                neighbourNumber = getSideNumb((int) newx, (int) newy + Block.MIDDLEWIDTH/2);
                if (neighbourNumber != 8 && getNeighbourBlock(neighbourNumber, 0).isObstacle())
                   validmovement = false;
            }
            
            //if movement allowed => move player   
            if (validmovement) {                
                pos[0] = newx;
                pos[1] = newy;
                
                //track the coordiante change, if there is one
                int sidennumb = getSideNumb();
                if (sidennumb != 8){                
                    switch(sidennumb) {
                        case 0:
                        case 1:
                                makeCoordinateStep(1, -1, topblock);
                                break;
                        case 2:    
                        case 3:
                                makeCoordinateStep(1, 1, topblock);
                                break;
                        case 4:
                        case 5:
                                makeCoordinateStep(-1, 1, topblock);
                                break;
                        case 6:
                        case 7:
                                makeCoordinateStep(-1, -1, topblock);
                                break;    
                    }
                }

                //set the offset for the rendering
                setOffset((int) pos[0] - Block.DIMENSION/2, (int) pos[1] - (int) pos[2] - Block.DIM2);
                //copy offset to topblock
                if (topblock != null) 
                    topblock.getBlock().setOffset(getOffsetX(), getOffsetY());
            }
        }
        //enable this line to see where to player stands:
        Controller.getMapDataSafe(getCoordX(), getCoordY(), getCoordZ()-1).setLightlevel(30);
   }
    
   /**
     * Make a step on the coordinate grid.
     * @param x left or right step
     * @param y the coodinate steps
     * @param topblock if you want to also move a block on top add a pointer to it. If not wanted: null.
     */
    private void makeCoordinateStep(int x, int y, Blockpointer topblock){
        //mirror the position around the center
        pos[1] += -1*y*Block.DIMENSION/2;
        pos[0] += -1*x*Block.DIMENSION/2;
        
        selfDestroy();
        if (topblock != null) topblock.setBlock(new Block(0));
        
        setAbsCoordY(getAbsCoordY()+y);
        if (x<0){
            if (getAbsCoordY() % 2 == 1) setAbsCoordX(getAbsCoordX()-1);
        } else {
            if (getAbsCoordY() % 2 == 0) setAbsCoordX(getAbsCoordX()+1);
        }
         
        selfRebuild();
        if (topblock != null) topblock.setBlock(new Block(getId()));
        
        //if there was a coordiante change recalc map.
        Controller.getMap().requestRecalc();
    }
    
       /**
     * Updates the block.
     * @param delta time since last update
     * @param topblock the block on top, if there is none set it to null
     */
    protected void update(int delta, Blockpointer topblock) {
        //calculate movement
        float t = delta/1000f; //t = time in s
        dir[2] += -Map.GRAVITY*t; //in m/s
        float newposZ = pos[2] + dir[2]*Block.DIMENSION*t; //m

        //land if standing in or under 0-level and there is an obstacle
        if (dir[2] <= 0
            && newposZ <= 0
            && (getCoordZ() == 0 || Controller.getMapData(getCoordX(), getCoordY(), getCoordZ()-1).isObstacle())
        ) {
            // fallsound.stop();
            dir[2] = 0;
            newposZ=0;
        }
        pos[2] = newposZ;
        
        //coordinate switch
        //down
        if (pos[2] < 0
            && getCoordZ() > 0
            && ! Controller.getMapDataSafe(getCoordX(), getCoordY(),getCoordZ()-1).isObstacle()){
          //  if (! fallsound.playing()) fallsound.play();
            
            selfDestroy();
            if (topblock != null) topblock.setBlock(new Block(0));
            setCoordZ(getCoordZ()-1);
            selfRebuild();
            if (topblock != null) topblock.setBlock(new Block(getId()));

            pos[2] += Block.DIMENSION;
            Controller.getMap().requestRecalc();
        } else {
            //up
            if (pos[2] >= Block.DIMENSION
                && getCoordZ() < Chunk.getBlocksZ()-2
                && !Controller.getMapDataSafe(getCoordX(), getCoordY(), getCoordZ()+2).isObstacle()){
                //if (! fallsound.playing()) fallsound.play();

                selfDestroy();
                if (topblock != null) topblock.setBlock(new Block(0));
                setCoordZ(getCoordZ()+1);
                selfRebuild();
                if (topblock != null) topblock.setBlock(new Block(getId()));

                pos[2] -= Block.DIMENSION;
                Controller.getMap().requestRecalc();
            } 
        }
        
        //set the offset for the rendering
        setOffset((int) (getPosX() - Block.DIMENSION/2), (int) (getPosY() - pos[2] - Block.DIMENSION/2));
        if (topblock != null) topblock.getBlock().setOffset(getOffsetX(), getOffsetY());  
    }
   
    /**
     * 
     * @return
     */
    public float getPosX() {
        return pos[0];
    }

    /**
     * 
     * @return
     */
    public float getPosY() {
        return pos[1];
    }

    /**
     * Set the pos[2]
     * @return
     */
    public float getPosZ() {
        return pos[2];
    }

    /**
     * 
     * @param pos[0]
     */
    public void setPos(float[] pos) {
        this.pos = pos;
    }

    
    /*
     * Returns true if the player is standing on ground.
     */
    /**
     * 
     * @return
     */
    public boolean isStanding(){
       return (dir[2] == 0 && pos[2] == 0);
    }

    /**
     * Jumpwith a specific speed
     * @param velo 
     */
    public void jump(float velo) {
        if (isStanding()) dir[2] = velo;
    }
    
    /**
     * Returns a normalized vector wich contains the direction of the block.
     * @return R
     */
    public float[] getDirectionVector(){
        return dir;
    }
}
