package full.bearded.dev.crud.app.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public class RandomTestUtils {

    public static String randomString(int length) {

        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String randomEmail() {

        return RandomStringUtils.randomAlphanumeric(10) + "@example.com";
    }

    public static int randomAge() {

        return ThreadLocalRandom.current().nextInt(18, 80);
    }

    public static long randomId() {

        return ThreadLocalRandom.current().nextLong(1, 100);
    }
}
