package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class Entity {
    
    GamePanel gp;
    
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, left4, right1, right2, right3, right4;
    public String direction;
    public String name;

    public int spriteCounter = 0;
    public int spriteNum = 2;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY, solidAreaDefaultWidth, solidAreaDefaultHeight;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;

    //CHARACTER LIFE
    public int maxLife;
    public int life;

    public Entity(GamePanel gp){
        this.gp = gp;
        this.solidAreaDefaultX = solidArea.x;
        this.solidAreaDefaultY = solidArea.y;
    }

    public void draw(Graphics2D g2, GamePanel gp){}

    public void update(){}

    public void setAction(){}

    public void speak(){}

    public BufferedImage setup(String ImagePath, float widthScale, float heightScale){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        int scaledHeight = (int)(gp.tileSize * heightScale);
        int scaledWidth = (int)(gp.tileSize * widthScale);

        try {
            image = ImageIO.read(getClass().getResourceAsStream(ImagePath + ".png"));
            image = uTool.scaleImage(image, scaledWidth, scaledHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
