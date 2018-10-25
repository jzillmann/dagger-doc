package io.morethan.daggerdoc;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation that can be applied next to a dagger.Module providing additional metadata about the module.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleDoc {

    String category() default "";

}
