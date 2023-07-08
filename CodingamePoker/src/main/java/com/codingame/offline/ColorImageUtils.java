package com.codingame.offline;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class ColorImageUtils {

  public static void main(String[] args) {
    Path dirpath = Paths.get("src\\main\\resources\\view\\assets\\cards4");
    File dir = dirpath.toFile();
    if (dir.isDirectory()) { // make sure it's a directory
      for (File f : dir.listFiles()) {
        try {
          if (f.getName().contains("_")) {
            continue;
          }
          System.err.println(f.getName());
          String newName = f.getName().replaceAll(".png", "_R.png");
          Path destination = dir.toPath().resolve(newName);
          BufferedImage image = ImageIO.read(f);
          image = colorImage(image, 0xff0042);
          ImageIO.write(image, "PNG", destination.toFile());

          newName = f.getName().replaceAll(".png", "_G.png");
          destination = dir.toPath().resolve(newName);
          image = ImageIO.read(f);
          image = colorImage(image, 0x08ff00);
          ImageIO.write(image, "PNG", destination.toFile());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static BufferedImage colorImage(BufferedImage image, int c) {
    Color color = new Color(c);
    tint(image, color);
    return image;
  }

  public static BufferedImage colorImage(BufferedImage image, int red, int green, int blue) {
    Color color = new Color(red, green, blue);
    tint(image, color);
    return image;
  }

  public static void tint(BufferedImage image, Color color) {
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        Color pixelColor = new Color(image.getRGB(x, y), true);
        int r = (pixelColor.getRed() + color.getRed()) / 2;
        int g = (pixelColor.getGreen() + color.getGreen()) / 2;
        int b = (pixelColor.getBlue() + color.getBlue()) / 2;
        int a = pixelColor.getAlpha();
//        System.err.println(x + " " + y + " ---- " + pixelColor.getRGB());
//        if (pixelColor.getRGB() != -1) {
//          continue;
//        }
        int rgba = (a << 24) | (r << 16) | (g << 8) | b;
        image.setRGB(x, y, rgba);
      }
    }
  }
}
