package at.jkvn.dtosimplify.core.api;

import java.util.List;

public class DtoSimplify {
    public static ViewBuilder view(Object obj) {
        return new ViewBuilder(obj);
    }

    public static ViewBuilder view(List<?> list) {
        return new ViewBuilder(list);
    }
}
