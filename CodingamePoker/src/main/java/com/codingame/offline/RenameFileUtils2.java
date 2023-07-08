package com.codingame.offline;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class RenameFileUtils2 {

  public static void main(String[] args) {
    Path dirpath = Paths.get("src\\main\\resources\\view\\assets\\cards4");
    File dir = dirpath.toFile();
    if (dir.isDirectory()) { // make sure it's a directory
      // for (File f : dir.listFiles()) {
      // try {
      // String newName = f.getName();
      // if (newName.endsWith("2.png")) {
      // newName = newName.replace("2.png", ".png");
      // Path newPath = dir.toPath().resolve(newName);
      // if (newPath.toFile().delete()) {
      // System.out.println("delete " + newPath);
      // } else {
      // System.out.println("delete failed " + newPath);
      // }
      // }
      // } catch (Exception e) {
      // e.printStackTrace();
      // }
      // }
      for (File f : dir.listFiles()) {
        try {
          String newName = f.getName().replaceAll("X", "S");
          Path newPath = dir.toPath().resolve(newName);
          System.err.println(newPath.toAbsolutePath());
          if (f.renameTo(newPath.toFile())) {
            System.out.println("Rename succesful");
          } else {
            System.out.println("Rename failed");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static void trimImage(File file) throws IOException {
    BufferedImage image = ImageIO.read(file);
    image = trimImage(image);
    ImageIO.write(image, "PNG", file);
  }

  private static BufferedImage trimImage(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int top = height / 2;
    int bottom = top;
    int left = width / 2;
    int right = left;
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (image.getRGB(x, y) != 0) {
          top = Math.min(top, y);
          bottom = Math.max(bottom, y);
          left = Math.min(left, x);
          right = Math.max(right, x);
        }
      }
    }
    return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
  }
  
}
