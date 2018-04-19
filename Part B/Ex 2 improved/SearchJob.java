import java.io.File;


public class SearchJob {

	private String name;
	private File file;
	
	public SearchJob (File file){
		name = file.getName();
		this.file = file;
	}
	
	public String getName (){
		return name;
	}
	
	public File getFile(){
		return file;
	}
}
