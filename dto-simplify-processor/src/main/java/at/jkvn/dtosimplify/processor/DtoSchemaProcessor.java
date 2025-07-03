package at.jkvn.dtosimplify.processor;

import at.jkvn.dtosimplify.core.annotation.DtoSchema;
import at.jkvn.dtosimplify.openapi.generation.DtoSchemaRegistry;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

@SupportedAnnotationTypes({
        "at.jkvn.dtosimplify.core.annotation.DtoSchema",
        "at.jkvn.dtosimplify.core.annotation.DtoSchema.Container"
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class DtoSchemaProcessor extends AbstractProcessor {

    private final Set<String> schemaClasses = new LinkedHashSet<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        collectSchemaClasses(roundEnv, DtoSchema.class);
        collectSchemaClasses(roundEnv, DtoSchema.Container.class);

        if (roundEnv.processingOver()) {
            DtoSchemaRegistry.writeMetaInfFile(schemaClasses, processingEnv.getFiler(), processingEnv.getMessager());
        }

        return true;
    }

    private void collectSchemaClasses(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClass) {
        for (Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            if (element instanceof TypeElement typeElement) {
                schemaClasses.add(typeElement.getQualifiedName().toString());
            }
        }
    }
}