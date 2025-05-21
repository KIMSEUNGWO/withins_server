package com.withins.entity;

import com.withins.enums.KoreanRegion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "organization")
public class Organization {

    @Column(name = "organizationId")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String address;
    private String url;

    @Enumerated(EnumType.STRING)
    private KoreanRegion region;


    @Builder.Default
    @OneToMany(mappedBy = "organization")
    private List<News> news = new ArrayList<>();

}
