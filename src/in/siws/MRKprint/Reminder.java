package in.siws.MRKprint;

import java.util.Timer;
import java.util.TimerTask;

class Reminder {

    Timer timer;

    public Reminder(int seconds) {
   timer = new Timer();
   timer.schedule(new RemindTask(), seconds*1000);
}

class RemindTask extends TimerTask {
   public void run() {
      
       System.exit(0);
       timer.cancel(); //Terminate the timer thread
   }
}


}