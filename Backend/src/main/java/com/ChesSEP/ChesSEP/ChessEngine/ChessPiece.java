package com.ChesSEP.ChesSEP.ChessEngine;

public class ChessPiece {

    private ChessPieceType type;
    private Color color;
    private boolean hasMoved;

    public ChessPiece(ChessPieceType type, Color color){
        this.type=type;
        this.color=color;
        hasMoved=false;
    }

    public ChessPiece(int id, int color){

        if(color==1){
            this.color=Color.WHITE;
        }else{
            this.color=Color.BLACK;
        }

        type=getTypeFromId(id);

        this.hasMoved=false;
    }

    public ChessPieceType getTypeFromId(int id ){
        for (ChessPieceType type : ChessPieceType.values()) {
            if(type.getId()==id){
                return type;
            }
        }

        return null;
    }

    public int getIdFromType(){
        return type.getId();
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ChessPieceType getType(){
        return this.type;
    }

    public void sethasMovedTrue(int zugId){
        hasMoved=true;
    }

    public void setHasMoved(boolean value){
        hasMoved=value;
    }

    public boolean hasMoved(){
        return hasMoved;
    }

    public boolean isEqual(ChessPiece another){
        if(another==null)
            return false;

        if(color!=another.getColor())
            return false;

        if(type!=another.getType())
            return false;

        return true;
    }

    @Override
    public String toString(){
        String result="";

        result+=type.name()+"\n";
        result+=color.name()+"\n";
        result+=hasMoved;

        return result;
    }
    
}
