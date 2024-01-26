package fr.cotedazur.univ.polytech.model.bot;

public enum DispatchState {
    TWOGOLDS("2golds"),
    DRAWCARD("drawCard"),

    CANTPLAY("cantPlay"),
    PLACEDISTRICT("putDistrict"),
    EARNDISTRICT_BISHOP("EarnDistrictBishop"),
    EARNDISTRICT_WARLORD("EarnDistrictWarlord"),
    EARNDISTRICT_KING("EarnDistrictKing"),
    EARNDISTRICT_MERCHANT("EarnDistrictMerchant"),
    STEAL("Steal"),
    DESTROY("Destroy"),
    KILL("Kill"),
    EXCHANGE("Exchange"),
    EXCHANGEDECK("ExchangeDeck"),
    EXCHANGEPLAYER("ExchangePlayer"),
    CARDSNOTWANTED("cardsNotWanted"),
    CARDSWANTED("cardsWanted");
    private String s;

    DispatchState(String s) {
        this.s = s;
    }

    public static DispatchState valueOfLabel(String label) {
        for (DispatchState e : values()) {
            if (e.s.equals(label)) {
                return e;
            }
        }
        return null;
    }

    public String getString() {
        return s;
    }

    @Override
    public String toString() {
        return s;
    }
}

