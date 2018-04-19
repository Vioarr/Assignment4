/**
 * Utility class used in order to perform searches on a string.
 * The search method implements the Knuth Morris Pratt Algorithm
 */
public class KMP {
 
	/**
	 * Calculates the failure function
	 * @param p the pattern to be searched
	 * @param f the calculated failure function
	 */
	private void failure(String p,int f[]){
        int i=1;
        int j=0;
         f[0]=0;
         int m=p.length();
        while (i<m)
        {
            if (p.charAt(j)==p.charAt(i))
            {
                f[i]=j+1;
                i++;
                j++;
            }
            else if(j>0)
                j=f[j-1];
                else
                {
                    f[i]=0;
                    i++;
                }
        }
    }
    
	/**
	 * Search for the pattern specified in the given text
	 * @param text plain text to be searched
	 * @param pattern pattern to be found in the text
	 * @param f Failure function calculated (go back to your CSI2110/2510 notes if you are unsure what this is :)
	 * @return an index specifying the start of the pattern in the text. In case the pattern is not found in the text,
	 * -1 is returned.
	 */
	public int search(String text,String pattern){
		// Create an array for the failure function
		int f[]=new int[pattern.length()];

		// get the failure function
        failure(pattern,f);
        
        // execute the knuth morris pratt algorithm
        int i=0;
        int j=0;
        int n=text.length();
        int m=pattern.length();
        while (i<n)
        {
            if (pattern.charAt(j)==text.charAt(i))
            {
                if(j==m-1)
                    return i-m+2;
                i++;
                j++;
            }
            else if(j>0)
            j=f[j-1];
            else i++;
        }
        return -1;
    }   

}