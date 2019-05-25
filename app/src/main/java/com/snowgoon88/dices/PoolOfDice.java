package com.snowgoon88.dices;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alain on 14/05/19.
 */

public class PoolOfDice {


    public PoolOfDice( int nbDice, int nbKept, boolean specialized ) {
        this( nbDice, nbKept, 0, specialized, false );
    }

    public PoolOfDice( int nbDice, int nbKept, int bonus,
                       boolean specialized, boolean minimum ) {
        this.nbDice = nbDice;
        this.nbKept = nbKept;
        this.bonus = bonus;
        this.specialized = specialized;
        this.minimum = minimum;
        this.result = 0;

        this.label = Integer.toString(nbDice);
        if (this.specialized) {
            this.label += "s";
        }
        this.label += "g";
        if (this.minimum) {
            this.label += "m";
        }
        this.label += Integer.toString(nbKept);

        if (this.bonus > 0) {
            this.label += "+"+Integer.toString(this.bonus);
        }
    }



    static public boolean is_valid( String desc ) {
        String pattern = "(?i)(\\d+s?gm?\\d+)(\\+\\d+)?";

        return desc.matches( pattern );
    }

    static public PoolOfDice setFromPattern( String desc ) throws InvalidPatternException {
        // check global pattern
        String pattern = "(?i)(\\d+s?gm?\\d+)(\\+\\d+)?";

        if (desc.matches( pattern) == false ) {
            throw new InvalidPatternException( "formule globalement invalide" );
        }


        // Check nbDice and nbKept, with nbDice >= nbKept
        Pattern nbDicePattern = Pattern.compile( "(\\d+)(s?gm?)" );
        Matcher matcher = nbDicePattern.matcher( desc );

        String res = "";
//        while (matcher.find()) {
//            // The group(0) is the whole match, group(1) should be nbDice
//            res += ":n/"+matcher.group()+"["+matcher.group(1)+"]["+matcher.group(2)+"]/";
//        }
//        matcher = nbDicePattern.matcher( desc );
        if ( matcher.find() == false ) {
            res += "R!";
            throw new InvalidPatternException( "no Dice");
        }
        int nbDice = Integer.decode( matcher.group(1) );


        Pattern nbKeptPattern = Pattern.compile( "(s?gm?)(\\d+)" );
        matcher = nbKeptPattern.matcher( desc );

//        while (matcher.find()) {
//            // The group(0) is the whole match, group(2) should be nbKept
//            res += ":k/"+matcher.group()+"["+matcher.group(1)+"]["+matcher.group(2)+"]/";
//        }
//        matcher = nbKeptPattern.matcher( desc );
        if ( matcher.find() == false ) {
            res += "K!";
            throw new InvalidPatternException( "no Kept");
        }
        int nbKept = Integer.decode( matcher.group(2) );

        // if nbKept > nbDice
        if (nbKept > nbDice ) {
            throw new InvalidPatternException( "nbDice < nbKept" );
            //res += "-FK";
        }

        // Read bonus, specilialized and minimum
        Pattern bonusPattern = Pattern.compile( "(\\+)(\\d+)" );
        matcher = bonusPattern.matcher( desc );
//        while (matcher.find()) {
//            // The group(0) is the whole match, group(2) should be bonus
//            res += ":b/"+matcher.group()+"["+matcher.group(1)+"]["+matcher.group(2)+"]/";
//        }
//        matcher = bonusPattern.matcher( desc );
        int bonus = 0;
        if (matcher.find() == true ) {
            bonus = Integer.decode(matcher.group(2));
        }

        // specialised
        boolean specialized = false;
        if (desc.indexOf( "s") >= 0) {
            specialized = true;
            res += ":s";
        }
        // keep minimum
        boolean minimum = false;
        if (desc.indexOf( "m") >= 0) {
            minimum = true;
            res += ":m";
        }

        // transform if too much dice
        if (nbDice >= 10 ) {
            int over = nbDice-10;
            nbDice = 10;
            while (over > 1 && nbKept < 10) {
                over -= 2;
                nbKept += 1;
            }
            if (nbKept == 10) {
                bonus += over * 2;
            }
        }

        res += "->R"+nbDice+"K"+nbKept+"B"+bonus;
        return new PoolOfDice( nbDice, nbKept, bonus, specialized, minimum );
    }


    public int roll() {
        ArrayList<Dice> pool = new ArrayList<Dice>(this.nbDice);
        this.result = 0;
        this.event = "";

        for (int i = 0; i < this.nbDice; i++) {
            Dice d = new Dice( 10, this.specialized );
            d.roll();
            pool.add( d );
        }


        if (this.minimum) {
            Collections.sort( pool, Collections.reverseOrder());
        }
        else {
            Collections.sort(pool);
        }
        for (int i = 0; i < nbKept; i++) {
            Dice d = pool.get(i);
            this.result += d.result;
            d.event += "k";
            pool.set(i, d);
        }

        this.result += this.bonus;

        this.event = Integer.toString( this.result ) + " <= ";
        String sep = "";
        for (Dice d : pool) {
            this.event += sep + d.event;
            sep = ", ";
        }

        return this.result;
    }

    // *********************************************************************************** Attributs
    int result;
    int nbDice;
    int nbKept;
    int bonus;
    boolean specialized;
    boolean minimum;
    public String event;
    public String label;
}
