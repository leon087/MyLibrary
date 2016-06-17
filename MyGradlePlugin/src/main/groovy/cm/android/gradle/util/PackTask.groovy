package cm.android.gradle.util

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class PackTask extends DefaultTask {
    @TaskAction
    void output() {
        //拷贝
        println("---------pack--------------")
        def outName = "${project.appName}-${project.android.defaultConfig.versionName}"
        def out = Util.pack2(project, outName)
        println("out = " + out)
        println("=====================doLast success.=========================")
    }
}
