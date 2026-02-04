package bet.astral.guiman.api.annotations;

import java.lang.annotation.Documented;

/**
 * The minecraft version which allows the use of this functional.
 * Methods annotated with this annotation will throw an exception if the method cannot be used.
 */
@Documented
public @interface MinecraftVersion {
    String value();
}
