package net.runelite.client.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
   private final Class<?> baseType;
   private final String typeFieldName;
   private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap();
   private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap();

   private RuntimeTypeAdapterFactory(Class<?> baseType, String typeFieldName) {
      if (typeFieldName != null && baseType != null) {
         this.baseType = baseType;
         this.typeFieldName = typeFieldName;
      } else {
         throw new NullPointerException();
      }
   }

   public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName) {
      return new RuntimeTypeAdapterFactory(baseType, typeFieldName);
   }

   public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType) {
      return new RuntimeTypeAdapterFactory(baseType, "type");
   }

   public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type, String label) {
      if (type != null && label != null) {
         if (!this.subtypeToLabel.containsKey(type) && !this.labelToSubtype.containsKey(label)) {
            this.labelToSubtype.put(label, type);
            this.subtypeToLabel.put(type, label);
            return this;
         } else {
            throw new IllegalArgumentException("types and labels must be unique");
         }
      } else {
         throw new NullPointerException();
      }
   }

   public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type) {
      return this.registerSubtype(type, type.getSimpleName());
   }

   public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
      if (type.getRawType() != this.baseType) {
         return null;
      } else {
         final Map<String, TypeAdapter<?>> labelToDelegate = new LinkedHashMap();
         final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedHashMap();
         Iterator var5 = this.labelToSubtype.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry<String, Class<?>> entry = (Map.Entry)var5.next();
            TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get((Class)entry.getValue()));
            labelToDelegate.put((String)entry.getKey(), delegate);
            subtypeToDelegate.put((Class)entry.getValue(), delegate);
         }

         return (new TypeAdapter<R>() {
            public R read(JsonReader in) throws IOException {
               JsonElement jsonElement = Streams.parse(in);
               JsonElement labelJsonElement = jsonElement.getAsJsonObject().remove(RuntimeTypeAdapterFactory.this.typeFieldName);
               String var10002;
               if (labelJsonElement == null) {
                  var10002 = String.valueOf(RuntimeTypeAdapterFactory.this.baseType);
                  throw new JsonParseException("cannot deserialize " + var10002 + " because it does not define a field named " + RuntimeTypeAdapterFactory.this.typeFieldName);
               } else {
                  String label = labelJsonElement.getAsString();
                  TypeAdapter<R> delegate = (TypeAdapter)labelToDelegate.get(label);
                  if (delegate == null) {
                     var10002 = String.valueOf(RuntimeTypeAdapterFactory.this.baseType);
                     throw new JsonParseException("cannot deserialize " + var10002 + " subtype named " + label + "; did you forget to register a subtype?");
                  } else {
                     return delegate.fromJsonTree(jsonElement);
                  }
               }
            }

            public void write(JsonWriter out, R value) throws IOException {
               Class<?> srcType = value.getClass();
               String label = (String)RuntimeTypeAdapterFactory.this.subtypeToLabel.get(srcType);
               TypeAdapter<R> delegate = (TypeAdapter)subtypeToDelegate.get(srcType);
               if (delegate == null) {
                  throw new JsonParseException("cannot serialize " + srcType.getName() + "; did you forget to register a subtype?");
               } else {
                  JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();
                  if (jsonObject.has(RuntimeTypeAdapterFactory.this.typeFieldName)) {
                     String var10002 = srcType.getName();
                     throw new JsonParseException("cannot serialize " + var10002 + " because it already defines a field named " + RuntimeTypeAdapterFactory.this.typeFieldName);
                  } else {
                     JsonObject clone = new JsonObject();
                     clone.add(RuntimeTypeAdapterFactory.this.typeFieldName, new JsonPrimitive(label));
                     Iterator var8 = jsonObject.entrySet().iterator();

                     while(var8.hasNext()) {
                        Map.Entry<String, JsonElement> e = (Map.Entry)var8.next();
                        clone.add((String)e.getKey(), (JsonElement)e.getValue());
                     }

                     Streams.write(clone, out);
                  }
               }
            }
         }).nullSafe();
      }
   }
}
