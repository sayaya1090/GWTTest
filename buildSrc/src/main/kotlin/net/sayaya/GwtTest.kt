package net.sayaya

import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkerExecutor
import org.wisepersist.gradle.plugins.gwt.AbstractGwtActionTask
import java.lang.Boolean
import javax.inject.Inject


abstract class GwtTest: SourceTask(), GwtSuperDevOptions {
    companion object {
        const val NAME = "gwtTest"
        abstract class GwtCodeServerWork: AbstractGwtActionTask("com.google.gwt.dev.codeserver.CodeServer"), WorkAction<GwtSuperDevOptions>, GwtSuperDevOptions {
            init {
                outputs.upToDateWhen { _ -> false }
            }
            override fun execute() {
                super.exec()
            }

            override fun addArgs() {
                if (Boolean.TRUE != useClasspathForSrc) {
                    for (srcDir in src) {
                        // TODO warning if file?
                        if (srcDir.exists() && srcDir.isDirectory) {
                            argIfSet("-src", srcDir)
                        }
                    }
                }
                dirArgIfSet("-workDir", workDir)
                argIfSet("-bindAddress", bindAddress)
                argIfSet("-port", port)
                argIfEnabled(noPrecompile, "-noprecompile")
                argOnOff(allowMissingSrc, "-allowMissingSrc", "-noallowMissingSrc")
                argOnOff(failOnError, "-failOnError", "-nofailOnError")
                argOnOff(compileTest, "-compileTest", "-nocompileTest")
                argIfSet("-compileTestRecompiles", compileTestRecompiles)
                argIfSet("-launcherDir", launcherDir)
                argIfSet("-logLevel", logLevel)
                argOnOff(closureFormattedOutput, "-XclosureFormattedOutput", "-XnoclosureFormattedOutput")
            }
        }
        abstract class GwtTestExtension @Inject constructor(project: Project) {

        }
    }

    abstract fun getWorkerExecutor(): WorkerExecutor
    @TaskAction
    fun exec() {
        val workQueue = getWorkerExecutor().noIsolation()
        workQueue.submit(GwtCodeServerWork::class.java) {
            this.configure(this@GwtTest)
        }
    }
    /*companion object {
        private lateinit var codeserver: Thread
        init {
            embeddedServer(Netty, port) { routing { staticResources("/", "static") } }.start(wait = false)
        }
    }*/


}