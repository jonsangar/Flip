package com.acme.flip;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class GameConfig extends ActionBarActivity {

    private static final int ACTION_PLAY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        //Botón inicio de partida
        Button btn = (Button)findViewById(R.id.startBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay();
            }
        });

        //Control número de celdas horizontales
        SeekBar xTiles = (SeekBar)findViewById(R.id.seekBarX);
        xTiles.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateXTitles(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateXTitles(xTiles.getProgress());

        //Control número de celdas verticales
        SeekBar yTiles = (SeekBar)findViewById(R.id.seekBarY);
        yTiles.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateYTiles(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateYTiles(yTiles.getProgress());

        //Control barra de colores
        SeekBar colors = (SeekBar)findViewById(R.id.seekBarColors);
        colors.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColors(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        updateColors(colors.getProgress());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_config,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.m_player:
                showPlayer();
                return true;
            case R.id.m_howto:
                showHowto();
                return true;
            case R.id.m_about:
                showAbout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        Intent i = new Intent(this,About.class);
        startActivity(i);
    }

    private void showHowto() {
        Intent i = new Intent(this,HowTo.class);
        startActivity(i);
    }

    private void showPlayer() {

    }

    private void updateColors(int progress) {
        TextView tv = (TextView)findViewById(R.id.seekBarColorsTxt);
        tv.setText(getString(R.string.num_colors) + (progress+2));
    }

    private void updateYTiles(int progress) {
        TextView tv = (TextView)findViewById(R.id.seekBarYTxt);
        tv.setText(getString(R.string.num_elem_y) + (progress+3));
    }

    private void updateXTitles(int progress) {
        TextView tv = (TextView) findViewById(R.id.seekBarXTxt);
        tv.setText(getString(R.string.num_elem_x) + (progress+3));
    }

    private void startPlay() {

        Intent i = new Intent(this,GameField.class);

        SeekBar sb = (SeekBar)findViewById(R.id.seekBarX);
        i.putExtra("xtiles",sb.getProgress());

        sb = (SeekBar)findViewById(R.id.seekBarY);
        i.putExtra("ytiles",sb.getProgress());

        sb = (SeekBar)findViewById(R.id.seekBarColors);
        i.putExtra("numcolors",sb.getProgress());

        RadioButton rb = (RadioButton)findViewById(R.id.radicoButtonC);
        i.putExtra("tile",rb.isChecked()?"C":"N");

        CheckBox cb = (CheckBox)findViewById(R.id.checkBoxSound);
        i.putExtra("hasSound",cb.isChecked());

        cb = (CheckBox)findViewById(R.id.checkBoxVibration);
        i.putExtra("hasVibration",cb.isChecked());

        //comenzar Activity
        startActivityForResult(i,ACTION_PLAY);



    }


}
