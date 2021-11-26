package github.com.xieliangji.pactdemo.workshop.consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Coder   谢良基
 * Date    2021/11/20 14:28
 */
//@Component
public class ConsoleInterface implements CommandLineRunner {

    private final ProductService productService;

    private List<Product> products;

    public ConsoleInterface(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printAllProducts();
            Integer choice = getUserChoice(scanner);
            if (choice == null || choice <= 0 || choice > products.size()) {
                System.out.println("Exiting...");
                break;
            }
            printProduct(choice);
        }
    }

    private void printAllProducts() {
        products = productService.getAllProducts();
        System.out.println("\n\nProducts\n----------------------------");
        IntStream.range(0, products.size()).forEach(index -> System.out.printf("%d) %s", index + 1, products.get(index).getName()));
    }

    private Integer getUserChoice(Scanner scanner) {
        System.out.println("Select item to view details: ");
        String choice = scanner.nextLine();
        return parseChoice(choice);
    }

    private void printProduct(int index) {
        String id = products.get(index - 1).getId();
        try {
            Product product = productService.getProduct(id);
            System.out.println("Product Details\n----------------------");
            System.out.println(product);
        } catch (Exception e) {
            System.out.printf("Failed to load product %s\n", id);
            System.out.println(e.getMessage());
        }
    }

    private Integer parseChoice(String choice) {
        try {
            return Integer.parseInt(choice);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
