package net.sayaya

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.wisepersist.gradle.plugins.gwt.GwtSuperDevOptions
import javax.inject.Inject

abstract class GwtTestOptions @Inject constructor(project: Project): GwtSuperDevOptions {
    companion object {
        const val NAME = "gwtTest"
    }
}