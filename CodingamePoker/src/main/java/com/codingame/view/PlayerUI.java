package com.codingame.view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.RoundedRectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.TextBasedEntity.TextAlign;
import com.codingame.model.object.Board;
import com.codingame.model.object.FiveCardHand;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.enumeration.Position;
import com.codingame.model.utils.AssertUtils;
import com.codingame.view.object.Graphic;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;

public class PlayerUI {

  private Group avatarGroup;
  private Group labelGroup;

  private Player player;

  private int id;
  private Text action;
  private Text stack;
  private Text position;
  private Text message;

  private boolean folded = false;
  private boolean eliminated = false;

  private RoundedRectangle playerRectangle;
  private Text name;
  private Sprite avatar;

  public PlayerUI(Player player, Graphic graphics) {
    id = player.getIndex();
    this.player = player;
    PlayerUICoordinates coordinates = ViewConstant.COORDINATES[id];
    createAvatar(player, graphics, coordinates);
    createLabel(graphics, coordinates);
  }

  private void createAvatar(Player player, Graphic graphics, PlayerUICoordinates coordinates) {
    avatarGroup = graphics.getGraphics().createGroup();

    playerRectangle = coordinates.getAvatarFrame()
      .createRoundedRectangle(graphics)
      .setLineWidth(10)
      .setFillColor(ViewConstant.AVATAR_COLOR)
      .setLineColor(player.getColorToken());
    avatarGroup.add(playerRectangle);

    avatar = coordinates.getAvatar().create(graphics);
    avatar.setImage(player.getAvatarToken());
    avatarGroup.add(avatar);

    name = coordinates.getName().create(graphics);
    String nickname = player.getNicknameToken();
    name.setText(nickname);
    name.setTextAlign(TextAlign.CENTER);
    avatarGroup.add(name);
    
    graphics.getTooltips().setTooltipText(avatarGroup, " " + id + " ");// space otherwise id 0 bugs
  }

  private void createLabel(Graphic graphics, PlayerUICoordinates coordinates) {
    labelGroup = graphics.createGroup();
    action = graphics.createText();
    ViewUtils.createTextRectangle(action, coordinates.getAction(), false, graphics, labelGroup);
    stack = graphics.createText();
    ViewUtils.createTextRectangle(stack, coordinates.getStack(), false, graphics, labelGroup);
    position = graphics.createText();
    ViewUtils.createTextRectangle(position, coordinates.getPosition(), true, graphics, labelGroup);
    message = graphics.createText();
    ViewUtils.createTextRectangle(message, coordinates.getMessage(), false, graphics, labelGroup);
  }

  void update(Graphic graphics) {
    Board board = graphics.getBoard();
    PlayerModel player = board.getPlayers().get(id);
    if (!eliminated) {
      AssertUtils.test(player.getTotalBetAmount() <= board.getPot(), player.getTotalBetAmount(),
          board.getPot());
      ViewUtils.updateText(graphics, stack, "$ " + player.getStack(),
          "BET : $ " + player.getTotalBetAmount());
      updatePosition(graphics);
      if (graphics.isDeal()) {
        setLast(board);
      }
      if (!graphics.isEnd()) {
        ViewUtils.updateText(graphics, action, player.getMessage(board));
        folded = player.isFolded();
      }
      if (graphics.isEnd() && graphics.isEndRound() && !board.isOver()) {
        ViewUtils.clearText(graphics, action);
      }
      ViewUtils.updateText(graphics, message, this.player.getMessage());

    }
  }

  private void updatePosition(Graphic graphics) {
    Position pos = graphics.getBoard().getPosition(id);
    ViewUtils.updateText(graphics, position, pos.getSmallLabel(), pos.getLabel());
  }

  private void setLast(Board board) {
    PlayerModel player = board.getPlayer(id);
    if (!eliminated) {
      System.err.println("setLast " + this.id + " " + board.getNextPlayerId());
      // playerRectangle
      // .setFillColor(this.id == board.getNextPlayerId() ? ViewConstant.AVATAR_COLOR_CURRENT
      // : ViewConstant.AVATAR_COLOR);
      avatarGroup.setAlpha(this.id == board.getNextPlayerId() ? 1 : 0.5);
    }
  }

  public void setEliminated(Graphic graphics) {
    if (graphics.isEnd()) {
      return;
    }
    PlayerModel player = getPlayer(graphics);
    if (player.isEliminated() && !eliminated) {
      System.err.println("setEliminated " + id);
      eliminated = true;
      // ViewUtils.clearText(graphics, stack);
      // ViewUtils.clearText(graphics, action);
      // ViewUtils.clearText(graphics, position);
      // playerRectangle.setFillColor(ViewConstant.AVATAR_COLOR_ELIMINATED);
      // name.setFillColor(ViewConstant.AVATAR_NAME_COLOR_ELIMINATED);
      //
      // avatarGroup.setAlpha(0.5);
      avatarGroup.setVisible(false);
      labelGroup.setVisible(false);
      graphics.commitWorldState();
    }
  }

  public void setWinOrLose(Graphic graphics, boolean win) {
    // String message = win ? "WIN" : "LOSE";
    PlayerModel player = getPlayer(graphics);
    if (!eliminated) {
      FiveCardHand hand = player.getBestPossibleHand();
      ViewUtils.updateText(graphics, action, (hand != null ? hand.getHandType().getLabel() : ""));
      // playerRectangle.setFillColor(win ? ViewConstant.AVATAR_COLOR_WIN :
      // ViewConstant.AVATAR_COLOR_LOSE);
    }
  }

  public PlayerModel getPlayer(Graphic graphics) {
    Board board = graphics.getBoard();
    PlayerModel player = board.getPlayers().get(id);
    return player;
  }

  public boolean isFolded() {
    return folded;
  }

  public boolean isEliminated() {
    return eliminated;
  }

}
