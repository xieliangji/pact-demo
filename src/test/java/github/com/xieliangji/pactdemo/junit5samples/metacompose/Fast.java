package github.com.xieliangji.pactdemo.junit5samples.metacompose;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Coder   谢良基
 * Date    2021/11/20 20:52
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag("fast")
public @interface Fast {
}
