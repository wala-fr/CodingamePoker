package com.codingame.view.object;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.Text.FontWeight;
import com.codingame.gameengine.module.entities.TextBasedEntity.TextAlign;
import com.codingame.view.parameter.ViewConstant;

public class TextPoint implements ISizePoint {

  private SizePoint point;
  private int fontSize;
  private FontWeight fontWeight = FontWeight.NORMAL;

  public TextPoint(SizePoint point, int fontSize, FontWeight fontWeight) {
    this.fontSize = fontSize;
    this.fontWeight = fontWeight;
    this.point = point.copy();
  }

  public TextPoint(int x, int y, int fontSize, FontWeight fontWeight, int width, int height) {
    this.fontSize = fontSize;
    this.fontWeight = fontWeight;
    this.point = new SizePoint(x, y, width, height);
  }

  public TextPoint(int x, int y) {
    this(x, y, ViewConstant.DEFAULT_FONT_SIZE, FontWeight.BOLD, 0, 0);
  }
  
  public TextPoint(int x, int y, int width) {
    this(x, y, ViewConstant.DEFAULT_FONT_SIZE, FontWeight.BOLD, width, 0);
  }

  public Text create(Graphic graphics) {
    Text text = graphics.getGraphics()
      .createText("")
      .setX(getX())
      .setY(getY())
      .setFontSize(fontSize)
      .setFontWeight(fontWeight)
      .setFontFamily(ViewConstant.FONT)
      // .setAnchorX(0.5)
      .setTextAlign(TextAlign.RIGHT)
      .setMaxWidth(point.getWidth() == 0 ? ViewConstant.TEXT_DEFAULT_WIDTH : point.getWidth())
      .setFillColor(ViewConstant.WRITE_COLOR);
    return text;
  }

  public TextPoint copy() {
    return new TextPoint(point, fontSize, fontWeight);
  }

  public int getFontSize() {
    return fontSize;
  }

  public FontWeight getFontWeight() {
    return fontWeight;
  }

  @Override
  public Point getPoint() {
    return point.getPoint();
  }

  @Override
  public SizePoint getPointSize() {
    return point;
  }

  @Override
  public String toString() {
    return "TextPoint [point=" + point + ", fontSize=" + fontSize + ", fontWeight=" + fontWeight
        + "]";
  }

}
