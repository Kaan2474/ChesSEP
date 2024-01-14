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
        this.specialEvent = specialEvent;
    }

    private String normalMove() {
        String zug ="";
        String convKoord = "";
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

        switch (newX) {
            case 0:
                convKoord += "8";
                break;
            case 1:
                convKoord += "7";
                break;
            case 2:
                convKoord += "6";
                break;
            case 3:
                convKoord += "5";
                break;
            case 4:
                convKoord += "4";
                break;
            case 5:
                convKoord += "3";
                break;
            case 6:
                convKoord += "2";
                break;
            case 7:
                convKoord += "1";
                break;
        }
        return zug + convKoord;
    }

    private String schlagMoveBauerTransform() {
        String bauerSchlag = "";
        String convKoord = "";
        switch (y) {
            case 0:
                bauerSchlag += "ax";
                break;
            case 1:
                bauerSchlag += "bx";
                break;
            case 2:
                bauerSchlag += "cx";
                break;
            case 3:
                bauerSchlag += "dx";
                break;
            case 4:
                bauerSchlag += "ex";
                break;
            case 5:
                bauerSchlag += "fx";
                break;
            case 6:
                bauerSchlag += "gx";
                break;
            case 7:
                bauerSchlag += "hx";
                break;
        }
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
        switch (newX) {
            case 0:
                convKoord += "8";
                break;
            case 1:
                convKoord += "7";
                break;
            case 2:
                convKoord += "6";
                break;
            case 3:
                convKoord += "5";
                break;
            case 4:
                convKoord += "4";
                break;
            case 5:
                convKoord += "3";
                break;
            case 6:
                convKoord += "2";
                break;
            case 7:
                convKoord += "1";
                break;
        }
        return bauerSchlag + convKoord;

    }

    private String schlagMove() {
        String bauerSchlag = "";
        String zug ="";
        String convKoord = "";
        if (movingPiece.getType() == ChessPieceType.BAUER && deletedPiece != null) {
            switch (y) {
                case 0:
                    bauerSchlag += "ax";
                    break;
                case 1:
                    bauerSchlag += "bx";
                    break;
                case 2:
                    bauerSchlag += "cx";
                    break;
                case 3:
                    bauerSchlag += "dx";
                    break;
                case 4:
                    bauerSchlag += "ex";
                    break;
                case 5:
                    bauerSchlag += "fx";
                    break;
                case 6:
                    bauerSchlag += "gx";
                    break;
                case 7:
                    bauerSchlag += "hx";
                    break;
            }
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
            switch (newX) {
                case 0:
                    convKoord += "8";
                    break;
                case 1:
                    convKoord += "7";
                    break;
                case 2:
                    convKoord += "6";
                    break;
                case 3:
                    convKoord += "5";
                    break;
                case 4:
                    convKoord += "4";
                    break;
                case 5:
                    convKoord += "3";
                    break;
                case 6:
                    convKoord += "2";
                    break;
                case 7:
                    convKoord += "1";
                    break;
            }
            return bauerSchlag + convKoord;

        } else {
            switch (movingPiece.getType()) {
                case TURM:
                    zug += "Rx";
                    break;
                case SPRINGER:
                    zug += "Nx";
                    break;
                case LAUFER:
                    zug += "Bx";
                    break;
                case KOENIGIN:
                    zug += "Qx";
                    break;
                case KOENIG:
                    zug += "Kx";
                    break;
            }
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

            switch (newX) {
                case 0:
                    convKoord += "8";
                    break;
                case 1:
                    convKoord += "7";
                    break;
                case 2:
                    convKoord += "6";
                    break;
                case 3:
                    convKoord += "5";
                    break;
                case 4:
                    convKoord += "4";
                    break;
                case 5:
                    convKoord += "3";
                    break;
                case 6:
                    convKoord += "2";
                    break;
                case 7:
                    convKoord += "1";
                    break;
            }
            return zug + convKoord;
        }
    }

    private String eventHandler() {

        if (specialEvent.equals("O-O")) {
            return "kleineRochade";
        } else if (specialEvent.equals("O-O-O")) {
            return "großeRochade";
        } else if (specialEvent.contains("=")) {
            if(deletedPiece!=null) {
                return "bauerTransformMitSchlag";
            }else{
                return "bauerTransformOhneSchlag";
            }
        } else if (specialEvent.equals("+")) {
            return "check";
        } else if (specialEvent.equals("#")) {
            return "checkMate";
        } else {
            return "";
        }
    }

    public String toStringKomprimiert(){
        String output ="";
        switch (eventHandler()) {
            case "kleineRochade":
                output = "O-O";
                break;
            case "großeRochade":
                output = "O-O-O";
                break;
            case "bauerTransformMitSchlag":
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
            case "bauerTransformOhneSchlag":
                if(specialEvent.startsWith("=TU")){
                    output = normalMove().substring(1) + "=R";
                }else if(specialEvent.startsWith("=SP")) {
                    output = normalMove().substring(1) + "=N";
                }else if(specialEvent.startsWith("=LA")) {
                    output = normalMove().substring(1) + "=B";
                }else{
                    output = normalMove().substring(1) + "=Q";
                }
                break;
            case "check":
                if (deletedPiece != null) {
                    output = schlagMove() + "+";
                } else {
                    output = normalMove() + "+";
                }
                break;
            case "checkMate":
                if (deletedPiece != null) {
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
