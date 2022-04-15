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
@Table(name = "c_application_details")
public class ApplicationDetails {
    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "applicationConv")
    )
    @GeneratedValue(generator = "generator")
    private Long applicationId;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ApplicationConv applicationConv;
    @Column(name = "create_date")
    private LocalDate createDate;
    @Column(name = "last_update_date")
    private LocalDate updateDate;
    @Column(name = "update_user_id")
    private Long userId;
    @Column(name = "note")
    private String note;
}
