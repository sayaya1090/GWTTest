package net.sayaya

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.wisepersist.gradle.plugins.gwt.GwtPluginExtension

class GwtPlugin: Plugin<Project> {
    private lateinit var extension: GwtPluginExtension
    override fun apply(project: Project) {
        extension = project.extensions.create("gwt", GwtPluginExtension::class.java);
        project.plugins.apply("org.wisepersist.gwt")
        project.dependencies.add("implementation", "io.ktor:ktor-server-core:2.3.9")
        project.dependencies.add("implementation", "io.ktor:ktor-server-netty:2.3.9")

        project.tasks.register(GwtTest.NAME, GwtTest::class.java) {
            description = "Runs the GWT Test mode"
            //(this as IConventionAware).conventionMapping.map("war", (Callable<File>) extension::getDevWar);
            //dependsOn(project.getTasks().named(JavaPlugin.CLASSES_TASK_NAME));
            //dependsOn(project.getTasks().named(TASK_WAR_TEMPLATE));
        }
        project.tasks.withType(GwtTest::class.java).configureEach {
            println("~~")
            this.configure(extension.superDev)
        }
    }
}
/*
 project.getTasks().register(TASK_GWT_DEV, GwtDev.class, task -> {
      task.setDescription("Runs the GWT development mode");
      ((IConventionAware) task).getConventionMapping()
              .map("war", (Callable<File>) extension::getDevWar);
      task.dependsOn(project.getTasks().named(JavaPlugin.CLASSES_TASK_NAME));
      task.dependsOn(project.getTasks().named(TASK_WAR_TEMPLATE));
    });
 */