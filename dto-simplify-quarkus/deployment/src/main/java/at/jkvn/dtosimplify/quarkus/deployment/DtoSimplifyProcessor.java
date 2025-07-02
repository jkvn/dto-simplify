package at.jkvn.dtosimplify.quarkus.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public class DtoSimplifyProcessor {

    private static final String FEATURE = "dto-simplify";

    @BuildStep
    FeatureBuildItem registerFeature() {
        return new FeatureBuildItem(FEATURE);
    }
    
}