package digital.moveto.botinok.client.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static Color getBottomLeftColor(String imageUrl) throws IOException {
        BufferedImage image = ImageIO.read(new URL(imageUrl));

        int x = 0;
        int y = image.getHeight() - 1;

        int rgb = image.getRGB(x, y);
        return new Color(rgb);
    }
}