package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Obj_Stair extends SuperObject{

    GamePanel gp;

    public Obj_Stair(GamePanel gp){

        this.gp = gp;

        name = "Stair";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/stair.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
