package com.codingame.view.object;

public interface IPoint {

  Point getPoint();

  default int getX() {
    return getPoint().getX();
  }
  
  default int getY() {
    return getPoint().getY();
  }
  
  default void setX(int x) {
    getPoint().setX(x);
  }

  default void setY(int y) {
    getPoint().setY(y);
  }
  
  default void add(IPoint p) {
    setX(getX() + p.getX());
    setY(getY() + p.getY());
  }
}
