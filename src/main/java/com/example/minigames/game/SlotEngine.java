package com.example.minigames.game;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

public class SlotEngine {
    public enum Symbol { CHERRY, LEMON, BELL, STAR, SEVEN }

    private static final Map<Symbol, Integer> PAYOUT = Map.of(
            Symbol.SEVEN, 50,
            Symbol.STAR, 20,
            Symbol.BELL, 10,
            Symbol.LEMON, 5,
            Symbol.CHERRY, 3
    );

    private final Random rng;

    public SlotEngine() { this(new SecureRandom()); }
    public SlotEngine(Random rng) { this.rng = rng; }

    public static class Spin {
        public final Symbol[] reels;
        public final BigDecimal payout;
        public Spin(Symbol[] reels, BigDecimal payout) {
            this.reels = reels;
            this.payout = payout;
        }
    }

    public Spin spin(BigDecimal bet) {
        Symbol[] vals = Symbol.values();
        Symbol[] r = new Symbol[3];
        for (int i = 0; i < 3; i++) r[i] = vals[rng.nextInt(vals.length)];
        BigDecimal payout = BigDecimal.ZERO;
        if (r[0] == r[1] && r[1] == r[2]) {
            payout = bet.multiply(BigDecimal.valueOf(PAYOUT.get(r[0])));
        }
        return new Spin(r, payout);
    }
}
