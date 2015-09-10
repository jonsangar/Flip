package com.acme.flip;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Jonathan on 08/09/2015.
 */
public class TileView  extends Button {

    //coordenadas
    public int x;
    public int y;

    //trama a mostrar;
    private int index;

    //maximas tramas
    private int topElements;

    public TileView(Context context) {
        super(context);
        this.x = 0;
        this.y = 0;
        this.index = 0;
        this.topElements = 0;

    }

    public TileView(Context context, int x, int y, int index, int topElements, int background) {
        super(context);
        this.x = x;
        this.y = y;
        this.index = index;
        this.topElements = topElements;
        this.setBackgroundResource(background);
    }

    public int getNexIndex(){
        this.index++;
        //controla si necesitamos comenzar el ciclo de tramas
        if(index == topElements) index = 0;
        return this.index;
    }
}
