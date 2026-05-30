package com.example.minigames.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class GameSession {
    private long id;
    private long userId;
    private String game;
    private BigDecimal bet;
    private BigDecimal payout;
    private String result;
    private OffsetDateTime createdAt;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public String getGame() { return game; }
    public void setGame(String game) { this.game = game; }
    public BigDecimal getBet() { return bet; }
    public void setBet(BigDecimal bet) { this.bet = bet; }
    public BigDecimal getPayout() { return payout; }
    public void setPayout(BigDecimal payout) { this.payout = payout; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
