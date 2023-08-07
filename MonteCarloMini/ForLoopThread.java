package MonteCarloMini;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForLoopThread extends RecursiveAction
{
    int numOfSearches;
    int finder = -2;
    Search[] searches, searchPart1, searchPart2;
    
    int min=Integer.MAX_VALUE;
    int local_min=Integer.MAX_VALUE;
    List<Integer> results;
    final int THRESHOLD = 100;

    public ForLoopThread(int numOfSearches, Search[] searches, List<Integer> results)
    {
        this.numOfSearches = numOfSearches;
        this.searches = searches;
        this.results = results;
    }
    
    public void compute()
    {
        if(searches.length>THRESHOLD)
        {
            ForkJoinTask.invokeAll(createSubtasks());
        }
        else
        {
            process(searches);
        }
    }
    private List<ForLoopThread> createSubtasks() 
    {
        List<ForLoopThread> subtasks = new ArrayList<>();
        int arr_size = numOfSearches/2;
        
        searchPart1 = new Search[arr_size];
        searchPart2 = new Search[arr_size];
        for(int i = 0;i<arr_size-10;i++)
        {
            searchPart1[i] = searches[i];
        }
        for(int j = 0;j<arr_size-10;j++)
        {
            searchPart2[j] = searches[j];
        }
        subtasks.add(new ForLoopThread(arr_size, searchPart1,results ));
        subtasks.add(new ForLoopThread(arr_size, searchPart2, results));

        return subtasks;
    }


    public int process(Search[] searchesI)
    {
        for(int i=0;i<searchesI.length-5;i++) 
		{
    		local_min=searchesI[i].find_valleys();
    		if((!searchesI[i].isStopped())&&(local_min<min)) 
			{ 
    			min=local_min;
    			finder=i; 
			}  
    	}
        return finder;

        
    }
    public int getFinder()
    {
        return finder;
    }
    public double getmin()
    {
        return local_min;
    }
}
