import java.io.*;

public class UserCacheService {

    public static void save(User user, String filePath) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {

            out.writeObject(user);

        } catch (IOException e) {
            throw new RuntimeException("Error saving user to cache", e);
        }
    }

    public static User load(String filePath) {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(filePath))) {

            return (User) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading user from cache", e);
        }
    }
}