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
        return zug + oldKoord(y,x) + convKoord;
    }

    private String schlagMoveBauerTransform() {
        String bauerSchlag = oldKoord(y,x)+"x";
        String convKoord = "";

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
            bauerSchlag += oldKoord(y,x) + "x";
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


    public String eventHandler(){
        switch (specialEvent) {
            case "O-O":
                return "kleineRochade";
            case "O-O-O":
                return "großeRochade";
            case "+":
                return "check";
            case "#":
                return "checkMate";
            }
        switch (bauerTransformPossibilities()) {
            case "transformCaptureAndCheckmate":
                return "transformCaptureAndCheckmate";
            case "transformCaptureAndCheck":
                return "transformCaptureAndCheck";
            case "transformAndCapture":
                return "transformAndCapture";
            case "transformAndCheckmate":
                return "transformAndCheckmate";
            case "transformAndCheck":
                return "transformAndCheck";
            case "transform":
                return "transform";
        }
        return "";
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




    /*private String eventHandler2() {

        if (specialEvent.equals("O-O")) {
            return "kleineRochade";
        } else if (specialEvent.equals("O-O-O")) {
            return "großeRochade";
        } else if (bauerTransformPossibilities().equals("transformCaptureAndCheckmate")) {
            return "transformCaptureAndCheckmate";
        }else if(bauerTransformPossibilities().equals("transformCaptureAndCheck")){
            return "transformCaptureAndCheck";
        }else if(bauerTransformPossibilities().equals("transformAndCapture")) {
            return "transformAndCapture";
        }else if(bauerTransformPossibilities().equals("transformAndCheckmate")) {
            return "transformAndCheckmate";
        }else if(bauerTransformPossibilities().equals("transformAndCheck")) {
            return "transformAndCheck";
        }else if(bauerTransformPossibilities().equals("transform")){
            return "transform";
        } else if (specialEvent.equals("+")) {
            return "check";
        } else if (specialEvent.equals("#")) {
            return "checkMate";
        } else {
            return "";
        }
    }

     */