package com.acmetelecom.calling;

public class CallInformation
{
    private String number;

    public CallInformation(String number){
        this.number = number;
    }

    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()){
            return false;
        }
        final CallInformation other = (CallInformation) obj;
        if (this.number.equals(other.getNumber()))
            return true;
        return false;
    }

    public String getNumber(){ return this.number; }

}
