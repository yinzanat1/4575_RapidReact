package org.firstinspires.ftc.teamcode;

public class AutoScheduler {
    public class scheduleItem {
       public int item;
       public float itemValue;
       public String itemName;
       public boolean completed;
        //constructor
       public scheduleItem (int itemIn, float itemValueIn, String itemNameIn) {
           item = itemIn;
           itemValue = itemValueIn;
           itemName = itemNameIn;
       }
    }
    scheduleItem[] autoArray = new scheduleItem[15];
    //constructor
    public AutoScheduler () {

    }
    public int arrayScheduler (boolean completedIn) {
// commented this out & added return to get the code to run
        //completed = completedIn;
return 1;
    }
}
