package model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "renter_type", discriminatorType = DiscriminatorType.STRING)
public abstract class RenterType extends AbstractEntity {

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


