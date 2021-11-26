package github.com.xieliangji.pactdemo.workshop.provider;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Coder   谢良基
 * Date    2021/11/25 11:10
 */
@RestController
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return repository.fetchAll();
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
        Optional<Product> product = repository.getById(id);
        return ResponseEntity.of(product);
    }
}
