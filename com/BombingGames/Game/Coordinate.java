package com.BombingGames.Game;

import com.BombingGames.Game.Gameobjects.Block;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM2;
import static com.BombingGames.Game.Gameobjects.GameObject.DIM4;
import static com.BombingGames.Game.Gameobjects.GameObject.DIMENSION;

/**
 *A coordinate poitns to a cell in the map. The coordainte can transfer between relative and absolute coordiantes.
 * Relative coordinates are similar to the currently laoded map array. Absolute coordinates  are indipendent of the current map but to acces themyou must have the chunk loaded.
 * @author Benedikt Vogler
 */
public class Coordinate {
    int x;
    int y;
    float height;
    int topleftX;//top left chunk x coordinate
    int topleftY;//topl left chunk Y coordinate
    
    /**
     * Creates a coordiante at the current relative postion.
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     * @param relative   True when the coordiantes are relative to the currently loaded map. False when they are absolute.
     */
    public Coordinate(int x, int y, int z,final boolean relative) {
        topleftX = Controller.getMap().getChunkCoords(0)[0];
        topleftY = Controller.getMap().getChunkCoords(0)[1];
        if (relative){
            this.x = x;
            this.y = y;
        } else {
            this.x = x - topleftX * Chunk.getBlocksX();
            this.y = y - topleftY * Chunk.getBlocksY();
        }
        this.height = z*Block.GAMEDIMENSION;
    }
    
     /**
     * Creates a coordiante at the current relative postion.
     * @param x
     * @param y
     * @param height The height of the Coordiante.
     * @param relative  True when the coordiantes are relative to the currently loaded map. False when they are absolute.
     */
    public Coordinate(int x, int y, float height, final boolean relative) {
        topleftX = Controller.getMap().getChunkCoords(0)[0];
        topleftY = Controller.getMap().getChunkCoords(0)[1];
        if (relative){
            this.x = x;
            this.y = y;
        } else {
            this.x = x - topleftX * Chunk.getBlocksX();
            this.y = y - topleftY * Chunk.getBlocksY();
        }
        this.height = height;
    }
    
    /**
     * Creates a coordiante with a specific top-left chunk. You should use the other constructors.
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     * @param chunkX The topleft chunk's x-coordinate
     * @param chunkY The topleft chunk's y-coordinate
     */
    public Coordinate(int x, int y, int z, int chunkX,  int chunkY) {
        this.x = x;
        this.y = y;
        this.height = z*Block.GAMEDIMENSION;
        topleftX = chunkX;
        topleftY = chunkY;
    }
    
    /**
     *Returns a coordinate pointing to the absolute(?) center of the map. Height is half the map's height.
     * @return
     */
    public static Coordinate getMapCenter(){
        return getMapCenter(Map.getBlocksZ()*Block.GAMEDIMENSION/2);
    }
    
    /**
     *Returns a corodinate pointing to the absolute(?) center of the map.
     * @param height You custom height.
     * @return
     */
    public static Coordinate getMapCenter(float height){
        return new Coordinate(
                                Chunk.getBlocksX()/2,
                                Chunk.getBlocksY()/2,
                                height,
                                false
                            );
    }
        
    
    /**
     *
     * @return
     */
    public int getRelX(){
        return x + (topleftX-Controller.getMap().getChunkCoords(0)[0]) * Chunk.getBlocksX();
    }
    /**
     *
     * @return
     */
    public int getRelY(){
        return y + (topleftY-Controller.getMap().getChunkCoords(0)[1]) * Chunk.getBlocksY();
    }
    
    /**
     *
     * @return
     */
    public int getAbsX(){
        return x + topleftX *Chunk.getBlocksX();
    }
    /**
     *
     * @return
     */
    public int getAbsY(){
         return y + topleftY *Chunk.getBlocksY();
    }
    
    /**
     *
     * @return
     */
    public int getZ(){
        return (int) (height/Block.GAMEDIMENSION);
    }
    
    /**
     *
     * @return
     */
    public float getHeight(){
        return height;
    }

    
    /**
     *
     * @param x
     */
    public void setRelX(int x){
        this.x = x;
    }
    
    /**
     *
     * @param y
     */
    public void setRelY(int y){
        this.y = y;
    }
    
    /**
     *
     * @param z
     */
    public void setZ(int z){
        this.height = z*Block.GAMEDIMENSION;
    }
    
    /**
     *
     * @param height
     */
    public void setHeight(float height){
        this.height = height;
    }
    
    /**
     *
     * @return
     */
    public int[] getRel(){
        return new int[]{getRelX(), getRelY(), getZ()};
    }
    
    /**
     *
     * @return
     */
    public int[] getAbs(){
        return new int[]{getAbsX(), getAbsY(), getZ()};
    }
    /**
     *
     * @param vector
     * @return
     */
    public Coordinate addVector(int[] vector) {
            Coordinate newvec = this;
            newvec.x += vector[0];
            newvec.y += vector[1];
            newvec.height += vector[2]*Block.GAMEDIMENSION;
            return newvec;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Coordinate addVector(int x, int y, int z) {
        return addVector(new int[]{x,y,z});
    }
    
    /**
     *
     * @return
     */
    public Block getBlock(){
        return Controller.getMapData(this);
    }
    
    /**
     *
     * @return
     */
    public float[] getCellOffset(){
        return Controller.getMap().getCellOffset(this);
    }
    
    /**
     *
     * @return
     */
    public Block getBlockSafe(){
        return Controller.getMapDataSafe(this);
    }
    

    
    /**
     * Has the object an offset (pos vector)?
     * @return when it has offset true, else false
     */
    public boolean hasOffset() {
        return getCellOffset()[0] != DIM2 || getCellOffset()[1] != DIM2 || getCellOffset()[2] != 0;
    }
    
   /**
     * The block hides the past block when it has sides and is not transparent (like normal block)
     * @return true when hiding the past Block
     */
    public boolean hidingPastBlock(){
        return (getBlock().hasSides() && ! getBlock().isTransparent() && ! hasOffset());
    }
    
    /**
     *
     * @return
     */
    public int getScreenPosX() {
        return getRelX() * DIMENSION //x-coordinate multiplied by it's dimension in this direction
               + (getRelY() % 2) * DIM2; //y-coordinate multiplied by it's dimension in this direction
    }
    
    /**
     *
     * @return
     */
    public int getScreenPosY() {
        return getRelY() * DIM4 //x-coordinate * the tile's size
               - (int) (getHeight() / Math.sqrt(2)); //take axis shortening into account
    }
}