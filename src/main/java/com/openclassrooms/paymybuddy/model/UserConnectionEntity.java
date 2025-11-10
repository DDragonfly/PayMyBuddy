package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_connection")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserConnectionEntity {

    @EmbeddedId
    private UserConnectionId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("connectionsId")
    @JoinColumn(name = "connections_id", nullable = false)
    private UserEntity connection;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
