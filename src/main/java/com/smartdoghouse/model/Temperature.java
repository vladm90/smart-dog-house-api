package com.smartdoghouse.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static java.util.Objects.isNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "TEMPERATURES")
@DynamicInsert
public class Temperature implements Serializable, Persistable<Long> {


    @Id
    @SequenceGenerator(name = "temperatures_sequence", sequenceName = "temperatures_seq")
    @GeneratedValue(generator = "temperatures_sequence")
    @Column(name = "T_ID")
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "T_DATE", nullable = false, updatable = false)
    private Date date;

    @Column(name = "T_OUTSIDE")
    private Double outside;

    @Column(name = "T_INSIDE_HAPPY")
    private Double insideHappy;

    @Column(name = "T_INSIDE_SNOOPY")
    private Double insideSnoopy;

    @Column(name = "T_IS_OPEN_HAPPY")
    private Boolean openHappy;

    @Column(name = "T_IS_OPEN_SNOOPY")
    private Boolean openSnoopy;



    @Override
    public boolean isNew() {
        return isNull(id);
    }
}
