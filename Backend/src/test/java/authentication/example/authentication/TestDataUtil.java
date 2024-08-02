package authentication.example.authentication;

import authentication.example.authentication.entities.StoreEntity;

import java.util.List;

public class TestDataUtil {

    private void TestDataUtil() {
    }

    public static StoreEntity createTestStoreA() {
        return StoreEntity.builder()
                .name("Seven Eleven")
                .categories(List.of("Drinks", "Snacks"))
                .storeCode("storeCode")
                .password("password")
                .build();    }

}
