package net.runelite.client.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Singleton;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineEvent.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AudioPlayer {
   private static final Logger log = LoggerFactory.getLogger(AudioPlayer.class);
   private Line prevLine;

   public void play(File file, float gain) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
      BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));

      try {
         this.play((InputStream)stream, gain);
      } catch (Throwable var7) {
         try {
            stream.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      stream.close();
   }

   public void play(Class<?> c, String path, float gain) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
      BufferedInputStream stream = new BufferedInputStream(c.getResourceAsStream(path));

      try {
         this.play((InputStream)stream, gain);
      } catch (Throwable var8) {
         try {
            stream.close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         throw var8;
      }

      stream.close();
   }

   public void play(InputStream stream, float gain) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
      AudioInputStream audio = AudioSystem.getAudioInputStream(stream);

      try {
         DataLine line = this.getSelfClosingLine(audio);
         if (gain != 0.0F) {
            this.trySetGain(line, gain);
         }

         line.start();
      } catch (Throwable var7) {
         if (audio != null) {
            try {
               audio.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (audio != null) {
         audio.close();
      }

   }

   private DataLine getSelfClosingLine(AudioInputStream stream) throws IOException, LineUnavailableException {
      Clip clip = AudioSystem.getClip();

      try {
         clip.open(stream);
      } catch (IOException var4) {
         IOException e = var4;
         clip.close();
         throw e;
      }

      clip.addLineListener((event) -> {
         if (event.getType() == Type.STOP) {
            synchronized(this) {
               if (this.prevLine != null) {
                  this.prevLine.close();
               }

               this.prevLine = clip;
            }
         }
      });
      return clip;
   }

   private void trySetGain(DataLine line, float gain) {
      try {
         FloatControl control = (FloatControl)line.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
         control.setValue(gain);
      } catch (Exception var4) {
         Exception e = var4;
         log.warn("Failed to set gain: {}", e.getMessage());
      }

   }
}
