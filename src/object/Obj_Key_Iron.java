package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Obj_Key_Iron extends SuperObject{

    GamePanel gp;

    public Obj_Key_Iron(GamePanel gp){

        this.gp = gp;

        name = "Key_Iron";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/key_iron.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
