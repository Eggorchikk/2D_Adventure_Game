package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import object.Obj_Health_Bar;
import object.Obj_Key;
import object.Obj_Key_Iron;
import object.Obj_Key_Linked;
import object.SuperObject;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40;
    BufferedImage keyImage, keyIronImage, keyLinkedImage, hpBar1, hpBar2, hpBar3, hpBar4, hpBar5, hpBar6;
    BufferedImage backgroundImage;
    public String currentDialogue = "";
    public String message = "";
    public boolean messageOn = false;
    public int messageCounter = 0;
    public int commandNum = 0;

    public UI(GamePanel gp){

        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        Obj_Key key = new Obj_Key(gp);
        keyImage = key.image;
        Obj_Key_Iron key_Iron = new Obj_Key_Iron(gp);
        keyIronImage = key_Iron.image;
        Obj_Key_Linked key_Linked = new Obj_Key_Linked(gp);
        keyLinkedImage = key_Linked.image;
        try {
             backgroundImage = ImageIO.read(getClass().getResourceAsStream("/res/other/menu_background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SuperObject hpBar = new Obj_Health_Bar(gp);
        hpBar1 = hpBar.image;
        hpBar2 = hpBar.image2;
        hpBar3 = hpBar.image3;
        hpBar4 = hpBar.image4;
        hpBar5 = hpBar.image5;
        hpBar6 = hpBar.image6;
    }
    public void draw(Graphics2D g2){

        this.g2 = g2; 
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        if(gp.gameState == gp.instructionState){
            drawInstructionScreen();
        }
        if(gp.gameState == gp.playState){
            drawKey(g2);
        }
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
        }
        if(gp.gameState == gp.preFightState){
            drawPreFightScreen("Dungeon", 60F);
            gp.player.life = gp.player.maxLife;
            drawPlayerLife();
        }
        if(gp.gameState == gp.winState){
            drawPreFightScreen("WIN!!!", 60F);
            drawPlayerLife();
        }
        if(gp.gameState == gp.deathState){
            drawPreFightScreen("Your journey ends here...", 45F);
            drawPlayerLife();
        }
        if(gp.gameState == gp.fightState){
            drawKey(g2);
            drawPlayerLife();
        }

        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
            g2.setColor(Color.white);
            int x = getXforCenterText(message);
            int y = gp.tileSize * 5;
            g2.drawString(message, x, y);

            messageCounter++;
            if (messageCounter > 120) {
                messageOn = false;
                messageCounter = 0;
            }
        }
    }

    public void drawTitleScreen(){

        //BACKGROUND
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);


        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 70F));
        String text = "Adventure game";
        int x = getXforCenterText(text);
        int y = gp.tileSize * 3;

        //SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x+3, y);
        //MAIN TEXT
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    public void drawInstructionScreen(){

        //BACKGROUND
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //TITLE NAME
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        String text = "Instruction";
        int x = getXforCenterText(text);
        int y = gp.tileSize * 2;
        g2.drawString(text, x, y);

        //MAIN TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        x = getXforCenterText("ENTER - start/end dialogue");
        text = "P - pause";
        y += 80;
        g2.drawString(text, x, y);

        text = "W A S D - move";
        y += 40;
        g2.drawString(text, x, y);

        text = "ENTER - start/end dialogue";
        y += 40;
        g2.drawString(text, x, y);

        text = "E / LMB - shoot";
        y += 40;
        g2.drawString(text, x, y);

        text = "Q / RMB - ultimate";
        y += 40;
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        text = "(Hint: talk to a villager first)";
        y += 50;
        g2.drawString(text, x, y);
    }

    public void drawPauseScreen(){

        Color black = new Color(0, 0, 0, 200);
        g2.setColor(black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.white);
        try {
            InputStream is = getClass().getResourceAsStream("/res/other/Seagram.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(40f);
            g2.setFont(font);
        } catch (Exception e) {
            e.printStackTrace();
            // fallback
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40f));
        }
        String text = "PAUSED";
        int x = getXforCenterText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen(){

        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4 + 20;
        drawSubWindow(x, y, width, height, 0, 255, 0);

        g2.setColor(Color.white);
        try {
            InputStream is = getClass().getResourceAsStream("/res/other/Seagram.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(27f);
            g2.setFont(font);
        } catch (Exception e) {
            e.printStackTrace();
            // fallback
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24f));
        }

        y += gp.tileSize;
        for(String line : currentDialogue.split("\n")){
            x = getXforCenterText(line);
             g2.drawString(line, x, y);
             y += 40;
        }   
    }

    public void drawPlayerLife(){

        int x = -gp.tileSize / 2;
        int y = gp.tileSize / 4;

        if(gp.player.life == 5){
            g2.drawImage(hpBar1, x, y, null);
        } else if(gp.player.life == 4){
            g2.drawImage(hpBar2, x, y, null);
        } else if(gp.player.life == 3){
            g2.drawImage(hpBar3, x, y, null);
        } else if(gp.player.life == 2){
            g2.drawImage(hpBar4, x, y, null);
        } else if(gp.player.life == 1){
            g2.drawImage(hpBar5, x, y, null);
        } else if(gp.player.life == 0){
            g2.drawImage(hpBar6, x, y, null);
        }
    }

    public void drawSubWindow(int x, int y, int width, int height, int r, int g, int b){

        Color black = new Color(0, 0, 0, 200);
        Color frame = new Color(r, g, b);
        g2.setColor(black);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(frame);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public int getXforCenterText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    public void drawKey(Graphics2D g2){
        int x = gp.tileSize / 2;
        int y = (int)(gp.tileSize * 0.5);

        if (gp.player.hasKey > 0) {
            // Draw visible key
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // fully visible
            g2.drawImage(keyImage, x, y, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, x + gp.tileSize + 3, y + gp.tileSize - 5);
        } else if (gp.player.hasIronKey > 0) {
            // Draw visible key
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // fully visible
            g2.drawImage(keyIronImage, x, y, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasIronKey, x + gp.tileSize + 3, y + gp.tileSize - 5);
        } else if (gp.player.hasLinkedKey > 0) {
            // Draw visible key
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // fully visible
            g2.drawImage(keyLinkedImage, x, y, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasLinkedKey, x + gp.tileSize + 3, y + gp.tileSize - 5);
        } else {
            // Draw transparent placeholder
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f)); // invisible
            g2.drawImage(keyImage, x, y, gp.tileSize, gp.tileSize, null);
            g2.drawString("x ", x + gp.tileSize + 3, y + gp.tileSize - 5);
        }
        // Reset opacity, so whole other UI is visible
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void drawPreFightScreen(String message, float fontSize){

        //BACKGROUND
        int x2 = gp.tileSize * 2;
        int y2 = gp.tileSize * 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 7;
        drawSubWindow(x2, y2, width, height, 100, 100, 100);

        //TITLE NAME
        g2.setColor(Color.white);
        try {
            InputStream is = getClass().getResourceAsStream("/res/other/Seagram.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(fontSize);
            g2.setFont(font);
        } catch (Exception e) {
            e.printStackTrace();
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, fontSize));
        }
        String text = message;
        int x = getXforCenterText(text);
        int y = gp.tileSize * 4;
        g2.drawString(text, x, y);
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
        messageCounter = 0;
    }
}
