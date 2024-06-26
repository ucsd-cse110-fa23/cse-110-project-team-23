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
        } else if (username.equals("") && password.equals("")) {
            System.out.println("Mock User already exists. Choose a different username.");
            return false;
        } 
        else {
            // Log the username and password
            System.out.println("Mock Account created: Username - " + username + ", Password - " + password);
            return true;
        }
    }

    @Override
    public String login() {
        // Check against the predetermined username and password pair
        if (MOCK_USERNAME.equals(username) && MOCK_PASSWORD.equals(password)) {
            System.out.println("Mock Login successful!");
            return "Mock Login successful!";
        } 
        if (username.equals("") && password.equals("")){
            System.out.println("Server is unavailable!");
            return "Server is unavailable!";
        }
        else {
            System.out.println("Mock Incorrect username/password!");
            return "Mock Incorrect username/password!";
        }
    }
}