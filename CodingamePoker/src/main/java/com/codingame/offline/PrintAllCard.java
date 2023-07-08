package com.codingame.offline;

import com.codingame.model.object.Card;
import com.codingame.model.object.Constant;

public class PrintAllCard {

  public static void main(String[] args) {
//    System.out.println("<style type=\"text/css\">\r\n"
//        + "table, th, td {\r\n"
//        + "  border: 1px solid black;\r\n"
//        + "  border-collapse: collapse;\r\n"
//        + "}"
//        + "td, th {align:center}"
//        + "</style>");
    System.out.println("<h1>cards</h1>");
    System.out.println("<table>");
    System.out.println("<tr><th>" + "small name" + "</th><th>" + "full name"  + "</th><th>"
        + "image" +"</th></tr>");
    for (Card card : Constant.ALL_CARDS) {
      System.out.println("<tr><th align=\"center\">" + card.toString() + "</th><td align=\"center\">" + card.getLabel() + "</td><td  align=\"center\">"
          + "<img src=\"../../../../../../resources/view/assets/cards4/"
          + card + ".png\" width=\"50\"/></td></tr>");
    }
    System.out.println("</table>");

  }
}
