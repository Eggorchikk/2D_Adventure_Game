package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Obj_Key_Linked extends SuperObject{

    GamePanel gp;

    public Obj_Key_Linked(GamePanel gp){

        this.gp = gp;

        name = "Key_Linked";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/key_wood.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}