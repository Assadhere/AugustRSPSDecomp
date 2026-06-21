package net.runelite.client.plugins.gpu;

import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import net.runelite.client.plugins.gpu.template.Template;
import org.lwjgl.opengl.GL33C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Shader {
   private static final Logger log = LoggerFactory.getLogger(Shader.class);
   @VisibleForTesting
   final List<Unit> units = new ArrayList();

   public Shader add(int type, String name) {
      this.units.add(new Unit(type, name));
      return this;
   }

   int compile(Template template) throws ShaderException {
      int program = GL33C.glCreateProgram();
      int[] shaders = new int[this.units.size()];
      int i = 0;
      boolean ok = false;

      while(true) {
         boolean var13 = false;

         try {
            var13 = true;
            if (i >= shaders.length) {
               GL33C.glLinkProgram(program);
               String err;
               if (GL33C.glGetProgrami(program, 35714) == 0) {
                  err = GL33C.glGetProgramInfoLog(program);
                  throw new ShaderException(err);
               }

               GL33C.glValidateProgram(program);
               if (GL33C.glGetProgrami(program, 35715) == 0) {
                  err = GL33C.glGetProgramInfoLog(program);
                  throw new ShaderException(err);
               }

               ok = true;
               var13 = false;
               break;
            }

            Unit unit = (Unit)this.units.get(i);
            int shader = GL33C.glCreateShader(unit.type);
            if (shader == 0) {
               throw new ShaderException("Unable to create shader of type " + unit.type);
            }

            String source = template.load(unit.filename);
            GL33C.glShaderSource(shader, source);
            GL33C.glCompileShader(shader);
            if (GL33C.glGetShaderi(shader, 35713) != 1) {
               String err = GL33C.glGetShaderInfoLog(shader);
               GL33C.glDeleteShader(shader);
               logShaderSource(source);
               throw new ShaderException(err);
            }

            GL33C.glAttachShader(program, shader);
            shaders[i++] = shader;
         } finally {
            if (var13) {
               while(i > 0) {
                  --i;
                  int shader = shaders[i];
                  GL33C.glDetachShader(program, shader);
                  GL33C.glDeleteShader(shader);
               }

               if (!ok) {
                  GL33C.glDeleteProgram(program);
               }

            }
         }
      }

      while(i > 0) {
         --i;
         int shader = shaders[i];
         GL33C.glDetachShader(program, shader);
         GL33C.glDeleteShader(shader);
      }

      if (!ok) {
         GL33C.glDeleteProgram(program);
      }

      return program;
   }

   private static void logShaderSource(String source) {
      int lineNum = 1;
      String[] var2 = source.split("\n");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String line = var2[var4];
         log.error("{}: {}", lineNum++, line);
      }

   }

   @VisibleForTesting
   static class Unit {
      private final int type;
      private final String filename;

      public Unit(int type, String filename) {
         this.type = type;
         this.filename = filename;
      }

      public int getType() {
         return this.type;
      }

      public String getFilename() {
         return this.filename;
      }
   }
}
