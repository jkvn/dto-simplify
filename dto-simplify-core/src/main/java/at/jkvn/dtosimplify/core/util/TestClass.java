package at.jkvn.dtosimplify.core.util;

import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.api.DtoSimplify;
import at.jkvn.dtosimplify.core.api.ViewBuilder;

public class TestClass {

    public static void main(String[] args) {
        TestClass2 testClass = new TestClass2();
        testClass.name = "Test Name";
        
        
        ViewBuilder viewBuilder = DtoSimplify
                .view(testClass)
                .as("admin");
    }
}

class TestClass2 {
    @Dto("admin")
    @Dto("profile2")
    String name;
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