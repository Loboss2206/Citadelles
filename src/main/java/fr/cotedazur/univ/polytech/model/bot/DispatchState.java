package fr.cotedazur.univ.polytech.model.bot;

public enum DispatchState {
    TWO_GOLDS("2golds"),
    DRAW_CARD("drawCard"),

    CANT_PLAY("cantPlay"),
    PLACE_DISTRICT("putDistrict"),
    EARNDISTRICT_BISHOP("EarnDistrictBishop"),
    EARNDISTRICT_WARLORD("EarnDistrictWarlord"),
    EARNDISTRICT_KING("EarnDistrictKing"),
    EARNDISTRICT_MERCHANT("EarnDistrictMerchant"),
    STEAL("Steal"),
    DESTROY("Destroy"),
    KILL("Kill"),
    EXCHANGE("Exchange"),
    EXCHANGE_DECK("ExchangeDeck"),
    EXCHANGE_PLAYER("ExchangePlayer"),
    CARDS_NOT_WANTED("cardsNotWanted"),
    CARDS_WANTED("cardsWanted");
    private String s;

    DispatchState(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}

