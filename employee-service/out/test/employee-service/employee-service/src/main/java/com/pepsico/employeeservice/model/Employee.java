@Entity
public class Employee {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String department;
    private LocalDate departmentJoinDate;
    private boolean isActive = true;
    private boolean isEligibleForPromotion = false;

    // Getters, Setters, Constructors
}