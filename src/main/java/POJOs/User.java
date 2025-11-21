package POJOs;

public class User {
    private Integer id;
    private String email;
    private String fullName;

    public User(Integer id, String email, String fullName) {
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

    public Integer getId() {
        return id;
    }


}
