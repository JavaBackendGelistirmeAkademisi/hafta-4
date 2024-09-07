import java.io.*;
import java.util.ArrayList;
import java.util.List;


// Ürün sınıfı
class Product {
    private String name;
    private int stock;
    private double price;

    public Product(String name, int stock, double price) {
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }


}

// Müşteri sınıfı
class Customer {
    private String name;


    public Customer(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }



}

// Sipariş sınıfı
class Order {
    private Customer customer;
    private List<Product> products;

    public Order(Customer customer, List<Product> products) {
        this.customer = customer;
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        StringBuilder orderDetails = new StringBuilder("Customer: " + customer.getName() + "\nProducts\n");
        for (Product product : products) {
            orderDetails.append("Product: ").append(product.getName()).append(" Price: ").append(product.getPrice()).append("$").append(" Number of product added: ").append(product.getStock()).append("\n");
        }
        return orderDetails.toString();
    }
}

// Exception sınıfı
class StockException extends Exception {
    public StockException(String message) {
        super(message);
    }
}

public class Main {
    private List<Product> products = new ArrayList<>();


    // Ürün ekleme
    public void addProduct(Product product) {
        products.add(product);
        System.out.println(product.getName() + " product added.");
    }

    // Sipariş oluşturma
    public void createOrder(Customer customer, List<Product> orderProducts) throws StockException {
        for (Product orderedProduct : orderProducts) {
            for (Product product : products) {
                if (product.getName().equals(orderedProduct.getName())) {
                    if (product.getStock() < orderedProduct.getStock()) {
                        throw new StockException("Insufficient stock! " + product.getName());
                    }
                    product.setStock(product.getStock() - orderedProduct.getStock());
                }
            }
        }
        Order order = new Order(customer, orderProducts);
        System.out.println("Order Created:\n" + order);
        writeOrderToFile(order);
    }

    // Dosyaya sipariş yazma
    public void writeOrderToFile(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write(order.toString());
            writer.newLine();
            System.out.println("Order was written to file.");
        } catch (IOException e) {
            System.err.println("Error writing to file. " + e.getMessage());
        }
    }

    // Ürünleri dosyadan okuma
    public void loadProductsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split(",");
                products.add(new Product(productData[0], Integer.parseInt(productData[1]), Double.parseDouble(productData[2])));
            }
            System.out.println("Products loaded from file.");
        } catch (IOException e) {
            System.err.println("Error reading from file. " + e.getMessage());
        }
    }

    // Ana metod
    public static void main(String[] args) {
        Main system = new Main();

        // Ürünleri dosyadan yükleme
        system.loadProductsFromFile("product.txt");

        // Müşteri oluşturma
        Customer customer = new Customer("Aslıhan Tuna");

        // Sipariş oluşturma
        List<Product> orderProducts = new ArrayList<>();
        orderProducts.add(new Product("Earphone", 1, 200.0));

        try {
            system.createOrder(customer, orderProducts);
        } catch (StockException e) {
            System.err.println("Order could not be created. " + e.getMessage());
        }
    }
}
