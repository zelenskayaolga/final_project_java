package com.zelenskaya.nestserava.app.repository.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "auth_session")
public class Session implements Serializable {
    @Id
    @Column(name = "id")
    private String jwtToken;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "destruction_date")
    private LocalDateTime destructionDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeout_id", nullable = false)
    private Timeout timeout;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
