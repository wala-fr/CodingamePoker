package com.codingame.view;

import com.codingame.game.Player;
import com.codingame.game.RefereeParameter;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.RoundedRectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.BlendableEntity.BlendMode;
import com.codingame.gameengine.module.entities.TextBasedEntity.TextAlign;
import com.codingame.model.object.FiveCardHand;
import com.codingame.model.object.PlayerModel;
import com.codingame.model.object.board.Board;
import com.codingame.model.object.enumeration.Position;
import com.codingame.model.utils.AssertUtils;
import com.codingame.view.object.Frame;
import com.codingame.view.object.Game;
import com.codingame.view.parameter.Color;
import com.codingame.view.parameter.ViewConstant;
import com.codingame.view.parameter.ViewUtils;
import com.codingame.win_percent.skeval.WinPercentUtils;

public class PlayerUI {

  private Group avatarGroup;
  private Group labelGroup;
  private RoundedRectangle playerRectangle;

  private Player player;
  private int id;

  private Text action;
  private Text stack;
  private Text win;
  // to print the win amount even when the opponent card are hidden in settings panel
  private Text winHide;

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

    playerRectangle = coordinates.getAvatarFrame()
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
    game.getGlobalViewData().addWinText(win);

    winHide = game.createText();
    ViewUtils.copy(win, winHide);
    labelGroup.add(winHide);
    game.getGlobalViewData().addWinText(winHide);

    position = game.createText();
    ViewUtils.createTextRectangle(position, coordinates.getPosition(), Color.RED, game, labelGroup);
    message = game.createText();
    ViewUtils.createTextRectangle(message, coordinates.getMessage(), Color.BLACK, game, labelGroup);
  }

  void update(Game game) {
    Board board = game.getBoard();
    PlayerModel player = board.getPlayer(id);
    if (!eliminated) {
      AssertUtils.test(player.getTotalBetAmount() <= board.getPot(), player.getTotalBetAmount(),
          board.getPot());
      setLast(game);
      updatePosition(game);
      ViewUtils.updateText(game, stack, "$ " + player.getStack(),
          "BET : $ " + player.getTotalBetAmount());
      clearWin(game, winHide);
      if (game.isFrame(Frame.LAST_END_CANCELLED)) {
        ViewUtils.clearText(game, action);
        clearWin(game, win);
      } else if (game.isFrame(Frame.END_HAND)) {
        ViewUtils.clearText(game, action);
        // add colored win amount at the end of each hand
        int amount = player.getWinAmount();
        boolean winner = amount >= 0;
        String w = ViewUtils.addSpaceBefore((winner ? "+" : "") + amount, 5);
        updateWin(game, win, w, winner);
        game.getTooltips().setTooltipText(win, ""); // to avoid bug showing it 2 times (winHide +
                                                    // win)
        updateWin(game, winHide, w, winner);
      } else {
        if (!RefereeParameter.CALCULATE_WIN_PERCENT || !game.getBoard().isCalculateWinChance()) {
          clearWin(game, win);
        } else {
          boolean sureWin;
          boolean sureLose;
          String w;
          String toolTipMessage;
          if (player.isFolded()) {
            w = ViewUtils.addSpaceBefore("0%", 5);
            toolTipMessage = "FOLDED";
            sureWin = false;
            sureLose = true;
          } else {
            double winPercent = WinPercentUtils.getWinPercent(id);
            int winPercentRounded = ViewUtils.round(winPercent);
            double splitPercent = WinPercentUtils.getSplitPercent(id);

            w = ViewUtils.addSpaceBefore(winPercentRounded + "%", 5);
            toolTipMessage = "WIN " + ViewUtils.roundTwoDecimal(winPercent) + "%"
                + ((RefereeParameter.CALCULATE_SPLIT_PERCENT && splitPercent > 0)
                    ? " - SPLIT " + ViewUtils.roundTwoDecimal(splitPercent) + "%"
                    : "");
            sureWin = WinPercentUtils.isSureWin(id);
            sureLose = WinPercentUtils.isSureLose(id);
          }
          ViewUtils.updateText(game, win, w, toolTipMessage);
          win.setFillColor(sureWin ? ViewConstant.WIN_COLOR
              : sureLose ? ViewConstant.LOSS_COLOR : ViewConstant.LABEL_TEXT_COLOR);
        }
        ViewUtils.updateText(game, message, this.player.getMessage());
        ViewUtils.updateText(game, action, player.getMessage(board));
      }
    }
  }

  private void clearWin(Game game, Text text) {
    ViewUtils.clearText(game, text);
    text.setFillColor(ViewConstant.LABEL_TEXT_COLOR);
  }

  private void updateWin(Game game, Text text, String w, boolean winner) {
    ViewUtils.updateText(game, text, w);
    text.setFillColor(winner ? ViewConstant.WIN_COLOR : ViewConstant.LOSS_COLOR);
  }


  private void updatePosition(Game game) {
    if (game.isFrame(Frame.DEAL_PLAYER)) {
      Position pos = game.getBoard().getPosition(id);
      ViewUtils.updateText(game, position, pos.getSmallLabel(), pos.getLabel());
    }
  }

  private void setLast(Game game) {
    if (!eliminated) {
      boolean highlight =
          this.id == game.getBoard().getNextPlayerId() && game.isFrame(Frame.ACTION);
      avatarGroup.setAlpha(highlight ? 1 : 0.5);
      playerRectangle
        .setFillColor(highlight ? ViewConstant.AVATAR_ACTIVE_COLOR : ViewConstant.AVATAR_COLOR);
    }
  }

  public void setEliminated(Game game) {
    if (!game.isFrame(Frame.DEAL_PLAYER)) {
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

  public void setFolded(boolean folded) {
    this.folded = folded;
  }

  public boolean isEliminated() {
    return eliminated;
  }

}
