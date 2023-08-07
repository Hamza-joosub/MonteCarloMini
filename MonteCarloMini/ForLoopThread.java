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
    final int THRESHOLD = 200;

    public ForLoopThread(int numOfSearches, Search[] searches, List<Integer> results)
    {
        this.numOfSearches = numOfSearches;
        this.searches = searches;
        this.results = results;
    }
    
    public void compute()
    {
        if(numOfSearches>THRESHOLD)
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
        if(numOfSearches>200)
            {
                searchPart1 = new Search[arr_size];
                searchPart2 = new Search[arr_size];
                for(int i = 0;i<arr_size;i++)
                {
                    searchPart1[i] = new Search(searches[i].getID(), searches[i].getPos_row(),searches[i].getPos_col() , searches[i].getTerrainArea());
                    //System.out.println(searchPart1[i].getID());
                }

                for(int j=0;j<arr_size;j++)
                {
                    searchPart2[j] = new Search(searches[j + arr_size].getID(), searches[j + arr_size].getPos_row(),searches[j + arr_size].getPos_col() , searches[j+arr_size].getTerrainArea());
                    //System.out.println(searchPart1[i].getID());
                }
                //System.out.println("first: " + searchPart1[0].getID() + "Last: " + searchPart1[arr_size-1].getID());
                //System.out.println("first: " + searchPart2[0].getID() + "Last: " + searchPart2[arr_size-1].getID());
                //System.out.println("break");
                subtasks.add(new ForLoopThread(arr_size, searchPart1,results ));
                subtasks.add(new ForLoopThread(arr_size, searchPart2, results));
            }   
        else
            {
                
            }
            return subtasks;
    }


    public int process(Search[] searchesI)
    {
        int finder = -2;
        for(int i=0;i<searchesI.length;i++) 
		{
    		local_min=searchesI[i].find_valleys();
    		if((!searchesI[i].isStopped())&&(local_min<min)) 
			{ 
    			min=local_min;
    			finder=i; 
			}  
    	}
        results.add(finder);
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
