package model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import exceptions.ParameterException;
import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.io.Serializable;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("renters")
@Data
@NoArgsConstructor
public class Renter {
    @PartitionKey
    @CqlName("personal_id")
    private String personalID;
    private String type = "RENTER";
    private String firstName;
    private String lastName;

    public Renter(String personalID, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;

    }

}
