import java.io.File;


public class SearchJob {

    private String name;
    private File file;
    private boolean taken;

    public SearchJob (File file){
        name = file.getName();
        taken = false;
        this.file = file;
    }

    public String getName (){
        return name;
    }

    public File getFile(){
        return file;
    }

    public void setTaken(boolean flag) { taken = flag; }

    public boolean isTaken() { return !taken; }
}
