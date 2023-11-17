package net.vadamdev.vmixin.api.injection;

import java.lang.annotation.*;

/**
 * @author VadamDev
 * @since 16/11/2023
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Inject {
    String method();

    At at();
}
