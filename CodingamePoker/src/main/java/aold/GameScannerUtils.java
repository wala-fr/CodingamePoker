package aold;
//package fr.wala.poker.utils;
//
//import java.util.List;
//import java.util.Random;
//import java.util.Scanner;
//import java.util.stream.Collectors;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import fr.wala.poker.object.Board;
//import fr.wala.poker.object.Player;
//import fr.wala.poker.variable.Parameter;
//
//public class GameScannerUtils {
//
//  private static final Logger logger = LoggerFactory.getLogger(GameScannerUtils.class);
//
//  public static void main(String[] args) {
//    Scanner scanner = new Scanner(System.in);
//    RandomUtils.init(new Random(0));
//    Board board = new Board(Parameter.PLAYER_MAX_NB);
//    board.reset();
//    board.initBlind();
//    board.initDeck();
//    board.dealFirst();
//    logger.info("{}", board);
//    while (true) {
//      while (!board.isTimeToDeal()) {
//        logger.info("{}", board);
//        logger.info("player {} bet", board.getNextPlayerId());
//        int bet = scanner.nextInt();
//        board.doBet(bet);
//      }
//      if (board.getBoardCards().size() == 5) {
//        break;
//      }
//      board.dealBoardCards();
//
//    }
//    List<Player> winners = board.findWinner();
//    // UPDATE PLAYER STACKS
//    logger.info("winners {}", winners.stream().map(p -> p.getId()).collect(Collectors.toList()));
//
//    logger.info("{}", board);
////    board.dealTurn();
////    logger.info("{}", board);
////    board.dealRiver();
////    logger.info("{}", board);
//  }
//  
// 
//}
