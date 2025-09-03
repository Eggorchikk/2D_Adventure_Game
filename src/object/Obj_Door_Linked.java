package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Obj_Door_Linked extends SuperObject{

    GamePanel gp;

    public Obj_Door_Linked(GamePanel gp){

        this.gp = gp;

        name = "Door_Linked";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/door.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}