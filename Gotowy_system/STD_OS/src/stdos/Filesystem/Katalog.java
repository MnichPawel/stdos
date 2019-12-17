package stdos.Filesystem;

import java.util.Vector;

public class Katalog {
    private String nazwa;
    public Vector<Katalog> dirs = new Vector<>();
    public Pliki files;

    Katalog(String nazwa) {
       files = new Pliki();
        this.nazwa = nazwa;
    }
    public Katalog(){

    }
    Katalog(Katalog newDir){
        this.nazwa = newDir.nazwa;
        this.dirs = newDir.dirs;
        this.files = newDir.files;
    }

    public String getName() {
        return nazwa;
    }

    public void setName(String nazwa) {
        this.nazwa = nazwa;
    }

    public Vector<Katalog> getDirectories() {
        return dirs;
    }

    public boolean czyKjest(String nazwa) {
        for (Katalog e : dirs) {
            if (e.getName().equals(nazwa)) {
                return true;
            }
        }
        return false;
    }

    public void KP_utwK(String nazwa){
        if(czyKjest(nazwa)){
            System.out.println("Katalog " + nazwa + " juz istnieje");
            return;
        }
        dirs.add(new Katalog(nazwa));

    }

    public Pliki getFiles() {
        return files;
    }

    public void KP_usunK(String nazwa) {
        for(Katalog e:dirs){
            if(e.getName().equals(nazwa)){
                dirs.remove(e);
                return;
            }
        }
        System.out.println("Brak katalogu o nazwie: " + nazwa);
    }

    public void tree(int level){
        String temp = "";
        //if(level==0) { temp = " "; }
        for (int i = 0; i < level; i++) {
            temp += "\t";
        }
        System.out.println(temp + nazwa + " ");
        for(Katalog e:dirs){
            e.tree(level+1);
        }
    }

    public void new_tree(String prefix, boolean isTail, boolean isRoot) {
        String currentDir = prefix + (isRoot ? "" : (isTail ? "└───" : "├───")) + nazwa;
        String prefixed = prefix + (isRoot ? "" : (isTail ? "    " : "│   "));


        System.out.println(currentDir);

        for (int i = 0; i < dirs.size() - 1; i++) {
            dirs.get(i).new_tree(prefixed, false, false);
        }
        if (dirs.size() > 0) {
            dirs.get(dirs.size() - 1)
                    .new_tree(prefixed, true, false);
        }
    }

}
