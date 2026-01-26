import java.io.Serializable;
import java.math.BigDecimal;

public class User implements Serializable {

    // boa prática
    private static final long serialVersionUID = 1L;

    private Long id;              // Object → pode ser null (ex: antes de salvar no banco)
    private String name;
    private Integer age;          // Object → campo opcional
    private BigDecimal balance;   // dinheiro → precisão > performance
    private boolean active;       // primitivo → sempre tem valor

    public User(Long id, String name, Integer age, BigDecimal balance, boolean active) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (age != null && age < 0) {
            throw new IllegalArgumentException("Age must be positive");
        }

        this.id = id;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.active = active;
    }

    public void debit(BigDecimal amount) {
        if (!active) {
            throw new IllegalStateException("User is inactive");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        balance = balance.subtract(amount);
    }

    public Integer getAge() {
        return age;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }
}