package KP;
import KP.Pliki;
import KP.Plik;
public class Test {
    static void CreateF(final byte[] name, final byte[] content) {
        final StringBuilder nameBuilder = new StringBuilder();
        for(final byte c : name) nameBuilder.append((char) c);
        final String filename = nameBuilder.toString();

        if (Katalogi.getCurrentDir().getFiles().czyPjest(filename))
            Katalogi.getCurrentDir().getFiles().KP_usunP(filename);

        Katalogi.getCurrentDir().getFiles().KP_utwP(filename, content);
    }


    static void flg(final byte[] name) {
        StringBuilder nameBuilder = new StringBuilder();
        for(final byte c : name) nameBuilder.append((char) c);
        Katalogi.getCurrentDir().getFiles().KP_pobP(nameBuilder.toString());
    }

    static void createKat(final byte[] sourceName, final byte[] targetName) {
        final String target = new String(targetName);

        if (Katalogi.getCurrentDir().getFiles().czyPjest(target))
            Katalogi.getCurrentDir().getFiles().KP_usunP(target);

        Katalogi.getCurrentDir().getFiles().KP_utwP(
                target,
                Katalogi.getCurrentDir().getFiles().KP_pobP(new String(sourceName))
        );
    }

    static void frm(final byte[] filename) {
        Katalogi.getCurrentDir().getFiles().KP_usunP(new String(filename));
    }

    static void fed(final byte[] filename, final byte[] newContent) {
        final String name = new String(filename);
        final Plik currentFile = Katalogi.getCurrentDir().getFiles();
        Katalogi.getCurrentDir().getFiles().KP_usunP(name);
    }
}
