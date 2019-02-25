package com.example.playware;

import android.os.Handler;
import android.service.quicksettings.Tile;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.MotoConnection;

import java.util.Random;


import static com.livelife.motolibrary.AntData.EVENT_PRESS;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

public class GameHitthetarget extends Game {
    MotoConnection connection = MotoConnection.getInstance();
    int currenttile;
    int timeInterval = 1000;
    int timestep = 100;
    Handler handler = new Handler();
    Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            int tile = getRandomTile();
            for (int t : connection.connectedTiles) {
                if (tile == t) {
                    connection.setTileColor(LED_COLOR_RED, tile);
                } else {
                    connection.setTileColor(LED_COLOR_OFF, t);
                }
                currenttile = tile;
                handler.postDelayed(gameRunnable,timeInterval);
            }
        }
    };

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int tileID = AntData.getId(message);
        if (event == EVENT_PRESS) {
            int color = AntData.getColorFromPress(message);
            if (tileID ==currenttile) {
                timeInterval -= timestep;
            } else {
                timeInterval += timestep;
            }
            if (timeInterval <= timestep) {
                timeInterval = timestep;
            }
        }
    }



//    public void onGameUpdate([byte]message) {
//        super.onGameUpdate(message);
//    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
    }

    int getRandomTile() {
        Random random = new Random(connection.connectedTiles.size());
        while (true) {
            int tile = random.nextInt();
            if (tile != currenttile) {
                return tile;
            }
        }
    }

}



