package com.ChesSEP.ChesSEP.ChessEngine;


import org.junit.jupiter.api.*;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

//Ohne H2 wird ein Error ausgegeben, der einfach stört und den Prozess in die länge zieht
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.defer-database-initialization=true",
        "spring.jpa.hibernate.ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
public class PGNMoveSet {

    //only moves without anything
    @Test
    @Order(1)
    public void normalMove(){
    //Arrange

        //Bauer
        ChessPiece movingBauer = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);
        ChessOperation pawn = new ChessOperation(6,0,5,0, movingBauer, null, "");

        //Springer
        ChessPiece movingSpringer = new ChessPiece(ChessPieceType.SPRINGER, Color.WHITE);
        ChessOperation knight = new ChessOperation(7,1,5,0, movingSpringer, null, "");

        //Turm
        ChessPiece movingTurm = new ChessPiece(ChessPieceType.TURM, Color.WHITE);
        ChessOperation rook = new ChessOperation(7,0,4,0, movingTurm, null, "");

        //Dame
        ChessPiece movingDame = new ChessPiece(ChessPieceType.KOENIGIN, Color.WHITE);
        ChessOperation queen = new ChessOperation(7,3,4,0,movingDame, null, "");

        //König
        ChessPiece movingKoenig = new ChessPiece(ChessPieceType.KOENIG, Color.WHITE);
        ChessOperation king = new ChessOperation(7,4,7,3,movingKoenig, null, "");

        //Läufer
        ChessPiece movingLaeufer = new ChessPiece(ChessPieceType.LAUFER, Color.WHITE);
        ChessOperation bishop = new ChessOperation(7,2, 5,0,movingLaeufer, null, "");

    //act
        String bauer = pawn.toStringKomprimiert();
        String springer = knight.toStringKomprimiert();
        String turm = rook.toStringKomprimiert();
        String dame = queen.toStringKomprimiert();
        String laeufer = bishop.toStringKomprimiert();
        String koenig = king.toStringKomprimiert();

    //Assert
        Assertions.assertEquals("a2a3", bauer);
        Assertions.assertEquals("Nb1a3", springer);
        Assertions.assertEquals("Ra1a4", turm);
        Assertions.assertEquals("Qd1a4", dame);
        Assertions.assertEquals("Bc1a3", laeufer);
        Assertions.assertEquals("Ke1d1", koenig);

    }

    @Test
    @Order(1)
    public void checkAfterNormalMove(){
        //Arrange

        //Bauer
        ChessPiece movingBauer = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);
        ChessOperation pawn = new ChessOperation(6,0,5,0, movingBauer, null, "+");

        //Springer
        ChessPiece movingSpringer = new ChessPiece(ChessPieceType.SPRINGER, Color.WHITE);
        ChessOperation knight = new ChessOperation(7,1,5,0, movingSpringer, null, "+");

        //Turm
        ChessPiece movingTurm = new ChessPiece(ChessPieceType.TURM, Color.WHITE);
        ChessOperation rook = new ChessOperation(7,0,4,0, movingTurm, null, "+");

        //Dame
        ChessPiece movingDame = new ChessPiece(ChessPieceType.KOENIGIN, Color.WHITE);
        ChessOperation queen = new ChessOperation(7,3,4,0,movingDame, null, "+");


        //Läufer
        ChessPiece movingLaeufer = new ChessPiece(ChessPieceType.LAUFER, Color.WHITE);
        ChessOperation bishop = new ChessOperation(7,2, 5,0,movingLaeufer, null, "+");

        //act
        String bauer = pawn.toStringKomprimiert();
        String springer = knight.toStringKomprimiert();
        String turm = rook.toStringKomprimiert();
        String dame = queen.toStringKomprimiert();
        String laeufer = bishop.toStringKomprimiert();


        //Assert
        Assertions.assertEquals("a2a3+", bauer);
        Assertions.assertEquals("Nb1a3+", springer);
        Assertions.assertEquals("Ra1a4+", turm);
        Assertions.assertEquals("Qd1a4+", dame);
        Assertions.assertEquals("Bc1a3+", laeufer);

    }

    //only capture moves
    @Test
    @Order(3)
    public void captureMove(){
    //Arrange

        //deletedPieces
        ChessPiece deletedBauer = new ChessPiece(ChessPieceType.BAUER, Color.BLACK);
        ChessPiece deletedLauefer = new ChessPiece(ChessPieceType.LAUFER, Color.BLACK);
        ChessPiece deletedSpringer = new ChessPiece(ChessPieceType.SPRINGER, Color.BLACK);
        ChessPiece deletedDame = new ChessPiece(ChessPieceType.KOENIGIN, Color.BLACK);
        ChessPiece deletedTurm = new ChessPiece(ChessPieceType.TURM, Color.BLACK);

        //Bauerx
        ChessPiece movingBauer = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);
        ChessOperation pawnxpawn = new ChessOperation(4,3,3,4, movingBauer, deletedBauer, "");
        ChessOperation pawnxknight = new ChessOperation(3,2,2,3, movingBauer, deletedLauefer, "");
        ChessOperation pawnxbishop = new ChessOperation(3,1,2,0, movingBauer, deletedSpringer, "");
        ChessOperation pawnxqueen = new ChessOperation(4,2,3,3, movingBauer, deletedDame, "");
        ChessOperation pawnxrook = new ChessOperation(6,1,5,0, movingBauer, deletedTurm, "");


        //Springerx
        ChessPiece movingSpringer = new ChessPiece(ChessPieceType.SPRINGER, Color.WHITE);
        ChessOperation knightxpawn = new ChessOperation(7,1,5,2, movingSpringer, deletedBauer, "");
        ChessOperation knightxknight = new ChessOperation(5,2,3,3, movingSpringer, deletedSpringer, "");
        ChessOperation knightxbishop = new ChessOperation(3,3,4,5,movingSpringer, deletedLauefer, "" );
        ChessOperation knightxqueen = new ChessOperation(3,3,4,5, movingSpringer, deletedDame, "");

        //Turmx
        ChessPiece movingTurm = new ChessPiece(ChessPieceType.TURM, Color.WHITE);
        ChessOperation rookxrook = new ChessOperation(7,0,0,0, movingTurm, deletedTurm, "");
        ChessOperation rookxpawn = new ChessOperation(7,0,1,0, movingTurm, deletedBauer,"");
        ChessOperation rookxknight = new ChessOperation(7,0,2,0, movingTurm, deletedSpringer, "");
        ChessOperation rookxqueen = new ChessOperation(7,0,3,0,movingTurm, deletedDame, "");
        ChessOperation rookxbishop = new ChessOperation(7,0,2,0, movingTurm, deletedLauefer, "");


        //Damex
        ChessPiece movingDame = new ChessPiece(ChessPieceType.KOENIGIN, Color.WHITE);
        ChessOperation queenxpawn = new ChessOperation(7,3,3,0,movingDame, deletedBauer, "");
        ChessOperation queenxrook = new ChessOperation(7,3,3,0, movingDame, deletedTurm, "");
        ChessOperation queenxknight = new ChessOperation(4,0,2,2, movingDame, deletedSpringer, "");
        ChessOperation queenxbishop = new ChessOperation(4,0, 2,0, movingDame, deletedLauefer, "");
        ChessOperation queenxqueen = new ChessOperation(7,3,0,3,movingDame, deletedDame, "");

        //Königx
        ChessPiece movingKoenig = new ChessPiece(ChessPieceType.KOENIG, Color.WHITE);
        ChessOperation kingxpawn = new ChessOperation(7,4,7,3,movingKoenig, deletedBauer, "");
        ChessOperation kingxrook = new ChessOperation(7,4,7,3,movingKoenig, deletedTurm, "");
        ChessOperation kingxqueen = new ChessOperation(7,4, 7,3, movingKoenig, deletedDame, "");
        ChessOperation kingxbishop = new ChessOperation(7,4,7,3, movingKoenig, deletedSpringer, "");
        ChessOperation kingxknight = new ChessOperation(7,4,7,3, movingKoenig, deletedSpringer, "");

        //Läuferx
        ChessPiece movingLaeufer = new ChessPiece(ChessPieceType.LAUFER, Color.WHITE);
        ChessOperation bishopxpawn = new ChessOperation(5,0, 3,2,movingLaeufer, deletedBauer, "");
        ChessOperation bishopxknight = new ChessOperation(5,0, 3,2,movingLaeufer, deletedSpringer, "");
        ChessOperation bishopxqueen = new ChessOperation(5,0,2,3, movingLaeufer, deletedDame, "");
        ChessOperation bishopxrook = new ChessOperation(7,2,5,0, movingLaeufer, deletedTurm, "");
        ChessOperation bishopxbishop = new ChessOperation(7,2,2,7, movingLaeufer, deletedLauefer, "");

    //act

        //Bauerx
        String bauer = pawnxpawn.toStringKomprimiert();
        String bauerxlaeufer = pawnxknight.toStringKomprimiert();
        String bauerxspringer = pawnxbishop.toStringKomprimiert();
        String bauerxdame = pawnxqueen.toStringKomprimiert();
        String bauerxturm = pawnxrook.toStringKomprimiert();

        //Springerx
        String springerxbauer = knightxpawn.toStringKomprimiert();
        String springerxspringer = knightxknight.toStringKomprimiert();
        String springerxlaeufer = knightxbishop.toStringKomprimiert();
        String springerxdame = knightxqueen.toStringKomprimiert();

        //Turmx
        String turmxturm = rookxrook.toStringKomprimiert();
        String turmxspringer = rookxknight.toStringKomprimiert();
        String turmxbauer = rookxpawn.toStringKomprimiert();
        String turmxdame = rookxqueen.toStringKomprimiert();
        String turmxlaeufer = rookxbishop.toStringKomprimiert();


        //Damex
        String damexbauer = queenxpawn.toStringKomprimiert();
        String damexturm = queenxrook.toStringKomprimiert();
        String damexspringer = queenxknight.toStringKomprimiert();
        String damexlaeufer = queenxbishop.toStringKomprimiert();
        String damexdame = queenxqueen.toStringKomprimiert();


        String laeuferxbauer = bishopxpawn.toStringKomprimiert();
        String laeuferxlaeufer = bishopxbishop.toStringKomprimiert();
        String laeuferxdame = bishopxqueen.toStringKomprimiert();
        String laeuferxturm = bishopxrook.toStringKomprimiert();
        String laeuferxspringer = bishopxknight.toStringKomprimiert();

        //Königx
        String koenigxbauer = kingxpawn.toStringKomprimiert();
        String koenigxdame = kingxqueen.toStringKomprimiert();
        String koenigxlaeufer = kingxbishop.toStringKomprimiert();
        String koenigxspringer = kingxknight.toStringKomprimiert();
        String koenigxturm = kingxrook.toStringKomprimiert();

    //Assert
        //Bauerx
        Assertions.assertEquals("d4xe5", bauer);
        Assertions.assertEquals("c5xd6", bauerxlaeufer);
        Assertions.assertEquals("b5xa6", bauerxspringer);
        Assertions.assertEquals("c4xd5", bauerxdame);
        Assertions.assertEquals("b2xa3", bauerxturm);

        //Springerx
        Assertions.assertEquals("Nb1xc3",springerxbauer);
        Assertions.assertEquals("Nc3xd5", springerxspringer);
        Assertions.assertEquals("Nd5xf4", springerxlaeufer);
        Assertions.assertEquals("Nd5xf4", springerxdame);

        //Turmx
        Assertions.assertEquals("Ra1xa8", turmxturm);
        Assertions.assertEquals("Ra1xa6", turmxspringer);
        Assertions.assertEquals("Ra1xa7", turmxbauer);
        Assertions.assertEquals("Ra1xa6", turmxlaeufer);
        Assertions.assertEquals("Ra1xa5", turmxdame);

        //Damex
        Assertions.assertEquals("Qd1xa5", damexbauer);
        Assertions.assertEquals("Qd1xa5", damexturm);
        Assertions.assertEquals("Qa4xc6", damexspringer);
        Assertions.assertEquals("Qa4xa6", damexlaeufer);
        Assertions.assertEquals("Qd1xd8", damexdame);


        //Läuferx
        Assertions.assertEquals("Ba3xc5", laeuferxbauer);
        Assertions.assertEquals("Ba3xc5", laeuferxspringer);
        Assertions.assertEquals("Ba3xd6", laeuferxdame);
        Assertions.assertEquals("Bc1xa3", laeuferxturm);
        Assertions.assertEquals("Bc1xh6", laeuferxlaeufer);

        //Königx
        Assertions.assertEquals("Ke1xd1", koenigxbauer);
        Assertions.assertEquals("Ke1xd1", koenigxdame);
        Assertions.assertEquals("Ke1xd1", koenigxspringer);
        Assertions.assertEquals("Ke1xd1", koenigxturm);
        Assertions.assertEquals("Ke1xd1", koenigxlaeufer);





    }

    //only moves where king is checked after capture
    @Test
    @Order(4)
    public void checkAfterCaptureMove(){

        //Arrange

        //deletedPieces
        ChessPiece deletedBauer = new ChessPiece(ChessPieceType.BAUER, Color.BLACK);
        ChessPiece deletedLauefer = new ChessPiece(ChessPieceType.LAUFER, Color.BLACK);
        ChessPiece deletedSpringer = new ChessPiece(ChessPieceType.SPRINGER, Color.BLACK);
        ChessPiece deletedDame = new ChessPiece(ChessPieceType.KOENIGIN, Color.BLACK);
        ChessPiece deletedTurm = new ChessPiece(ChessPieceType.TURM, Color.BLACK);

        //Bauerx+
        ChessPiece movingBauer = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);
        ChessOperation pawnxpawn = new ChessOperation(4,3,3,4, movingBauer, deletedBauer, "+");
        ChessOperation pawnxknight = new ChessOperation(3,2,2,3, movingBauer, deletedLauefer, "+");
        ChessOperation pawnxbishop = new ChessOperation(3,1,2,0, movingBauer, deletedSpringer, "+");
        ChessOperation pawnxqueen = new ChessOperation(4,2,3,3, movingBauer, deletedDame, "+");
        ChessOperation pawnxrook = new ChessOperation(6,1,5,0, movingBauer, deletedTurm, "+");


        //Springerx+
        ChessPiece movingSpringer = new ChessPiece(ChessPieceType.SPRINGER, Color.WHITE);
        ChessOperation knightxpawn = new ChessOperation(7,1,5,2, movingSpringer, deletedBauer, "+");
        ChessOperation knightxknight = new ChessOperation(5,2,3,3, movingSpringer, deletedSpringer, "+");
        ChessOperation knightxbishop = new ChessOperation(3,3,4,5,movingSpringer, deletedLauefer, "+" );
        ChessOperation knightxqueen = new ChessOperation(3,3,4,5, movingSpringer, deletedDame, "+");

        //Turmx+
        ChessPiece movingTurm = new ChessPiece(ChessPieceType.TURM, Color.WHITE);
        ChessOperation rookxrook = new ChessOperation(7,0,0,0, movingTurm, deletedTurm, "+");
        ChessOperation rookxpawn = new ChessOperation(7,0,1,0, movingTurm, deletedBauer,"+");
        ChessOperation rookxknight = new ChessOperation(7,0,2,0, movingTurm, deletedSpringer, "+");
        ChessOperation rookxqueen = new ChessOperation(7,0,3,0,movingTurm, deletedDame, "+");
        ChessOperation rookxbishop = new ChessOperation(7,0,2,0, movingTurm, deletedLauefer, "+");


        //Damex+
        ChessPiece movingDame = new ChessPiece(ChessPieceType.KOENIGIN, Color.WHITE);
        ChessOperation queenxpawn = new ChessOperation(7,3,3,0,movingDame, deletedBauer, "+");
        ChessOperation queenxrook = new ChessOperation(7,3,3,0, movingDame, deletedTurm, "+");
        ChessOperation queenxknight = new ChessOperation(4,0,2,2, movingDame, deletedSpringer, "+");
        ChessOperation queenxbishop = new ChessOperation(4,0, 2,0, movingDame, deletedLauefer, "+");
        ChessOperation queenxqueen = new ChessOperation(7,3,0,3,movingDame, deletedDame, "+");

        //Königx+
        ChessPiece movingKoenig = new ChessPiece(ChessPieceType.KOENIG, Color.WHITE);
        ChessOperation kingxpawn = new ChessOperation(7,4,7,3,movingKoenig, deletedBauer, "+");
        ChessOperation kingxrook = new ChessOperation(7,4,7,3,movingKoenig, deletedTurm, "+");
        ChessOperation kingxqueen = new ChessOperation(7,4, 7,3, movingKoenig, deletedDame, "+");
        ChessOperation kingxbishop = new ChessOperation(7,4,7,3, movingKoenig, deletedSpringer, "+");
        ChessOperation kingxknight = new ChessOperation(7,4,7,3, movingKoenig, deletedSpringer, "+");

        //Läuferx+
        ChessPiece movingLaeufer = new ChessPiece(ChessPieceType.LAUFER, Color.WHITE);
        ChessOperation bishopxpawn = new ChessOperation(5,0, 3,2,movingLaeufer, deletedBauer, "+");
        ChessOperation bishopxknight = new ChessOperation(5,0, 3,2,movingLaeufer, deletedSpringer, "+");
        ChessOperation bishopxqueen = new ChessOperation(5,0,2,3, movingLaeufer, deletedDame, "+");
        ChessOperation bishopxrook = new ChessOperation(7,2,5,0, movingLaeufer, deletedTurm, "+");
        ChessOperation bishopxbishop = new ChessOperation(7,2,2,7, movingLaeufer, deletedLauefer, "+");

        //act

        //Bauerx+
        String bauer = pawnxpawn.toStringKomprimiert();
        String bauerxlaeufer = pawnxknight.toStringKomprimiert();
        String bauerxspringer = pawnxbishop.toStringKomprimiert();
        String bauerxdame = pawnxqueen.toStringKomprimiert();
        String bauerxturm = pawnxrook.toStringKomprimiert();

        //Springerx+
        String springerxbauer = knightxpawn.toStringKomprimiert();
        String springerxspringer = knightxknight.toStringKomprimiert();
        String springerxlaeufer = knightxbishop.toStringKomprimiert();
        String springerxdame = knightxqueen.toStringKomprimiert();

        //Turmx+
        String turmxturm = rookxrook.toStringKomprimiert();
        String turmxspringer = rookxknight.toStringKomprimiert();
        String turmxbauer = rookxpawn.toStringKomprimiert();
        String turmxdame = rookxqueen.toStringKomprimiert();
        String turmxlaeufer = rookxbishop.toStringKomprimiert();


        //Damex+
        String damexbauer = queenxpawn.toStringKomprimiert();
        String damexturm = queenxrook.toStringKomprimiert();
        String damexspringer = queenxknight.toStringKomprimiert();
        String damexlaeufer = queenxbishop.toStringKomprimiert();
        String damexdame = queenxqueen.toStringKomprimiert();

        //Läuferx+
        String laeuferxbauer = bishopxpawn.toStringKomprimiert();
        String laeuferxlaeufer = bishopxbishop.toStringKomprimiert();
        String laeuferxdame = bishopxqueen.toStringKomprimiert();
        String laeuferxturm = bishopxrook.toStringKomprimiert();
        String laeuferxspringer = bishopxknight.toStringKomprimiert();

        //Königx+
        String koenigxbauer = kingxpawn.toStringKomprimiert();
        String koenigxdame = kingxqueen.toStringKomprimiert();
        String koenigxlaeufer = kingxbishop.toStringKomprimiert();
        String koenigxspringer = kingxknight.toStringKomprimiert();
        String koenigxturm = kingxrook.toStringKomprimiert();

        //Assert
        //Bauerx+
        Assertions.assertEquals("d4xe5+", bauer);
        Assertions.assertEquals("c5xd6+", bauerxlaeufer);
        Assertions.assertEquals("b5xa6+", bauerxspringer);
        Assertions.assertEquals("c4xd5+", bauerxdame);
        Assertions.assertEquals("b2xa3+", bauerxturm);

        //Springerx+
        Assertions.assertEquals("Nb1xc3+",springerxbauer);
        Assertions.assertEquals("Nc3xd5+", springerxspringer);
        Assertions.assertEquals("Nd5xf4+", springerxlaeufer);
        Assertions.assertEquals("Nd5xf4+", springerxdame);

        //Turmx+
        Assertions.assertEquals("Ra1xa8+", turmxturm);
        Assertions.assertEquals("Ra1xa6+", turmxspringer);
        Assertions.assertEquals("Ra1xa7+", turmxbauer);
        Assertions.assertEquals("Ra1xa6+", turmxlaeufer);
        Assertions.assertEquals("Ra1xa5+", turmxdame);

        //Damex+
        Assertions.assertEquals("Qd1xa5+", damexbauer);
        Assertions.assertEquals("Qd1xa5+", damexturm);
        Assertions.assertEquals("Qa4xc6+", damexspringer);
        Assertions.assertEquals("Qa4xa6+", damexlaeufer);
        Assertions.assertEquals("Qd1xd8+", damexdame);


        //Läuferx+
        Assertions.assertEquals("Ba3xc5+", laeuferxbauer);
        Assertions.assertEquals("Ba3xc5+", laeuferxspringer);
        Assertions.assertEquals("Ba3xd6+", laeuferxdame);
        Assertions.assertEquals("Bc1xa3+", laeuferxturm);
        Assertions.assertEquals("Bc1xh6+", laeuferxlaeufer);

        //Königx+
        Assertions.assertEquals("Ke1xd1+", koenigxbauer);
        Assertions.assertEquals("Ke1xd1+", koenigxdame);
        Assertions.assertEquals("Ke1xd1+", koenigxspringer);
        Assertions.assertEquals("Ke1xd1+", koenigxturm);
        Assertions.assertEquals("Ke1xd1+", koenigxlaeufer);


    }

    //2x castling moves
    @Test
    @Order(5)
    public void castling(){
    //Arrange

        //König
        ChessPiece movingKoenig = new ChessPiece(ChessPieceType.KOENIG, Color.WHITE);

        //kleine Rochade
        ChessOperation kingsideCastling = new ChessOperation(7,4,7,6,movingKoenig, null, "O-O");

        //große Rochade
        ChessOperation queensideCastling = new ChessOperation(7,4,7,2, movingKoenig, null, "O-O-O");


    //act
        String kleineRochade = kingsideCastling.toStringKomprimiert();
        String großeRochade = queensideCastling.toStringKomprimiert();

    //Asserts

        Assertions.assertEquals("O-O", kleineRochade);
        Assertions.assertEquals("O-O-O", großeRochade);


    }

    //only bauer transform moves without capture and check at one coordinate
    @Test
    @Order(6)
    public void bauerTransformMove(){
    //Arrange
        ChessPiece bauer = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);

        ChessOperation bauerDame = new ChessOperation(1,0,0,0, bauer, null, "=DAME");
        ChessOperation bauerTurm = new ChessOperation(1,0,0,0, bauer, null, "=TURM");
        ChessOperation bauerSpringer = new ChessOperation(1,0,0,0,bauer, null, "=SPRINGER");
        ChessOperation bauerLaeufer = new ChessOperation(1,0,0,0,bauer, null, "=LAUFER");

    //Act
        String transformToQueen = bauerDame.toStringKomprimiert();
        String transformToRook = bauerTurm.toStringKomprimiert();
        String transformToKnight = bauerSpringer.toStringKomprimiert();
        String transformToBishop = bauerLaeufer.toStringKomprimiert();

    //Assert
        Assertions.assertEquals("a7a8=Q", transformToQueen);
        Assertions.assertEquals("a7a8=R", transformToRook);
        Assertions.assertEquals("a7a8=N", transformToKnight);
        Assertions.assertEquals("a7a8=B", transformToBishop);

    }

    //transform with capture
    @Test
    @Order(7)
    public void bauerTransformMoveWithCapture(){
        //Arrange
        ChessPiece bauer = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);
        ChessPiece deletedKnight = new ChessPiece(ChessPieceType.SPRINGER, Color.BLACK);

        ChessOperation bauerDame = new ChessOperation(1,0,0,1, bauer, deletedKnight, "=DAME");
        ChessOperation bauerTurm = new ChessOperation(1,0,0,1, bauer, deletedKnight, "=TURM");
        ChessOperation bauerSpringer = new ChessOperation(1,0,0,1,bauer, deletedKnight, "=SPRINGER");
        ChessOperation bauerLaeufer = new ChessOperation(1,0,0,1,bauer, deletedKnight, "=LAUFER");

        //Act
        String transformToQueen = bauerDame.toStringKomprimiert();
        String transformToRook = bauerTurm.toStringKomprimiert();
        String transformToKnight = bauerSpringer.toStringKomprimiert();
        String transformToBishop = bauerLaeufer.toStringKomprimiert();

        //Assert
        Assertions.assertEquals("a7xb8=Q", transformToQueen);
        Assertions.assertEquals("a7xb8=R", transformToRook);
        Assertions.assertEquals("a7xb8=N", transformToKnight);
        Assertions.assertEquals("a7xb8=B", transformToBishop);

    }

    //transform with capture and check
    @Test
    @Order(8)
    public void bauerTransformMoveWithCaptureAndCheck(){
        //Arrange
        ChessPiece bauer = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);
        ChessPiece deletedKnight = new ChessPiece(ChessPieceType.SPRINGER, Color.BLACK);

        ChessOperation bauerDame = new ChessOperation(1,0,0,1, bauer, deletedKnight, "=DAME+");
        ChessOperation bauerTurm = new ChessOperation(1,0,0,1, bauer, deletedKnight, "=TURM+");
        ChessOperation bauerSpringer = new ChessOperation(1,0,0,1,bauer, deletedKnight, "=SPRINGER+");
        ChessOperation bauerLaeufer = new ChessOperation(1,0,0,1,bauer, deletedKnight, "=LAUFER+");

        //Act
        String transformToQueen = bauerDame.toStringKomprimiert();
        String transformToRook = bauerTurm.toStringKomprimiert();
        String transformToKnight = bauerSpringer.toStringKomprimiert();
        String transformToBishop = bauerLaeufer.toStringKomprimiert();

        //Assert
        Assertions.assertEquals("a7xb8=Q+", transformToQueen);
        Assertions.assertEquals("a7xb8=R+", transformToRook);
        Assertions.assertEquals("a7xb8=N+", transformToKnight);
        Assertions.assertEquals("a7xb8=B+", transformToBishop);

    }

    //transform with capture and checkmate
    @Test
    @Order(9)
    public void bauerTransformMoveWithCaptureAndCheckmate(){
        //Arrange
        ChessPiece bauer = new ChessPiece(ChessPieceType.BAUER, Color.WHITE);
        ChessPiece deletedKnight = new ChessPiece(ChessPieceType.SPRINGER, Color.BLACK);

        ChessOperation bauerDame = new ChessOperation(1,0,0,1, bauer, deletedKnight, "=DAME#");
        ChessOperation bauerTurm = new ChessOperation(1,0,0,1, bauer, deletedKnight, "=TURM#");
        ChessOperation bauerSpringer = new ChessOperation(1,0,0,1,bauer, deletedKnight, "=SPRINGER#");
        ChessOperation bauerLaeufer = new ChessOperation(1,0,0,1,bauer, deletedKnight, "=LAUFER#");

        //Act
        String transformToQueen = bauerDame.toStringKomprimiert();
        String transformToRook = bauerTurm.toStringKomprimiert();
        String transformToKnight = bauerSpringer.toStringKomprimiert();
        String transformToBishop = bauerLaeufer.toStringKomprimiert();

        //Assert
        Assertions.assertEquals("a7xb8=Q#", transformToQueen);
        Assertions.assertEquals("a7xb8=R#", transformToRook);
        Assertions.assertEquals("a7xb8=N#", transformToKnight);
        Assertions.assertEquals("a7xb8=B#", transformToBishop);

    }


}
