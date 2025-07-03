package at.jkvn.dtosimplify.core.api;

import at.jkvn.dtosimplify.core.mapping.DtoMapper;
import at.jkvn.dtosimplify.core.mapping.DtoMappingContext;

import java.util.List;
import java.util.Map;

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

    public Map<String, Object> map() {
        DtoMappingContext context = DtoMapper.map(source, profile);
        return context.getFields();
    }

    public List<Object> mapList() {
        return List.of();
    }
}