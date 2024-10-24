package model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Card")
public class Card extends RenterType {

    @Override
    public int maxVolumes(int volumes) {
        return volumes == 0 ? 10 : volumes;
    }

    @Override
    public String getRenterTypeInfo() {
        return "Card";
    }
}
