package com.dnocode.lib.business.list.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dino
 * annotation used for defince
 * binding beetween model field and
 * view components
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {
    /**components ids  @return*/
    int[] to();
    Class[] on();
    boolean skipAutoBinding() default  false;
}
