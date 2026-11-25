
package RentEasy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {

        // SPORTS
        addProduct("Cricket Kit", "Sports", "Complete cricket kit for practice and matches.", "150", "Ahmedabad", "/images/cricket-kit.jpg");
        addProduct("Football", "Sports", "Quality football for outdoor games.", "80", "Ahmedabad", "/images/football.png");
        addProduct("Badminton Racket", "Sports", "Badminton racket for beginners and players.", "60", "Ahmedabad", "/images/badminton-racket.jpg");
        addProduct("Basketball", "Sports", "Basketball for indoor and outdoor games.", "100", "Ahmedabad", "/images/basketball.jpg");
        addProduct("Tennis Racket", "Sports", "Tennis racket for practice and matches.", "90", "Ahmedabad", "/images/tennis-racket.jpg");
        addProduct("Volleyball", "Sports", "Volleyball for team games and practice.", "70", "Ahmedabad", "/images/volleyball.jpg");
        addProduct("Table Tennis Set", "Sports", "Table tennis rackets and balls.", "80", "Ahmedabad", "/images/table tennis set.jpg");
        addProduct("Skating Kit", "Sports", "Skating equipment for recreational use.", "120", "Ahmedabad", "/images/skating kit.jpg");

        // ELECTRONICS
        addProduct("DSLR Camera", "Electronics", "Camera for photography and special events.", "700", "Ahmedabad", "/images/DSLR camera.jpg");
        addProduct("Laptop", "Electronics", "Laptop for study, work and presentations.", "500", "Ahmedabad", "/images/laptop.jpg");
        addProduct("Projector", "Electronics", "Projector for presentations and movie nights.", "400", "Ahmedabad", "/images/projector.jpg");
        addProduct("Tablet", "Electronics", "Tablet for studying, reading and entertainment.", "250", "Ahmedabad", "/images/tablet.jpg");
        addProduct("Gaming Console", "Electronics", "Gaming console for entertainment.", "350", "Ahmedabad", "/images/gaming console.jpg");
        addProduct("Computer Monitor", "Electronics", "Monitor for work, study and presentations.", "200", "Ahmedabad", "/images/computer monitor.jpg");
        addProduct("Headphones", "Electronics", "Headphones for music and online meetings.", "100", "Ahmedabad", "/images/headphones.jpg");

        // BICYCLES
        addProduct("Mountain Bicycle", "Bicycles", "Bicycle for cycling and outdoor adventures.", "250", "Ahmedabad", "/images/mountain bicycle.jpg");
        addProduct("Road Bicycle", "Bicycles", "Road bicycle for regular cycling.", "300", "Ahmedabad", "/images/road bicycle.jpg");
        addProduct("Hybrid Bicycle", "Bicycles", "Hybrid bicycle for city rides and fitness.", "280", "Ahmedabad", "/images/hybrid bicycle.jpg");
        addProduct("Kids Bicycle", "Bicycles", "Bicycle suitable for children.", "150", "Ahmedabad", "/images/kids bicycle.jpg");
        addProduct("Electric Bicycle", "Bicycles", "Electric bicycle for convenient travel.", "450", "Ahmedabad", "/images/electric bicycle.jpg");

        // EVENT EQUIPMENT
        addProduct("Bluetooth Speaker", "Event Equipment", "Portable speaker for parties and events.", "180", "Ahmedabad", "/images/bluetooth speaker.jpg");
        addProduct("DJ Sound System", "Event Equipment", "Sound system for parties and events.", "600", "Ahmedabad", "/images/DJ sound system.jpg");
        addProduct("Wireless Microphone", "Event Equipment", "Wireless microphone for functions and events.", "150", "Ahmedabad", "/images/wireless microphone.jpg");
        addProduct("Stage Lights", "Event Equipment", "Lighting equipment for stage decoration.", "300", "Ahmedabad", "/images/stage lights.jpg");
        addProduct("Party Tent", "Event Equipment", "Tent for outdoor parties and functions.", "500", "Ahmedabad", "/images/party tent.jpg");
        addProduct("Folding Chairs Set", "Event Equipment", "Chairs for gatherings and events.", "200", "Ahmedabad", "/images/folding chair set.jpg");
    }

    private void addProduct(
            String name,
            String category,
            String description,
            String price,
            String location,
            String imageUrl) {

        Product existingProduct = productRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .filter(product -> product.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (existingProduct != null) {

            existingProduct.setImageUrl(imageUrl);
            productRepository.save(existingProduct);

        } else {

            Product product = new Product(
                    name,
                    category,
                    description,
                    new BigDecimal(price),
                    location,
                    imageUrl
            );

            productRepository.save(product);
        }
    }
}
