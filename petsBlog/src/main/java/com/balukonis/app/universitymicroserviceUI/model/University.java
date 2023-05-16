package com.balukonis.app.universitymicroserviceUI.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "universities")
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @Embedded
    @NonNull
    private UniversityAddress address;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UniversityAddress getAddress() {
        return address;
    }

    public void setAddress(UniversityAddress address) {
        this.address = address;
    }
}

