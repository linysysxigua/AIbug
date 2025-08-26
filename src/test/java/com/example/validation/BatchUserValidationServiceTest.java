import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class BatchUserValidationServiceTest {
    
    @Test
    public void testValidateUserEmails() {
        BatchUserValidationService service = new BatchUserValidationService();
        
        List<User> users = Arrays.asList(
            createUser("1", "test@gmail.com"),
            createUser("2", "invalid-email"),
            createUser("3", "user@company.com")
        );
        
        List<ValidationResult> results = service.validateUserEmails(users);
        assertEquals(3, results.size());
        assertTrue(results.get(0).isValid());
        assertFalse(results.get(1).isValid());
        assertTrue(results.get(2).isValid());
    }
    
    @Test
    public void testRemoveDuplicateUsers() {
        BatchUserValidationService service = new BatchUserValidationService();
        
        List<User> users = Arrays.asList(
            createUser("1", "test@gmail.com"),
            createUser("2", "test@gmail.com"), // duplicate
            createUser("3", "user@company.com")
        );
        
        List<User> uniqueUsers = service.removeDuplicateUsers(users);
        assertEquals(2, uniqueUsers.size());
    }
    
    private User createUser(String id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        return user;
    }
}
