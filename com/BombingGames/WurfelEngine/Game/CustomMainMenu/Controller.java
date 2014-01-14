/*
 * Copyright 2013 Benedikt Vogler.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * * Neither the name of Bombing Games nor Benedikt Vogler nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.BombingGames.WurfelEngine.Game.CustomMainMenu;

import com.BombingGames.WurfelEngine.Core.BasicMainMenu;
import com.BombingGames.WurfelEngine.Game.CustomGameController;
import com.BombingGames.WurfelEngine.Game.CustomGameView;
import com.BombingGames.WurfelEngine.Game.ExplosivesDemoController;
import com.BombingGames.WurfelEngine.Game.ExplosivesDemoView;
import com.BombingGames.WurfelEngine.WEMain;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/**
 * The controlelr of the main Menu manages the data.
 * @author Benedikt
 */
public class Controller {
    
    private final MenuItem[] menuItems = new MenuItem[4];
    private final Sound fx;
    
    /**
     * Creates a new Controller
     */
    public Controller() {
        TextureAtlas texture = new TextureAtlas(Gdx.files.internal("com/BombingGames/WurfelEngine/Game/CustomMainMenu/Images/MainMenu.txt"), true);
                
        menuItems[0] = new MenuItem(0, texture.getRegions().get(3));
        menuItems[1] = new MenuItem(1, texture.getRegions().get(1));
        menuItems[2] = new MenuItem(2, texture.getRegions().get(0));
        menuItems[3] = new MenuItem(3, texture.getRegions().get(2));
        
        fx = Gdx.audio.newSound(Gdx.files.internal("com/BombingGames/WurfelEngine/Game/CustomMainMenu/click2.wav"));
    }
    
    /**
     * updates game logic
     * @param delta
     */
    public void update(int delta){
        if (menuItems[0].isClicked()){
            BasicMainMenu.setLoadMap(true);
            fx.play();
            WEMain.initGame(new CustomGameController(), new CustomGameView());
        } else if (menuItems[1].isClicked()) { 
                BasicMainMenu.setLoadMap(false);
                fx.play();
                WEMain.initGame(new CustomGameController(), new CustomGameView());
            } else if (menuItems[2].isClicked()){
                    BasicMainMenu.setLoadMap(false);
                    fx.play();
                    WEMain.initGame(new ExplosivesDemoController(), new ExplosivesDemoView());
                } else if (menuItems[3].isClicked()){
                    fx.play();
                    Gdx.app.exit();
        }
    }

    public void show(){
        Gdx.input.setInputProcessor(new InputListener());
    }
    /**
     *
     * @return
     */
    public MenuItem[] getMenuItems() {
        return menuItems;
    }

    /**
     *
     */
    public void dispose(){
        fx.dispose();
    }

    private class InputListener implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ESCAPE)
                Gdx.app.exit();
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return true;
        }

        @Override
        public boolean scrolled(int amount) {
            return true;
        }
    }
}