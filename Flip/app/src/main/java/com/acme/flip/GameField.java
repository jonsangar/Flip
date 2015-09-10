package com.acme.flip;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Jonathan on 08/09/2015.
 */
public class GameField extends Activity {

    private static final int[] colors = new int[]{
            R.drawable.ic_1c,
            R.drawable.ic_2c,
            R.drawable.ic_3c,
            R.drawable.ic_4c,
            R.drawable.ic_5c,
            R.drawable.ic_6c
    };

    private static final int[] numbers = new int[]{
            R.drawable.ic_1n,
            R.drawable.ic_2n,
            R.drawable.ic_3n,
            R.drawable.ic_4n,
            R.drawable.ic_5n,
            R.drawable.ic_6n
    };

    //mantener el array que el usuario haya decidido usar
    private int[] pictures = null;

    //Número máximo de celdas horizontales y verticales
    private int topTileX = 3;
    private int topTileY = 3;

    //Número máximo de elementos a utilizar
    private int topElements = 2;

    //Si ha seleccionado usar o no sonido y vibración
    private boolean hasSound = false;
    private boolean hasVibration = false;

    /* Array con los identificadores de la celdas cuando se añadan al layout para poder
    * recuperarlos durante la partida */
    private int ids[][] = null;

    /* Array para guardar los valores de los índices de cada una de las celdas,
    * se utilizará para agilizar la comprobación de si la partida ha terminado o no */
    private int values[][] = null;

    //Contador con número de pulsaciones del jugador
    private int numberOfClicks = 0;

    //Para reproducir un sonido cuando el usuario cambie la celda
    private MediaPlayer mp = null;

    //Para hacer vibrar el dispositivo cuando el usuario cambie una celda
    private Vibrator vibratorService = null;

    //Mostrará en pantalla las veces que el usuario ha pulsado una celda
    private TextView tvNumberOfClicks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamefield);
        vibratorService = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(this,R.raw.touch);
        tvNumberOfClicks = (TextView)findViewById(R.id.clicksTxt);
        Bundle extras = getIntent().getExtras();
        topTileX = extras.getInt("xtiles") + 3;
        topTileY = extras.getInt("ytiles") + 3;

        topElements = extras.getInt("numcolors") + 2;

        //usar colores o números
        if("C".equals(extras.getString("tile"))){
            pictures = colors;
        } else {
            pictures = numbers;
        }

        hasSound = extras.getBoolean("hasSound");
        hasVibration = extras.getBoolean("hasVibration");

        //Limpiamos liner layout para evitar que se guarden resultados de partidas anteriores
        LinearLayout ll = (LinearLayout)findViewById(R.id.fieldLandscape);
        ll.removeAllViews();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels /topTileX;
        int height = (dm.heightPixels - 180)
                /topTileY;

        ids = new int[topTileX][topTileY];
        values = new int[topTileX][topTileY];

        Random r = new Random(System.currentTimeMillis());
        int tilePicturesToShow = r.nextInt(topElements);

        int ident = 0;

        for(int i=0;i<topTileY;i++){
            LinearLayout l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            for(int j=0;j<topTileX;j++){
                tilePicturesToShow = r.nextInt(topElements);
                //guardamos la trama a mostrar
                values[j][i] = tilePicturesToShow;
                TileView tv = new TileView(this,j,i,tilePicturesToShow,topElements,
                        pictures[tilePicturesToShow]);
                ident++;
                //se asigna identificador al objeto creado
                tv.setId(ident);
                //se guarda el identificador en la matriz ids
                ids[j][i] = ident;
                tv.setHeight(height);
                tv.setWidth(width);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        hasClick(((TileView) v).x, ((TileView) v).y);
                    }
                });
                l2.addView(tv);
            }
            ll.addView(l2);
        }

        Chronometer t = (Chronometer)findViewById(R.id.chronometer);
        t.start();
    }

    public void hasClick(int x, int y){
        if (hasSound) mp.start();
        if (hasVibration) vibratorService.vibrate(100);

        changeView(x,y);

        //Calculamos que celdas vecinas deben cambiar.

        //Comprobamos que no estamos en celda de esquinas
        if(x==0 && y==0){
            changeView(0,1);
            changeView(1,1);
            changeView(1,0);
        }

        else if(x==0 && y == topTileY - 1){
            changeView(0,topTileY - 2);
            changeView(1,topTileY - 2);
            changeView(1,topTileY - 1);
        }

        else if(x == topTileX - 1 && y == 0){
            changeView(topTileX - 2,0);
            changeView(topTileX - 2,1);
            changeView(topTileX - 1,1);
        }

        else if(x == topTileX-1 && y == topTileY - 1) {
            changeView(topTileX - 2, topTileY - 1);
            changeView(topTileX - 2, topTileY - 2);
            changeView(topTileX - 1, topTileY - 2);
        }

        //Ahora comprobamos los lados del tablero

        else if(x == 0){
            changeView(x,y+1);
            changeView(x,y-1);
            changeView(x+1,y);
        }


        else if(y == 0){
            changeView(x-1,y);
            changeView(x+1,y);
            changeView(x,y+1);
        }

        else if(x == topTileX - 1){
            changeView(x,y-1);
            changeView(x,y+1);
            changeView(x-1,y);
        }

        else if(y == topTileY - 1){
            changeView(y,x-1);
            changeView(y,x+1);
            changeView(y-1,x);
        }

        //el resto
        else{
            changeView(x,y-1);
            changeView(x,y+1);
            changeView(x-1,y);
            changeView(x+1,y);
        }

        numberOfClicks++;
        tvNumberOfClicks.setText(getString(R.string.score_clicks)+numberOfClicks);

        //Se ha acabado la partida?
        checkIfFinished();

    }

    private void checkIfFinished() {
        int targetValue = values[0][0];
        for(int i=0;i<topTileY;i++){
            for(int j=0;j<topTileX;j++){
                if(targetValue!=values[j][i]) return;
            }
        }
        Intent resultIntent = new Intent((String)null);
        resultIntent.putExtra("numberOfClicks",numberOfClicks);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    private void changeView(int x, int y) {
        TileView tt = (TileView)findViewById(ids[x][y]);
        int newIndex = tt.getNexIndex();
        values[x][y] = newIndex;
        tt.setBackgroundResource(pictures[newIndex]);
        tt.invalidate();
    }

}
