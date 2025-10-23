package full.bearded.dev.crud.app.user.model;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private int age;

    public UserResponse(final Long id, final String name, final String email, final int age) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public UserResponse() {}

    public Long getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {

        return email;
    }

    public int getAge() {

        return age;
    }
}