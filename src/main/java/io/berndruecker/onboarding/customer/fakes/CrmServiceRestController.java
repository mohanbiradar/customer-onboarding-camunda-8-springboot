package io.berndruecker.onboarding.customer.fakes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * Fake CRM REST controller to simulate adding a customer to a CRM system.
 * Used for local development and process integration testing.
 */
@RestController
public class CrmServiceRestController {

    private static final Logger logger = LoggerFactory.getLogger(CrmServiceRestController.class);

    /**
     * Simulates adding a customer to the CRM system.
     * Logs the request and returns 200 OK. Can be extended to simulate errors.
     *
     * @param requestBody the request body (simulated data)
     * @param exchange the server web exchange
     * @return HTTP 200 OK if successful
     */
    @PutMapping("/crm/customer")
    public ResponseEntity<String> addCustomerToCrmFake(@RequestBody(required = false) String requestBody, ServerWebExchange exchange) {
        try {
            logger.info("CRM REST API called. Request body: {}", requestBody);
            // Simulate CRM processing logic here
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            logger.error("Error in CRM REST API: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CRM error: " + e.getMessage());
        }
    }

}
