/**
 * Main class where the execution will start from
 * @author Hussein Al Osman
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
 
public class Main {



    /**
     * Get all the files from a directory
     * @param dir File instance representing the directory
     * @return Array List of all the files in the directory
     */
    private static ArrayList<File> getFiles (File dir){
        ArrayList<File> filesList = new ArrayList<File>();

        String [] fileNames = dir.list();

        for (String fileName: fileNames){
            File file = new File(dir.getAbsoluteFile()+"/"+fileName);
            if (file.isFile()){
                filesList.add(file);
            }

        }

        return filesList;
    }


    public static void main(String args[]) throws IOException {
        
        if (args.length > 0){ // Make sure an argument was passed to this program

            // The argument should represent a directory
            File dir = new File (args[0]);

            // Check if dir is in fact a directory
            if (dir.isDirectory()){

                // Read the files in the directory
                ArrayList<File> filesList = getFiles(dir);

                // Get the pattern that will be searched for by prompting the user
                System.out.println("Enter the pattern to look for:");
                Scanner scanIn = new Scanner(System.in);
                String pattern  = scanIn.nextLine();
                scanIn.close();

                // Create an array of search jobs
                SearchJob [] jobs = new SearchJob[filesList.size()];
                for (int i = 0; i < jobs.length; i++){
                    jobs[i] = new SearchJob(filesList.get(i));
                }

                ThreadGroup theBoys = new ThreadGroup("The Squad");
                theBoys.setDaemon(true);
                for(int i = 0; i < jobs.length; i++) {
                    SearchingRunnable r = new SearchingRunnable(jobs[i], pattern, "Thread-"+Integer.toString(i+1));
                    Thread temp = new Thread(theBoys, r, Integer.toString(i+1));
                    temp.start();
                    System.out.println(theBoys.activeCount()+" current active threads in "+theBoys.getName());
                }

            }
            else {
                // The argument specified is not a path for a directory
                System.err.println("Incorrect argument: must be a directory");
            }
        }
        else {
            // No argument were passed to the program
            System.err.println("Missing argument: files directory");
        }
    }
    
}
class SearchingRunnable implements Runnable {
    private SearchJob job;
    private String pattern;
    private String name;
    public SearchingRunnable(SearchJob job, String pattern, String name) {
        this.job = job;
        this.pattern = pattern;
        this.name = name;
    }
    public void run() {
        // Call the search task in order to search for the entered sequence
        SearchTask searchTask = new SearchTask(job, pattern);
        searchTask.runSearch();
        // Print message saying the thread's done for ease of understanding
        int threadsLeftAfterDone = Thread.currentThread().getThreadGroup().activeCount()-1;
        System.out.println(name + " done, " + threadsLeftAfterDone + " thread(s) left in " + Thread.currentThread().getThreadGroup().getName());
    }
}