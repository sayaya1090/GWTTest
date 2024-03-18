package net.sayaya

import org.gradle.api.internal.IConventionAware
import org.gradle.workers.WorkParameters
import org.wisepersist.gradle.plugins.gwt.GwtSuperDevOptions
import java.io.File
import java.util.concurrent.Callable

interface GwtSuperDevOptions : GwtSuperDevOptions, WorkParameters {
    fun configure(options: GwtSuperDevOptions) {
        val conventionMapping = (this as IConventionAware).conventionMapping
        conventionMapping.map("workDir", Callable { options.workDir } as Callable<String?>)
        conventionMapping.map("bindAddress", Callable { options.bindAddress } as Callable<String?>)
        conventionMapping.map("port", Callable { options.port } as Callable<Int>)
        conventionMapping.map("noPrecompile", Callable { options.noPrecompile } as Callable<Boolean?>)
        conventionMapping.map("useClasspathForSrc", Callable { options.useClasspathForSrc } as Callable<Boolean?>)
        conventionMapping.map("allowMissingSrc", Callable { options.allowMissingSrc } as Callable<Boolean?>)
        conventionMapping.map("failOnError", Callable { options.failOnError } as Callable<Boolean?>)
        conventionMapping.map("compileTest", Callable { options.compileTest } as Callable<Boolean?>)
        conventionMapping.map("compileTestRecompiles", Callable { options.compileTestRecompiles } as Callable<Int?>)
        conventionMapping.map("launcherDir", Callable { options.launcherDir } as Callable<File?>)
        conventionMapping.map("closureFormattedOutput") { options.closureFormattedOutput }
    }
}