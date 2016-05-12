package cm.android.gradle.util

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class PackTask extends DefaultTask {
    @TaskAction
    void output() {
        Util.pack(this.project)
    }
}
