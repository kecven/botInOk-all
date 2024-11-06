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

    public static void main(String[] args) {
        try {
            Color color = getBottomLeftColor("https://media.licdn.com/dms/image/v2/D4D35AQHQoNemeXfl9w/profile-framedphoto-shrink_100_100/profile-framedphoto-shrink_100_100/0/1727156752906?e=1731492000&v=beta&t=n9rYQJV5v7QyZl6vZOaVs9Eoc6dnhgAXRZtNNGYVcaA");
            System.out.println("RGB: " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }
}