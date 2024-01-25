package fr.cotedazur.univ.polytech.model.golds;

public class StackOfGolds {
    private int nbCoins;

    public StackOfGolds(){
        this.nbCoins = 30;
    }

    public int takeAGold(){
        if(nbCoins <= 0){
            return 0;
        } else {
            nbCoins--;
            return 1;
        }
    }

    public void addGoldsToStack(int coinsToAdd){
        this.nbCoins += coinsToAdd;
        if(this.nbCoins > 30){
            this.nbCoins = 30;
        }
    }
    public int getNbGolds() {
        return nbCoins;
    }
}
