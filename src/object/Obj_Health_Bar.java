package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Obj_Health_Bar extends SuperObject{

    GamePanel gp;

    public Obj_Health_Bar(GamePanel gp){

        this.gp = gp;

        name = "Healh_Bar";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/healthBar/health_bar-1.png.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/res/healthBar/health_bar-2.png.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/res/healthBar/health_bar-3.png.png"));
            image4 = ImageIO.read(getClass().getResourceAsStream("/res/healthBar/health_bar-4.png.png"));
            image5 = ImageIO.read(getClass().getResourceAsStream("/res/healthBar/health_bar-5.png.png"));
            image6 = ImageIO.read(getClass().getResourceAsStream("/res/healthBar/health_bar-6.png.png"));
            image = uTool.scaleImage(image, gp.tileSize * 5, gp.tileSize);
            image2 = uTool.scaleImage(image2, gp.tileSize * 5, gp.tileSize);
            image3 = uTool.scaleImage(image3, gp.tileSize * 5, gp.tileSize);
            image4 = uTool.scaleImage(image4, gp.tileSize * 5, gp.tileSize);
            image5 = uTool.scaleImage(image5, gp.tileSize * 5, gp.tileSize);
            image6 = uTool.scaleImage(image6, gp.tileSize * 5, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
