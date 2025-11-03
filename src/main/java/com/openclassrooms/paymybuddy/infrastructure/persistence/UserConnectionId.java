package com.openclassrooms.paymybuddy.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class UserConnectionId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "connections_id")
    private Integer connectionsId;
}
