package net.runelite.client.plugins.timetracking.clocks;

import com.google.common.base.Strings;
import com.google.common.collect.Comparators;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.timetracking.SortOrder;
import net.runelite.client.plugins.timetracking.TimeTrackingConfig;

@Singleton
public class ClockManager {
   @Inject
   private ConfigManager configManager;
   @Inject
   private TimeTrackingConfig config;
   @Inject
   private Notifier notifier;
   @Inject
   private Gson gson;
   private final List<Timer> timers = new CopyOnWriteArrayList();
   private final List<Stopwatch> stopwatches = new ArrayList();
   private final ClockTabPanel clockTabPanel = new ClockTabPanel(this);

   void addTimer() {
      this.timers.add(new Timer("Timer " + (this.timers.size() + 1), (long)(this.config.defaultTimerMinutes() * 60)));
      this.saveTimers();
      ClockTabPanel var10000 = this.clockTabPanel;
      Objects.requireNonNull(var10000);
      SwingUtilities.invokeLater(var10000::rebuild);
   }

   void addStopwatch() {
      List var10000 = this.stopwatches;
      int var10003 = this.stopwatches.size();
      var10000.add(new Stopwatch("Stopwatch " + (var10003 + 1)));
      this.saveStopwatches();
      ClockTabPanel var1 = this.clockTabPanel;
      Objects.requireNonNull(var1);
      SwingUtilities.invokeLater(var1::rebuild);
   }

   void removeTimer(Timer timer) {
      this.timers.remove(timer);
      this.saveTimers();
      ClockTabPanel var10000 = this.clockTabPanel;
      Objects.requireNonNull(var10000);
      SwingUtilities.invokeLater(var10000::rebuild);
   }

   void removeStopwatch(Stopwatch stopwatch) {
      this.stopwatches.remove(stopwatch);
      this.saveStopwatches();
      ClockTabPanel var10000 = this.clockTabPanel;
      Objects.requireNonNull(var10000);
      SwingUtilities.invokeLater(var10000::rebuild);
   }

   public long getActiveTimerCount() {
      return this.timers.stream().filter(Clock::isActive).count();
   }

   public long getActiveStopwatchCount() {
      return this.stopwatches.stream().filter(Clock::isActive).count();
   }

   public boolean checkCompletion() {
      boolean changed = false;
      Iterator var2 = this.timers.iterator();

      while(var2.hasNext()) {
         Timer timer = (Timer)var2.next();
         if (timer.isActive() && timer.getDisplayTime() == 0L) {
            timer.pause();
            changed = true;
            this.notifier.notify(this.config.timerNotification(), "[" + timer.getName() + "] has finished counting down.");
            if (timer.isLoop()) {
               timer.start();
            }
         }
      }

      if (changed) {
         this.saveTimers();
         ClockTabPanel var10000 = this.clockTabPanel;
         Objects.requireNonNull(var10000);
         SwingUtilities.invokeLater(var10000::rebuild);
      }

      return changed;
   }

   public boolean checkTimerOrder() {
      SortOrder sortOrder = this.config.sortOrder();
      if (sortOrder != SortOrder.NONE) {
         Comparator<Timer> comparator = Comparator.comparingLong(Timer::getDisplayTime);
         if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
         }

         if (!Comparators.isInOrder(this.timers, comparator)) {
            this.timers.sort(comparator);
            ClockTabPanel var10000 = this.clockTabPanel;
            Objects.requireNonNull(var10000);
            SwingUtilities.invokeLater(var10000::rebuild);
            return true;
         }
      }

      return false;
   }

   public void checkForWarnings() {
      Iterator var1 = this.timers.iterator();

      while(var1.hasNext()) {
         Timer timer = (Timer)var1.next();
         timer.setWarning(timer.getDisplayTime() <= (long)this.config.timerWarningThreshold());
      }

   }

   public void loadTimers() {
      String timersJson = this.configManager.getConfiguration("timetracking", "timers");
      if (!Strings.isNullOrEmpty(timersJson)) {
         List<Timer> timers = (List)this.gson.fromJson(timersJson, (new TypeToken<ArrayList<Timer>>() {
         }).getType());
         this.timers.clear();
         this.timers.addAll(timers);
         ClockTabPanel var10000 = this.clockTabPanel;
         Objects.requireNonNull(var10000);
         SwingUtilities.invokeLater(var10000::rebuild);
      }

   }

   public void loadStopwatches() {
      String stopwatchesJson = this.configManager.getConfiguration("timetracking", "stopwatches");
      if (!Strings.isNullOrEmpty(stopwatchesJson)) {
         List<Stopwatch> stopwatches = (List)this.gson.fromJson(stopwatchesJson, (new TypeToken<ArrayList<Stopwatch>>() {
         }).getType());
         this.stopwatches.clear();
         this.stopwatches.addAll(stopwatches);
         ClockTabPanel var10000 = this.clockTabPanel;
         Objects.requireNonNull(var10000);
         SwingUtilities.invokeLater(var10000::rebuild);
      }

   }

   public void clear() {
      this.timers.clear();
      this.stopwatches.clear();
      ClockTabPanel var10000 = this.clockTabPanel;
      Objects.requireNonNull(var10000);
      SwingUtilities.invokeLater(var10000::rebuild);
   }

   void saveToConfig() {
      this.saveTimers();
      this.saveStopwatches();
   }

   void saveTimers() {
      String json = this.gson.toJson(this.timers);
      this.configManager.setConfiguration("timetracking", "timers", json);
   }

   void saveStopwatches() {
      String json = this.gson.toJson(this.stopwatches);
      this.configManager.setConfiguration("timetracking", "stopwatches", json);
   }

   public List<Timer> getTimers() {
      return this.timers;
   }

   public List<Stopwatch> getStopwatches() {
      return this.stopwatches;
   }

   public ClockTabPanel getClockTabPanel() {
      return this.clockTabPanel;
   }
}
