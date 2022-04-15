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
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "sm_legal")
public class Legal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name_legal")
    private String nameLegal;
    @Column(name = "unp")
    private String unp;
    @Column(name = "iban")
    private String ibanByByn;
    @Column(name = "total_employee")
    private Integer totalEmployee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private TypeLegal type;
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "legal",
            orphanRemoval = true
    )
    private LegalDetails legalDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Legal legal = (Legal) o;
        return Objects.equals(id, legal.id) &&
                Objects.equals(nameLegal, legal.nameLegal) &&
                Objects.equals(unp, legal.unp) &&
                Objects.equals(ibanByByn, legal.ibanByByn) &&
                Objects.equals(totalEmployee, legal.totalEmployee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameLegal, unp, ibanByByn, totalEmployee);
    }
}
