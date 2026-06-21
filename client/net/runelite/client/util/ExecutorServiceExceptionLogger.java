package net.runelite.client.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExecutorServiceExceptionLogger implements ScheduledExecutorService {
   private final ScheduledExecutorService service;

   private static Runnable monitor(Runnable command) {
      return RunnableExceptionLogger.wrap(command);
   }

   private static <V> Callable<V> monitor(Callable<V> command) {
      return CallableExceptionLogger.wrap(command);
   }

   public <T> Future<T> submit(Callable<T> task) {
      return this.service.submit(monitor(task));
   }

   public <T> Future<T> submit(Runnable task, T result) {
      return this.service.submit(monitor(task), result);
   }

   public Future<?> submit(Runnable task) {
      return this.service.submit(monitor(task));
   }

   public void execute(Runnable command) {
      this.service.execute(monitor(command));
   }

   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
      return this.service.scheduleAtFixedRate(monitor(command), initialDelay, period, unit);
   }

   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
      return this.service.scheduleWithFixedDelay(monitor(command), initialDelay, delay, unit);
   }

   public void shutdown() {
      this.service.shutdown();
   }

   public List<Runnable> shutdownNow() {
      return this.service.shutdownNow();
   }

   public boolean isShutdown() {
      return this.service.isShutdown();
   }

   public boolean isTerminated() {
      return this.service.isTerminated();
   }

   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      return this.service.awaitTermination(timeout, unit);
   }

   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
      return this.service.invokeAll(tasks);
   }

   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
      return this.service.invokeAll(tasks, timeout, unit);
   }

   public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
      return this.service.invokeAny(tasks);
   }

   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return this.service.invokeAny(tasks, timeout, unit);
   }

   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      return this.service.schedule(command, delay, unit);
   }

   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
      return this.service.schedule(callable, delay, unit);
   }

   public ExecutorServiceExceptionLogger(ScheduledExecutorService service) {
      this.service = service;
   }
}
