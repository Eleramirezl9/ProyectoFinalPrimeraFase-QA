import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "password123";
        String hash1 = "$2a$10$Wk6.xosJh4tgCWsZ28nHYuytrAUWHrbKT2CLya1DjPXz7wnwJnMfy";
        String hash2 = "$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e";

        System.out.println("Testing hash 1 (admin): " + encoder.matches(rawPassword, hash1));
        System.out.println("Testing hash 2 (others): " + encoder.matches(rawPassword, hash2));

        // Generate new hash
        String newHash = encoder.encode(rawPassword);
        System.out.println("New hash for 'password123': " + newHash);
        System.out.println("Verify new hash: " + encoder.matches(rawPassword, newHash));
    }
}
