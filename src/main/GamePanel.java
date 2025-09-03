package main;

import entity.Bullet;
import entity.Entity;
import entity.Player;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //960 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxWorldWidth = tileSize * maxWorldCol;
    public final int maxWorldHeight = tileSize * maxWorldRow;
    // FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    public boolean mouseClickedL = false;
    public boolean mouseClickedR = false;
    public int mouseX, mouseY;

    public ArrayList<Bullet> bullets = new ArrayList<>();

    int FPS = 60;
    JButton startButton, quitButton, nextButton, startDungeonButton, quitDungeonButton; 
    JButton restartButton, resumeButton, menuButton, exitButton;

    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetup aSetup = new AssetSetup(this);
    public KeyHandler keyH = new KeyHandler(this);
    ButtonSetup bSetup = new ButtonSetup(this);
    TileManager tileM = new TileManager(this);
    public UI ui = new UI(this);
    Thread gameThread;

    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc[] = new Entity[10];
    public Entity enemy[] = new Entity[10];

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int fightState = 4;
    public final int instructionState = 5;
    public final int preFightState = 6;
    public final int deathState = 7;
    public final int winState = 8;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyH);

        addMouseListener(new MouseAdapter(){
            @Override
            public  void mousePressed(MouseEvent e){
                if (e.getButton() == MouseEvent.BUTTON1){
                    mouseClickedL = true;
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
                if (e.getButton() == MouseEvent.BUTTON3){
                    mouseClickedR = true;
                }
            }
        });

        bSetup.setButtons();
    }

    public void setupGame(){

        aSetup.setNPC();
        aSetup.setObject();
        aSetup.setEnemy();
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
        setFullScreen();
    }

    public void setFullScreen(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if(delta >= 1) {
                update();
                drawGame();
                repaint();
                delta--;
            }
        }
    }

    public void update() {

        if(gameState == playState){
            player.update();
            for(int i = 0; i < npc.length; i++){
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
        }
        if(gameState == preFightState){
            tileM.usingDungeonMap = true;
            aSetup.resetEnemy();
        }
        if(gameState == deathState){
            tileM.usingDungeonMap = true;
            aSetup.resetEnemy();
        }
        if(gameState == fightState){
            tileM.usingDungeonMap = true;
            player.update();
            for(int i = 0; i < enemy.length; i++){
                if (enemy[i] != null) {
                    enemy[i].update();
                }
            }
            for (Bullet b : bullets) {
                b.update();
            }

            if (!areEnemiesAlive()) {
                gameState = winState;
                keyH.upPressed = false;
                keyH.downPressed = false;
                keyH.leftPressed = false;
                keyH.rightPressed = false;
                player.direction = "down";
            }

            if (player.life <= 0){
                gameState = deathState;
                keyH.upPressed = false;
                keyH.downPressed = false;
                keyH.leftPressed = false;
                keyH.rightPressed = false;
                player.direction = "down";
            }
        }
        if(gameState == winState){
            tileM.usingDungeonMap = true;
        }
        if(gameState == pauseState){
            
        }
        if(gameState == fightState){
            if((keyH.ePressed || mouseClickedL) && player.attackCounter > 50){
                if(!mouseClickedL){
                    Point mouse = MouseInfo.getPointerInfo().getLocation();
                    SwingUtilities.convertPointFromScreen(mouse, this);
                    mouseX = mouse.x;
                    mouseY = mouse.y;
                }
                shootBullet(mouseX, mouseY);
                player.attackCounter = 0;
                keyH.ePressed = false;
                mouseClickedL = false;
            }
            if((keyH.qPressed || mouseClickedR) && player.ultimateCounter > 300){
                ultimate();
                player.ultimateCounter = 0;
                keyH.qPressed = false;
                mouseClickedR = false;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
    }

    public void drawGame() {
        
        if(gameState == titleState){
            ui.draw(g2);
            bSetup.setButtonsVisible(true, true, false, false, false, false, true, false);
            resumeButton.setBounds(550, 500, 300, 75);
            resumeButton.setFont(new Font("Arial", Font.BOLD, 32));
        } 
        else if(gameState == pauseState){
            ui.draw(g2);
            if (keyH.previousState == playState){
                bSetup.setButtonsVisible(false, false, false, false, false, false, true, true);
                resumeButton.setBounds(870, 700, 300, 75);
                resumeButton.setFont(bSetup.font);
                menuButton.setBounds(270, 700, 300, 75);
            }
            if (keyH.previousState == fightState){
                bSetup.setButtonsVisible(false, false, false, false, true, false, true, false);
                quitDungeonButton.setBounds(270, 700, 300, 75);
                resumeButton.setBounds(870, 700, 300, 75);
                resumeButton.setFont(bSetup.font);
                menuButton.setBounds(270, 700, 300, 75);
            }
            //TILE
            tileM.draw(g2);
            if(keyH.previousState == playState){
                // OBJECT
                for(int i = 0; i < obj.length; i++){
                    if(obj[i] != null){
                        obj[i].draw(g2, this);
                    }
                }
                if(player.worldY <= npc[0].worldX){
                    // PLAYER
                    player.draw(g2);
                }
                // NPC
                for(int i = 0; i < npc.length; i++){
                    if(npc[i] != null){
                        npc[i].draw(g2, this);
                    }
                }
                if(player.worldY > npc[0].worldX){
                    // PLAYER
                    player.draw(g2);
                }
            } else if (keyH.previousState == fightState) {
                // ENEMY
                for(int i = 0; i < enemy.length; i++){
                    if(enemy[i] != null){
                        enemy[i].draw(g2, this);
                    }
                }
                // PLAYER
                player.draw(g2);
                //BULLETS
                for(int i = 0; i < bullets.size(); i++){
                    if(bullets.get(i).active){
                        bullets.get(i).draw(g2);
                    } else {
                        bullets.remove(i--);
                    }
                }
            }
            // UI
            ui.draw(g2);
        }
        else if(gameState == instructionState){
            ui.draw(g2);
            bSetup.setButtonsVisible(false, false, true, false, false, false, false, false);
        }
        else if(gameState == preFightState || gameState == winState || gameState == deathState){
            ui.draw(g2);

            if(gameState == preFightState){
                bSetup.setButtonsVisible(false, false, false, true, true, false, false, false);
                quitDungeonButton.setBounds(570, 520, 300, 75);
            }
            if(gameState == winState){
                bSetup.setButtonsVisible(false, false, false, false, false, false, false, true);
                menuButton.setBounds(570, 520, 300, 75);
                keyH.previousState = winState;
            }
            if(gameState == deathState){
                bSetup.setButtonsVisible(false, false, false, false, true, true, false, false);
                quitDungeonButton.setBounds(570, 520, 300, 75);
            }
            //TILE
            tileM.draw(g2);
            // ENEMY
            for(int i = 0; i < enemy.length; i++){
                if(enemy[i] != null){
                    enemy[i].draw(g2, this);
                }
            }
            // PLAYER
            player.draw(g2);
            // UI
            ui.draw(g2);
        }
        else if(gameState == fightState){
            bSetup.setButtonsVisible(false, false, false, false, false, false, false, false);
            //TILE
            tileM.draw(g2);
            // ENEMY
            for(int i = 0; i < enemy.length; i++){
                if(enemy[i] != null){
                    enemy[i].draw(g2, this);
                }
            }
            // PLAYER
            player.draw(g2);
            // UI
            ui.draw(g2);
            //BULLETS
            for(int i = 0; i < bullets.size(); i++){
                if(bullets.get(i).active){
                    bullets.get(i).draw(g2);
                } else {
                    bullets.remove(i--);
                }
            }
        }
        else if(gameState == playState || gameState == dialogueState){
            bSetup.setButtonsVisible(false, false, false, false, false, false, false, false);
            //TILE
            tileM.draw(g2);
            // OBJECT
            for(int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    obj[i].draw(g2, this);
                }
            }
            if(player.worldY <= npc[0].worldY){
                // PLAYER
                player.draw(g2);
            }
            // NPC
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].draw(g2, this);
                }
            }
            if(player.worldY > npc[0].worldY){
                // PLAYER
                player.draw(g2);
            }
            // UI
            ui.draw(g2);
        }
    }

    public void shootBullet(int mouseX, int mouseY){
        int targetX = player.worldX - player.screenX + mouseX - 243;
        int targetY = player.worldY - player.screenY + mouseY - 140;
        bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, targetX - 24, targetY - 79, "Player"));
    }

    public void ultimate(){
        bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, player.worldX, player.worldY - 500, "Player"));
                bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, player.worldX + 500, player.worldY - 500, "Player"));
                bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, player.worldX + 500, player.worldY, "Player"));
                bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, player.worldX + 500, player.worldY + 500, "Player"));
                bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, player.worldX, player.worldY + 500, "Player"));
                bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, player.worldX - 500, player.worldY + 500, "Player"));
                bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, player.worldX - 500, player.worldY, "Player"));
                bullets.add(new Bullet(this, player, "/res/other/bullet", player.worldX, player.worldY, player.worldX - 500, player.worldY - 500, "Player"));
    }

    public boolean areEnemiesAlive() {
        for (int i = 0; i < enemy.length; i++) {
            if (enemy[i] != null && enemy[i].life > 0) {
                return true;
            }
        }
        return false;
    }

    public void restartGame() {
        player.setDefaultValues();
        aSetup.setObject();
        aSetup.setNPC();
        aSetup.setEnemy();
        bullets.clear();
        tileM.usingDungeonMap = false;
    }
}
