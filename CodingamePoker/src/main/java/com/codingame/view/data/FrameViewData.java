package com.codingame.view.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.google.inject.Singleton;

@Singleton
public class FrameViewData {

  public String serialize() {
    List<String> lines = new ArrayList<>();
    return lines.stream().collect(Collectors.joining("\n"));
  }

}
