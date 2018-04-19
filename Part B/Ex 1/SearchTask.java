/**
 * Implements a non-multithreaded search mechanism in order to find a pattern in the list of files
 * @author Hussein Al Osman
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class SearchTask {
	
	private SearchJob [] searchJobs;
	private String pattern;
	private KMP kmp;
	
	/**
	 * Constructor accepting an array of search jobs
	 * @param searchJobs an array of search jobs
	 * @param pattern you are looking for
	 */
	public SearchTask (SearchJob [] searchJobs, String pattern){
		
		this.searchJobs = searchJobs;
		this.pattern = pattern;
		
		kmp = new KMP();
	}
	
	/**
	 * Constructor accepting a single search job
	 * @param searchJob a single search job
	 * @param pattern you are looking for
	 */
	public SearchTask (SearchJob searchJob, String pattern){
		
		this.searchJobs = new SearchJob [1];
		searchJobs[0] = searchJob;
		
		this.pattern = pattern;
		
		kmp = new KMP();
	}
	
	
	/**
	 * Run the search by going through all the files one line at a time and looking for the pattern
	 */
	public void runSearch(){

		for (SearchJob job: searchJobs){ // Go through the list of files
			try {
				// Create a buffered reader for each file
				FileReader fr = new FileReader(job.getFile());
				BufferedReader br = new BufferedReader(fr);
				String line;
				int counter = 0;
				
				// Read one line at a time
				while((line=br.readLine()) != null){
					counter++; // keep track of the line number
										
			        // Perform the search on that line
					int pos= kmp.search(line, pattern);
					
					if (pos > 0)
						foundPattern(job.getName(), counter);

					
				}
				
				// Close the buffered reader resource
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * In the current implementation, this method only prints a message to the console whenever the pattern
	 * is found in a file
	 * @param fileName name of the file where the pattern was found
	 * @param lineNumber number of the line where the pattern was found
	 */
	private void foundPattern (String fileName, int lineNumber){
		System.out.println("Sequence found in: "+fileName+" at line: "+lineNumber);
	}

}
