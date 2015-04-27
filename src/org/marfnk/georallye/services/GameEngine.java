package org.marfnk.georallye.services;

import java.util.ArrayList;

import org.marfnk.georallye.R;
import org.marfnk.georallye.adapter.Observer;
import org.marfnk.georallye.data.Constants;
import org.marfnk.georallye.models.Quest;
import org.marfnk.georallye.views.Dialogs;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class GameEngine {

    private static final String TAG = "GameEngine";
    private Activity activity;
    private QuestManager questManager;
    private Quest currentQuest;
    private Navigator navigator;
    private String STATE_COMPLETED_QUESTS = "STATE_COMPLETED_QUESTS";
    private String STATE_REVEILED_QUEST = "STATE_REVEILED_QUEST";
    private String STATE_OPEN_QUEST = "STATE_OPEN_QUEST";

    public GameEngine(Activity activity, QuestManager qm, Navigator navi) {
        this.activity = activity;
        this.questManager = qm;
        this.navigator = navi;
        
        navigator.addObserver(new Observer() {
            @Override
            public void onNotified() {
                checkDistanceTrigger();
            }
        });

        updateCurrentQuest();
    }

    private void updateCurrentQuest() {
        currentQuest = questManager.getCurrentQuest();
    }

    public void processCode(String code) {
        final String cleanCode = code.trim().toLowerCase(Constants.LOCALE);

        if (questManager.hasOpenQuest(code)) {
            updateCurrentQuest();

            final Runnable activateNewQuest = new Runnable() {
                @Override
                public void run() {
                    // activate new quest
                    questManager.activateQuest(cleanCode);
                    updateCurrentQuest();
                    
                    //navigate to new location
                    navigator.setLocation(currentQuest.getLatitude(), currentQuest.getLongitude());
                    
                    // show welcome message
                    showInfoForQuest(currentQuest, "Auf geht's...");
                }
            };

            final Runnable completeCurrentQuest = new Runnable() {
                @Override
                public void run() {
                    completeCurrentQuest(activateNewQuest);
                }
            };

            // start procedure
            completeCurrentQuest.run();
        } else {
            Dialogs.showErrorDialog(activity, R.string.wrong_code, R.string.wrong_code_button);
        }
    }

    private void completeCurrentQuest(final Runnable nextStep) {
        if (currentQuest != null) {
            currentQuest.setCompleted(true);
            Dialogs.showCompleteMessageDialog(activity, currentQuest.getTitle(), "Wir sind die Größten!", nextStep,
                    currentQuest.getCompleteMessage());
        } else {
            nextStep.run();
        }
    }

    public void showInfoForQuestAtPosition(int position) {
        Quest q = questManager.getActivatedQuestByPosition(position);
        showInfoForQuest(q, "Jetzt haben wirs verstanden.");
    }
    
    public void showInfoForQuest(Quest q, String buttonText) {
        if (q != null) {
            if (q.isCompleted()) {
                Dialogs.showCompleteMessageDialog(activity, q.getTitle(), buttonText, null,
                        q.getWelcomeMessage(), q.getQuestMessage(), q.getCompleteMessage());
            } else {
                //not completed
                if (q.isReveiled()) {
                    Dialogs.showCompleteMessageDialog(activity, q.getTitle(), buttonText, null,
                            currentQuest.getWelcomeMessage(), q.getQuestMessage());
                } else {
                    Dialogs.showCompleteMessageDialog(activity, q.getTitle(), buttonText, null,
                            currentQuest.getWelcomeMessage());
                }
            }
        }
    }
    
    private void checkDistanceTrigger() {
        updateCurrentQuest();
        if (currentQuest != null && !currentQuest.isReveiled()) {
            float currentDistance = navigator.getDistance();
            if (currentDistance <= currentQuest.getDistanceTrigger()) {
                currentQuest.setReveiled(true);
                showInfoForQuest(currentQuest, "Challenge accepted...");
            }
        }
    }

    public void getGameState(Editor editor) {
        
        ArrayList<String> completedQuests = new ArrayList<String>();
        String openQuestCode = null;
        String reveiledQuestCode = null;
        
        for (Quest q: questManager.getActivatedQuests()) {
            if (q.isCompleted()) {
                completedQuests.add(q.getCode());
            } else if (q.isReveiled()) {
                reveiledQuestCode = q.getCode();
            } else {
                openQuestCode = q.getCode();
            }
        }
        
        editor.putString(STATE_COMPLETED_QUESTS, TextUtils.join(";", completedQuests)); //n items, joined by ";"
        editor.putString(STATE_REVEILED_QUEST, reveiledQuestCode); //max 1
        editor.putString(STATE_OPEN_QUEST, openQuestCode); //max 1
    }
    
    public void restoreGameState(SharedPreferences sharedPref) {
        if (sharedPref != null) {
            String completedQuests = sharedPref.getString(STATE_COMPLETED_QUESTS, null);
            String reveiledQuest = sharedPref.getString(STATE_REVEILED_QUEST, null);
            String openQuest = sharedPref.getString(STATE_OPEN_QUEST, null);
            
            if (completedQuests != null && !completedQuests.isEmpty()) {
                String[] completedQuestsArray = completedQuests.split(";");
                for (String code: completedQuestsArray) {
                    questManager.silentlyActivateQuest(code, true, true);
                }
            }
            if (reveiledQuest != null) {
                questManager.silentlyActivateQuest(reveiledQuest, true, false);
            }
            if (openQuest != null) {
                questManager.silentlyActivateQuest(openQuest, false, false);
            }
        }
        //restart navigation
        updateCurrentQuest();
        if (currentQuest != null) {
            navigator.setLocation(currentQuest.getLatitude(), currentQuest.getLongitude());
        }
    }
    
    public void resetGame() {
        questManager.reset();
        currentQuest = null;
        navigator.resetLocation();
    }
}
