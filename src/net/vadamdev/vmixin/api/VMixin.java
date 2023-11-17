package net.vadamdev.vmixin.api;

import java.lang.annotation.*;

/**
 * @author VadamDev
 * @since 16/11/2023
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface VMixin {
    String target();
}
