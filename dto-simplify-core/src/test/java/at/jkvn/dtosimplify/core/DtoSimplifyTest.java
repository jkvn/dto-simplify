package at.jkvn.dtosimplify.core;

import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.api.DtoSimplify;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DtoSimplifyTest {
    static class Address {
        @Dto("public")
        @Dto("admin")
        String city;

        @Dto("admin")
        String street;

        public Address(String city, String street) {
            this.city = city;
            this.street = street;
        }
    }

    static class User {
        @Dto("public")
        @Dto("admin")
        private String username;

        @Dto("admin")
        private String internalId;

        @Dto("admin")
        private Address address;

        public User(String username, String internalId) {
            this.username = username;
            this.internalId = internalId;
        }

        public User(String username, String internalId, Address address) {
            this.username = username;
            this.internalId = internalId;
            this.address = address;
        }
    }

    @Test
    void shouldIncludeOnlyPublicFields() {
        User user = new User("alice", "admin-secret");
        Map<String, Object> result = asMap(user, "public");

        assertEquals(1, result.size());
        assertEquals("alice", result.get("username"));
        assertFalse(result.containsKey("internalId"));
    }

    @Test
    void shouldIncludeAllAdminFields() {
        User user = new User("bob", "admin-456");
        Map<String, Object> result = asMap(user, "admin");

        assertEquals("bob", result.get("username"));
        assertEquals("admin-456", result.get("internalId"));
    }

    @Test
    void shouldMapNestedDto() {
        Address address = new Address("Vienna", "Main Street 12");
        User user = new User("carol", "id-789", address);

        Map<String, Object> result = asMap(user, "admin");

        assertTrue(result.containsKey("address"));
        Map<String, Object> nested = (Map<String, Object>) result.get("address");

        assertEquals("Vienna", nested.get("city"));
        assertEquals("Main Street 12", nested.get("street"));
    }

    @Test
    void shouldMapListOfDtos() {
        List<User> users = List.of(
                new User("dave", "id-1"),
                new User("eve", "id-2")
        );

        List<?> mapped = (List<?>) DtoSimplify.view(users).as("public").map();

        assertEquals(2, mapped.size());
        assertTrue(mapped.get(0) instanceof Map);
        assertEquals("dave", ((Map<?, ?>) mapped.get(0)).get("username"));
    }

    @Test
    void shouldMapArrayOfDtos() {
        User[] users = {
                new User("frank", "id-3"),
                new User("grace", "id-4")
        };

        Object result = DtoSimplify.view(users).as("admin").map();

        assertTrue(result instanceof List<?>);
        assertEquals("id-4", ((Map<?, ?>) ((List<?>) result).get(1)).get("internalId"));
    }

    @Test
    void shouldMapMapOfDtos() {
        Map<String, User> users = Map.of(
                "user1", new User("heidi", "id-5"),
                "user2", new User("ivan", "id-6")
        );

        Object result = DtoSimplify.view(users).as("public").map();

        assertTrue(result instanceof Map<?, ?>);
        Map<?, ?> resultMap = (Map<?, ?>) result;

        assertTrue(resultMap.containsKey("user1"));
        assertEquals("heidi", ((Map<?, ?>) resultMap.get("user1")).get("username"));
    }

    @Test
    void shouldIncludeNullValuesInAdminView() {
        User user = new User(null, null);
        Map<String, Object> result = asMap(user, "admin");

        assertTrue(result.containsKey("username"));
        assertNull(result.get("username"));
        assertTrue(result.containsKey("internalId"));
        assertNull(result.get("internalId"));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object dto, String profile) {
        Object result = DtoSimplify.view(dto).as(profile).map();
        assertInstanceOf(Map.class, result);
        return (Map<String, Object>) result;
    }
}