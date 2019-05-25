package com.snowgoon88.dices;

/**
 * Created by alain on 13/05/19.
 */

import java.util.Random;
import java.lang.String;

public class Dice implements Comparable<Dice> {

    public Dice( int type, boolean specialized ) {
        this.type = type;
        this.specialized = specialized;
        this.rnd = new Random();
        this.event = "";
        this.result = 0;
    }

    public int roll() {
        this.result = 1 + this.rnd.nextInt( this.type );
        this.event += Integer.toString( result );

        if (this.specialized) {
            while (this.result == 1) {
                this.event += "/";
                result = this.rnd.nextInt( this.type );
                this.event += Integer.toString( this.result );
            }
        }

        if (this.result == 10) {
            this.event += "+";
            Dice addedDice = new Dice( this.type, false);
            this.result += addedDice.roll();
            this.event += addedDice.event;
            this.event += ":"+Integer.toString( this.result );
        }

        return this.result;
    }

    @Override
    /** Descending order */
    public int compareTo( Dice otherDice ) {
        return otherDice.result - this.result;
    }

    // *********************************************************************************** Attributs
    int result;
    int type;
    boolean specialized;
    Random rnd;
    public String event;
}
