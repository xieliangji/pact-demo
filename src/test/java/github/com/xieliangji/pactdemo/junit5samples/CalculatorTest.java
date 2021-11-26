package github.com.xieliangji.pactdemo.junit5samples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Coder   谢良基
 * Date    2021/11/20 20:40
 */
// Test classes must not be abstract and must have a single constructor.
public class CalculatorTest {

    // unless otherwise stated, all core annotations are located in org.junit.jupiter.api package in the junit-jupiter-api module.
    // Test Method: any instance method that is directly annotated or meta-annotated with
    // @Test, @RepeatedTest, @ParameterizedTest, @TestFactory, or @TestTemplate
    @Test
    public void testAdd() {
        Calculator calculator = new Calculator();
        assertEquals(2, calculator.add(1, 1));
    }
}
