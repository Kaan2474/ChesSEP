package com.ChesSEP.ChesSEP.ChessGame;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
class MatchRequestID implements Serializable {Long RequestorID; Long InvitedID;}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MatchRequest {

    @EmbeddedId MatchRequestID matchRequestID;

}
