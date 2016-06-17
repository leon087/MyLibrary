package cm.android.gradle.util

import org.gradle.api.Plugin
import org.gradle.api.Project

public class SignPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        if (!project.android) {
            println("ggg only android")
        }

        project.android.signingConfigs {
//            test {
//                storeFile project.rootProject.file("keystore/cm_debug.jks")
//                storePassword "mdm.debug"
//                keyAlias "mdm_debug"
//                keyPassword "mdm.debug"
//            }

            InputStream local = null;
            InputStream jks = null;
            Properties properties = new Properties()
            try {
                local = project.rootProject.file('local.properties').newDataInputStream()
                properties.load(local)

//                String dir = properties.getProperty('jks.dir')
//                jks = new BufferedInputStream(new FileInputStream(dir));
//                properties.load(jks)
            } catch (Exception e) {
                println(e.getMessage())
            } finally {
                Util.closeQuietly(local)
                Util.closeQuietly(jks)
            }

            dbg {
                storeFile project.rootProject.file("keystore/cm_debug.jks")
                storePassword "mdm.debug"
                keyAlias "mdm_debug"
                keyPassword "mdm.debug"
            }

            cm {
                try {
                    storeFile project.rootProject.file(properties.storeFile)
                    storePassword properties.storePassword
                    keyAlias properties.keyAlias
                    keyPassword properties.keyPassword
                } catch (Exception e) {
                    initWith(dbg)
                }
            }

            zds {
                try {
                    storeFile project.rootProject.file(properties.storeFile_zds)
                    storePassword properties.storePassword_zds
                    keyAlias properties.keyAlias_zds
                    keyPassword properties.keyPassword_zds
                } catch (Exception e) {
                    initWith(dbg)
                }
            }
        }
    }
}
