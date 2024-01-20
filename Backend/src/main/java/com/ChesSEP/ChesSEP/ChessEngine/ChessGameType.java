package com.ChesSEP.ChesSEP.ChessEngine;

public enum ChessGameType {
   
    PVP(0),
    PUZZLE(1),
    PVE(2);


    private int id;    

    private ChessGameType(int id){
        this.id=id;
    }

    public int getid(){
        return id;
    }
}
