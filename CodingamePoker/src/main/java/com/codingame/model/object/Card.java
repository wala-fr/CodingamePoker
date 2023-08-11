package com.codingame.model.object;

import java.util.Objects;
import com.codingame.model.object.enumeration.Rank;
import com.codingame.model.object.enumeration.Suit;
import com.codingame.model.utils.CardUtils;

public class Card implements Comparable<Card> {

  private Rank rank;
  private Suit suit;

  public Card(Rank rank, Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  public Rank getRank() {
    return rank;
  }

  public Suit getSuit() {
    return suit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, suit);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Card other = (Card) obj;
    return rank == other.rank && suit == other.suit;
  }

  @Override
  public String toString() {
    if (this == CardUtils.BURNED) {
      return CardUtils.BURNED_STR;
    }
    if (this == CardUtils.ELIMINATED) {
      return CardUtils.ELIMINATED_STR;
    }
    return rank.getName() + suit.getName();
  }

  @Override
  public int compareTo(Card o) {
    return rank.ordinal() - o.rank.ordinal();
  }

  public String getLabel() {
    return (rank.toString() + " of " + suit.toString()).toUpperCase();
  }

}


