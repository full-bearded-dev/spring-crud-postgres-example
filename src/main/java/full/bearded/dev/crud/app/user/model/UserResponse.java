package full.bearded.dev.crud.app.user.model;

public class UserResponse {

    private Long id;
    private String name;
    private String email;

    public UserResponse(final Long id, final String name, final String email) {

        this.id = id;
        this.name = name;
        this.email = email;
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
}