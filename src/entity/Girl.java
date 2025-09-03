package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.GamePanel;

public class Girl extends Entity{

    private final float heightScale = 1.8f;
    private final float widthScale = 2.2f;
    private final float heightScaleSpecial = 1.7f;
    String dialogues[] = new String[20];
    private int dialogueIndex = 0;
    private boolean delayActive = false;
    private int delayCounter = 0;

    public Girl(GamePanel gp){
        super(gp);

        name = "Girl";
        direction = "down";
        speed = 1;
        spriteNum = 2;
        getImage();
        setDialogue();

        solidArea = new Rectangle(30, 0, 45, 30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

    public void getImage(){

        up1 = setup("/res/girl/girl_up1", widthScale, heightScale);
        up2 = setup("/res/girl/girl_up2", widthScale, heightScale);
        up3 = setup("/res/girl/girl_up3", widthScale, heightScale);
        down1 = setup("/res/girl/girl_down1", widthScale, heightScale);
        down2 = setup("/res/girl/girl_down2", widthScale, heightScale);
        down3 = setup("/res/girl/girl_down3", widthScale, heightScale);
        left1 = setup("/res/girl/girl_left1", widthScale, heightScaleSpecial);
        left2 = setup("/res/girl/girl_left2", widthScale, heightScaleSpecial);
        left3 = setup("/res/girl/girl_left3", widthScale, heightScaleSpecial);
        right1 = setup("/res/girl/girl_right1", widthScale, heightScaleSpecial);
        right2 = setup("/res/girl/girl_right2", widthScale, heightScaleSpecial);
        right3 = setup("/res/girl/girl_right3", widthScale, heightScaleSpecial);

    }

    public void setDialogue(){
        dialogues[0] = "Greetings, traveler!\n" + 
                        "I'm glad to welcome you to our region. \n" + 
                        "Would you be willing to help us?..\n";
        dialogues[1] = "For ages, our village has suffered from raids \n" + 
                        "by the dungeon dwellers.\n" + 
                        "They plunder our homes, steal our crops, \n" + 
                        "and kill our people... \n";
        dialogues[2] = "Find a way into the dungeon \n" + 
                        "and defeat the evil creatures!.. \n";
        dialogues[3] = "All our hope rests on you.\n" + "\n" + "\n" +
                        "Good luck on your journey! \n";
    }

    @Override
    public void speak(){
        
        if(dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
    }

    @Override
    public void setAction(){

        actionLockCounter++;

        if(actionLockCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if(i <= 25) {
                direction = "up";
            } else if(i > 25 && i <=50){
                direction = "down";
            } else if(i > 50 && i <=75){
                direction = "left";
            } else {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }

    public void triggerMovementDelay() {
        delayActive = true;
        delayCounter = 0;
        direction = "down";
        spriteNum = 2;
    }

    @Override
    public void update(){

        setAction();

        if (delayActive) {
            delayCounter++;
            if (delayCounter >= 90) { // 2 seconds at 60 FPS
                delayActive = false;
                delayCounter = 0;
            } else {
                return; // Skip rest of update to keep her still
            }
        }

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkPlayer(this);

        // If COLLISION IS TRUE, PLAYER CAN'T MOVE
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
        if(spriteCounter > 14){
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
    
    @Override
    public void draw(Graphics2D g2, GamePanel gp){

        BufferedImage image = null;

        int yOffset = (int)(gp.tileSize * (heightScale - 1));

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        switch(direction){
            case "left":
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                if(spriteNum == 3){
                    image = left3;
                }
                if(spriteNum == 4){
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                if(spriteNum == 3){
                    image = right3;
                }
                if(spriteNum == 4){
                    image = right2;
                }
                break;
            case "up":
                if(spriteNum == 1){
                    image = up1;
                }
                if(spriteNum == 2){
                    image = up1;
                }
                if(spriteNum == 3){
                    image = up3;
                }
                if(spriteNum == 4){
                    image = up3;
                }
                break;
            case "down":
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2){
                    image = down1;
                }
                if(spriteNum == 3){
                    image = down3;
                }
                if(spriteNum == 4){
                    image = down3;
                }
                break;
            default:
                break;
        }
        if(gp.gameState == gp.pauseState || collisionOn){
            switch (gp.player.direction) {
                case "up":
                    // direction = "down";
                    image = up2;
                    break;
                case "down":
                    // direction = "up";
                    image = down2;
                    break;
                case "left":
                    // direction = "right";
                    image = left2;
                    break;
                case "right":
                    // direction = "left";
                    image = right2;
                    break;
            }
        }

        if(gp.gameState == gp.dialogueState){
            image = down2;
        }
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
            worldY - 2 * gp.tileSize < gp.player.worldY + gp.player.screenY){
            g2.drawImage(image, screenX, screenY - yOffset, null);
        }
    }
}
