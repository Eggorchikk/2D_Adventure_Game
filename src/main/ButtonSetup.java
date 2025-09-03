package main;

import java.awt.*;
import java.io.InputStream;
import javax.swing.JButton;

public class ButtonSetup {

    GamePanel gp;
    public static Font font;

    public ButtonSetup(GamePanel gp){
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/res/other/Seagram.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(32f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public void setButtons(){
        
        int buttonWidth = 300;
        int buttonHeight = 75;

        gp.startButton = createButton("NEW GAME", 550, 380, 
                            buttonWidth, buttonHeight, false, () -> {
            gp.gameState = gp.instructionState;
            gp.restartGame();
        });

        gp.quitButton = createButton("QUIT", 550, 620, 
                            buttonWidth, buttonHeight, false, () -> {
            System.exit(0);
        });

        gp.nextButton = createButton("START", 1050, 
                            730, buttonWidth, buttonHeight, false, () -> {
            gp.gameState = gp.playState;
            ((entity.Girl) gp.npc[0]).triggerMovementDelay();
        });

        gp.startDungeonButton = createButton("START", 570, 400,
                            buttonWidth, buttonHeight, true, () -> {
            gp.gameState = gp.fightState;
        });

        gp.quitDungeonButton = createButton("EXIT", 570, 520,
                            buttonWidth, buttonHeight, true, () -> {
            gp.gameState = gp.playState;
            gp.player.worldX = gp.tileSize * 36;
            gp.player.worldY = gp.tileSize * 40;
            gp.player.direction = "down";
            gp.keyH.downPressed = false;
            gp.keyH.upPressed = false;
            gp.keyH.leftPressed = false;
            gp.keyH.rightPressed = false;
            gp.tileM.usingDungeonMap = false;
        });

        gp.restartButton = createButton("RESTART", 570, 400, 
                            buttonWidth, buttonHeight, true, () -> {
            gp.gameState = gp.fightState;
            gp.player.worldX = gp.tileSize * 25;
            gp.player.worldY = gp.tileSize * 21;
        });

        gp.resumeButton = createButton("RESUME", 870, 700, 
                            buttonWidth, buttonHeight, true, () -> {
            if (gp.keyH.previousState != gp.winState) {
                if (gp.keyH.previousState == gp.fightState) {
                    gp.gameState = gp.fightState;
                } else if (gp.keyH.previousState == gp.playState) {
                    gp.gameState = gp.playState;
                }
            }
        });

        gp.menuButton = createButton("MENU", 270, 700, 
                            buttonWidth, buttonHeight, true, () -> {
            gp.gameState = gp.titleState;
        });

        gp.setLayout(null);
        gp.add(gp.startButton);
        gp.add(gp.quitButton);
        gp.add(gp.nextButton);
        gp.add(gp.startDungeonButton);
        gp.add(gp.quitDungeonButton);
        gp.add(gp.restartButton);
        gp.add(gp.resumeButton);
        gp.add(gp.menuButton);
    }

    private JButton createButton(String text, int x, int y, int width, int height, boolean specialFont, Runnable onClick){

        JButton button = new JButton(text);
        if (!specialFont) {
            button.setFont(new Font("Arial", Font.BOLD, 32));
        } else {
                button.setFont(font);
        }
        button.setBounds(x, y, width, height);
        button.setFocusPainted(false);
        button.setVisible(false);
        button.addActionListener(e -> {
            onClick.run();
            gp.requestFocusInWindow();
        });
        return button;
    }

    public void setButtonsVisible(boolean start, boolean quit, boolean next, boolean startD,
                                boolean quitD, boolean restart, boolean resume, boolean menu){
        gp.startButton.setVisible(start);
        gp.quitButton.setVisible(quit);
        gp.nextButton.setVisible(next);
        gp.startDungeonButton.setVisible(startD);
        gp.quitDungeonButton.setVisible(quitD);
        gp.restartButton.setVisible(restart);
        gp.resumeButton.setVisible(resume);
        gp.menuButton.setVisible(menu);
    }
}
