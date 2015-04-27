package org.marfnk.georallye.data;

import java.util.LinkedList;
import java.util.List;

import org.marfnk.georallye.models.Quest;

/**
 * This is the place to set the quests for your geocaching adventure.
 * Every quest has three messages:
 *   1: A _welcomeMessage_ is displayed, when the quest code is entered.
 *   2: A _questMessage_ is displayed as soon as the distance falls under _distanceTrigger_
 *   3: A _completeMessage_ is displayed after the next quest code was entered but before the next welcomeMessage
 */
public class Quests {
    
    public static List<Quest> getQuests() {
        LinkedList<Quest> quests = new LinkedList<Quest>();
        
        Quest q0 = new Quest();
        q0.setCode("start");
        q0.setTitle("Hoch hinaus!");
        q0.setLatitude(53.601102);
        q0.setLongitude(10.071421);
        q0.setDistanceTrigger(20);
        q0.setWelcomeMessage("Willkommen zur ersten Aufgabe des GeoCaching. Finde die Brücke bei Otto in Hamburg!");
        q0.setQuestMessage("Von hier aus kann man welches Tor sehen? (Tipp: Jetzt \"tor1\" eingeben.)");
        q0.setCompleteMessage("Geschafft! Das war die erste Aufgabe. Weiter geht's!");
        quests.add(q0);
        
        Quest q1 = new Quest();
        q1.setCode("tor1");
        q1.setTitle("Abfahrt");
        q1.setLatitude(53.566304);
        q1.setLongitude(10.020599);
        q1.setDistanceTrigger(20);
        q1.setWelcomeMessage("Gehe zu Tor 1!");
        q1.setQuestMessage("[Neue Aufgabe] (Nächster Code: \"xx22\")");
        q1.setCompleteMessage("Sehr gut gemacht!");
        quests.add(q1);
        
        Quest q2 = new Quest();
        q2.setCode("xx22");
        q2.setTitle("How do they smoke?");
        q2.setLatitude(53.564730);
        q2.setLongitude(10.015311);
        q2.setDistanceTrigger(50);
        q2.setWelcomeMessage("Finde die drei Männer in einem Boot?!");
        q2.setQuestMessage("Das war die Geocaching Tour! Sehr gut gemacht!");
        q2.setCompleteMessage("");
        quests.add(q2);
        
        return quests;
    }

}
