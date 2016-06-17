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

        project.android.applicationVariants.all { variant ->
            //重命名
            Util.rename(variant, project.appName)
        }

        //自动归档Task
        project.task('pack', type: PackTask)
//        project.build.dependsOn PackTask

        //自动归档Plugin
//        def assembleFlag = false
//
//        this.project.android.applicationVariants.all { variant ->
//            variant.assemble.doLast {
//                assembleFlag = true
//            }
//        }
//
//        this.project.gradle.buildFinished {
//            result ->
//                if (result.failure) {
//                    println("-------- build failure-----------")
//                    return
//                }
//
//                if (!assembleFlag) {
//                    println("-------- not pack -----------")
//                    return
//                }
//
//                //拷贝
//                println("---------pack--------------")
//                def outName = "${project.appName}-${project.android.defaultConfig.versionName}"
//                def out = Util.pack2(project, outName)
//                println("out = " + out)
//                println("=====================doLast success.=========================")
//        }

        //old plugin
//        project.android.applicationVariants.all { variant ->
////            改名
////            Util.rename(variant, project.appName)
//
//            //拷贝
//            variant.assemble.doLast {
//                variant.outputs.each { output ->
//                    //改名
//                    String out = Util.createOut(variant, project.appName)
//                    output.outputFile = new File(output.outputFile.parent, out)
//
//                    Util.deleteUnaligned(output)
//                }
//
//                println("---------pack--------------")
//                def outName = "${project.appName}-${project.android.defaultConfig.versionName}"
//                def out = Util.pack(project, outName)
//                println("out = " + out)
//                println("=====================doLast success.=========================")
//            }
//        }
    }
//        project.task('pack', type: PackTask)
}