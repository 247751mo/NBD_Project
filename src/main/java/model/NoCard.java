package model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("NoCard")
public class NoCard extends RenterType {

    @Override
    public int maxVolumes(int volumes) {
        return volumes == 0 ? 5 : volumes;
    }

    @Override
    public String getRenterTypeInfo() {
        return "No Card";
    }
}
