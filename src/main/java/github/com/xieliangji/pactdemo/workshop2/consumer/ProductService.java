package github.com.xieliangji.pactdemo.workshop2.consumer;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Coder   谢良基
 * Date    2021/11/25 13:33
 *
 * ---------------------------------------------------------------------------------------------------------------------
 * Scenario:
 * -- 1. Product Catalog application (Consumer). It provides a console interface to query the Product for product info.
 * -- 2. Product Service (Provider). Provides useful things about products, such listing all and individual product detail.
 *
 * --Step1 Simple Consumer calling Provider
 */
@Service
public class ProductService {
    private final RestTemplate restTemplate;

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getAllProducts() {
        return restTemplate.exchange("/products", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {}).getBody();
    }

    public Product getProduct(int id) {
        return restTemplate.getForEntity("/product/{id}", Product.class, id).getBody();
    }
}
