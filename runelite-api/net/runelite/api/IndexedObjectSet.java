package net.runelite.api;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IndexedObjectSet<T> extends Iterable<T> {
   T byIndex(int var1);

   default Stream<T> stream() {
      return StreamSupport.stream(this.spliterator(), false);
   }

   default Spliterator<T> spliterator() {
      return Spliterators.spliteratorUnknownSize(this.iterator(), 17);
   }
}
