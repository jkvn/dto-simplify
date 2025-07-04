# DTO-Simplify


> ⚠️ **This library is in an early development phase. It is not production-ready yet and its API may change. Use with caution.**


**DTO-Simplify** is a lightweight Java library for **Spring** and **Quarkus (In Development)** that simplifies the creation and management of DTOs – with a primary focus on **API responses**.

The goal is to eliminate the need for verbose DTO classes by letting you declare **which fields should be visible in which view** (e.g., `public`, `admin`, `self`) directly in your model classes.

---

## ✨ Features

- 🎯 Multiple views per DTO (e.g., `admin`, `public`, `self`)
- ✅ Declarative visibility via annotations
- 🔄 Automated serialization based on views
- 📘 Optional OpenAPI / Swagger integration
- ⚙️ Works with Spring and Quarkus
- 🚫 No more Jackson @JsonView hacks

---

## 📤 Response Handling (View-based Serialization)

### ✅ Example: Multiple views declared inline

```java
class User {

    @Dto("public")
    @Dto("admin")
    private String username;

    @Dto("admin")
    private String internalId;
}
```

---

### 🗂️ Alternative: Central view mapping via `@DtoViews`

```java
@DtoViews({
    @DtoView("public", {"username"}),
    @DtoView("admin", {"username", "internalId"})
})
class User {
    private String username;
    private String internalId;
}
```

---

## 🧪 Serialization Example

```java
User user = new User("username", "internalId");

Map<String, Object> map = DtoSimplify.view("admin").map(user);
```

Works with single objects, collections, and nested DTOs.

---

## 📘 OpenAPI Support (optional)

If enabled, DtoSimplify can generate a distinct OpenAPI schema per view:

- `UserDto_Public`
- `UserDto_Admin`
- `UserDto_Self`

Compatible with SpringDoc, Swagger UI and Quarkus OpenAPI extensions.

```java
@DtoSchema(value = "admin", description = "Admin view of User")
@DtoSchema(value = "public", description = "Admin view of User")
@DtoViews({
        @DtoView("public", {"username"}),
        @DtoView("admin", {"username", "internalId"})
})
class User {
    private String username;
    private String internalId;
}


@GetMapping("/user/admin")
@SchemaResponse(variants = {
        @SchemaVariant(dto = User.class, profile = "admin"),
        @SchemaVariant(dto = User.class, profile = "public"),
})
public Object getAdminUser() {
    User user = new User("username", "internalId");
    return DtoSimplify.view(user).as("admin").map();
}
```

---

## 📦 Installation

**Maven:**

```xml
<dependency>
  <groupId>at.jkvn.dtosimplify</groupId>
  <artifactId>dto-simplify-core</artifactId>
  <version>1.0.0</version>
</dependency>
```

**Gradle:**

```kotlin
implementation("at.jkvn.dtosimplify:dto-simplify-core:1.0.0")
```

---

## 🧪 Beta: Inline Request Definition (optional)

**Note:** This feature is in **beta** and under active development.

Instead of creating separate request classes, you can define expected request fields inline:

```java
@PostMapping("/register")
public ResponseEntity<?> register(
        @DtoRequest({
                @DtoRequestProfile(name = "register_email", fields = {
                        @DtoField(name = "email", required = true),
                        @DtoField(name = "password", required = true, min = 6)
                }),
                @DtoRequestProfile(name = "register_username", fields = {
                        @DtoField(name = "username", required = true, max = 16),
                        @DtoField(name = "password", required = true, min = 6)
                })
        }) DtoSimplifyRequest req
) {
    String username = req.get("username");
    ...
}
```

```java
@PostMapping("/register")
public ResponseEntity<?> register(
        @DtoRequest({
                @DtoRequestProfile({
                        @DtoField(name = "email", required = true),
                        @DtoField(name = "password", required = true, min = 6)
                }),
                @DtoRequestProfile({
                        @DtoField(name = "username", required = true, max = 16),
                        @DtoField(name = "password", required = true, min = 6)
                })
        }) DtoSimplifyRequest req
) {
    String username = req.get("username");
    ...
}
```

Goals:
- ✨ No boilerplate request DTOs
- ✅ Bean validation-style annotations
- 📘 OpenAPI schema generation
- 🔐 Safe and centralized request data access

> 🚧 This feature is **experimental** and will evolve.

---

## 🚧 Todo
- [x] Handle list of DTOs
- [x] @DtoViews annotations
- [x] More OpenAPI integration (Lists)
- [ ] Quarkus support
- [ ] Inline request definition (beta)
- [ ] More examples and documentation
- [ ] tests and bug fixes
- [ ] Performance optimizations
- [ ] Error handling improvements

## 📄 License

[MIT License](https://github.com/jkvn/dto-simplify/blob/main/LICENSE)
