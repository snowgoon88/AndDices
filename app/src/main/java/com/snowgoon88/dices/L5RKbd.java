package com.snowgoon88.dices;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by alain on 15/05/19.
 */

public class L5RKbd extends LinearLayout implements View.OnClickListener {

    private RollDiceL5R _activity = null;


    //        private Button button1, button2, button3, button4,
//                button5, button6, button7, button8,
//                button9, button0, buttonDelete, buttonEnter;
    private PoolOfDice _pool;
    private EditText _editText;
    private TextView _resText;

    private SparseArray<String> _keyValues = new SparseArray<>();
    private InputConnection _inputConnection;

    public L5RKbd(Context context) {
        this(context, null, 0);
    }

    public L5RKbd(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public L5RKbd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        _pool = new PoolOfDice(1, 1, false);

        // Init SparseArray
        _keyValues.put( R.id.btn_0, "0" );
        _keyValues.put( R.id.btn_1, "1" );
        _keyValues.put( R.id.btn_2, "2" );
        _keyValues.put( R.id.btn_3, "3" );
        _keyValues.put( R.id.btn_4, "4" );
        _keyValues.put( R.id.btn_5, "5" );
        _keyValues.put( R.id.btn_6, "6" );
        _keyValues.put( R.id.btn_7, "7" );
        _keyValues.put( R.id.btn_8, "8" );
        _keyValues.put( R.id.btn_9, "9" );
        _keyValues.put( R.id.btn_k, "g" );
        _keyValues.put( R.id.btn_low, "m" );
        _keyValues.put( R.id.btn_plus, "+" );
        _keyValues.put( R.id.btn_ok , "" );
        _keyValues.put( R.id.btn_special, "s" );
        //_keyValues.put( R.id.btn_del, "" );
        //_keyValues.put( R.id.btn_clr, "" );

        // inflate
        LayoutInflater.from(context).inflate( R.layout.keyboard_lr, this, true);

        // connect buttons
        for (int id = 0; id < _keyValues.size(); id++) {
            Button btn = (Button) findViewById( _keyValues.keyAt( id ));
            btn.setOnClickListener( this );
        }
        ImageButton ibtn = findViewById( R.id.btn_del );
        ibtn.setOnClickListener( this );
        ibtn = findViewById( R.id.btn_clr );
        ibtn.setOnClickListener( this );


        // Connection with keyboard own EditText
        _editText = (EditText) findViewById( R.id.textKeyboard );
        //MyKeyboard keyboard = (MyKeyboard) findViewById(R.id.keyboard);
        _editText.setRawInputType( InputType.TYPE_CLASS_TEXT );
        _editText.setTextIsSelectable(true);
        // put cursor at end
        _editText.setSelection( _editText.getText().length());

        InputConnection ic = _editText.onCreateInputConnection(new EditorInfo());
        //keyboard.setInputConnection(ic);
        this.setInputConnection(ic);

        _resText = (TextView) findViewById( R.id.resView );
    }

    @Override
    public void onClick(View view) {
        if (_inputConnection == null)
            return;

        if (view.getId() == R.id.btn_del) {
            CharSequence selectedText = _inputConnection.getSelectedText(0);
            _resText.setText( "DEL-"+selectedText+"-" );

            if (TextUtils.isEmpty(selectedText)) {
                _inputConnection.deleteSurroundingText(1, 0);
            }
            else {
                _inputConnection.commitText("", 1);
            }
        }
        else if (view.getId() == R.id.btn_clr) {
            CharSequence currentText = _inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
            CharSequence beforCursorText = _inputConnection.getTextBeforeCursor(currentText.length(), 0);
            CharSequence afterCursorText = _inputConnection.getTextAfterCursor(currentText.length(), 0);
            _inputConnection.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
            //_inputConnection.commitText( "", 1);
        }
        else if (view.getId() == R.id.btn_ok) {
            ExtractedText eText = _inputConnection.getExtractedText( new ExtractedTextRequest(), 0);
            String content = eText.text.toString();
            content = content.replaceFirst( "Pool : ", "");


            try {
                PoolOfDice pool = PoolOfDice.setFromPattern(content);
                // send message to activity
                if (_activity != null) {
                    _activity.addPool(pool);
                }
                content += " : V ";
                content += pool.label;
            }
            catch (InvalidPatternException e) {
                content += " : NOT Valid "+ e.getMessage();
            }

            _resText.setText( content );

            //_inputConnection.commitText( "=>valid?", 1 /* cursor after txt*/ );
        }
        else {
            String value = _keyValues.get(view.getId());
            _resText.setText( "ADD-"+value+"-" );
            _inputConnection.commitText(value, 1);
        }
    }

    public void setActivity( RollDiceL5R act ) {
        _activity = act;
    }
    public void setInputConnection(InputConnection ic) {
        _inputConnection = ic;
    }
}

