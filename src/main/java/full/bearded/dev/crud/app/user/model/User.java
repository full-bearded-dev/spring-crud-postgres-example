package full.bearded.dev.crud.app.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;

    public User(final Long id, final String name, final String email, final int age) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public User() {}

    public Long getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(final String email) {

        this.email = email;
    }

    public int getAge() {

        return age;
    }

    public void setAge(final int age) {

        this.age = age;
    }
}
