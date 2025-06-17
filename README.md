🧾 **Project Overview**

This project is a microservices-based system for managing employee lifecycle events. 
It includes:
EmployeeService – Manages employee records via REST APIs and publishes events to Kafka.
HcmService – Manages employee HCM data by consuming Kafka events.
PromotionService – Evaluates promotion eligibility using business rules and HCM data from Kafka.

** Architecture**
High-Level Diagram

![emplyeeLifecyclemng](https://github.com/user-attachments/assets/51fdb514-25eb-4664-8bf2-e5f45b1e7c9e)

**Microservices Patterns Used**
Event-driven architecture using Kafka
Loose coupling between services
Single Responsibility Principle for services
Resilience via retry

** **API Usage Instructions****
Use swagger UI to interact with the EmployeeService APIs: (http://<EMPLOYEE-SERVICE-HOST>:<PORT>/swagger-ui/index.html#/employee-controller/all)
Create Employee
Update Employee
Deactivate Employee
List Employees
 
