package com.ChesSEP.ChesSEP.ChessEngine;

public enum difficulty {

    EASY(2),
    MEDIUM(4),
    HARD(6);
    
    private int depth;

    private difficulty(int depth){
        this.depth=depth;
    }

    public int getDepth(){
        return depth;
    }
}
