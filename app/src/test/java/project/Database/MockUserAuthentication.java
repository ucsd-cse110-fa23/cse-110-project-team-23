package project.Database;

public class MockUserAuthentication extends UserAuthentication {

    // Predetermined username and password pair for testing
    public static final String MOCK_USERNAME = "mockUser";
    public static final String MOCK_PASSWORD = "mockPassword";

    public MockUserAuthentication(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean createAccount() {
        // Check if the account already exists
        if (MOCK_USERNAME.equals(username) && MOCK_PASSWORD.equals(password)) {
            System.out.println("Mock User already exists. Choose a different username.");
            return false;
        } else {
            // Log the username and password
            System.out.println("Mock Account created: Username - " + username + ", Password - " + password);
            return true;
        }
    }

    @Override
    public int login() {
        // Check against the predetermined username and password pair
        if (MOCK_USERNAME.equals(username) && MOCK_PASSWORD.equals(password)) {
            System.out.println("Mock Login successful!");
            return 1;
        } else {
            System.out.println("Mock Incorrect username/password!");
            return 3;
        }
    }
}