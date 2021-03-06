package github.com.xieliangji.pactdemo.workshop.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Coder   谢良基
 * Date    2021/11/25 10:35
 *
 * --------------------------------------
 * Step4 - Verify the Provider
 * 1) way1: copy the pact file to provider side.
 */
@Provider("ProviderApplication")
@PactFolder("pacts")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductPactProviderTest {

    @LocalServerPort
    int port;

    @MockBean
    ProductRepository repository;

    @BeforeEach
    void setup(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("products exist")
    void setupProductsExistState() {
        Mockito.when(repository.fetchAll()).thenReturn(
                Arrays.asList(new Product("09", "CREDIT_CARD", "Gem Visa", "v1"),
                        new Product("10", "CREDIT_CARD", "28 Degrees", "v1")));
    }

    @State("product with ID 10 exists")
    void setupProductWithId10ExistsState() {
        Mockito.when(repository.getById("10")).thenReturn(
                Optional.of(new Product("10", "CREDIT_CARD", "28 Degrees", "v1")));
    }

    @State({
            "no products exist",
            "product with ID 11 does not exist"
    })
    void setupNoProductsExistState() {
        Mockito.when(repository.fetchAll()).thenReturn(Collections.emptyList());
    }
}
