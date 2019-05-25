package com.snowgoon88.dices;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class RollDiceL5R extends AppCompatActivity {

    private static String LOG = "RollDiceL5R";


    private L5RKbd _l5r_keyboard;
    private LinearLayout _pool_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_dice_l5_r);

        // no System Keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        _l5r_keyboard = (L5RKbd) findViewById( R.id.l5RKbd );
        _l5r_keyboard.setActivity( this );
        _pool_layout = (LinearLayout) findViewById( R.id.scroll_lin );


//        // inflate a keyboard
//        LayoutInflater inflater = LayoutInflater.from(RollDiceL5R.this);
//        //View kbdLayout = inflater.inflate(R.layout.keyboard_lr, null);
//        L5RKbd l5rKbd = new L5RKbd( this );
//        l5rKbd.setId( R.id.l5rkbd_layout ); // static, and defined in res/values/ids.xml
//
//
//        ConstraintLayout constLayout = (ConstraintLayout) findViewById(R.id.main_layout);
//        ConstraintLayout.LayoutParams kbdLayParam = new ConstraintLayout.LayoutParams(
//                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        l5rKbd.setLayoutParams(kbdLayParam);
//
//        constLayout.addView( l5rKbd );
//
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone( constLayout );
//
//        constraintSet.connect( l5rKbd.getId(), ConstraintSet.BOTTOM, constLayout.getId(), ConstraintSet.BOTTOM, 16);
//        constraintSet.connect( l5rKbd.getId(), ConstraintSet.LEFT, constLayout.getId(), ConstraintSet.LEFT, 16);
//        constraintSet.connect( l5rKbd.getId(), ConstraintSet.RIGHT, constLayout.getId(), ConstraintSet.RIGHT, 16);
//
//        constraintSet.applyTo( constLayout );

        // Add two pools
//        Log.d( LOG, "Adding 4 pool" );
//        PoolOfDice p1 = new PoolOfDice( 5, 2, false );
//        PoolOfDice p2 = new PoolOfDice( 3, 3, true );
//        LinearLayout scrollLinear = findViewById( R.id.scroll_lin );
//        for (int i = 0; i < 2; i++) {
//            scrollLinear.addView( new PoolOfDiceView(this, p1));
//            scrollLinear.addView( new PoolOfDiceView(this, p2));
//        }


    }

    public void addPool( PoolOfDice pool ) {
        //Log.d( LOG, "Adding pool label="+pool.label );
        pool.roll();
        _pool_layout.addView( new PoolOfDiceView( this, pool ));
    }

    // TODO: 19/05/19 Test changeFontSize
    public void changeFontSize( int size ) {
        // for Keyboard
        TableLayout tabLayout = (TableLayout) findViewById(R.id.keyboard_layout);
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tabLayout.getChildAt(i);;
            for (int j = 0; j < row.getChildCount(); j++) {
                View child = row.getChildAt(j);
                if (child instanceof Button) {
                    Button btn = (Button) child;
                    btn.setTextSize(TypedValue.COMPLEX_UNIT_PT, size);
                }
            }
        }

        // for all PoolOfDiceView
        for (int i = 0; i < _pool_layout.getChildCount(); i++) {
            PoolOfDiceView pv = (PoolOfDiceView) _pool_layout.getChildAt(i);
            pv.changeFontSize( size );
        }
    }

    // make a roll
    public void makeRoll( View view ) {
        Dice dice = new Dice( 10, true);
        int res = dice.roll();

        String msg = "Res : " + Integer.toString(res) + "=" + dice.event;
        TextView resultText = (TextView) findViewById(R.id.resText1);
        resultText.setText( msg );

    }

    // Make PoolRoll
    public void makePool( View view ) {
        PoolOfDice pool = new PoolOfDice( 4, 2, true);
        int res = pool.roll();

        String msg = "Res : " + pool.event;
        TextView resultText = (TextView) findViewById(R.id.resText2);
        resultText.setText( msg );
    }
}
