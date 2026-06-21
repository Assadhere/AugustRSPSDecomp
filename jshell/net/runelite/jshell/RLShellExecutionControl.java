package net.runelite.jshell;

import java.util.Map;
import jdk.jshell.execution.DirectExecutionControl;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControlProvider;
import jdk.jshell.spi.ExecutionEnv;

public class RLShellExecutionControl extends DirectExecutionControl implements ExecutionControlProvider {
   public String name() {
      return this.getClass().getName();
   }

   public ExecutionControl generate(ExecutionEnv env, Map<String, String> parameters) throws Throwable {
      return this;
   }
}
