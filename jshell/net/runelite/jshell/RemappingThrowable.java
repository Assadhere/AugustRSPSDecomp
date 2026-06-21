package net.runelite.jshell;

import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import jdk.jshell.EvalException;

class RemappingThrowable extends Throwable {
   private final String source;
   private final Map<String, Integer> offsets;
   private final Throwable wrapped;
   private final Map<Throwable, Throwable> dejaVu;

   public RemappingThrowable(String source, Map<String, Integer> offsets, Throwable other) {
      this(source, offsets, other, new HashMap());
   }

   private RemappingThrowable(String source, Map<String, Integer> offsets, Throwable other, Map<Throwable, Throwable> dejaVu) {
      this.source = source;
      this.offsets = offsets;
      this.wrapped = other;
      this.dejaVu = dejaVu;
      dejaVu.put(this.wrapped, this);
      this.setStackTrace((StackTraceElement[])Stream.of(this.wrapped.getStackTrace()).map((e) -> {
         Integer boxOffset = (Integer)offsets.get(e.getFileName());
         if (boxOffset == null) {
            return e;
         } else {
            int offset = boxOffset;
            int line = e.getLineNumber();

            for(int i = 0; i <= offset && i < source.length(); ++i) {
               if (source.charAt(i) == '\n') {
                  ++line;
               }
            }

            return new StackTraceElement(Strings.isNullOrEmpty(e.getClassName()) ? "Shell" : e.getClassName(), Strings.isNullOrEmpty(e.getMethodName()) ? "global" : e.getMethodName(), "", line);
         }
      }).toArray((x$0) -> {
         return new StackTraceElement[x$0];
      }));
      if (this.wrapped.getCause() != null) {
         this.initCause(this.remap(this.wrapped.getCause()));
      }

      Throwable[] var5 = this.wrapped.getSuppressed();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Throwable suppressed = var5[var7];
         this.addSuppressed(this.remap(suppressed));
      }

   }

   private Throwable remap(Throwable other) {
      Throwable remap = (Throwable)this.dejaVu.get(other);
      if (remap == null) {
         remap = new RemappingThrowable(this.source, this.offsets, other, this.dejaVu);
      }

      return (Throwable)remap;
   }

   public String getMessage() {
      return this.wrapped.getMessage();
   }

   public String getLocalizedMessage() {
      return this.wrapped.getLocalizedMessage();
   }

   public synchronized Throwable fillInStackTrace() {
      return this;
   }

   public String toString() {
      String className;
      if (this.wrapped instanceof EvalException) {
         className = ((EvalException)this.wrapped).getExceptionClassName();
      } else {
         className = this.wrapped.getClass().getName();
      }

      String message = this.wrapped.getLocalizedMessage();
      return message == null ? className : className + ": " + message;
   }
}
