package github.com.xieliangji.pactdemo.junit5samples.metacompose;

import github.com.xieliangji.pactdemo.junit5samples.CalculatorTest;

/**
 * Coder   谢良基
 * Date    2021/11/20 21:00
 */
public class InheritedCalculatorTest extends CalculatorTest {
    // inherit all test methods and lifecycle methods

    // Lifecycle Method: any method that is directly annotated or meta-annotated with
    // @BeforeAll, @AfterAll, @BeforeEach, or @AfterEach
}
