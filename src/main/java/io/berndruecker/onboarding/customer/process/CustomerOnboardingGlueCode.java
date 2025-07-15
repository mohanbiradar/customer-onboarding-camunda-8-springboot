package io.berndruecker.onboarding.customer.process;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Glue code for the customer onboarding process.
 * Handles integration with external systems (e.g., CRM) via service tasks.
 */
@Component
public class CustomerOnboardingGlueCode {

    private static final Logger logger = LoggerFactory.getLogger(CustomerOnboardingGlueCode.class);

    // This would ideally be externalized to application properties
    public static final String ENDPOINT_CRM = "http://localhost:8080/crm/customer";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Worker for the 'addCustomerToCrm' service task.
     * Calls the fake CRM REST endpoint to add a customer.
     *
     * @param customerName the name of the customer to add
     */
    @JobWorker(type = "addCustomerToCrm")
    public void addCustomerToCrm(@Variable String customerName) {
        try {
            logger.info("Adding customer to CRM via REST: {}", customerName);
            String request = "someData for customer: " + customerName;
            restTemplate.put(ENDPOINT_CRM, request);
            logger.info("Successfully added customer '{}' to CRM.", customerName);
        } catch (RestClientException e) {
            logger.error("Failed to add customer '{}' to CRM: {}", customerName, e.getMessage(), e);
            // Optionally, handle error propagation to the process here
        } catch (Exception e) {
            logger.error("Unexpected error while adding customer '{}' to CRM: {}", customerName, e.getMessage(), e);
        }
    }

}
