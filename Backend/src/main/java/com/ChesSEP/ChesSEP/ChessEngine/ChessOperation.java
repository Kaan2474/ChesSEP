package com.ChesSEP.ChesSEP.ChessEngine;

public class ChessOperation {
    int x,y,newX,newY;

    String specialEvent = "";
    ChessPiece movingPiece,deletedPiece;

    public ChessOperation(int x, int y, int newX, int newY, ChessPiece movingPiece, ChessPiece deletedPiece, String specialEvent){
        this.x=x;
        this.y=y;
        this.newX=newX;
        this.newY=newY;
        this.movingPiece=movingPiece;
        this.deletedPiece=deletedPiece;
        this.specialEvent = specialEvent == null? "" : specialEvent;
    }

    //alte koordinate, von wo aus bewegt wurde (für pgn)
    private String oldKoord(int y, int x){
        String oldKoord="";
        switch (y) {
            case 0:
                oldKoord += "a";
                break;
            case 1:
                oldKoord += "b";
                break;
            case 2:
                oldKoord += "c";
                break;
            case 3:
                oldKoord += "d";
                break;
            case 4:
                oldKoord += "e";
                break;
            case 5:
                oldKoord += "f";
                break;
            case 6:
                oldKoord += "g";
                break;
            case 7:
                oldKoord += "h";
                break;
        }
        switch (x) {
            case 0:
                oldKoord += "8";
                break;
            case 1:
                oldKoord += "7";
                break;
            case 2:
                oldKoord += "6";
                break;
            case 3:
                oldKoord += "5";
                break;
            case 4:
                oldKoord += "4";
                break;
            case 5:
                oldKoord += "3";
                break;
            case 6:
                oldKoord += "2";
                break;
            case 7:
                oldKoord += "1";
                break;
        }
        return oldKoord;
    }


    //ziel koordinate (für pgn)
    private String convKoord(int newY, int newX){
        String convKoord ="";

        switch (newY) {
            case 0:
                convKoord += "a";
                break;
            case 1:
                convKoord += "b";
                break;
            case 2:
                convKoord += "c";
                break;
            case 3:
                convKoord += "d";
                break;
            case 4:
                convKoord += "e";
                break;
            case 5:
                convKoord += "f";
                break;
            case 6:
                convKoord += "g";
                break;
            case 7:
                convKoord += "h";
                break;
        }
        for (int i = 0; i < 8; i++) {
            if(newX==i) {
                convKoord += Integer.toString(8 - i);
            }
        }

        return convKoord;
    }


    //ein ganz normaler zug, ohne besonderheit (für pgn)
    private String normalMove() {
        String zug ="";
        switch (movingPiece.getType()) {
            case BAUER:
                zug += "";
                break;
            case TURM:
                zug += "R";
                break;
            case SPRINGER:
                zug += "N";
                break;
            case LAUFER:
                zug += "B";
                break;
            case KOENIGIN:
                zug += "Q";
                break;
            case KOENIG:
                zug += "K";
                break;
        }

        return zug + oldKoord(y,x) + convKoord(newY, newX);
    }


    //wenn ein bauer eine figur geschlagen hat und dadurch transformiert weden kann (für pgn)
    private String schlagMoveBauerTransform() {
        String bauerSchlag = oldKoord(y,x)+"x";
        return bauerSchlag + convKoord(newY, newX);

    }


    //eine figur wird geschlagen (für pgn)
    private String schlagMove() {
        String bauerSchlag = "";
        String zug ="";
        if (movingPiece.getType() == ChessPieceType.BAUER && deletedPiece != null) {
            bauerSchlag += oldKoord(y,x) + "x";

            return bauerSchlag + convKoord(newY, newX);

        } else {
            switch (movingPiece.getType()) {
                case TURM:
                    zug += "R" + oldKoord(y,x) + "x";
                    break;
                case SPRINGER:
                    zug += "N" + oldKoord(y,x) + "x";
                    break;
                case LAUFER:
                    zug += "B" + oldKoord(y,x) + "x";
                    break;
                case KOENIGIN:
                    zug += "Q" + oldKoord(y,x) + "x";
                    break;
                case KOENIG:
                    zug += "K" + oldKoord(y,x) + "x";
                    break;
                case BAUER:
                    break;
            }

            return zug + convKoord(newY, newX);
        }
    }



    /*
        Flag String für transformationen
        alle möglichen ereignisse, die passieren wenn ein bauer transformiert wird
        - gegner wird schach gesetzt
        - gegner wird schachmatt gesetzt
        - gegner figur wird geschlagen, dann mit transformierter figur schach gesetzt
        - gegner figur wird geschlagen, dann mit transformierter figur schachmatt gesetzt
     */
    private String bauerTransformPossibilities() {
        if (deletedPiece != null) {
            if (specialEvent.contains("=") && specialEvent.contains("#")) {
                return "transformCaptureAndCheckmate"; //=a7xa8=Q#
            } else if (specialEvent.contains("=") && specialEvent.contains("+")) {
                return "transformCaptureAndCheck"; //=a7xa8=Q+
            } else if(specialEvent.contains("=")){
                return "transformAndCapture"; //=a7xa8=Q
            }else {
                return "";
            }
        } else {
            if (specialEvent.contains("=") && specialEvent.contains("#")) {
                return "transformAndCheckmate"; //=a7a8=Q#
            } else if (specialEvent.contains("=") && specialEvent.contains("+")) {
                return "transformAndCheck"; //=a7a8=Q+
            } else if(specialEvent.contains("=")){
                return "transform"; //=a7a8=Q
            }else{
                return "";
            }
        }
    }


    /*
        Flag String kleine rochade, große rochade, check, checkmate und transformationen
        transform möglichkeiten und andere besonderheiten wie rochade, check und checkmate werden abgearbeitet

        signalisiert was, wie übersetzt werden muss
     */
    public String eventHandler(){
        return switch (specialEvent) {
            case "O-O" -> "kleineRochade";
            case "O-O-O" -> "großeRochade";
            case "+" -> "check";
            case "#" -> "checkMate";
            default -> switch (bauerTransformPossibilities()) {
                case "transformCaptureAndCheckmate" -> "transformCaptureAndCheckmate";
                case "transformCaptureAndCheck" -> "transformCaptureAndCheck";
                case "transformAndCapture" -> "transformAndCapture";
                case "transformAndCheckmate" -> "transformAndCheckmate";
                case "transformAndCheck" -> "transformAndCheck";
                case "transform" -> "transform";
                default -> "";
            };
        };
    }


    /*
        die eigentliche toString()-Methode
        Wie eine ChessOperation ausgegeben werden soll
     */
    public String toStringKomprimiert(){
        String output ="";
        switch (eventHandler()) {
            case "kleineRochade":
                output = "O-O";
                break;
            case "großeRochade":
                output = "O-O-O";
                break;
            case "transformAndCapture":
                if(specialEvent.startsWith("=TU")){
                    output = schlagMoveBauerTransform() + "=R";
                }else if(specialEvent.startsWith("=SP")) {
                    output = schlagMoveBauerTransform() + "=N";
                }else if(specialEvent.startsWith("=LA")) {
                    output = schlagMoveBauerTransform() + "=B";
                }else{
                    output = schlagMoveBauerTransform() + "=Q";
                }
                break;
            case "transformCaptureAndCheck":
                if(specialEvent.startsWith("=TU")){
                    output = schlagMoveBauerTransform() + "=R+";
                }else if(specialEvent.startsWith("=SP")) {
                    output = schlagMoveBauerTransform() + "=N+";
                }else if(specialEvent.startsWith("=LA")) {
                    output = schlagMoveBauerTransform() + "=B+";
                }else{
                    output = schlagMoveBauerTransform() + "=Q+";
                }
                break;
            case "transformCaptureAndCheckmate":
                if(specialEvent.startsWith("=TU")){
                    output = schlagMoveBauerTransform() + "=R#";
                }else if(specialEvent.startsWith("=SP")) {
                    output = schlagMoveBauerTransform() + "=N#";
                }else if(specialEvent.startsWith("=LA")) {
                    output = schlagMoveBauerTransform() + "=B#";
                }else{
                    output = schlagMoveBauerTransform() + "=Q#";
                }
                break;
            case "transform":
                if(specialEvent.startsWith("=TU")){
                    output = normalMove() + "=R";
                }else if(specialEvent.startsWith("=SP")) {
                    output = normalMove() + "=N";
                }else if(specialEvent.startsWith("=LA")) {
                    output = normalMove() + "=B";
                }else{
                    output = normalMove() + "=Q";
                }
                break;
            case "transformAndCheckmate":
                if(specialEvent.startsWith("=TU")){
                    output = normalMove() + "=R#";
                }else if(specialEvent.startsWith("=SP")) {
                    output = normalMove() + "=N#";
                }else if(specialEvent.startsWith("=LA")) {
                    output = normalMove() + "=B#";
                }else{
                    output = normalMove() + "=Q#";
                }
                break;
            case "transformAndCheck":
                if(specialEvent.startsWith("=TU")){
                    output = normalMove() + "=R+";
                }else if(specialEvent.startsWith("=SP")) {
                    output = normalMove() + "=N+";
                }else if(specialEvent.startsWith("=LA")) {
                    output = normalMove() + "=B+";
                }else{
                    output = normalMove() + "=Q+";
                }
                break;
            case "check":
                if (deletedPiece != null && !specialEvent.contains("=")) {
                    output = schlagMove() + "+";
                } else {
                    output = normalMove() + "+";
                }
                break;
            case "checkMate":
                if (deletedPiece != null && !specialEvent.contains("=")) {
                    output = schlagMove() + "#";
                } else {
                    output = normalMove() + "#";
                }
                break;
            case "":
                if(deletedPiece != null){
                    output = schlagMove();
                }else{
                    output = normalMove();
                }
        }
        return output;
    }
}
