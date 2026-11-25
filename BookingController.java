
package RentEasy;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ProductRepository productRepository;

    public BookingController(
            BookingRepository bookingRepository,
            ProductRepository productRepository) {
        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
    }

    // Create a booking
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {

        if (booking.getCustomerName() == null ||
                booking.getCustomerEmail() == null ||
                booking.getStartDate() == null ||
                booking.getEndDate() == null ||
                booking.getProductId() == null) {
            return ResponseEntity.badRequest().body(
           "Missing details: productId=" + booking.getProductId()
           + ", name=" + booking.getCustomerName()
           + ", email=" + booking.getCustomerEmail()
           + ", startDate=" + booking.getStartDate()
           + ", endDate=" + booking.getEndDate()
        );
        }

        if (!booking.getEndDate().isAfter(booking.getStartDate())
                && !booking.getEndDate().equals(booking.getStartDate())) {
            return ResponseEntity.badRequest()
                    .body("End date must not be before start date.");
        }

        if (booking.getStartDate().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest()
                    .body("Start date cannot be in the past.");
        }

        Product product = productRepository
                .findById(booking.getProductId())
                .orElse(null);

        if (product == null || !product.isAvailable()) {
            return ResponseEntity.badRequest()
                    .body("Product is not available.");
        }

        long days = ChronoUnit.DAYS.between(
                booking.getStartDate(),
                booking.getEndDate()) + 1;

        BigDecimal total = product.getPricePerDay()
                .multiply(BigDecimal.valueOf(days));

        booking.setTotalPrice(total);
        booking.setStatus("CONFIRMED");

        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    // Get all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get bookings by customer email
    @GetMapping("/customer/{email}")
    public List<Booking> getCustomerBookings(
            @PathVariable String email) {
        return bookingRepository
                .findByCustomerEmailIgnoreCase(email);
    }

    // Cancel booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {

        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        booking.setStatus("CANCELLED");

        return ResponseEntity.ok(bookingRepository.save(booking));
    }
}
