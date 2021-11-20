package github.com.xieliangji.pactdemo.workshop.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Coder   谢良基
 * Date    2021/11/20 14:25
 */
@Configuration
public class ProductServiceConfig {

    @Bean
    public RestTemplate productServiceRestTemplate(@Value("${provider.port:8085}") int port) {
        return new RestTemplateBuilder().rootUri("http://localhost:" + port).build();
    }
}
