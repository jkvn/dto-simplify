package at.jkvn.dtosimplify.processor;

import at.jkvn.dtosimplify.core.annotation.DtoSchema;
import at.jkvn.dtosimplify.openapi.generation.DtoSchemaRegistry;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.TreeSet;

@SupportedAnnotationTypes("at.jkvn.dtosimplify.core.annotation.DtoSchema.Container")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class DtoSchemaProcessor extends AbstractProcessor {

    private final Set<String> schemaClasses = new TreeSet<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(DtoSchema.Container.class)) {
            if (element instanceof TypeElement typeElement) {
                String fqcn = typeElement.getQualifiedName().toString();
                schemaClasses.add(fqcn);
            }
        }

        if (roundEnv.processingOver()) {
            DtoSchemaRegistry.writeMetaInfFile(schemaClasses, processingEnv.getFiler(), processingEnv.getMessager());
        }

        return true;
    }
}