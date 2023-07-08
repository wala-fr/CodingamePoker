package com.wala.poker.model.object;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wala.poker.model.TestUtils;

public class BoardTest {

  private static final Logger logger = LoggerFactory.getLogger(BoardTest.class);

  @ParameterizedTest
  @MethodSource("extractFiles")
  public void testFile(Path path) {
    BoardInput input = new BoardInput(path);
    input.doAssertions();
  }
  
  public static Stream<Arguments> extractFiles() throws IOException {
    return TestUtils.extractFiles("src\\test\\resources\\bestHand");

  }

}
