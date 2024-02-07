package fr.cotedazur.univ.polytech.model.golds;

public class StackOfGolds {
    private int nbCoins;

    public StackOfGolds() {
        this.nbCoins = 30;
    }

    /**
     * Function that takes a gold from the stack if there is at least one
     * @return the amount that can be taken
     */
    public int takeAGold() {
        if (nbCoins <= 0) {
            return 0;
        } else {
            nbCoins--;
            return 1;
        }
    }

    /**
     * Function that adds golds to the stack
     * @param coinsToAdd the amount of golds to add
     */
    public void addGoldsToStack(int coinsToAdd) {
        this.nbCoins += coinsToAdd;
        if (this.nbCoins > 30) {
            this.nbCoins = 30;
        }
    }

    public int getNbGolds() {
        return nbCoins;
    }
}
