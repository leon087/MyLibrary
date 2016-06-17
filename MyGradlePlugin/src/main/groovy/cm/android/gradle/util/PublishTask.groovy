package cm.android.gradle.util

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

public class PublishTask extends DefaultTask {
    @Optional
    String message = 'cm'

    @TaskAction
    void publishPack() {
        println("---------start publish task--------------")

        this.project.android.applicationVariants.all { variant ->
            switch (message){
                case 'cm':
                    println("=====set signconfig cm in applicationVariants=======")
                    break
                case 'zds':
                    println("=====set signconfig zds in applicationVariants=======")
            }

        }
    }
}