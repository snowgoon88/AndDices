package com.snowgoon88.dices;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by alain on 18/05/19.
 */

public class PoolOfDiceView
        extends LinearLayout
        implements View.OnClickListener
{
    private static String LOG = "PoolOfDiceView";

    private View _view;
    private PoolOfDice _pool;


    public PoolOfDiceView(Context context, PoolOfDice pool) {
        super(context);
        _pool = pool;
        init(context);
    }

    private void init(Context context) {
        // inflate
        LayoutInflater.from(context).inflate( R.layout.pool_view, this, true);

        // Set label of pool_btn
        Button btn = (Button) findViewById( R.id.pool_btn );
        btn.setText( _pool.label );
        btn.setOnClickListener( this );
        // Set callbacks
        ImageButton imgBtn = findViewById( R.id.del_btn );
        imgBtn.setOnClickListener( this );
        // Set text
        TextView resultText = (TextView) findViewById(R.id.pool_txt);
        String msg = "Res : " + _pool.event;
        resultText.setText( msg );
    }

    public void changeFontSize( int size ) {
        // Button Pool
        Button btn = (Button) findViewById( R.id.pool_btn );
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PT, size);
        // TextView
        TextView resultText = (TextView) findViewById(R.id.pool_txt);
        resultText.setTextSize(TypedValue.COMPLEX_UNIT_PT, size);
    }

    @Override
    public void onClick(View view) {
        // Roll
        if (view.getId() == R.id.pool_btn) {
            int res = _pool.roll();
            String msg = "Res : " + _pool.event;
            TextView resultText = (TextView) findViewById(R.id.pool_txt);
            resultText.setText( msg );
        }

        // Delete
        if (view.getId() == R.id.del_btn) {
            ((ViewGroup) getParent()).removeView( this );
        }
    }

}
