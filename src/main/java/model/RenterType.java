package model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "renter_type", discriminatorType = DiscriminatorType.STRING)
public abstract class RenterType extends com.rental.model.AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key auto-generated
    private Long id;

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
