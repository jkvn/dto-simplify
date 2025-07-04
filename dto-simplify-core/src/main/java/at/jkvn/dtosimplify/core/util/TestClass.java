package at.jkvn.dtosimplify.core.util;

import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.annotation.DtoSchema;
import at.jkvn.dtosimplify.core.api.DtoSimplify;
import at.jkvn.dtosimplify.core.api.ViewBuilder;

import java.time.LocalDateTime;

public class TestClass {

    public static void main(String[] args) {
        TestClass4 testClass4 = new TestClass4();
        testClass4.adresse = "Test Adresse";
        
        TestClass2 testClass = new TestClass2();
        testClass.name = "Test Name";
        TestClass3 testClass3 = new TestClass3();
        testClass3.testClass4 = testClass4;
        testClass3.name = "Test Name 3";
        testClass.testClass3 = testClass3;

        
        
        ViewBuilder viewBuilder = DtoSimplify
                .view(testClass)
                .as("admin");

        System.out.println(viewBuilder.map());
    }
}

@DtoSchema(value = "admin", description = "View for admin users")
@DtoSchema(value = "user", description = "View for regular users")
class TestClass2 {
    @Dto("admin")
    @Dto("user")
    String name;
    
    @Dto("admin")
    TestClass3 testClass3;

}

@DtoSchema(value = "admin", description = "View for admin users")
class TestClass3 {
    @Dto("admin")
    String name;
    @Dto("admin")
    TestClass4 testClass4;
}

@DtoSchema(value = "admin", description = "View for admin users")
class TestClass4 {
    @Dto("admin")
    String adresse;

    @Dto("admin")
    LocalDateTime dateTime = LocalDateTime.now();
}

/*@DtoViews{
    @DtoView("profile1", {"name")
}
class TestClass3 {
    private String name;
}
*/

/*
class TestClass4 {
    @Dto({"profile1", "profile2"})
    private String name;
}
*/