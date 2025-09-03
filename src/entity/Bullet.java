package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Bullet extends Entity{

    public double x, y;
    public double dx, dy;
    public boolean active = true;
    private BufferedImage image;
    private String ignoreName;
    private String entityName;

    public Bullet(GamePanel gp, Entity entity, String fileNmae, int startX, int startY, int targetX, int targetY, String ignoreName){
        super(gp);

        name = "Bullet";
        solidArea = new Rectangle(0, 0, 16, 16);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        direction = "right";
        this.ignoreName = ignoreName;
        this.entityName = entity.name;

        this.y = startY - 15;
        this.x = startX;
        solidArea.x += 17;
        solidArea.y += 16;
        if(entity.name.equals("Wizard")){
            if(entity.direction == "right"){
                this.x = startX + 35;
                targetX -= 35;
                this.y = startY - 60;
            } else {
                this.x = startX - 15;
                targetX += 15;
                this.y = startY - 60;
            }
        }

        int speed = 4;
        targetY += 60;

        double angle = Math.atan2(targetY - startY, targetX - startX);
        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;

        image = setup(fileNmae, 1, 1);
    }

    public void update() {
        x += dx;
        y += dy;

        int bulletWorldX = (int) x;
        int bulletWorldY = (int) y;

        worldX = (int) x;
        worldY = (int) y;

        collisionOn = false;

        // Tile Collision
        gp.cChecker.checkTile(this);
        if (collisionOn) {
            active = false;
            return;
        }
        // Entity collision
        int otherIndex = checkBullet(this, gp.enemy, ignoreName);
        if (otherIndex != 999) {
            if(!entityName.equals("Wizard")){
                gp.enemy[otherIndex].life--;
            }
            active = false;
            return;
        }

        if(ignoreName != "Player"){
            Rectangle playerWorldRect = new Rectangle(
                gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y - 30,
                gp.player.solidArea.width,
                gp.player.solidArea.height + 40
            );

            Rectangle bulletRect = new Rectangle(
                bulletWorldX + solidArea.x,
                bulletWorldY + solidArea.y,
                solidArea.width,
                solidArea.height
            );

            if (bulletRect.intersects(playerWorldRect)) {
                gp.player.life--;
                active = false;
                return;
            }
        }

        if (x < 0 || y < 0 || x > gp.maxWorldWidth || y > gp.maxWorldHeight) {
            active = false;
        }
    }
    
    public void draw(Graphics2D g2) {
        int screenX = (int)(x - gp.player.worldX + gp.player.screenX);
        int screenY = (int)(y - gp.player.worldY + gp.player.screenY);
        g2.drawImage(image, screenX, screenY, null);
    }

    public int checkBullet(Entity entity, Entity[] target, String ignoreName){
        int index = 999;

        Rectangle entityArea = new Rectangle(
            entity.worldX + entity.solidArea.x,
            entity.worldY + entity.solidArea.y,
            entity.solidArea.width,
            entity.solidArea.height
        );

        for(int i = 0; i < target.length; i++){
            if(target[i] != null && target[i] != entity && !target[i].name.equals(ignoreName)){
                Rectangle targetArea = new Rectangle(
                    target[i].worldX + target[i].solidArea.x,
                    target[i].worldY + target[i].solidArea.y,
                    target[i].solidArea.width,
                    target[i].solidArea.height
                );

                if(entityArea.intersects(targetArea)){
                    entity.collisionOn = true;
                    index = i;
                    break;
                }
            }
        }

        return index;
    }
}
