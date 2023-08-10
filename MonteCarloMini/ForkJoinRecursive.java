package MonteCarloMini;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * The ForkJoinRecursive class is a Java class that implements the RecursiveAction interface and is
 * used to divide a large array of Search objects into smaller subtasks and process them using multiple
 * threads.
 */
public class ForkJoinRecursive extends RecursiveAction
{
    //declarations
    int numOfSearches;
    Search[] searches, searchPart1, searchPart2;
    List<int[]> results;//holds all contenders of the minimum
    final int THRESHOLD = 150000; //threshold on whether to split array 

    public ForkJoinRecursive(int numOfSearches, Search[] searches, List<int[]> results)
    {
        this.numOfSearches = numOfSearches;
        this.searches = searches;
        this.results = results;
    }
    
    /**
     * The compute() function checks if the number of searches is greater than a threshold, and if so,
     * creates subtasks and invokes them using ForkJoinTask, otherwise it processes the searches and
     * adds the results to a list.
     */
    public void compute()
    {
        if(numOfSearches>THRESHOLD)
        {
            ForkJoinTask.invokeAll(createSubtasks());
        }
        else
        {
            results.add(process(searches));
        }
    }
    /**
     * The function creates subtasks by dividing the searches array into two parts and creating a new
     * ForkJoinRecursive object for each part.
     * 
     * return The method is returning a List of ForkJoinRecursive objects which will be executed by multiple threads
     */
    private List<ForkJoinRecursive> createSubtasks() 
    {
        List<ForkJoinRecursive> subtasks = new ArrayList<>();
        int arr_size = numOfSearches/2;
                searchPart1 = new Search[arr_size];
                searchPart2 = new Search[arr_size];
                searchPart1 = Arrays.copyOfRange(searches,0,arr_size-1);
                searchPart2 = Arrays.copyOfRange(searches,arr_size,numOfSearches-1);
                subtasks.add(new ForkJoinRecursive(arr_size, searchPart1,results ));
                subtasks.add(new ForkJoinRecursive(arr_size, searchPart2,results));
            return subtasks;
    }

    /**
     * The function "process" takes an array of Search objects, finds the minimum value returned by
     * the "find_valleys" method for each object, and returns an array containing the minimum value and
     * the index of the object that found it.
     */
    public int[] process(Search[] searchesI)
    {
        int min=Integer.MAX_VALUE;
    	int local_min=Integer.MAX_VALUE;
    	int finder;
        int[] answer = new int[3];
        for(int i=0;i<searchesI.length;i++) 
		{
    		local_min=searchesI[i].find_valleys();
    		if((!searchesI[i].isStopped())&&(local_min<min)) 
			{ 
    			min=local_min;
    			finder=i; //keep track of who found it
			}   
            else
            {
                continue;
            }     
        answer[0] = min;
        answer[1] = finder;        
    	}
        return answer;    
    }
}