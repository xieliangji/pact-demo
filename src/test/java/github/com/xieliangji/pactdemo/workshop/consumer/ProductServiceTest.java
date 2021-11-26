package github.com.xieliangji.pactdemo.workshop.consumer;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Coder   谢良基
 * Date    2021/11/20 14:43
 *
 * --------------------------------------------------
 * Step2 - Client Tested but integration fails
 * 1) Test our client code hits the expected endpoint
 * 2) That the response is marshalled into an object that is usable, with the correct ID
 */
public class ProductServiceTest {

    private WireMockServer wireMockServer;

    private ProductService productService;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(wireMockServer.baseUrl()).build();
        productService = new ProductService(restTemplate);
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void getAllProducts() {
        // mockserver stubbing - /products
        wireMockServer.stubFor(get(urlPathEqualTo("/products"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[" +
                                "{\"id\": \"9\", \"type\": \"CREDIT_CARD\", \"name\": \"Gem Visa\", \"version\": \"v2\"}," +
                                "{\"id\": \"10\", \"type\": \"CREDIT_CARD\", \"name\": \"28 Degrees\", \"version\": \"v1\"}" +
                                "]")));

        List<Product> expected = Arrays.asList(
                new Product("9", "CREDIT_CARD", "Gem Visa", "v2"),
                new Product("10", "CREDIT_CARD", "28 Degrees", "v1")
        );

        List<Product> products = productService.getAllProducts();
        assertEquals(expected, products);
    }

    @Test
    void getProductById() {
        // mockserver stubbing - /product/{id}
        wireMockServer.stubFor(get(urlPathEqualTo("/product/50"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"50\", \"type\": \"CREDIT_CARD\", \"name\": \"28 Degrees\", \"version\": \"v1\"}")));

        Product expected = new Product("50", "CREDIT_CARD", "28 Degrees", "v1");
        Product product = productService.getProduct("50");
        assertEquals(expected, product);
    }
}
