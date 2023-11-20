package com.codingame.view.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.view.CardSprite;

public class GlobalViewData {

  private List<Integer> cardIds = new ArrayList<>();
  private List<Integer> debugCardIds = new ArrayList<>();
  private Map<Integer, List<Integer>> showOpponentCardIds = new TreeMap<>();
  private List<Integer> winTextIds = new ArrayList<>();
  private List<Integer> percentTimeIds = new ArrayList<>();
  private List<Integer> foldCardIds = new ArrayList<>();

  public void addCard(CardSprite card) {
    cardIds.add(card.getCardId());
    debugCardIds.add(card.getDebugCardId());
  }

  public void addShowOpponentCard(Sprite card, int playerId) {
    showOpponentCardIds.computeIfAbsent(playerId, k -> new ArrayList<>()).add(card.getId());
  }

  public void addWinText(Entity text) {
    winTextIds.add(text.getId());
  }

  public void addPercentTime(Entity text) {
    percentTimeIds.add(text.getId());
  }

  public void addFoldCard(CardSprite cardSprite) {
    foldCardIds.add(cardSprite.getCardId());
    foldCardIds.add(cardSprite.getDebugCardId());
    foldCardIds.add(cardSprite.getShowCardId());
  }

  public String serialize() {
    List<String> lines = new ArrayList<>();
    lines.add(SerializeUtils.serialize(cardIds));
    lines.add(SerializeUtils.serialize(debugCardIds));
    lines.add(SerializeUtils.serialize(showOpponentCardIds));
    lines.add(SerializeUtils.serialize(foldCardIds));
    lines.add(SerializeUtils.serialize(percentTimeIds));
    lines.add(SerializeUtils.serialize(winTextIds));
    return lines.stream().collect(Collectors.joining("\n"));
  }

}
