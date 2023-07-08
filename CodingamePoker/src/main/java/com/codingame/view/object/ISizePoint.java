package com.codingame.view.object;

public interface ISizePoint extends IPoint {

  SizePoint getPointSize();

  default int getWidth() {
    return getPointSize().getWidth();
  }

  default int getHeight() {
    return getPointSize().getHeight();
  }
}
