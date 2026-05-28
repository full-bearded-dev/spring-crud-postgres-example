package full.bearded.dev.crud.app.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.security.admin")
public class AdminProperties {

    private String password;
    private String email;
}