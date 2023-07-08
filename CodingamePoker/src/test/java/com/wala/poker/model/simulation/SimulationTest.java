package com.wala.poker.model.simulation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wala.poker.model.TestUtils;
import com.wala.poker.model.object.BoardInput;

public class SimulationTest {

  private static final Logger logger = LoggerFactory.getLogger(SimulationTest.class);

  @ParameterizedTest
  @MethodSource("extractFiles")
  public void testFile(Path path) {
    TestUtils.initParameter();
    SimulationInput input = new SimulationInput(path);
    input.executeActions();
  }
  
  public static Stream<Arguments> extractFiles() throws IOException {
    return TestUtils.extractFiles("src\\test\\resources\\turnSimulation");
  }
  
}
