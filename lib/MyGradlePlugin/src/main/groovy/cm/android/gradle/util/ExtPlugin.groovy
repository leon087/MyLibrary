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

        project.android.applicationVariants.all { variant ->
            //重命名
            Util.rename(variant, project.appName)

            //拷贝
//            variant.assemble.doLast {
//                //TODO ggg 会执行多次，确保只在最后执行
//
//                println("---------pack--------------")
//                def outName = "${project.appName}-${project.android.defaultConfig.versionName}"
//                def out = Util.pack2(project, outName)
//                println("out = " + out)
//                println("=====================doLast success.=========================")
//            }
        }

        project.gradle.buildFinished { result ->
            if (result.failure) {
                println("-------- build failure-----------")
            } else {
                //拷贝
                println("---------pack--------------")
                def outName = "${project.appName}-${project.android.defaultConfig.versionName}"
                def out = Util.pack2(project, outName)
                println("out = " + out)
                println("=====================doLast success.=========================")
            }
        }
    }
//        project.task('pack', type: PackTask)
}