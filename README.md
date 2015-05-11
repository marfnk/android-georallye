# android-georallye
A simple-but-fun GPS-enhanced, compass-only, geo caching rallye-app for custom adventures

![Screenshot 1](https://raw.githubusercontent.com/marfnk/android-georallye/master/screenshot_1_small.png)
![Screenshot 2](https://raw.githubusercontent.com/marfnk/android-georallye/master/screenshot_2_small.png)

# What?
This is an app for geeks that like to do their own geocaching-like rallye. Therefore you have to set your own quests. Each quest has a GPS location and a task/question. Once the user is near to the location the question is reveilled. She/He can now input the answer and the next quest with a new location gets activated. The GPS coordinates are hidden the whole time by purpose: You only have a compass direction and the distance to the target location. This is trickier than searching a position on a map.

# Create your own rallye with friends
1. Clone this project, open it with ADT (compilation with Android v4.2.2 preferred)
2. Open data/Quests.java and create your quests (follow the instructions in that file). A quest looks like that: 
    
        Quest q = new Quest();
        q.setCode("start");
        q.setTitle("Introduction");
        q.setLatitude(53.553017);
        q.setLongitude(9.994772);
        q.setDistanceTrigger(10);
        q.setWelcomeMessage("Welcome to this rallye! Follow the compass to the center of Hamburg, Germany until the distance is as low as possible. A quest will be displayed as soon as you're nearer than 10m!");
        q.setQuestMessage("This is an example quest. The answer is the access code to the next quest. When was Mozart born?");
        q.setCompleteMessage("Congrats! You finished your first quest!");
        quests.add(q); //add as many as you like

3. Compile and test, share the app with you friends and have a nice georallye-adventure