package org.marfnk.georallye.data;

import java.util.LinkedList;
import java.util.List;

import org.marfnk.georallye.models.Quest;

/**
 * This is the place to set the quests for your geocaching adventure. Every
 * quest has three messages: 
 * 
 * 1: A _welcomeMessage_ is displayed, when the quest
 * code is entered.
 * 
 * 2: A _questMessage_ is displayed as soon as the distance
 * falls under _distanceTrigger_
 * 
 * 3: A _completeMessage_ is displayed after the
 * next quest code was entered but before the next welcomeMessage
 * 
 * Use http://www.latlong.net/ to get the coordinates of a target position.
 */
public class Quests {

    public static List<Quest> getQuests() {
        LinkedList<Quest> quests = new LinkedList<Quest>();

        Quest startQuest = new Quest();
        startQuest.setCode("start");
        startQuest.setTitle("Introduction");
        startQuest.setLatitude(53.553017);
        startQuest.setLongitude(9.994772);
        startQuest.setDistanceTrigger(10);
        startQuest.setWelcomeMessage("Welcome to this rallye! Follow the compass until the distance is as low as possible. A quest will be displayed as soon as you're nearer than 10m!");
        startQuest.setQuestMessage("This is an example quest. The answer is the access code to the next quest. When was Mozart born?");
        startQuest.setCompleteMessage("Congrats! You finished your first quest!");
        quests.add(startQuest);
        
        //The rallye always ends with a location, no code is then entered. 
        Quest finalLocation = new Quest();
        finalLocation.setCode("1756");
        finalLocation.setTitle("Final quest");
        finalLocation.setLatitude(52.518612);
        finalLocation.setLongitude(13.375168);
        finalLocation.setDistanceTrigger(5);
        finalLocation.setWelcomeMessage("For this quest you have to travel to the Berlin Reichstag!");
        finalLocation.setQuestMessage("Congrats! You finished this example rallye.");
        finalLocation.setCompleteMessage(null); // since this is the last quest, there is no complete message
        quests.add(finalLocation);

        
        //add as many quests as you like
        return quests;
    }

}
