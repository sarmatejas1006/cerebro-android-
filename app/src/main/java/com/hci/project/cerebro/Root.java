package com.hci.project.cerebro;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Root {
    /**
     * The value of the JSON root.
     * Results in {"value" : object}
     */
    String value();
}