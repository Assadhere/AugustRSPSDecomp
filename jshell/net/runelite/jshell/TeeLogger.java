package net.runelite.jshell;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class TeeLogger implements Logger {
   private static final String TRACE = "[TRACE] ";
   private static final String DEBUG = "[DEBUG] ";
   private static final String INFO = "[INFO] ";
   private static final String WARN = "[WARN] ";
   private static final String ERROR = "[ERROR] ";
   private final Logger delegate;
   private final Consumer<String> messageConsumer;

   public String getName() {
      return "RLShell";
   }

   public boolean isTraceEnabled() {
      return true;
   }

   private void log(String level, String message, Object... format) {
      FormattingTuple fmt = MessageFormatter.arrayFormat(message, format);
      StringBuilder msg = new StringBuilder();
      msg.append(level).append(fmt.getMessage());
      Throwable throwable = fmt.getThrowable();
      if (throwable != null) {
         msg.append("\n");
         CharArrayWriter caw = new CharArrayWriter();
         PrintWriter pw = new PrintWriter(caw);

         try {
            throwable.printStackTrace(pw);
         } catch (Throwable var12) {
            try {
               pw.close();
            } catch (Throwable var11) {
               var12.addSuppressed(var11);
            }

            throw var12;
         }

         pw.close();
         msg.append(caw.toString());
      }

      this.messageConsumer.accept(msg.toString());
   }

   public void trace(String msg) {
      this.delegate.trace(msg);
      this.log("[TRACE] ", msg);
   }

   public void trace(String format, Object arg) {
      this.delegate.trace(format, arg);
      this.log("[TRACE] ", format, arg);
   }

   public void trace(String format, Object arg1, Object arg2) {
      this.delegate.trace(format, arg1, arg2);
      this.log("[TRACE] ", format, arg1, arg2);
   }

   public void trace(String format, Object... arguments) {
      this.delegate.trace(format, arguments);
      this.log("[TRACE] ", format, arguments);
   }

   public void trace(String msg, Throwable t) {
      this.delegate.trace(msg, t);
      this.log("[TRACE] ", msg, t);
   }

   public boolean isTraceEnabled(Marker marker) {
      return true;
   }

   public void trace(Marker marker, String msg) {
      this.delegate.trace(marker, msg);
      this.log("[TRACE] ", msg);
   }

   public void trace(Marker marker, String format, Object arg) {
      this.delegate.trace(marker, format, arg);
      this.log("[TRACE] ", format, arg);
   }

   public void trace(Marker marker, String format, Object arg1, Object arg2) {
      this.delegate.trace(marker, format, arg1, arg2);
      this.log("[TRACE] ", format, arg1, arg2);
   }

   public void trace(Marker marker, String format, Object... argArray) {
      this.delegate.trace(marker, format, argArray);
      this.log("[TRACE] ", format, argArray);
   }

   public void trace(Marker marker, String msg, Throwable t) {
      this.delegate.trace(marker, msg, t);
      this.log("[TRACE] ", msg, t);
   }

   public boolean isDebugEnabled() {
      return true;
   }

   public void debug(String msg) {
      this.delegate.debug(msg);
      this.log("[DEBUG] ", msg);
   }

   public void debug(String format, Object arg) {
      this.delegate.debug(format, arg);
      this.log("[DEBUG] ", format, arg);
   }

   public void debug(String format, Object arg1, Object arg2) {
      this.delegate.debug(format, arg1, arg2);
      this.log("[DEBUG] ", format, arg1, arg2);
   }

   public void debug(String format, Object... arguments) {
      this.delegate.debug(format, arguments);
      this.log("[DEBUG] ", format, arguments);
   }

   public void debug(String msg, Throwable t) {
      this.delegate.debug(msg, t);
      this.log("[DEBUG] ", msg, t);
   }

   public boolean isDebugEnabled(Marker marker) {
      return true;
   }

   public void debug(Marker marker, String msg) {
      this.delegate.debug(marker, msg);
      this.log("[DEBUG] ", msg);
   }

   public void debug(Marker marker, String format, Object arg) {
      this.delegate.debug(marker, format, arg);
      this.log("[DEBUG] ", format, arg);
   }

   public void debug(Marker marker, String format, Object arg1, Object arg2) {
      this.delegate.debug(marker, format, arg1, arg2);
      this.log("[DEBUG] ", format, arg1, arg2);
   }

   public void debug(Marker marker, String format, Object... arguments) {
      this.delegate.debug(marker, format, arguments);
      this.log("[DEBUG] ", format, arguments);
   }

   public void debug(Marker marker, String msg, Throwable t) {
      this.delegate.debug(marker, msg, t);
      this.log("[DEBUG] ", msg, t);
   }

   public boolean isInfoEnabled() {
      return true;
   }

   public void info(String msg) {
      this.delegate.info(msg);
      this.log("[INFO] ", msg);
   }

   public void info(String format, Object arg) {
      this.delegate.info(format, arg);
      this.log("[INFO] ", format, arg);
   }

   public void info(String format, Object arg1, Object arg2) {
      this.delegate.info(format, arg1, arg2);
      this.log("[INFO] ", format, arg1, arg2);
   }

   public void info(String format, Object... arguments) {
      this.delegate.info(format, arguments);
      this.log("[INFO] ", format, arguments);
   }

   public void info(String msg, Throwable t) {
      this.delegate.info(msg, t);
      this.log("[INFO] ", msg, t);
   }

   public boolean isInfoEnabled(Marker marker) {
      return true;
   }

   public void info(Marker marker, String msg) {
      this.delegate.info(marker, msg);
      this.log("[INFO] ", msg);
   }

   public void info(Marker marker, String format, Object arg) {
      this.delegate.info(marker, format, arg);
      this.log("[INFO] ", format, arg);
   }

   public void info(Marker marker, String format, Object arg1, Object arg2) {
      this.delegate.info(marker, format, arg1, arg2);
      this.log("[INFO] ", format, arg1, arg2);
   }

   public void info(Marker marker, String format, Object... arguments) {
      this.delegate.info(marker, format, arguments);
      this.log("[INFO] ", format, arguments);
   }

   public void info(Marker marker, String msg, Throwable t) {
      this.delegate.info(marker, msg, t);
      this.log("[INFO] ", msg, t);
   }

   public boolean isWarnEnabled() {
      return true;
   }

   public void warn(String msg) {
      this.delegate.warn(msg);
      this.log("[WARN] ", msg);
   }

   public void warn(String format, Object arg) {
      this.delegate.warn(format, arg);
      this.log("[WARN] ", format, arg);
   }

   public void warn(String format, Object... arguments) {
      this.delegate.warn(format, arguments);
      this.log("[WARN] ", format, arguments);
   }

   public void warn(String format, Object arg1, Object arg2) {
      this.delegate.warn(format, arg1, arg2);
      this.log("[WARN] ", format, arg1, arg2);
   }

   public void warn(String msg, Throwable t) {
      this.delegate.warn(msg, t);
      this.log("[WARN] ", msg, t);
   }

   public boolean isWarnEnabled(Marker marker) {
      return true;
   }

   public void warn(Marker marker, String msg) {
      this.delegate.warn(marker, msg);
      this.log("[WARN] ", msg);
   }

   public void warn(Marker marker, String format, Object arg) {
      this.delegate.warn(marker, format, arg);
      this.log("[WARN] ", format, arg);
   }

   public void warn(Marker marker, String format, Object arg1, Object arg2) {
      this.delegate.warn(marker, format, arg1, arg2);
      this.log("[WARN] ", format, arg1, arg2);
   }

   public void warn(Marker marker, String format, Object... arguments) {
      this.delegate.warn(marker, format, arguments);
      this.log("[WARN] ", format, arguments);
   }

   public void warn(Marker marker, String msg, Throwable t) {
      this.delegate.warn(marker, msg, t);
      this.log("[WARN] ", msg, t);
   }

   public boolean isErrorEnabled() {
      return true;
   }

   public void error(String msg) {
      this.delegate.error(msg);
      this.log("[ERROR] ", msg);
   }

   public void error(String format, Object arg) {
      this.delegate.error(format, arg);
      this.log("[ERROR] ", format, arg);
   }

   public void error(String format, Object arg1, Object arg2) {
      this.delegate.error(format, arg1, arg2);
      this.log("[ERROR] ", format, arg1, arg2);
   }

   public void error(String format, Object... arguments) {
      this.delegate.error(format, arguments);
      this.log("[ERROR] ", format, arguments);
   }

   public void error(String msg, Throwable t) {
      this.delegate.error(msg, t);
      this.log("[ERROR] ", msg, t);
   }

   public boolean isErrorEnabled(Marker marker) {
      return true;
   }

   public void error(Marker marker, String msg) {
      this.delegate.error(marker, msg);
      this.log("[ERROR] ", msg);
   }

   public void error(Marker marker, String format, Object arg) {
      this.delegate.error(marker, format, arg);
      this.log("[ERROR] ", format, arg);
   }

   public void error(Marker marker, String format, Object arg1, Object arg2) {
      this.delegate.error(marker, format, arg1, arg2);
      this.log("[ERROR] ", format, arg1, arg2);
   }

   public void error(Marker marker, String format, Object... arguments) {
      this.delegate.error(marker, format, arguments);
      this.log("[ERROR] ", format, arguments);
   }

   public void error(Marker marker, String msg, Throwable t) {
      this.delegate.error(marker, msg, t);
      this.log("[ERROR] ", msg, t);
   }

   public TeeLogger(Logger delegate, Consumer<String> messageConsumer) {
      this.delegate = delegate;
      this.messageConsumer = messageConsumer;
   }
}
