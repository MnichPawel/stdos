package stdos.Filesystem;
import java.util.Stack;
import stdos.Filesystem.Katalog;
public class Katalogi{

    private static final String ROOT_NAME = "P:";
    private static final String DIR_SEPARATOR_REGEX = "[\\\\/]";

    private static Katalog kat = new Katalog(ROOT_NAME);
    private static Katalog rootDir = kat;
    private static Katalog currentDir = kat;
    private static Katalog targetDir = kat;
    private static Katalog sourceDir = kat;
    private static Stack<Katalog> history = new Stack<>();
    private static Stack<Katalog> targetHistory = new Stack<>();
    private static Stack<Katalog> sourceHistory = new Stack<>();

    public static Katalog getDir() {
        return kat;
    }

    public static Katalog getCurrentDir() {
        return currentDir;
    }

    public static Katalog getTargetDir() {
        return targetDir;
    }

    public static Katalog getSourceDir() {
        return sourceDir;
    }



    public static Katalog findDirectory(String name, String src) {
        switch(src){
            case "current":
                for (Katalog e : currentDir.getDirectories()) {
                    if (e.getName().equals(name)) {
                        return e;
                    }
                }
                break;
            case "target":
                for (Katalog e : targetDir.getDirectories()) {
                    if (e.getName().equals(name)) {
                        return e;
                    }
                }
                break;
            case "source":
                for (Katalog e : sourceDir.getDirectories()) {
                    if (e.getName().equals(name)) {
                        return e;
                    }
                }
                break;
        }
        return null;
    }

    public static String path(String filePath) {
        targetHistory = (Stack<Katalog>) history.clone();
        targetDir = new Katalog(currentDir);
        String[] path = filePath.split(DIR_SEPARATOR_REGEX);
        for (String e : path) {
            if (e == path[path.length - 1]) {
                break;
            }

            Katalogi.setTargetDir(e);
        }
        return path[path.length - 1];
    }

    public static String[] path(String targetPath, String sourcePath) {
        targetHistory = (Stack<Katalog>) history.clone();
        targetDir = new Katalog(currentDir);
        sourceHistory = (Stack<Katalog>) history.clone();
        sourceDir = new Katalog(currentDir);

        String[] tPath = targetPath.split(DIR_SEPARATOR_REGEX);
        String[] sPath = sourcePath.split(DIR_SEPARATOR_REGEX);
        for (String e : tPath) {
            if (e == tPath[tPath.length - 1]) {
                break;
            }
            Katalogi.setTargetDir(e);
        }
        for (String e : sPath) {
            if (e == sPath[sPath.length - 1]) {
                break;
            }
            Katalogi.setSourceDir(e);
        }
        String pathName[] = new String[2];
        pathName[0] = tPath[tPath.length - 1];
        pathName[1] = sPath[sPath.length - 1];
        return pathName;
    }

    public static String getPath() {
        String history = "";
        for (Katalog e: Katalogi.getHistory()){
            history += e.getName() + "\\";
        }
        String dirName = Katalogi.getCurrentDir().getName();
        if (dirName.charAt(dirName.length()-1) == ':') dirName += "\\";
        return history + dirName;
    }

    public static void setCurrentDir(String name) {
        if (name.equals("..")) {
            if (history.empty()) {
                System.out.println("Already at top level");
                return;
            }
            currentDir = history.pop();
        } else if (name.toLowerCase().equals(ROOT_NAME.toLowerCase())){
            history = new Stack<>();
            currentDir = rootDir;

        } else {
            Katalog findDir = findDirectory(name, "current");
            if (findDir != null) {
                history.push(currentDir);
                Katalogi.currentDir = findDir;
            } else {
                System.out.println("No such directory");
            }
        }
    }

    public static void setTargetDir(String name) {
        if (name.equals("..")) {
            if (targetHistory.empty()) {
                System.out.println("Already at top level");
                return;
            }
            targetDir = targetHistory.pop();
        } else if (name.equals(ROOT_NAME.toLowerCase())){
            targetHistory = new Stack<>();
            targetDir = rootDir;

        } else {
            Katalog findDir = findDirectory(name, "target");
            if (findDir != null) {
                targetHistory.push(targetDir);
                Katalogi.targetDir = findDir;
            } else {
                System.out.println("No such target directory");
            }
        }
    }

    public static void setSourceDir(String name) {
        if (name.equals("..")) {
            if (sourceHistory.empty()) {
                System.out.println("Already at top level");
                return;
            }
            sourceDir = sourceHistory.pop();
        } else if(name.equals(ROOT_NAME.toLowerCase())){
            sourceHistory = new Stack<>();
            sourceDir = rootDir;
        } else {
            Katalog findDir = findDirectory(name, "source");
            if (findDir != null) {
                sourceHistory.push(sourceDir);
                Katalogi.sourceDir = findDir;
            } else {
                System.out.println("No such source directory");
            }
        }
    }

    public static void setDir(Katalog kat) {
        Katalogi.kat = kat;
    }

    public static Stack<Katalog> getHistory() {
        return history;
    }
}
