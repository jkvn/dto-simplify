package at.jkvn.dtosimplify.core.api;

import at.jkvn.dtosimplify.core.mapping.DtoMapper;
import at.jkvn.dtosimplify.core.mapping.DtoMappingContext;

import java.util.List;

public class ViewBuilder {
    
    private final Object source;
    private String view = "DEFAULT";

    public ViewBuilder(Object source) {
        this.source = source;
    }

    public ViewBuilder as(String viewName) {
        this.view = viewName;
        return this;
    }

    public Object map() {
        DtoMappingContext context = new DtoMappingContext(view);
        return DtoMapper.map(source, context);
    }

    public List<Object> mapList() {
        return List.of();
    }
}