package full.bearded.dev.crud.app.user.model;

public class UserUpdateRequest {

    private String name;
    private String email;

    public UserUpdateRequest(final String name, final String email) {

        this.name = name;
        this.email = email;
    }

    public UserUpdateRequest() {}

    public String getName() {

        return name;
    }

    public String getEmail() {

        return email;
    }
}