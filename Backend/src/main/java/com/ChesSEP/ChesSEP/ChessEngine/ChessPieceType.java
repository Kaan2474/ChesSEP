package com.ChesSEP.ChesSEP.ChessEngine;

public enum ChessPieceType {
    BAUER(1),
    TURM(2),
    SPRINGER(3),
    LAUFER_S(4),
    KOENIGIN(5),
    KOENIG(6),
    LAUFER_W(7);

    private int id;

    ChessPieceType(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }
}
