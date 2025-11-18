package POJOs;

public class User {
    private String id;
    private String email;
    private String fullName;

    public User(String id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }
    public User(String email) {
        this.email = email;
    }
    public String getFullName() {
        return fullName;
    }
}
