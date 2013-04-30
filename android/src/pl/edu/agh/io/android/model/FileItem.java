package pl.edu.agh.io.android.model;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/29/13
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileItem {
    private final String[] soundExtensions = { "mp3", "vaw", "ogg", "flac", "wma" };
    private final String[] imageExtensions = { "jpg", "png", "gif", "jpeg", "tif", "tiff" };

    private String name;
    private String path;
    private boolean dir;

    public FileItem(String name, String path, boolean isDirectory){
        this.name=name;
        this.path=path;
        this.dir=isDirectory;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString(){
        return name;
    }

    public boolean isDirectory(){
        return dir;
    }

    public boolean isSound(){
        return isA(soundExtensions);
    }

    public boolean isImage(){
        return isA(imageExtensions);
    }

    private boolean isA(String[] what){
        for(String extension: what)
            if(name.endsWith(extension))
                return true;
        return false;
    }
}