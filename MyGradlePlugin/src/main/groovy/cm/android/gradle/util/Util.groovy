package cm.android.gradle.util

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.CopySpec

public class Util {
    public static def computeVersionName(Project project) {
        return project.versionName + "." + project.versionCode + "." + buildTime()
    }

    public static def buildTime() {
        def date = new Date()
        def formattedDate = date.format('MMddHH')
        return formattedDate
    }

    public static def isWindows() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("windows") >= 0) {
            return true;
        }
        return false;
    }

    public static void rename(def variant, def appName) {
        def outName = createName(variant, appName)
        variant.outputs.each { output ->
            output.outputFile = new File(output.outputFile.parent, "${variant.buildType.name}/" + outName)
        }
    }

    public static def createName(def variant, def appName) {
        def versionName = variant.getVersionName()

        def productFlavorName = ''
        if (variant.productFlavors.size() > 0) {
            productFlavorName = variant.productFlavors.name
        }

        def outName = "${appName}-${versionName}${productFlavorName}.apk"
//        return "${variant.buildType.name}" + '/' + outName;
        return outName;
    }

    public static void deleteUnaligned(def output) {
        File unaligned = output.packageApplication.outputFile;
        File aligned = output.outputFile
        println("ggggg unaligned = " + unaligned + ",aligned = " + aligned)
        if (!unaligned.getName().equalsIgnoreCase(aligned.getName())) {
//            println "deleting " + unaligned.getName()
//            unaligned.delete()
        }
    }

//    public static void deleteUnaligned(def outputs) {
//        outputs.each { output ->
//            File unaligned = output.packageApplication.outputFile;
//            File aligned = output.outputFile
//            if (!unaligned.getName().equalsIgnoreCase(aligned.getName())) {
//                println "deleting " + unaligned.getName()
//                unaligned.delete()
//            }
//        }
//    }

    public static def pack(Project project, def outName) {
        //copy
        def buildRoot = project.buildDir
        def outputs = "outputs"
        def apk = 'apk'
        def mapping = 'mapping'

        def buildOutputs = new File(buildRoot, outputs)
        def buildApk = new File(buildOutputs, apk)
        def buildMapping = new File(buildOutputs, mapping)
//        def outName = "${project.appName}-${project.defaultConfig.versionName}"

        def out = new File(buildRoot, outName)
        def outApk = new File(out, apk)
        def outMapping = new File(out, mapping)

        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
                copySpec.from(buildApk)
                copySpec.into(outApk)
            }
        })

        //TODO ggg 遍历rtest与release，并过滤掉其下的旧版本
        println("ggg ")

        //删除debug
        File debug = new File(outApk, "debug")
        if (debug.exists()) {
            def result = debug.deleteDir()
        }
        //删除旧版本
        File rtest = new File(outApk, "rtest")
        File release = new File(outApk, "release")
        File[] rtestFiles = rtest.listFiles(new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                if (name.contains(outName)) {
                    return false
                }
                return true
            }
        })
        if (rtestFiles != null) {
            for (int i = 0; i < rtestFiles.size(); i++) {
                rtestFiles[i].delete()
            }
        }
        File[] releaseFiles = release.listFiles(new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                if (name.contains(outName)) {
                    return false
                }
                return true
            }
        })
        if (releaseFiles != null) {
            for (int i = 0; i < releaseFiles.size(); i++) {
                releaseFiles[i].delete()
            }
        }
        //copy mapping
//        copy {
//            from buildMapping
//            into outMapping
//        }
        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
                copySpec.from(buildMapping)
                copySpec.into(outMapping)
            }
        })

        return out
    }

    public static def pack2(Project project, def outName) {
        def buildRoot = project.buildDir
        def outputs = "outputs"
        def mapping = 'mapping'
        def apk = 'apk'

        def buildOutputs = new File(buildRoot, outputs)
        def buildApk = new File(buildOutputs, apk)
        def buildMapping = new File(buildOutputs, mapping)
//        def outName = "${project.appName}-${project.defaultConfig.versionName}"

        def out = new File(buildRoot, outName)
        def outApk = new File(out, apk)
        def outMapping = new File(out, mapping)

        //清理旧包
        out.delete();

        //copy apk
        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
//                new FileTreeBuilder(buildApk).

                copySpec.include('**/*' + outName + '*')
                copySpec.exclude('debug')

                copySpec.from(buildApk)
                copySpec.into(outApk)
            }
        })

        //copy mapping
//        copy {
//            from buildMapping
//            into outMapping
//        }
        project.copy(new Action<CopySpec>() {
            @Override
            void execute(CopySpec copySpec) {
                copySpec.from(buildMapping)
                copySpec.into(outMapping)
            }
        })

        return out
    }

    public static def wrapStr(def str) {
        return '"' + str + '"';
    }

    public static def wrapString(def str) {
        return '"' + str + '"';
    }

    public static def getKeyStore(def project, def propertyFile) {
        if (propertyFile == null) {
            return 'debug'
        }

        def props = new Properties();
        try {
            def signingConfig = 'release';

            def ins = new BufferedInputStream(new FileInputStream(propertyFile));
            props.load(ins);
            ins.close();
            project.android.signingConfigs {
                "$signingConfig" {
                    storeFile = new File(props.getProperty('storeFile'));
                    storePassword = props.getProperty('storePassword');
                    keyAlias = props.getProperty('keyAlias');
                    keyPassword = props.getProperty('keyPassword');
                }
            }
            return "$signingConfig";
        } catch (Exception e) {
            return 'debug';
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (RuntimeException rethrown) {
            throw rethrown;
        } catch (Exception e) {
            print(e.getMessage());
//            logger.error(e.getMessage(), e);
        }
    }

}
