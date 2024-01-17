package fr.cotedazur.univ.polytech.model.card;

public enum Color {
    BLUE("Bleu"),
    GREEN("Vert"),
    RED("Rouge"),
    YELLOW("Jaune"),
    PURPLE("Violet"),
    GRAY("Gris");

    private final String colorName;

    Color(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }
}
