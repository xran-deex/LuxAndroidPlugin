package com.github.luxlang

import com.github.luxlang.LuxCompile
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete

class LuxAndroidPlugin implements Plugin<Project> {
  void apply(Project project) {
    project.plugins.apply('android')

    project.tasks.compileLux.doFirst {
      project.android.applicationVariants.all { variant ->
        // append to the classpath only if the build type matches
        if(variant.name == project.lux.build) {
          classpath += variant.getJavaCompile().classpath
          // add the destination dir to the classpath. this contains any compiled Java code that 
          // the Lux app relies on such as R.class
          classpath += project.files(variant.getJavaCompile().destinationDir)
        }
      }
      // now add the android boot classpath. this contains the android.jar and support libraries
      project.android.getBootClasspath().each { f -> classpath += project.files(f) }
    }

    project.task('copyLuxClasses', type: Copy).configure({
     from "$project.buildDir/$project.lux.classesPath"
     into "$project.buildDir/intermediates/classes/$project.lux.build"
    }).dependsOn(project.tasks.compileLux)

    project.task('deleteClasses', type: Delete).doLast {
       project.delete("$project.buildDir/intermediates/classes/$project.lux.build")
    }

    project.afterEvaluate {
        project.tasks.preBuild.finalizedBy project.tasks.deleteClasses
        if(project.lux.build == "debug") {
          project.tasks.compileLux.dependsOn project.tasks.compileDebugJavaWithJavac
          project.tasks.transformClassesWithDexForDebug.dependsOn project.tasks.copyLuxClasses
        }
        if(project.lux.build == "release") {
          project.tasks.compileLux.dependsOn project.tasks.compileReleaseJavaWithJavac
          project.tasks.transformClassesWithDexForRelease.dependsOn project.tasks.copyLuxClasses
        }
    }

    project.tasks.whenTaskAdded { task ->
    }
  }
}
