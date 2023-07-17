package com.codingame.view.object;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.RoundedRectangle;
import com.codingame.gameengine.module.entities.Sprite;

public class SizePoint implements ISizePoint {

  private Point point;
  private int width;
  private int height;

  public SizePoint(Point point, int width, int height) {
    this(point.getX(), point.getY(), width, height);
  }

  public SizePoint(int x, int y, int width, int height) {
    this.point = new Point(x, y);
    this.width = width;
    this.height = height;
  }

  public SizePoint copy() {
    return new SizePoint(point, width, height);
  }
  
  public Sprite create(Game graphics) {
    return create(graphics.getGraphics());
  }

  public Sprite create(GraphicEntityModule graphics) {
    Sprite ret = graphics.createSprite()
      .setX(getX())
      .setY(getY())
//      .setAnchorX(0.5)
      .setBaseWidth(width)
      .setBaseHeight(height);
    return ret;
  }
  
  public RoundedRectangle createRoundedRectangle(Game graphics) {
    return createRoundedRectangle(graphics.getGraphics());
  }
  
  public RoundedRectangle createRoundedRectangle(GraphicEntityModule graphics) {
   return graphics
    .createRoundedRectangle()
    .setX(getX())
    .setY(getY())
//    .setAnchorX(0.5)
    .setWidth(width)
    .setHeight(height);
  }

  public Point getPoint() {
    return point;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  @Override
  public SizePoint getPointSize() {
    return this;
  }

  @Override
  public String toString() {
    return "SizePoint [point=" + point + ", width=" + width + ", height=" + height + "]";
  }


}
