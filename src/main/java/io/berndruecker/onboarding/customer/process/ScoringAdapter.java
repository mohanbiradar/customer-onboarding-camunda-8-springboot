package io.berndruecker.onboarding.customer.process;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Worker for scoring a customer as part of the onboarding process.
 * This worker is triggered by the 'scoreCustomer' service task in the BPMN process.
 */
@Component
public class ScoringAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ScoringAdapter.class);

    /**
     * Scores a customer based on input variables from the process instance.
     *
     * @param variables input process variables (e.g., customerName, customerEmail)
     * @return map of output variables (e.g., score)
     */
    @JobWorker
    public Map<String, Object> scoreCustomer(Map<String, Object> variables) {
        HashMap<String, Object> resultVariables = new HashMap<>();
        try {
            logger.info("Scoring customer with input variables: {}", variables);
            // Example: Use customer name/email to calculate a score (replace with real logic)
            int score = 42; // Default score
            if (variables != null && variables.containsKey("customerName")) {
                String customerName = variables.get("customerName").toString();
                // Simple example: score based on name length
                score = 40 + Math.min(customerName.length(), 10);
            }
            resultVariables.put("score", score);
            logger.info("Calculated score: {}", score);
        } catch (Exception e) {
            logger.error("Error scoring customer: {}", e.getMessage(), e);
            // Optionally, set an error variable or rethrow
            resultVariables.put("score", -1);
            resultVariables.put("scoringError", e.getMessage());
        }
        return resultVariables;
    }

}
