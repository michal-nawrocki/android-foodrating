package com.mxn672.foodrating.data;

public enum Distance {
    ONE_MILE(1), TWO_MILES(2), THREE_MILES(3), FIVE_MILES(5), TEN_MILES(10), NO_LIMIT(-1);

    public double distance;

     Distance(double val){
        this.distance = val;
    }

    public double getMiles(){
        return this.distance;
    }
}
