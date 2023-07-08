package com.codingame.offline;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class CompressImageUtils {

  public static void main(String[] args) {
    Path dirpath = Paths.get("src\\main\\resources\\view\\assets\\cards4");
    File dir = dirpath.toFile();
    if (dir.isDirectory()) { // make sure it's a directory
      for (File f : dir.listFiles()) {
        try {
          if (f.getName().contains("_")) {
            continue;
          }
          String outputPathStr = f.getPath().toString().replace("cards4", "cards5");//>.replace("png", "jpg");
          compressImage(f.toPath(), Paths.get(outputPathStr));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void compressImage(Path inputPath, Path outputPath) throws IOException {
    BufferedImage originalImage = ImageIO.read(inputPath.toFile());

      int targetWidth= originalImage.getWidth() / 2;
      int targetHeight= originalImage.getHeight() / 2;

      BufferedImage resizedImage = new BufferedImage(targetWidth , targetHeight, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics2D = resizedImage.createGraphics();
      graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
      graphics2D.dispose();
     ImageIO.write(resizedImage, "PNG", outputPath.toFile());
  }

}
