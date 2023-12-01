package com.ChesSEP.ChesSEP.ChessEngine;

public enum Color {
    BLACK(2),
    WHITE(1);

    private int id;

    Color(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }
}
