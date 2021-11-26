package github.com.xieliangji.pactdemo.junit5samples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;

/**
 * Coder   谢良基
 * Date    2021/11/21 10:45
 */
// omit public usually.
// Test Class: Must not be <abstract> and must have single <constructor>
// Test Class: top-level class
@DisplayName("Writing Tests step by step") // declare a custom display name for test class or test method
class WritingTests {

    /**
     * Nested: 1) Non-Static Test Class; 2) contains at least one test method.
     */
    @DisplayName("First Test")
    @Nested
    class FirstTest {
        /**
         * Test Method: 1) instance method; 2) directly annotated or meta-annotated
         * with @Test, @RepeatedTest, @ParameterizedTest, @TestFactory, or @TestTemplate
         * not <b>abstract</b>, either return a value
         */
        @Test
        void testAdd() {
            Assertions.assertEquals(2, new Calculator().add(1, 1));
        }
    }

    @Nested
    @DisplayName("Test Class can be inherited")
    class InheritFirstTest extends FirstTest {
        // Inherits all test methods in test class FirstTest.
        /**
         * Test Methods & Lifecycle Methods may be declared locally within current test class, inherited from superclass,
         * or inherited from interfaces
         */
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("fast")    // meta-annotation
    @Test           // meta-annotation
    @interface FastTest {} // @FastTest compose-annotation, owns both functionality of @Test & @Tag

    /**
     * -------------------------------------------------------------------------------------------------------------------------
     * Lifecycle Method: method that is directly annotated or meta-annotated with @BeforeAll, @AfterAll, @BeforeEach, @AfterEach
     * static: @BeforeAll, @AfterAll must be static unless the test class is annotated with @TestInstance(Lifecycle.PER_CLASS)
     * non-static: @BeforeEach & @AfterEach always be non-static
     * -------------------------------------------------------------------------------------------------------------------------
     */
    @BeforeAll // cannot be annotated within @Nested Test Class.
    static void setupAll() {
        System.out.println("Lifecycle Method <BeforeAll> does run once before all test method");
    }

    @AfterAll // cannot be annotated within @Nested Test Class.
    static void teardownAll() {
        System.out.println("Lifecycle Method <AfterAll> does run once after all test method");
    }

    @BeforeEach
    void setupEach() {
        System.out.println("Lifecycle Method <BeforeEach> does run before each test method");
    }

    @AfterEach
    void teardownEach() {
        System.out.println("Lifecycle Method <AfterEach> does run after each test method");
    }

    /**
     * Display Names:
     * Priority: @DisplayName -> @DisplayNameGeneration -> Default configured <class>DisplayNameGenerator</class> ->
     * org.junit.jupiter.api.DisplayNameGenerator.Standard
     */
    @Nested
    @DisplayName("Customize Display Name of nested test class")
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class CustomDisplayName {
        @DisplayName("Custom Display Name of test method")
        @Test
        void customDisplayName() {
            Assertions.assertEquals(1, 2, "assert fail cause 1 != 2");
        }

        @Test
        void will_be_separated_by_space() {
            Assertions.fail("Just Fail");
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Assertions: org.junit.jupiter.api.Assertions gather all static assertion methods.
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Nested
    @DisplayName("assertion methods")
    class AssertionsTest {
        @Test
        void assertSamples() {
            // you can group several assertions into one.
            Assertions.assertAll("label",
                    () -> Assertions.assertEquals(1, 1),
                    () -> Assertions.assertTrue(true),
                    () -> {
                        String a = "LiSiJing";
                        String b = "SunZJ";
                        Assertions.assertEquals(a, b, "will fail.");
                    },
                    () -> Assertions.assertAll("second",
                            () -> Assertions.assertArrayEquals(new int[]{1, 2, 3}, new int[]{1, 2, 3}),
                            () -> Assertions.assertNotEquals(1, 2)));
            Assertions.assertTimeout(Duration.ofSeconds(5), () -> System.out.println("print less than 5 seconds for sure"));
        }
    }

    /**
     * Assumptions: org.junit.jupiter.api.Assumptions class static methods.
     * if assumption false, test method will be ignored.
     */
    @Nested
    class AssumptionTest {
        @Test // ignored
        void assumptionTest() {
            Assumptions.assumeTrue("1".equals(System.getenv("2")));
            System.out.println("AssumptionTestPrinting: " + System.getenv("2"));
        }

        @Test // running
        void assumptionRun() {
            Assumptions.assumeTrue(true);
            System.out.println("AssumptionUnIgnoredPrinting: --------------------------");
        }
    }

    /**
     * Disabling Tests: @Disable - recommend provide the reason for explain disabling.
     */
    @Nested
    class DisableTest {
        @Test
        @Disabled("Disabled Reason: For Disabling Tests Testing.")
        void ignoredTest() {
            System.out.println("DisablingTestPrinting: if this be viewed, error!!!!");
        }
    }

    /**
     * Conditional Test Execution: org.junit.jupiter.api.condition
     * when multiple <class>ExecutionCondition</class> extensions are registered, a container or test is disabled as soon as
     * one of the conditions returns disabled.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @EnabledOnOs(OS.MAC)
    @interface TestOnMac {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @EnabledIf("customEnable")
    @interface LiSiJ{}

    @Nested
    @DisplayName("Conditional Test Execution")
    class ConditionalExecuting {

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void runningOnlyOnWindows() {
            System.out.println("WindowsEnabled: WIN10");
        }

        @TestOnMac // on non -Mac os, ignored.
        void onlyOnMac() {
            System.out.println("MacEnabled: MAC");
        }

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void jdk8() {
            System.out.println("JDK8: if not, in view that is error!!!");
        }

        @Test
        @EnabledOnJre(JRE.JAVA_11)
        void jdk11() {
            System.out.println("JDK11: jdk11 printing...");
        }

        @Test
        @EnabledForJreRange(max=JRE.JAVA_10)
        void jdk11Ignored() {
            System.out.println("JDK11: Error!!!");
        }

        @Test
        @EnabledIfSystemProperty(named = "user", matches = "黎思静")
        void sysProp() {
            System.out.println("SystemProperty: 黎思静， 加油！");
        }

        @Test
        @EnabledIfSystemProperties({@EnabledIfSystemProperty(named = "user", matches = "黎思静", disabledReason = "除非是黎思静")})
        void sysProps() {
            System.out.println("SystemProperties: 黎思静，最棒！");
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk*", disabledReason = "NOT SET JAVA_HOME")
        void env() {
            System.out.println("EnvironmentVariable: 黎思静，最好看！");
        }

        @Test
        @EnabledIfEnvironmentVariables({@EnabledIfEnvironmentVariable(named = "USER", matches = "黎思静", disabledReason = "黎思静不在")})
        void envs() {
            System.out.println("Envs: 黎思静，真美！");
        }

        boolean customEnable() {
            return true;
        }

        @LiSiJ
        @Test
        @DisplayName("For Li Si Jing")
        void causeLISIJING() {
            System.out.println("CustomConditionalTestingPrinting: ---------------------");
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Tagging and Filtering: group tests by tag, and then can be selected for running.
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Nested
    @Tag("黎思静的宝宝")
    class LSJDBaby {
        @Test
        @Tag("")
        void baby1() {
            System.out.println("Baby1: --++--++");
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test Execution Order:
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestClassOrder(ClassOrderer.OrderAnnotation.class)
    class OrderedTests {
        @Test
        @Order(1)
        void b() {
            System.out.println("OrderedPrinting: b");
        }

        @Test
        @Order(2)
        void a() {
            System.out.println("OrderedPrinting: a");
        }

        @Nested
        @Order(2)
        class OrderedNested1 {
            @Test
            void orderNested1() {
                System.out.println("OrderedClassNested1Printing: ...");
            }
        }

        @Nested
        @Order(1)
        class OrderedNested2 {
            @Test
            void orderNested2() {
                System.out.println("OrderedClassNested2Printing: ...");
            }
        }
    }

    /**
     * Test Instance Lifecycle: JUnit creates a new instance of each test class before executing each test method.
     */
    @Nested
    class InstanceLifecycle {
        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        class EachTestMethodGiveAInstance {
            private int state = 0;

            @Test
            @Order(1)
            void changeState() {
                this.state = 1;
                System.out.println("InstanceLifecycle(EachGive): change state success.");
            }

            @Test
            @Order(2)
            void readState() {
                System.out.println("InstanceLifecycle(EachGive): read state = " + state);
            }
        }

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class InstanceLifecycleSameInstance {
            private int state = 0;

            @BeforeAll
            void perClassAllowBeforeAll() {
                System.out.println("Per_Class BeforeAll non-static");
            }

            @AfterAll
            void perClassAllowAfterAll() {
                System.out.println("Per_Class AfterAll non-static");
            }

            @Test
            @Order(1)
            void changeState() {
                this.state = 1;
                System.out.println("InstanceLifecycle(EachGive): change state success.");
            }

            @Test
            @Order(2)
            void readState() {
                System.out.println("InstanceLifecycle(EachGive): read state = " + state);
            }
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Nested Tests: above shows.
     * 1) non-static inner class
     * 2) default @BeforeAll, @AfterAll methods do not work. - Java does not allow static members in inner class
     * -----------------------------------------------------------------------------------------------------------------
     */


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Dependency Injection for Constructors and Methods:
     * If a test class constructor, a test method, or a lifecycle method accepts a parameter, the parameter must be
     * resolved at runtime by a registered <cass>ParameterResolver</cass>
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Nested
    class DependencyInjectionTest {

        @Nested
        class ConstructorInjection {
            ConstructorInjection(TestInfo testInfo, TestReporter testReporter) {
                System.out.println("DependencyInjection(constructor):" + testInfo + " ... " + testReporter);
            }
            @Test
            void call() {}
        }

        @Test
        void methodInjection(TestInfo testInfo, TestReporter testReporter) {
            System.out.println("DependencyInjection(method):" + testInfo + " ... " + testReporter);
            testReporter.publishEntry("fuck");
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test Interfaces and Default Methods
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Nested
    class InterfaceTest implements InterfaceTests {}


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Repeated Tests:
     *
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class RepeatedTests {
        private int state = 0;

        @BeforeEach
        void beforeEach() {
            System.out.println("RepeatedTestsPrinting: Before...");
        }

        @RepeatedTest(value = 10, name = "{displayName}-{currentRepetition}-{totalRepetitions}")
        void repeatedTest() {
            state += 1;
            System.out.println("RepeatedTestsPrinting: " + state);
        }
    }
}


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface InterfaceTests {
    @BeforeAll
    default void beforeAll(TestReporter reporter) {
        reporter.publishEntry("InterfaceTests - BeforeAll");
        System.out.println("InterfaceTests - BeforeAll");
    }

    @AfterAll
    default void afterAll(TestReporter reporter) {
        reporter.publishEntry("InterfaceTests - AfterAll");
        System.out.println("InterfaceTests - AfterAll");
    }

    @BeforeEach
    default void beforeEach(TestInfo testInfo) {
        System.out.println("InterfaceTests: BeforeEach - " + testInfo);
    }

    @AfterEach
    default void afterEach(TestInfo testInfo) {
        System.out.println("InterfaceTests: AfterEach - " + testInfo);
    }

    @Test
    default void interfaceTest() {
        System.out.println("InterfaceTests: ....");
    }
}