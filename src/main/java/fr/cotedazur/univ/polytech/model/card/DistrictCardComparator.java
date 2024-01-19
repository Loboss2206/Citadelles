package fr.cotedazur.univ.polytech.model.card;

import java.util.Comparator;

public class DistrictCardComparator implements Comparator<DistrictCard> {


    @Override
    public int compare(DistrictCard o1, DistrictCard o2) {
        return Integer.compare(o1.getDistrictValue(), o2.getDistrictValue());
    }
}
