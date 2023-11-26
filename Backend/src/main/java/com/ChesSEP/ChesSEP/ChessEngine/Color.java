package com.ChesSEP.ChesSEP.ChessEngine;

public enum Color {
    BLACK(1),
    WHITE(2);

    private int id;

    Color(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }
}
