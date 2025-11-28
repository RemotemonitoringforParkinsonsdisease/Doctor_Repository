package POJOs;

public class User {
    private Integer id;
    private String email;

    public User(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
