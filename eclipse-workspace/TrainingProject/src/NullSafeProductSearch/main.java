package NullSafeProductSearch;

import java.util.*;
import java.util.stream.Collectors;

class Product {
    private String name;
    private String category;
    private Double price;

    public Product(String name, String category, Double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("Product{name='%s', category='%s', price=%s}", 
                             name, category, price);
    }
}

class ProductSearchService {

    // 1. Find first product by category
    public Optional<Product> findFirstByCategory(List<Product> products, String category) {
        if (category == null) {
            return Optional.empty();
        }
        return products.stream()
                .filter(p -> Optional.ofNullable(p.getCategory())
                        .map(c -> c.equalsIgnoreCase(category))
                        .orElse(false))
                .findFirst();
    }

    // 2. Find products in price range
    public List<Product> findByPriceRange(List<Product> products, double minPrice, double maxPrice) {
        return products.stream()
                .filter(p -> Optional.ofNullable(p.getPrice())
                        .map(price -> price >= minPrice && price <= maxPrice)
                        .orElse(false))
                .collect(Collectors.toList());
    }

    // 3. Get product names ignoring null
    public List<String> findProductNames(List<Product> products) {
        return products.stream()
                .map(Product::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

public class main {
    public static void main(String[] args) {
        List<Product> catalog = Arrays.asList(
                new Product("Laptop", "Electronics", 75000.0),
                new Product("Shirt", "Fashion", 1500.0),
                new Product("Book", null, 500.0),
                new Product("Smartphone", "Electronics", null),
                new Product(null, "Home", 1200.0)
        );

        ProductSearchService service = new ProductSearchService();

        // Test 1: Find first by category
        System.out.println("First Electronics product: " +
                service.findFirstByCategory(catalog, "Electronics").orElse(null));

        // Test 2: Find products by price range
        System.out.println("Products in range 1000 - 2000: " +
                service.findByPriceRange(catalog, 1000, 2000));

        // Test 3: Get all product names
        System.out.println("Product names: " +
                service.findProductNames(catalog));
    }
}
