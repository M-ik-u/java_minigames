package com.example.minigames.game;

import com.example.minigames.game.BlackjackEngine.Card;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Состояние одной партии блэкджека — хранится в HTTP-сессии. */
public class BlackjackRound implements Serializable {
    public final List<Card> deck;
    public final List<Card> player = new ArrayList<>();
    public final List<Card> dealer = new ArrayList<>();
    public BigDecimal bet;
    public boolean finished;
    public boolean doubled;
    public String outcome; // WIN / LOSE / PUSH / BJ
    public BigDecimal payout = BigDecimal.ZERO;

    public BlackjackRound(List<Card> deck, BigDecimal bet) {
        this.deck = deck;
        this.bet = bet;
    }

    public Card draw() {
        return deck.remove(deck.size() - 1);
    }

    public int playerScore() { return BlackjackEngine.score(player); }
    public int dealerScore() { return BlackjackEngine.score(dealer); }

    // Геттеры для EL (JSP). Поля публичные, но JSP EL читает только bean-свойства.
    public List<Card> getPlayer() { return player; }
    public List<Card> getDealer() { return dealer; }
    public BigDecimal getBet() { return bet; }
    public boolean isFinished() { return finished; }
    public boolean isDoubled() { return doubled; }
    public String getOutcome() { return outcome; }
    public BigDecimal getPayout() { return payout; }
    public int getPlayerScore() { return playerScore(); }
    public int getDealerScore() { return dealerScore(); }
}
