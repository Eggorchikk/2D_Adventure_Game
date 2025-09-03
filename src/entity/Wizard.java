package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Wizard extends Entity{

    private final float heightScale = 2.8f;
    private final float widthScale = 1.4f;
    private int atackCounter = 0;

    public Wizard(GamePanel gp){
        super(gp);

        name = "Wizard";
        solidArea.x = 10;
        solidArea.y = -(int)(gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidAreaDefaultWidth = solidArea.width;
        solidAreaDefaultHeight = solidArea.height;
        maxLife = 2;
        life = maxLife;

        direction = "right";
        getImage();
    }

    public void getImage(){

        left1 = setup("/res/wizard/wizard_sprites-1.png", widthScale, heightScale);
        left2 = setup("/res/wizard/wizard_sprites-2.png", widthScale, heightScale);
        left3 = setup("/res/wizard/wizard_sprites-3.png", widthScale, heightScale);
        left4 = setup("/res/wizard/wizard_sprites-4.png", widthScale, heightScale);
        right1 = setup("/res/wizard/wizard_sprites-5.png", widthScale, heightScale);
        right2 = setup("/res/wizard/wizard_sprites-6.png", widthScale, heightScale);
        right3 = setup("/res/wizard/wizard_sprites-7.png", widthScale, heightScale);
        right4 = setup("/res/wizard/wizard_sprites-8.png", widthScale, heightScale);
    }

    public void setAction() {

        atackCounter++;
        if(atackCounter > 150){
            gp.bullets.add(new Bullet(gp, this, "/res/other/fireball", worldX, worldY, gp.player.worldX, gp.player.worldY, "Wizard"));
            atackCounter = 0;
        }
    }

    @Override
    public void update(){

        if(life <= 0){
            solidArea = new Rectangle(0, 0, 0, 0);
            return;
        }

        setAction();

        gp.cChecker.checkEntity(this, gp.enemy);
        gp.cChecker.checkPlayer(this);

        if(gp.player.worldX > worldX){
            direction = "right";
        } else {
            direction = "left";
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


        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
            worldY - 2 * gp.tileSize < gp.player.worldY + gp.player.screenY){
            switch(direction){
                case "left":
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
                    break;
                case "right":
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
                    break;
                default:
                    break;
            }
                
            g2.drawImage(image, screenX, screenY - yOffset, null);

            int screenSolidX = screenX + solidArea.x;
            int screenSolidY = screenY + solidArea.y;
        }
    }
}
