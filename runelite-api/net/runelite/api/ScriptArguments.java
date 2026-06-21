package net.runelite.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Documented
@Target({ElementType.FIELD})
@interface ScriptArguments {
   int integer() default 0;

   int Long() default 0;

   int string() default 0;
}
