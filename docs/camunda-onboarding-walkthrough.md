# Camunda Customer Onboarding Walkthrough

This document explains how the Camunda-based customer onboarding process works in this project. Use it as a reference to understand the flow and how the Java code connects to the BPMN process.

---

## 1. The Big Picture

**Goal:**
Automate customer onboarding with a mix of automated and manual steps.

**Main Actors:**
- The process engine (Camunda 8/Zeebe)
- Your Java application (Spring Boot)
- A human user (for approval)

---

## 2. The Process Flow (BPMN)

1. **Start Event: "Customer order received"**
   - The process is triggered (e.g., by a REST API call or UI action).
   - Example: A new customer order is submitted.

2. **Service Task: "Score customer"**
   - Automated step.
   - The process engine asks your Java code to calculate a customer score.
   - Java code implements the logic for this (see `ScoringAdapter.java`).

3. **User Task: "Approve customer order"**
   - Manual step.
   - A user sees a form (with score and other info) and decides if the order can be processed automatically.
   - The form is defined in the BPMN and shown in the Camunda Tasklist or your custom UI.

4. **Exclusive Gateway: "Order accepted?"**
   - Decision point.
   - If the user checked "Automatic Processing?", the process continues.
   - If not, the process ends with rejection.

5. **Service Task: "Create customer order in CRM system"**
   - Automated step.
   - If accepted, the process engine asks your Java code to create the customer in the CRM.
   - Java code implements this (see `CrmServiceRestController.java`).

6. **End Events**
   - Either "Customer order rejected" or "Customer order processed".

---

## 3. How Java Code Connects to BPMN

- **Service Tasks** in BPMN (like "Score customer") have a `zeebe:taskDefinition type="scoreCustomer"`.
- In Java, you write a worker that listens for jobs of type `scoreCustomer` and executes the logic.
- **User Tasks** are handled by the Camunda Tasklist or your UI, using the form defined in BPMN.

---

## 4. Where to Find the Code

- **Scoring logic:**  
  `src/main/java/io/berndruecker/onboarding/customer/process/ScoringAdapter.java`
- **CRM logic:**  
  `src/main/java/io/berndruecker/onboarding/customer/fakes/CrmServiceRestController.java`
- **Process glue code:**  
  `src/main/java/io/berndruecker/onboarding/customer/process/CustomerOnboardingGlueCode.java`
- **REST API:**  
  `src/main/java/io/berndruecker/onboarding/customer/rest/CustomerOnboardingRestController.java`

---

## 5. What Happens at Runtime

1. **A new order is submitted** (via REST or UI).
2. **Camunda starts the process** (from BPMN).
3. **Service task "Score customer"** triggers Java code to calculate score.
4. **User task "Approve customer order"** waits for a human to review and decide.
5. **Gateway checks decision** (automatic processing or not).
6. **If accepted, "Create customer in CRM"** triggers Java code to add to CRM.
7. **Process ends** (either processed or rejected).

---

## 6. Next Steps

- See the Java code for a specific step.
- Run the process and see it in action.
- Plan improvements (e.g., add more fields, automate more steps).

**Use this file as a quick reference for understanding and extending the Camunda onboarding process!** 

---

## 7. Running Camunda 8 Locally with Docker Compose

To run Camunda 8 (Zeebe, Operate, Tasklist, etc.) locally, use Docker Compose. This will set up all required services for local development.

### Docker Compose Services
- **zeebe**: The workflow engine (broker)
- **operate**: Web UI for process monitoring
- **tasklist**: Web UI for user tasks
- **elasticsearch**: Required for Operate and Tasklist

### Example `docker-compose.yml`

See the project root for a ready-to-use `docker-compose.yml` file. Key ports:
- Zeebe gRPC: `localhost:26500` (used by your Spring Boot app)
- Operate UI: [http://localhost:8081](http://localhost:8081)
- Tasklist UI: [http://localhost:8082](http://localhost:8082)
- Elasticsearch: `localhost:9200`

### How to Start
1. Open a terminal in your project root.
2. Run:
   ```sh
   docker-compose up -d
   ```
3. Wait for all services to start (may take a minute).

### Accessing the UIs
- **Operate:** [http://localhost:8081](http://localhost:8081)
- **Tasklist:** [http://localhost:8082](http://localhost:8082)

### Next Steps
- Start your Spring Boot app (it will connect to Zeebe at `localhost:26500`).
- Deploy your BPMN process (via your app or Operate UI).
- Test your process end-to-end! 