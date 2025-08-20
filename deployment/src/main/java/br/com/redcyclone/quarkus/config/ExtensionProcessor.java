package br.com.redcyclone.quarkus.config;

import br.com.redcyclone.quarkus.security.taglib.AnonymousTagHandler;
import br.com.redcyclone.quarkus.security.taglib.AuthenticatedTagHandler;
import br.com.redcyclone.quarkus.security.taglib.AuthorizeTagHandler;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing build steps for the Quarkus extension
 *
 * @author Leonardo Bernardes
 */

@SuppressWarnings("unused")
class ExtensionProcessor {

    private static final String FEATURE = "quarkus-security-taglib";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerForReflection(BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        final List<String> classes = new ArrayList<>();

        classes.add(AnonymousTagHandler.class.getName());
        classes.add(AuthenticatedTagHandler.class.getName());
        classes.add(AuthorizeTagHandler.class.getName());

        reflectiveClass.produce(ReflectiveClassBuildItem.builder(classes.toArray(new String[0]))
                .constructors()
                .methods()
                .fields()
                .serialization()
                .build());
    }
}
