import java.math.BigDecimal;


public class Main {
    public static void main(String[] args) {
        User user = new User(
                1L,
                "Yan",
                null, // campo opcional
                new BigDecimal("100.00"),
                true
        );

        UserCacheService.save(user, "user.cache");

        User cachedUser = UserCacheService.load("user.cache");

        cachedUser.debit(new BigDecimal("30.00"));

        System.out.println("User active: " + cachedUser.isActive());
        System.out.println(cachedUser.getName());
    }
}