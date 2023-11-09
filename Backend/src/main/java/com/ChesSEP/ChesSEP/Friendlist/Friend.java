package com.ChesSEP.ChesSEP.Friendlist;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable class FriendID implements Serializable {Long FriendID1; Long FriendID2;}

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class Friend {
    @EmbeddedId FriendID friendID;

    @Column
    @Enumerated(EnumType.STRING)
    private FriendTyp type;
}
