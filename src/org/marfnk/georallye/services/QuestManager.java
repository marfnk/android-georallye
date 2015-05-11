package org.marfnk.georallye.services;

import java.util.LinkedList;
import java.util.List;

import org.marfnk.georallye.adapter.Observer;
import org.marfnk.georallye.data.Quests;
import org.marfnk.georallye.models.Quest;


public class QuestManager {

    private List<Quest> allQuests;
    private List<Quest> activatedQuests;
    private List<Observer> actionListeners = new LinkedList<Observer>();

    public QuestManager() {
        allQuests = Quests.getQuests();
        activatedQuests = new LinkedList<Quest>();
    }

    public boolean hasOpenQuest(String id) {
        for (Quest q : allQuests) {
            if (!q.isCompleted() && q.getCode().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public Quest activateQuest(String id) {
        Quest newQuest = null;
        for (Quest q : allQuests) {
            if (q.getCode().equals(id)) {
                newQuest = q;
                activatedQuests.add(q);
                break;
            }
        }
        if (newQuest != null) {
            notifyListeners();
        }
        return newQuest;
    }

    public void completeCurrentQuest() {
        for (Quest q: activatedQuests) {
            if (!q.isCompleted()) {
                q.setCompleted(true);
            }
        }
    }

    public Quest getActivatedQuestByPosition(int position) {
        return activatedQuests.get(position);
    }

    public void addQuestActivationListener(Observer al) {
        actionListeners.add(al);
    }

    private void notifyListeners() {
        for (Observer al : actionListeners) {
            al.onNotified();
        }
    }

    public List<Quest> getActivatedQuests() {
        return new LinkedList<Quest>(activatedQuests);
    }

    public Quest getCurrentQuest() {
        if (activatedQuests.isEmpty()) {
            return null;
        }
        Quest lastQuest = activatedQuests.get(activatedQuests.size()-1);
        if (lastQuest.isCompleted()) {
            return null;
        } else {
            return lastQuest;
        }
    }

    public void silentlyActivateQuest(String code, boolean reveiled, boolean completed) {
        Quest q = activateQuest(code);
        if (q != null) {
            q.setReveiled(reveiled);
            q.setCompleted(completed);
        }
    }

    public void reset() {
        allQuests = Quests.getQuests();
        activatedQuests.clear();
    }

}
