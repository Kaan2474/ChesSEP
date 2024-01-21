package com.ChesSEP.ChesSEP.ChessEngine;

public enum difficulty {

    EASY(2),
    MEDIUM(3),
    HARD(4);
    
    private int depth;

    private difficulty(int depth){
        this.depth=depth;
    }

    public int getDepth(){
        return depth;
    }
}
