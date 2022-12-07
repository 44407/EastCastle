package eastcastle.util;

import java.util.TimerTask;

/**
 * Wraps TimerTask instances to ensure that all executions do not break the
 * timer due to a thrown exception
 */
public class SafeTimerTask extends TimerTask {
   private final TimerTask task;

   public SafeTimerTask(TimerTask task) {
      this.task = task;
   }

   public void run() {
      try {
         task.run();
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }
}