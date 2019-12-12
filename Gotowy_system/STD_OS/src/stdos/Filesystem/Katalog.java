package stdos.Filesystem;

import java.util.Vector;

public class Katalog {
    private String nazwa;
    private Vector<Katalog> dirs = new Vector<>();
    private Pliki files;

    Katalog(String nazwa) {
       files = new Pliki();
        this.nazwa = nazwa;
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

    public boolean directoryExists(String nazwa) {
        for (Katalog e : dirs) {
            if (e.getName().equals(nazwa)) {
                return true;
            }
        }
        return false;
    }

    public void addDirectory(String nazwa){
        if(directoryExists(nazwa)){
           // Shell.println("Directory " + nazwa + " already exists");
            return;
        }
        dirs.add(new Katalog(nazwa));

    }

    public Pliki getFiles() {
        return files;
    }

    public void removeDir(String nazwa) {
        for(Katalog e:dirs){
            if(e.getName().equals(nazwa)){
                dirs.remove(e);
                return;
            }
        }
        //Interfejs.println("No directory named: " + nazwa);
    }

    public void tree(int level){
        String temp = "";
        //if(level==0) { temp = " "; }
        for (int i = 0; i < level; i++) {
            temp += "\t";
        }
        //Interfejs.println(temp + name + " ");
        for(Katalog e:dirs){
            e.tree(level+1);
        }
    }

    public void new_tree(String prefix, boolean isTail, boolean isRoot) {
        String currentDir = prefix + (isRoot ? "" : (isTail ? "└───" : "├───")) + nazwa;
        String prefixed = prefix + (isRoot ? "" : (isTail ? "    " : "│   "));


        //Interfejs.println(currentDir);

        for (int i = 0; i < dirs.size() - 1; i++) {
            dirs.get(i).new_tree(prefixed, false, false);
        }
        if (dirs.size() > 0) {
            dirs.get(dirs.size() - 1)
                    .new_tree(prefixed, true, false);
        }
    }

}
