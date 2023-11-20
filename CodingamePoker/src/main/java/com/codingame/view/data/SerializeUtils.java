package com.codingame.view.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SerializeUtils {

  public static String join(Object... args) {
    return Stream.of(args).map(String::valueOf).collect(Collectors.joining(" "));
  }

  public static String serialize(List<Integer> ints) {
    return serialize(ints, " ");
  }

  public static String serialize(Map<Integer, List<Integer>> ints) {
    List<Integer> all = new ArrayList<>();
    ints.forEach((k, v) -> all.addAll(v));
    return serialize(all, " ");
  }

  public static String serialize(List<Integer> ints, String delimiter) {
    return ints.stream().map(String::valueOf).collect(Collectors.joining(delimiter));
  }
}
