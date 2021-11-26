package github.com.xieliangji.pactdemo.workshop.provider;

import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Coder   谢良基
 * Date    2021/11/25 11:05
 */
@Repository
public class ProductRepository {

    private final Map<String, Product> PRODUCTS = new HashMap<>();

    public List<Product> fetchAll() {
        initProducts();
        return new ArrayList<>(PRODUCTS.values());
    }

    public Optional<Product> getById(String id) {
        initProducts();
        return Optional.ofNullable(PRODUCTS.get(id));
    }

    private void initProducts() {
        PRODUCTS.put("09", new Product("09", "CREDIT_CARD", "Gem Visa", "v1"));
        PRODUCTS.put("10", new Product("10", "CREDIT_CARD", "28 Degrees", "v1"));
        PRODUCTS.put("11", new Product("11", "PERSONAL_LOAN", "MyFlexiPay", "v2"));
    }
}
