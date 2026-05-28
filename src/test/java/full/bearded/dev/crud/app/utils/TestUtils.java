package full.bearded.dev.crud.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TestUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String asJsonString(final Object object) {

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
