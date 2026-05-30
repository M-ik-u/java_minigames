package com.example.minigames.game;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RouletteEngine {

    public enum BetType { NUMBER, RED, BLACK, EVEN, ODD }

    private static final Set<Integer> RED = Set.of(
            1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36
    );

    public static class Bet {
        public final BetType type;
        public final Integer number; // только для NUMBER
        public final BigDecimal amount;
        public Bet(BetType type, Integer number, BigDecimal amount) {
            this.type = type;
            this.number = number;
            this.amount = amount;
        }
    }

    public static class Outcome {
        public final int number;
        public final BigDecimal totalBet;
        public final BigDecimal totalPayout;
        public Outcome(int number, BigDecimal totalBet, BigDecimal totalPayout) {
            this.number = number;
            this.totalBet = totalBet;
            this.totalPayout = totalPayout;
        }
        public int getNumber() { return number; }
        public BigDecimal getTotalBet() { return totalBet; }
        public BigDecimal getTotalPayout() { return totalPayout; }
    }

    private final Random rng;

    public RouletteEngine() { this(new SecureRandom()); }
    public RouletteEngine(Random rng) { this.rng = rng; }

    public Outcome spin(List<Bet> bets) {
        int n = rng.nextInt(37); // 0..36
        return new Outcome(n, total(bets), payout(bets, n));
    }

    public BigDecimal total(List<Bet> bets) {
        BigDecimal s = BigDecimal.ZERO;
        for (Bet b : bets) s = s.add(b.amount);
        return s;
    }

    public BigDecimal payout(List<Bet> bets, int n) {
        BigDecimal p = BigDecimal.ZERO;
        for (Bet b : bets) {
            if (wins(b, n)) p = p.add(b.amount.multiply(multiplier(b.type)));
        }
        return p;
    }

    private boolean wins(Bet b, int n) {
        return switch (b.type) {
            case NUMBER -> b.number != null && b.number == n;
            case RED -> n != 0 && RED.contains(n);
            case BLACK -> n != 0 && !RED.contains(n);
            case EVEN -> n != 0 && n % 2 == 0;
            case ODD -> n != 0 && n % 2 == 1;
        };
    }

    private BigDecimal multiplier(BetType t) {
        return t == BetType.NUMBER ? BigDecimal.valueOf(36) : BigDecimal.valueOf(2);
    }

    public static boolean isRed(int n) {
        return n != 0 && RED.contains(n);
    }
}
