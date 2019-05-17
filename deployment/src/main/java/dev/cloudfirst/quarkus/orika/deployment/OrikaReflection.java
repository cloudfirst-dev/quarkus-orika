package dev.cloudfirst.quarkus.orika.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.substrate.ReflectiveClassBuildItem;

public class OrikaReflection {
  @BuildStep
  public void registerCoreReflections(BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
    allConstructors(reflectiveClass, ma.glasnost.orika.converter.DefaultConverterFactory.class);
    simpleConstructor(reflectiveClass,
        ma.glasnost.orika.constructor.SimpleConstructorResolverStrategy.class);
  }

  private void allConstructors(BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
      final Class<?> clazz) {
    // FIXME simpleConstructor is not optimized yet to only enlist the no-arg constructor
    simpleConstructor(reflectiveClass, clazz);
  }

  /**
   * Register classes which we know will only need to be created via their no-arg constructor
   */
  private void simpleConstructor(BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
      final Class<?> clazz) {
    simpleConstructor(reflectiveClass, clazz.getName());
  }

  private void simpleConstructor(BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
      final String clazzName) {
    reflectiveClass.produce(new ReflectiveClassBuildItem(false, false, clazzName));
  }
}
