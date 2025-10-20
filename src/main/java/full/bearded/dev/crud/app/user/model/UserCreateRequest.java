package full.bearded.dev.crud.app.user.model;

public class UserCreateRequest {

    private String name;
    private String email;

    public UserCreateRequest(final String name, final String email) {

        this.name = name;
        this.email = email;
    }

    public UserCreateRequest() {}

    public String getName() {

        return name;
    }

    public String getEmail() {

        return email;
    }
}
