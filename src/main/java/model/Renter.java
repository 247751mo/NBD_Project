package model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import exceptions.ParameterException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("renters")
@Data
@NoArgsConstructor
public class Renter {
    @PartitionKey
    @CqlName("personal_id")
    private long personalId;
    private String type = "RENTER";
    private String firstName;
    private String lastName;

    public Renter(long personalId, String firstName, String lastName) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;

    }

}
