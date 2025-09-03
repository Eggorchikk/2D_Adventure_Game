package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean enterPressed;
    public boolean ePressed = false;
    public boolean qPressed = false;
    public int previousState;

    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState == gp.playState || gp.gameState == gp.fightState) {
            if(code == KeyEvent.VK_W){
                upPressed = true;
            }
            if(code == KeyEvent.VK_S){
                downPressed = true;
            }
            if(code == KeyEvent.VK_A){
                leftPressed = true;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = true;
            }
            if(code == KeyEvent.VK_ENTER){
                enterPressed = true;
            }
            if(code == KeyEvent.VK_E){
                ePressed = true;
            }
            if(code == KeyEvent.VK_Q){
                qPressed = true;
            }
        }

        if(gp.gameState == gp.dialogueState){
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;
            if(code == KeyEvent.VK_ENTER){
                gp.gameState = gp.playState;
                if (gp.npc[0] instanceof entity.Girl) {
                    ((entity.Girl) gp.npc[0]).triggerMovementDelay();
                }
            }
        }
        if(code == KeyEvent.VK_P){
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
                upPressed = false;
                downPressed = false;
                leftPressed = false;
                rightPressed = false;
                previousState = gp.playState;
            } else if (gp.gameState == gp.fightState) {
                gp.gameState = gp.pauseState;
                upPressed = false;
                downPressed = false;
                leftPressed = false;
                rightPressed = false;
                previousState = gp.fightState;
            } else if (gp.gameState == gp.pauseState && previousState == gp.playState) {
                gp.gameState = gp.playState;
                if (gp.npc[0] instanceof entity.Girl) {
                    ((entity.Girl) gp.npc[0]).triggerMovementDelay();
                }
            } else if (gp.gameState == gp.pauseState && previousState == gp.fightState) {
                gp.gameState = gp.fightState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();

        if (gp.gameState == gp.playState || gp.gameState == gp.fightState) {
            if(code == KeyEvent.VK_W){
                upPressed = false;
            }
            if(code == KeyEvent.VK_S){
                downPressed = false;
            }
            if(code == KeyEvent.VK_A){
                leftPressed = false;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = false;
            }
        }
    }
}
