package org.sportstogo.backend.config;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import com.stripe.Stripe;

@Component
public class StripeConfig {

    private final StripeProperties props;

    public StripeConfig(StripeProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = props.getApiKey();
    }
}
