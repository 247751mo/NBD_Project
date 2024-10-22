package com.rental.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;


@MappedSuperclass
@Access(AccessType.FIELD)
@Embeddable
public abstract class AbstractEntity implements Serializable {

    @Version
    @NotNull
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    protected long version;
}