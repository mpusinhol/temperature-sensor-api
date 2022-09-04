package com.mpusinhol.temperaturesensorapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.Instant;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"value", "unit", "timestamp"})})
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TemperatureUnit unit;

    @Column(nullable = false)
    private Instant timestamp;

    //@EqualsAndHashCode is not recommended in JPA entities as it may affect performance
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Temperature that = (Temperature) o;

        return new EqualsBuilder().append(value, that.value).append(unit, that.unit).append(timestamp, that.timestamp).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).append(unit).append(timestamp).toHashCode();
    }
}
