package com.ChesSEP.ChesSEP.ChessEngine;

public class ChessOperation {
    int x,y,newX,newY;
    ChessPiece movingPiece,deletedPiece;

    public ChessOperation(int x, int y, int newX, int newY, ChessPiece movingPiece, ChessPiece deletedPiece){
        this.x=x;
        this.y=y;
        this.newX=newX;
        this.newY=newY;
        this.movingPiece=movingPiece;
        this.deletedPiece=deletedPiece;
    }

    @Override 
    public String toString(){
        return "notImplemented";  //dummy convert to chessnotation String
    }
}
