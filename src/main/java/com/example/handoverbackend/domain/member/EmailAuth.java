package com.example.handoverbackend.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Table(name = "email_auth")
@Getter
public class EmailAuth {


    @Id
    @Column(name = "email_key", nullable = false)
    private String key;

    @Column(name = "email", nullable = false)
    private String email;

    public EmailAuth(String key, String email) {
        this.key = key;
        this.email = email;
    }


}
