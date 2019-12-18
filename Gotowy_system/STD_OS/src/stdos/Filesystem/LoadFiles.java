package stdos.Filesystem;

import stdos.Filesystem.Katalogi;

import java.io.*;

public class LoadFiles {

    //private static final String PathToDiskWin = "Gotowy_system/STD_OS/src/stdos/Disk/"; //For project cloned from Github directly to IntelliJ
    private static final String PathToDiskWin = "resources/Disk/"; //For project copied from Github
    private static final String PathForFilesFS = "C";
    private static final String[] allowedExtension = new String[] {"txt", "exe"}; //Extensions without dot

    private static String getFileExtension(String filename) {
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i+1);
        }
        return extension;
    }

    private static boolean checkFileExtensionFS(String extension) {
        for(String ext:allowedExtension) {
            if(extension.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    public static void loadProgramsToFilesystem() {
        Katalogi.getDir().KP_utwK(PathForFilesFS);
        Katalogi.setCurrentDir(PathForFilesFS);

        File diskWin = new File(PathToDiskWin);

        File[] diskWinFiles = diskWin.listFiles();
        for (File f : diskWinFiles) {
            if(!checkFileExtensionFS(getFileExtension(f.getName()))) {
                continue;
            }
            String fileContent = "";
            try {
                BufferedReader br = new BufferedReader(new FileReader(PathToDiskWin + f.getName()));
                while (true) {
                    String currentLine = br.readLine();
                    if (currentLine != null)  {
                        fileContent = fileContent + currentLine + "\n";
                    }
                    else {
                        break;
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Blad: " + e.getMessage());
            }
            Katalogi.getCurrentDir().getFiles().KP_utwP(f.getName());
            Katalogi.getCurrentDir().getFiles().KP_otwP(f.getName());
            Katalogi.getCurrentDir().getFiles().KP_dopP(f.getName(), fileContent.getBytes());
            Katalogi.getCurrentDir().getFiles().KP_zamkP(f.getName());
        }
    }
}
