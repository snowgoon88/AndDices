package com.snowgoon88.dices;

/**
 * Created by alain on 19/05/19.
 */

class InvalidPatternException extends Exception {
    public InvalidPatternException(String s) {
        super(s);
    }
    public InvalidPatternException(){
        super();
    }
}
