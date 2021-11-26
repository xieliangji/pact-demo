package github.com.xieliangji.pactdemo.junit5samples.metacompose;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Coder   谢良基
 * Date    2021/11/20 20:54
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag("fast")
@Test
public @interface FastTest {
}
