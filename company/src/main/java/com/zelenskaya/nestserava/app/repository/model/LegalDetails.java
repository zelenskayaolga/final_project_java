package com.zelenskaya.nestserava.app.repository.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "sm_legal_details")
public class LegalDetails {
    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "legal")
    )

    @GeneratedValue(generator = "generator")
    private Long legalId;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Legal legal;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Column(name = "update_date")
    private LocalDate updateDate;
    @Column(name = "note")
    private String note;
}
