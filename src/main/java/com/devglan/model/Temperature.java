package com.devglan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "temperature")
public class Temperature {


    @Id
  /*  @SequenceGenerator(name = "history_sequence", sequenceName = "history_seq")
    @GeneratedValue(generator = "history_sequence")*/
    @Column(name = "id")
    private Long id;



    @CreationTimestamp
   // @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "name")
    private String name;





}
