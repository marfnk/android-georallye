package org.marfnk.georallye.services;

import android.content.SharedPreferences;

public class PersistanceManager {
    
    private GameEngine gameEngine;
    private SharedPreferences preferences;

    public PersistanceManager(SharedPreferences pref, GameEngine g) {
        preferences = pref;
        gameEngine = g;
    }
    
    public void restoreGameState() {
        gameEngine.restoreGameState(preferences);
    }

    public void saveGameState() {
        SharedPreferences.Editor editor = preferences.edit();
      
        editor.clear();
        gameEngine.getGameState(editor);
        editor.commit();        
    }
}
