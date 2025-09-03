package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.GamePanel;

public class Knight extends Entity{

    private final float heightScale = 1.8f;
    private final float widthScale = 1.0f;
    private int directionChangeCounter = 0;
    private int velocityX = 0;
    private int velocityY = 0;
    private Random random = new Random();
    private int attackCooldown = 0;

    public Knight(GamePanel gp){
        super(gp);

        name = "Knight";
        speed = 2;
        maxLife = 2;
        life = maxLife;
        direction = "left";

        solidArea.x = 0;
        solidArea.y = -20;
        solidArea.width = 48;
        solidArea.height = 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidAreaDefaultWidth = solidArea.width;
        solidAreaDefaultHeight = solidArea.height;
        

        getImage(); 
    }

    public void getImage(){

        left1 = setup("/res/knight/knight_sprites_left_stand", widthScale, heightScale);
        left2 = setup("/res/knight/knight_sprites_left_move", widthScale, heightScale);
        left3 = setup("/res/knight/knight_sprites_left_raise", widthScale, heightScale);
        left4 = setup("/res/knight/knight_sprites_left_attack", widthScale, heightScale);
        right1 = setup("/res/knight/knight_sprites_right_stand", widthScale, heightScale);
        right2 = setup("/res/knight/knight_sprites_right_move", widthScale, heightScale);
        right3 = setup("/res/knight/knight_sprites_right_raise", widthScale, heightScale);
        right4 = setup("/res/knight/knight_sprites_right_atack", widthScale, heightScale);
    }

    public void setAction() {
        directionChangeCounter++;

        if (directionChangeCounter >= 10) {
            int dx = gp.player.worldX - worldX;
            int dy = gp.player.worldY - worldY;

            double length = Math.sqrt(dx * dx + dy * dy);

            if (length != 0) {
                velocityX = (int)(speed * dx / length);
                velocityY = (int)(speed * dy / length);

                if (Math.abs(dx) > Math.abs(dy)) {
                    direction = dx > 0 ? "right" : "left";
                } else {
                    direction = dy > 0 ? "down" : "up";
                }
            } else {
                velocityX = 0;
                velocityY = 0;
                direction = "right";
            }

            directionChangeCounter = 0;
        }
    }

    private void chooseRandomDirection() {
        int dir = random.nextInt(4);
        switch(dir) {
            case 0:
                direction = "up";
                velocityX = 0;
                velocityY = -speed;
                break;
            case 1:
                direction = "down";
                velocityX = 0;
                velocityY = speed;
                break;
            case 2:
                direction = "left";
                velocityX = -speed;
                velocityY = 0;
                break;
            case 3:
                direction = "right";
                velocityX = speed;
                velocityY = 0;
                break;
        }
    }

    @Override
    public void update(){
        if(life <= 0){
            solidArea = new Rectangle(0, 0, 0, 0);
            return;
        }

        attackCooldown++;
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkEntity(this, gp.enemy);
        gp.cChecker.checkPlayer(this);

        // If COLLISION IS FLASE, PLAYER CAN'T MOVE
        if(!collisionOn){
            worldX += velocityX;
            worldY += velocityY;
        } else {
            if(collisionOn && attackCooldown > 100 && gp.cChecker.hitPlayer){
                gp.player.life --;
                attackCooldown = 0;
                gp.cChecker.hitPlayer = false;
            }
            chooseRandomDirection();
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
    
    @Override
    public void draw(Graphics2D g2, GamePanel gp){
        if(life <= 0){
            return;
        }

        BufferedImage image = null;

        int yOffset = (int)(gp.tileSize * (heightScale - 1));

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(gp.player.worldX <= worldX){
            if(spriteNum == 1){
                image = left4;
            }
            if(spriteNum == 2){
                image = left1;
            }
            if(spriteNum == 3){
                image = left2;
            }
            if(spriteNum == 4){
                image = left3;
            }
        }
        if(gp.player.worldX > worldX){
            if(spriteNum == 1){
                image = right4;
            }
            if(spriteNum == 2){
                image = right1;
            }
            if(spriteNum == 3){
                image = right2;
            }
            if(spriteNum == 4){
                image = right3;
            }
        }
        if(gp.gameState == gp.dialogueState || gp.gameState == gp.pauseState){

            if(image == right1 || image == right2 || image == right3 || image == right4){
                image = right1;
            } else {
                image = left1;
            }
        }
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
            worldY - 2 * gp.tileSize < gp.player.worldY + gp.player.screenY){
            g2.drawImage(image, screenX, screenY - yOffset, null);

            int screenSolidX = screenX + solidArea.x;
            int screenSolidY = screenY + solidArea.y;
        }
    }
}
