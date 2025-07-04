package at.jkvn.dtosimplify.core.api;

import at.jkvn.dtosimplify.core.mapping.DtoMapper;

public class ViewBuilder {
    private final Object source;
    private String profile = "DEFAULT";

    public ViewBuilder(Object source) {
        this.source = source;
    }

    public ViewBuilder as(String viewName) {
        this.profile = viewName;
        return this;
    }

    public Object map() {
        return DtoMapper.map(source, profile);
    }
}