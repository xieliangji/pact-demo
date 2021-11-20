package github.com.xieliangji.pactdemo.workshop.consumer;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Coder   谢良基
 * Date    2021/11/20 14:19
 */
@Service
public class ProductService {

    private final RestTemplate restTemplate;

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getAllProducts() {
        return restTemplate.exchange("/products",
                HttpMethod.GET,
                getResponseEntity(),
                new ParameterizedTypeReference<List<Product>>(){}).getBody();
    }

    private HttpEntity<String> getResponseEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date())));
        return new HttpEntity<>(headers);
    }

    public Product getProduct(String id) {
        return restTemplate.exchange("/product/{id}",
                HttpMethod.GET,
                getResponseEntity(),
                Product.class, id).getBody();
    }
}
