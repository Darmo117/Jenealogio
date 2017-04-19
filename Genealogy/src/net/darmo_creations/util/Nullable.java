package net.darmo_creations.util;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation specifies a parameter that accepts the value <code>null</code>. It is just a hint
 * for developpers and not for reflection purposes.
 * 
 * @author Damien Vergnet
 */
@Documented
@Retention(SOURCE)
@Target(PARAMETER)
public @interface Nullable {}
