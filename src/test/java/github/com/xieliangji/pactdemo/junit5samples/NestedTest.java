package github.com.xieliangji.pactdemo.junit5samples;

import org.junit.jupiter.api.*;

/**
 * Coder   谢良基
 * Date    2021/11/21 10:21
 */
@DisplayName("Nested Sample") // give a meaningful description for test.
public class NestedTest {

    @BeforeAll
    static void setupAll() {
        System.out.println("run once before all test methods.");
    }

    @AfterAll
    static void teardownAll() {
        System.out.println("run once after all test methods.");
    }

    @BeforeEach
    void setupEach() {
        System.out.println("run before a test method.");
    }

    @AfterEach
    void teardownEach() {
        System.out.println("run after a test method.");
    }

    @Test
    @DisplayName("Outer test method")
    void outTestMethod() {
        System.out.println("Outer...");
    }

    @Nested
    class InnerTest1 {

        @Test
        void innerTestMethod() {
            System.out.println(getClass());
        }
    }

    @Nested
    class InnerTest2 {

        @Test
        void innerTestMethod() {
            System.out.println(getClass());
        }
    }
}
