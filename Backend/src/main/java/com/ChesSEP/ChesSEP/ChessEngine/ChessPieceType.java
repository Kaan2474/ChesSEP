package com.ChesSEP.ChesSEP.ChessEngine;

public enum ChessPieceType {
    BAUER(1),
    TURM(2),
    SPRINGER(3),
    LAUFER(4),
    KOENIGIN(5),
    KOENIG(6);

    private int id;

    ChessPieceType(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }
}
