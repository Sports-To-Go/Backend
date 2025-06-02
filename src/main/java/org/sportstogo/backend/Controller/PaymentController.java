package org.sportstogo.backend.Controller;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Enums.PeriodType;
import org.sportstogo.backend.Models.Revenue;
import org.sportstogo.backend.Repository.RevenueRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final RevenueRepository revenueRepository;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> data) throws Exception {
        long amount = ((Number) data.get("amount")).longValue();
        String currency = (String) data.get("currency");

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .putMetadata("rental_id", (String) data.get("rentalId"))
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        // Calculate revenue (15%)
        double revenueAmount = amount * 0.15 / 100.0; // Stripe uses cents

        // Get first day of the current month
        LocalDate periodStart = LocalDate.now().withDayOfMonth(1);
        PeriodType periodType = PeriodType.MONTHLY;

        Optional<Revenue> optionalRevenue = revenueRepository.findByPeriodStartAndPeriodType(periodStart, periodType);
        if (optionalRevenue.isPresent()) {
            Revenue existingRevenue = optionalRevenue.get();
            existingRevenue.setTotalAmount(existingRevenue.getTotalAmount() + revenueAmount);
            revenueRepository.save(existingRevenue);
        } else {
            Revenue newRevenue = new Revenue(periodStart, periodType, revenueAmount);
            revenueRepository.save(newRevenue);
        }

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        return ResponseEntity.ok(response);
    }
}
