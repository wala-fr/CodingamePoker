package com.codingame.view.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.view.CardSprite;

public class GlobalViewData {
  
  private List<Integer> cardIds = new ArrayList<>();
  private List<Integer> debugCardIds = new ArrayList<>();
  private List<Integer> showOpponentCardIds = new ArrayList<>();
  private List<Integer> winTextIds = new ArrayList<>();

  public void addCard(CardSprite card) {
    cardIds.add(card.getCardId());
    debugCardIds.add(card.getDebugCardId());
  }
  
  public void addShowOpponentCard(Sprite card) {
    showOpponentCardIds.add(card.getId());
  }
  
  public void addWinText(Entity text) {
    winTextIds.add(text.getId());
  }

  public String serialize() {
    List<String> lines = new ArrayList<>();
    lines.add(SerializeUtils.serialize(cardIds));
    lines.add(SerializeUtils.serialize(debugCardIds));
    lines.add(SerializeUtils.serialize(showOpponentCardIds));
    lines.add(SerializeUtils.serialize(winTextIds));
    return lines.stream().collect(Collectors.joining("\n"));
  }

}
