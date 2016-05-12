package cm.android.gradle.util

import org.gradle.api.Plugin
import org.gradle.api.Project

public class ExtPlugin implements Plugin<Project> {
    void apply(Project project) {
//        project.configurations {
//        }

        if (!project.android) {
            println("ggg only android")
        }

//        project.task('copy', type: Copy)

        project.android.applicationVariants.all { variant ->
            //改名
            Util.rename(variant, project.appName)

            //拷贝
            variant.assemble.doLast {
                println("---------delete Unaligned--------------")
                Util.deleteUnaligned(variant.outputs)

                println("---------pack--------------")
                def outName = "${project.appName}-${project.android.defaultConfig.versionName}"
                def out = Util.pack(project, outName)
                println("out = " + out)
                println("=====================doLast success.=========================")
            }
        }
    }
//        project.task('pack', type: PackTask)
}