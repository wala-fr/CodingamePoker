package com.codingame.view.object;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.view.parameter.ViewConstant;

public class Point implements IPoint {

  protected int x;
  protected int y;

  public Point(int x, int y) {
    super();
    this.x = x;
    this.y = y;
  }
  
  public Point copy() {
    return new Point(x,y);
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
  
  public void setPosition(Entity<?> e) {
    e.setX(x);
    e.setY(y);
  }
  
//  public void verticalSymetric() {
//    y = ViewConstant.HEIGHT - y;
//  }
//
//  public void horizonalSymetric() {
//    x = ViewConstant.WIDTH - x;
//  }

  @Override
  public Point getPoint() {
    return this;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
  
  
}
