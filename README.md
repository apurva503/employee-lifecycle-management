🧾 **Project Overview**

This project is a microservices-based system for managing employee lifecycle events. 
It includes:
EmployeeService – Manages employee records via REST APIs and publishes events to Kafka.
HcmService – Manages employee HCM data by consuming Kafka events.
PromotionService – Evaluates promotion eligibility using business rules and HCM data from Kafka.

** Architecture**
High-Level Diagram
        ![image](https://github.com/user-attachments/assets/0a1ca45c-1f51-4a4d-894b-58e9101b13d6)

