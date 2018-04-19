/**
 * Main class where the execution will start from
 * @author Hussein Al Osman
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

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
                SearchingRunnable r = new SearchingRunnable(jobs,pattern);
                ThreadGroup theSquad = new ThreadGroup("The Squad");
                Thread Chaz = new Thread(theSquad,r, "Chaz");
                Thread Dom = new Thread(theSquad,r, "Dom");
                Thread Todd = new Thread(theSquad,r, "Todd");
                Thread Josh = new Thread(theSquad,r,"Josh");
                Chaz.start();
                Dom.start();
                Todd.start();
                Josh.start();
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
    private SearchJob[] jobs;
    private String pattern;
    private volatile int activeThreads = 4;
    private int count;
    public SearchingRunnable(SearchJob[] jobs, String pattern) {
        this.jobs = jobs;
        this.pattern = pattern;
        this.count = 0;
    }
    public synchronized void run() {
        // Call the search task in order to search for the entered sequence
        while(count < jobs.length) {
            //if the current job it was looking for is taken it looks for the job right after that and so on
            if(!jobs[count].isTaken()) {
                count++;
            } else {
                jobs[count].setTaken(true);
                SearchTask searchTask = new SearchTask(jobs[count], pattern);
                searchTask.runSearch();
                //if this is the only thread left running it notifies all other threads if it's the last
                //one to finish, if it's not the first to finish it just waits
                if(activeThreads == 1) {
                    System.out.println(Thread.currentThread().getName() + ": Job done");
                    System.out.println("All 4 jobs completed, restarting");
                    activeThreads = 4;
                    this.notifyAll();
                } else {
                    try {
                        activeThreads--;
                        System.out.println(Thread.currentThread().getName() + ": Job done");
                        System.out.println(activeThreads + " current active threads");
                        this.wait();
                    }
                    catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }
        this.notifyAll(); //getting any leftover threads started in case the number of files isn't divided evenly by 4
        System.out.println("No more jobs for " + Thread.currentThread().getName() + ", killing");
    }
}