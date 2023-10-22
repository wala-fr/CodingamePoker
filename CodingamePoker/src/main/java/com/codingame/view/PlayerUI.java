package com.codingame.view;

import com.codingame.game.Player;
import com.codingame.game.RefereeParameter;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.RoundedRectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.TextBasedEntity.TextAlign;
import com.codingame.model.object.FiveCardHand;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.Position;
import com.codingame.model.utils.AssertUtils;
import com.codingame.model.utils.skeval.WinPercentUtils;
import com.codingame.view.object.Game;
import com.codingame.view.parameter.Color;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;

public class PlayerUI {

  private Group avatarGroup;
  private Group labelGroup;

  private Player player;
  private int id;

  private Text action;
  private Text stack;
  private Text win;
  private Text position;
  private Text message;

  private boolean folded = false;
  private boolean eliminated = false;

  public PlayerUI(Player player, Game game) {
    id = player.getIndex();
    this.player = player;
    PlayerUICoordinates coordinates = ViewUtils.getPlayerUICoordinates(game, id);
    createAvatar(player, game, coordinates);
    createLabel(game, coordinates);
  }

  private void createAvatar(Player player, Game game, PlayerUICoordinates coordinates) {
    avatarGroup = game.getGraphics().createGroup();

    RoundedRectangle playerRectangle = coordinates.getAvatarFrame()
      .createRoundedRectangle(game)
      .setLineWidth(10)
      .setFillColor(ViewConstant.AVATAR_COLOR)
      .setLineColor(player.getColorToken());
    avatarGroup.add(playerRectangle);

    Sprite avatar = coordinates.getAvatar().create(game);
    avatar.setImage(player.getAvatarToken());
    avatarGroup.add(avatar);

    Text name = coordinates.getName().create(game);
    String nickname = player.getNicknameToken();
    name.setText(nickname);
    name.setTextAlign(TextAlign.CENTER);
    avatarGroup.add(name);

    avatarGroup.setZIndex(ViewConstant.Z_INDEX_BOARD);

    game.getTooltips().setTooltipText(avatarGroup, " " + id + " ");// space otherwise id 0 bugs
  }

  private void createLabel(Game game, PlayerUICoordinates coordinates) {
    labelGroup = game.createGroup();
    action = game.createText();
    ViewUtils.createTextRectangle(action, coordinates.getAction(), Color.BLACK, game, labelGroup);
    stack = game.createText();
    ViewUtils.createTextRectangle(stack, coordinates.getStack(), Color.BLACK, game, labelGroup);

    win = game.createText();
    ViewUtils.createTextRectangle(win, coordinates.getWin(), Color.BLACK, game, labelGroup);

    position = game.createText();
    ViewUtils.createTextRectangle(position, coordinates.getPosition(), Color.RED, game, labelGroup);
    message = game.createText();
    ViewUtils.createTextRectangle(message, coordinates.getMessage(), Color.BLACK, game, labelGroup);
  }

  void update(Game game) {
    Board board = game.getBoard();
    PlayerModel player = board.getPlayers().get(id);
    if (!eliminated) {
      AssertUtils.test(player.getTotalBetAmount() <= board.getPot(), player.getTotalBetAmount(),
          board.getPot());
      ViewUtils.updateText(game, stack, "$ " + player.getStack(),
          "BET : $ " + player.getTotalBetAmount());
      if (board.isOver()) {
        int amount = player.getWinAmount();
        boolean winner = amount >= 0;
        String w = ViewUtils.addSpaceBefore((winner ? "+" : "") + amount, 5);
        ViewUtils.updateText(game, win, w);
        win.setFillColor(winner ? ViewConstant.WIN_COLOR : ViewConstant.LOSS_COLOR);
      } else {
        if (player.isFolded() || !RefereeParameter.CALCULATE_WIN_PERCENT
            || !game.getBoard().isCalculateWinChance()) {
          ViewUtils.clearText(game, win);
          win.setFillColor(ViewConstant.LABEL_TEXT_COLOR);
        } else {
          double winPercent = WinPercentUtils.getWinPercent(id);
          int winPercentRounded = ViewUtils.round(winPercent);
          double splitPercent = WinPercentUtils.getSplitPercent(id);

          String w = ViewUtils.addSpaceBefore(winPercentRounded + "%", 5);
          String toolTipMessage = "WIN " + ViewUtils.roundTwoDecimal(winPercent) + "%"
              + ((RefereeParameter.CALCULATE_SPLIT_PERCENT && splitPercent > 0)
                  ? " - SPLIT " + ViewUtils.roundTwoDecimal(splitPercent) + "%"
                  : "");
          ViewUtils.updateText(game, win, w, toolTipMessage);
          win.setFillColor(WinPercentUtils.isSureWin(id) ? ViewConstant.WIN_COLOR
              : WinPercentUtils.isSureLose(id) ? ViewConstant.LOSS_COLOR
                  : ViewConstant.LABEL_TEXT_COLOR);
        }
      }
      updatePosition(game);
      if (game.isDeal()) {
        setLast(board);
      }
      if (!game.isEnd()) {
        ViewUtils.updateText(game, action, player.getMessage(board));
        folded = player.isFolded();
      }
      if (game.isLastHandCanceled()) {
        ViewUtils.clearText(game, action);
      }
      ViewUtils.updateText(game, message, this.player.getMessage());

    }
  }

  private void updatePosition(Game game) {
    Position pos = game.getBoard().getPosition(id);
    ViewUtils.updateText(game, position, pos.getSmallLabel(), pos.getLabel());
  }

  private void setLast(Board board) {
    if (!eliminated) {
      avatarGroup.setAlpha(this.id == board.getNextPlayerId() ? 1 : 0.5);
    }
  }

  public void setEliminated(Game game) {
    if (game.isEnd()) {
      return;
    }
    PlayerModel player = getPlayer(game);
    if (player.isEliminated() && !eliminated) {
      eliminated = true;
      avatarGroup.setVisible(false);
      labelGroup.setVisible(false);
      game.commitWorldState();
    }
  }

  public void setWinOrLose(Game game, boolean win) {
    // String message = win ? "WIN" : "LOSE";
    PlayerModel player = getPlayer(game);
    if (!eliminated) {
      FiveCardHand hand = player.getBestPossibleHand();
      if (hand != null) {
        ViewUtils.updateText(game, action, hand.getShortLabel(), hand.getLabel());
      }
      // playerRectangle.setFillColor(win ? ViewConstant.AVATAR_COLOR_WIN :
      // ViewConstant.AVATAR_COLOR_LOSE);
    }
  }

  public PlayerModel getPlayer(Game game) {
    Board board = game.getBoard();
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
