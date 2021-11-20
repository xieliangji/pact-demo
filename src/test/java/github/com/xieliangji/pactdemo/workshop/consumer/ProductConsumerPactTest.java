package github.com.xieliangji.pactdemo.workshop.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonArrayMinLike;
import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Coder   谢良基
 * Date    2021/11/20 14:57
 */
@ExtendWith(PactConsumerTestExt.class)
public class ProductConsumerPactTest {

    private Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    @Pact(consumer = "ConsumerApplication", provider = "ProviderApplication")
    public RequestResponsePact getAllProducts(PactDslWithProvider provider) {
        return provider.given("products exist")
                .uponReceiving("get all products")
                .method("GET")
                .path("/products")
                .matchHeader("Authorization", "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")
                .willRespondWith()
                .status(200)
                .headers(headers())
                .body(newJsonArrayMinLike(2, array ->
                        array.object(obj -> {
                            obj.stringType("id", "09");
                            obj.stringType("type", "CREDIT_CARD");
                            obj.stringType("name", "Gem Visa");
                        })
                ).build())
                .toPact();
    }

    @Pact(consumer = "ConsumerApplication", provider = "ProviderApplication")
    public RequestResponsePact noProductsExist(PactDslWithProvider provider) {
        return provider.given("no products exist")
                .uponReceiving("get all products")
                .method("GET")
                .path("/products")
                .matchHeader("Authorization", "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")
                .willRespondWith()
                .status(200)
                .headers(headers())
                .body("[]")
                .toPact();
    }

    @Pact(consumer = "ConsumerApplication", provider = "ProviderApplication")
    public RequestResponsePact allProductsNoAuthToken(PactDslWithProvider provider) {
        return provider.given("products exist")
                .uponReceiving("get all products with no auth")
                .method("GET")
                .path("/products")
                .willRespondWith()
                .status(401)
                .toPact();
    }

    @Pact(consumer = "ConsumerApplication", provider = "ProviderApplication")
    public RequestResponsePact getID10Product(PactDslWithProvider provider) {
        return provider.given("product with ID 10 exists")
                .uponReceiving("get product with ID 10")
                .method("GET")
                .path("/product/10")
                .matchHeader("Authorization", "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")
                .willRespondWith()
                .status(200)
                .headers(headers())
                .body(newJsonBody(obj -> {
                    obj.stringType("id", "10");
                    obj.stringType("type", "CREDIT_CARD");
                    obj.stringType("name", "28 Degrees");
                }).build())
                .toPact();
    }

    @Pact(consumer = "ConsumerApplication", provider = "ProviderApplication")
    public RequestResponsePact productDoesNotExist(PactDslWithProvider provider) {
        return provider.given("product with ID 11 does not exist")
                .uponReceiving("get product with ID 11")
                .method("GET")
                .path("/product/11")
                .matchHeader("Authorization", "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Pact(consumer = "ConsumerApplication", provider = "ProviderApplication")
    public RequestResponsePact productWithNoAuthToken(PactDslWithProvider provider) {
        return provider.given("product with ID exists")
                .uponReceiving("get product by id 10 with no auth token")
                .method("GET")
                .path("/product/10")
                .willRespondWith()
                .status(401)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getAllProducts")
    public void getAllProducts_whenProductsExist(MockServer mockServer) {
        Product product = new Product();
        product.setId("09");
        product.setType("CREDIT_CARD");
        product.setName("Gem Visa");

        List<Product> expected = Arrays.asList(product, product);

        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        List<Product> products = new ProductService(restTemplate).getAllProducts();
        assertEquals(expected, products);
    }

    @Test
    @PactTestFor(pactMethod = "noProductsExist")
    public void getAllProducts_whenNoProductExist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();

        List<Product> products = new ProductService(restTemplate).getAllProducts();
        assertEquals(Collections.emptyList(), products);
    }

    @Test
    @PactTestFor(pactMethod = "allProductsNoAuthToken")
    public void getAllProducts_whenNoAuth(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();

        HttpClientErrorException e = assertThrows(HttpClientErrorException.class, () -> new ProductService(restTemplate).getAllProducts());
        assertEquals(401, e.getRawStatusCode());
    }

    @Test
    @PactTestFor(pactMethod = "getID10Product")
    public void getProductById_whenProductWithId10Exists(MockServer mockServer) {
        Product expected = new Product();
        expected.setId("10");
        expected.setType("CREDIT_CARD");
        expected.setName("28 Degrees");

        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        Product product = new ProductService(restTemplate).getProduct("10");

        assertEquals(expected, product);
    }

    @Test
    @PactTestFor(pactMethod = "productDoesNotExist")
    public void getProductById_whenProductWithId11DoesNotExist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();

        HttpClientErrorException e = assertThrows(HttpClientErrorException.class, () -> new ProductService(restTemplate).getProduct("11"));

        assertEquals(404, e.getRawStatusCode());
    }

    @Test
    @PactTestFor(pactMethod = "productWithNoAuthToken")
    public void getProductById_whenNoAuth(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();

        HttpClientErrorException e = assertThrows(HttpClientErrorException.class, () -> new ProductService(restTemplate).getProduct("10"));
        assertEquals(401, e.getRawStatusCode());
    }
}
