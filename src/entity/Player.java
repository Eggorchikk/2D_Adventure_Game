package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{

    KeyHandler keyH;

    public int screenX;
    public int screenY;
    public int hasKey;
    public int hasLinkedKey;
    public int hasIronKey;
    public int attackCounter;
    public int ultimateCounter;
    public final float heightScale = 1.6f;
    public final float widthScale = 1f;

    public Player(GamePanel gp, KeyHandler keyH){

        super(gp);
        this.gp = gp;
        this.keyH = keyH;
        name = "Player";

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(17, 16, 14, 20);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 5;
        direction = "down";
        maxLife = 5;
        life = maxLife;
        hasKey = 0;
        hasIronKey = 0;
        hasLinkedKey = 0;
        attackCounter = 0;
        ultimateCounter = 0;
    }

    public void getPlayerImage(){

        up1 = setup("/res/player/player_up1", widthScale, heightScale);
        up2 = setup("/res/player/player_up2", widthScale, heightScale);
        up3 = setup("/res/player/player_up3", widthScale, heightScale);
        down1 = setup("/res/player/player_down1", widthScale, heightScale);
        down2 = setup("/res/player/player_down2", widthScale, heightScale);
        down3 = setup("/res/player/player_down3", widthScale, heightScale);
        left1 = setup("/res/player/player_left1", widthScale, heightScale);
        left2 = setup("/res/player/player_left2", widthScale, heightScale);
        left3 = setup("/res/player/player_left3", widthScale, heightScale);
        right1 = setup("/res/player/player_right1", widthScale, heightScale);
        right2 = setup("/res/player/player_right2", widthScale, heightScale);
        right3 = setup("/res/player/player_right3", widthScale, heightScale);
        System.out.println("All images loaded");
    }

    public void update(){

        attackCounter++;
        ultimateCounter++;

        dialogueStart();

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){
            if(keyH.upPressed == true){
                direction = "up";
            }
            else if(keyH.downPressed == true){
                direction = "down";
            }
            else if(keyH.leftPressed == true){
                direction = "left";
            }
            else if(keyH.rightPressed == true){
                direction = "right";
            }

            // COLLISION CHECK
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objectIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objectIndex);

            // CHECK NPC COLLISION
            if(gp.gameState == gp.playState){
                int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
                interactNPC(npcIndex);
            }

            // CHECK ENEMY COLLISION
            if(gp.gameState == gp.fightState){
                int enemyIndex = gp.cChecker.checkEntity(this, gp.enemy);
                interactEnemy(enemyIndex);
            }

            // If COLLISION IS FLASE, PLAYER CAN'T MOVE
            if(!collisionOn){

                switch(direction){
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                    default:
                        break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 10){
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 3;
                }
                else if(spriteNum == 3){
                    spriteNum = 4;
                }
                else if(spriteNum == 4){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }   
    }

    public void pickUpObject(int i){

        if(i != 999){

            String objectName = gp.obj[i].name;

            switch(objectName){
                case "Key":
                    hasKey++;
                    gp.obj[i] = null;
                    System.out.println("Key" + hasKey);
                    break;
                case "Key_Linked":
                    hasLinkedKey++;
                    gp.obj[i] = null;
                    System.out.println("Key" + hasLinkedKey);
                    break;
                case "Key_Iron":
                    hasIronKey++;
                    gp.obj[i] = null;
                    System.out.println("Key" + hasIronKey);
                    break;
                case "Door":
                    if(hasKey > 0){
                        gp.obj[i] = null;
                        hasKey--;
                    } else {
                        gp.ui.showMessage("You need the right key!");
                    }
                    System.out.println("Key" + hasKey);
                    break;
                case "Door_Linked":
                    if(hasLinkedKey > 0){
                        for(int j = 0; j < gp.obj.length; j++) {
                            if(gp.obj[j] != null && gp.obj[j].name.equals("Door_Linked")){
                                gp.obj[j] = null;
                            }
                        }
                        hasLinkedKey--;
                    } else {
                        gp.ui.showMessage("You need the right key!");
                    }
                    System.out.println("Key" + hasLinkedKey);
                    break;
                case "Door_Iron":
                    if(hasIronKey > 0){
                        gp.obj[i] = null;
                        hasIronKey--;
                    } else{
                        gp.ui.showMessage("You need the right key!");
                    }
                    System.out.println("Key" + hasIronKey);
                    break;
                case "Stair":
                    gp.gameState = gp.preFightState;
                    keyH.downPressed = false;
                    keyH.upPressed = false;
                    keyH.leftPressed = false;
                    keyH.rightPressed = false;
                    worldX = 25 * gp.tileSize;
                    worldY = 21 * gp.tileSize;
                    break;
            }
        }
    }

    public void interactNPC(int i){
        
        if(i != 999) {
        }
    }

    public void interactEnemy(int i){
        
        if(i != 999) {
        }
    }

    public void dialogueStart(){
        
        for (int j = 0; j < gp.npc.length; j++) {
            if(gp.keyH.enterPressed){
                if (gp.npc[j] != null && gp.npc[j].name.equals("Girl")) {
                    if (gp.cChecker.isPlayerNear(gp.npc[j], 2)) {
                            gp.gameState = gp.dialogueState;
                            gp.npc[j].speak();
                    }
                }
            }
        }
        gp.keyH.enterPressed = false;
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        switch(direction){
            case "up":
                if(spriteNum == 1){
                    image = up1;
                }
                if(spriteNum == 2 || spriteNum == 4){
                    image = up2;
                }
                if(spriteNum == 3){
                    image = up3;
                }
                break;
            case "down":
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2 || spriteNum == 4){
                    image = down2;
                }
                if(spriteNum == 3){
                    image = down3;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2 || spriteNum == 4){
                    image = left2;
                }
                if(spriteNum == 3){
                    image = left3;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2 || spriteNum == 4){
                    image = right2;
                }
                if(spriteNum == 3){
                    image = right3;
                }
                break;
            default:
                break;
        }
        if(keyH.upPressed == false && keyH.downPressed == false && keyH.leftPressed == false && keyH.rightPressed == false){
            switch (direction) {
                case "up":
                    image = up2;
                    break;
                case "down":
                    image = down2;
                    break;
                case "left":
                    image = left2;
                    break;
                case "right":
                    image = right2;
                    break;
            }
        }
        if(gp.gameState == gp.pauseState){
            switch (direction) {
                case "up":
                    image = up2;
                    break;
                case "down":
                    image = down2;
                    break;
                case "left":
                    image = left2;
                    break;
                case "right":
                    image = right2;
                    break;
            }
        }
        if(gp.gameState == gp.dialogueState){
            image = down2;
        }
        int yOffset = (int)(gp.tileSize * (heightScale - 1));
        g2.drawImage(image, screenX, screenY - yOffset, null);
    }
}
