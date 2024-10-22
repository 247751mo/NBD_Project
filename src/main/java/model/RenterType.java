package model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "renter_type", discriminatorType = DiscriminatorType.STRING)
public abstract class RenterType {

    public abstract int maxVolumes(int volumes);

    public abstract String getRenterTypeInfo();
}

@Entity
@DiscriminatorValue("NoCard")

class NoCard extends RenterType {

    @Override
    public int maxVolumes(int volumes) {
        return volumes == 0 ? 5 : volumes;
    }

    @Override
    public String getRenterTypeInfo() {
        return "No Card";
    }
}

@Entity
@DiscriminatorValue("Card")
class Card extends RenterType {

    @Override
    public int maxVolumes(int volumes) {
        return volumes == 0 ? 10 : volumes;
    }

    @Override
    public String getRenterTypeInfo() {
        return "Card";
    }
}
