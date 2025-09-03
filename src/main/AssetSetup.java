package main;

import entity.Girl;
import entity.Knight;
import entity.Wizard;
import object.Obj_Door;
import object.Obj_Door_Iron;
import object.Obj_Door_Linked;
import object.Obj_Key;
import object.Obj_Key_Iron;
import object.Obj_Key_Linked;
import object.Obj_Stair;

public class AssetSetup {

    GamePanel gp;

    public AssetSetup(GamePanel gp){

        this.gp = gp;
    }

    public void setNPC(){

        gp.npc[0] = new Girl(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;
    }
    
    public void setObject(){

        gp.obj[0] = new Obj_Key_Iron(gp);
        gp.obj[0].worldX = 15 * gp.tileSize;
        gp.obj[0].worldY = 37 * gp.tileSize;

        gp.obj[1] = new Obj_Key_Linked(gp);
        gp.obj[1].worldX = 39 * gp.tileSize;
        gp.obj[1].worldY = 7 * gp.tileSize;

        gp.obj[2] = new Obj_Key(gp);
        gp.obj[2].worldX = 8 * gp.tileSize;
        gp.obj[2].worldY = 9 * gp.tileSize;

        gp.obj[3] = new Obj_Door_Linked(gp);
        gp.obj[3].worldX = 17 * gp.tileSize;
        gp.obj[3].worldY = 40 * gp.tileSize;

        gp.obj[4] = new Obj_Door_Linked(gp);
        gp.obj[4].worldX = 9 * gp.tileSize;
        gp.obj[4].worldY = 34 * gp.tileSize;

        gp.obj[5] = new Obj_Door(gp);
        gp.obj[5].worldX = 33 * gp.tileSize;
        gp.obj[5].worldY = 10 * gp.tileSize;

        gp.obj[6] = new Obj_Door_Iron(gp);
        gp.obj[6].worldX = 36 * gp.tileSize;
        gp.obj[6].worldY = 38 * gp.tileSize;

        gp.obj[7] = new Obj_Stair(gp);
        gp.obj[7].worldX = 36 * gp.tileSize;
        gp.obj[7].worldY = 42 * gp.tileSize;
    }

    public void setEnemy(){

        gp.enemy[0] = new Wizard(gp);
        gp.enemy[0].worldX = gp.tileSize * 28;
        gp.enemy[0].worldY = gp.tileSize * 23;

        gp.enemy[1] = new Wizard(gp);
        gp.enemy[1].worldX = gp.tileSize * 22;
        gp.enemy[1].worldY = gp.tileSize * 23;

        gp.enemy[2] = new Knight(gp);
        gp.enemy[2].worldX = gp.tileSize * 21;
        gp.enemy[2].worldY = gp.tileSize * 20;

        gp.enemy[3] = new Knight(gp);
        gp.enemy[3].worldX = gp.tileSize * 29;
        gp.enemy[3].worldY = gp.tileSize * 20;

        gp.enemy[4] = new Knight(gp);
        gp.enemy[4].worldX = gp.tileSize * 25;
        gp.enemy[4].worldY = gp.tileSize * 25;
    }

    public void resetEnemy(){

        gp.bullets.clear();

        for (int i = 0; i < gp.enemy.length; i++){
            if (gp.enemy[i] != null){
                gp.enemy[i].life = gp.enemy[i].maxLife;
                gp.enemy[i].solidArea.x = gp.enemy[i].solidAreaDefaultX;
                gp.enemy[i].solidArea.y = gp.enemy[i].solidAreaDefaultY;
                gp.enemy[i].solidArea.width = gp.enemy[i].solidAreaDefaultWidth;
                gp.enemy[i].solidArea.height = gp.enemy[i].solidAreaDefaultHeight;
            }
        }
        gp.enemy[0].worldX = gp.tileSize * 28;
        gp.enemy[0].worldY = gp.tileSize * 23;

        gp.enemy[1].worldX = gp.tileSize * 22;
        gp.enemy[1].worldY = gp.tileSize * 23;

        gp.enemy[2].worldX = gp.tileSize * 21;
        gp.enemy[2].worldY = gp.tileSize * 20;

        gp.enemy[3].worldX = gp.tileSize * 29;
        gp.enemy[3].worldY = gp.tileSize * 20;

        gp.enemy[4].worldX = gp.tileSize * 25;
        gp.enemy[4].worldY = gp.tileSize * 25;

        gp.player.life = gp.player.maxLife;
        gp.player.attackCounter = 0;
        gp.player.ultimateCounter = 0;
    }
}
