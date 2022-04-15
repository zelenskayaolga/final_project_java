package com.zelenskaya.nestserava.app.repository.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "с_application_сonv")
public class ApplicationConv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "application_conv_id")
    private String applicationConvId;
    @Column(name = "legal_id")
    private Long legalId;
    @Column(name = "employee_id")
    private Long employeeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "value_ind_id", nullable = false)
    private ValueInd valueInd;
    @Column(name = "percent_conv")
    private Float percentConv;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "applicationConv",
            orphanRemoval = true
    )
    private ApplicationDetails applicationDetails;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "value_leg_id", nullable = false)
    private ValueLeg valueLeg;
}
