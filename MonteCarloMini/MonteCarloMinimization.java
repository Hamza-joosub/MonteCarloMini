package MonteCarloMini;
import java.util.ArrayList;
import java.util.List;
/* Serial  program to use Monte Carlo method to 
 * locate a minimum in a function
 * This is the reference sequential version (Do not modify this code)
 * Michelle Kuttel 2023, University of Cape Town
 * Adapted from "Hill Climbing with Montecarlo"
 * EduHPC'22 Peachy Assignment" 
 * developed by Arturo Gonzalez Escribano  (Universidad de Valladolid 2021/2022)
 */
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

class MonteCarloMinimization
{
	static final boolean DEBUG=false;
	
	static long startTime = 0;
	static long endTime = 0;

	//timers - note milliseconds
	private static void tick()
	{
		startTime = System.currentTimeMillis();
	}
	private static void tock()
	{
		endTime=System.currentTimeMillis(); 
	}
	
public static void main(String[] args)  
{
    	int rows, columns; //grid size
    	double xmin, xmax, ymin, ymax; //x and y terrain limits
    	TerrainArea terrain;  //object to store the heights and grid points visited by searches
    	double searches_density;	// Density - number of Monte Carlo  searches per grid position - usually less than 1!

     	int num_searches;		// Number of searches
    	Search [] searches;		// Array of searches
    	Random rand = new Random();  //the random number generator
    	/* 
    	if (args.length!=7) {  
    		System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);
    	}
    	rows =Integer.parseInt( args[0] );
    	columns = Integer.parseInt( args[1] );
    	xmin = Double.parseDouble(args[2] );
    	xmax = Double.parseDouble(args[3] );
    	ymin = Double.parseDouble(args[4] );
    	ymax = Double.parseDouble(args[5] );
    	searches_density = Double.parseDouble(args[6] );
	
    	if(DEBUG) {
    	
    		System.out.printf("Arguments, Rows: %d, Columns: %d\n", rows, columns);
    		System.out.printf("Arguments, x_range: ( %f, %f ), y_range( %f, %f )\n", xmin, xmax, ymin, ymax );
    		System.out.printf("Arguments, searches_density: %f\n", searches_density );
    		System.out.printf("\n");
    	}
    	*/



		rows = 1600;
		columns = 1600;
		xmin = 0;
		xmax = 100;
		ymin = 0;
		ymax = 100;
		int finder = 0;
		searches_density = 0.2;
    	// Initialize 
    	terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	num_searches = (int)( rows * columns * searches_density );
    	searches= new Search [num_searches];
    	for (int i=0;i<num_searches;i++)
		{ 
    		searches[i]=new Search(i+1, rand.nextInt(rows),rand.nextInt(columns),terrain);
		}
      	if(DEBUG) 
		{
    		/* Print initial values */
    		System.out.printf("Number searches: %d\n", num_searches);
    		//terrain.print_heights();
    	}
		int min = Integer.MAX_VALUE;
		System.out.println(num_searches);
		System.out.println(searches.length);
		List<int[]> results = new ArrayList<>();
    	//start timer
    	tick();
		ForkJoinRecursive thing = new ForkJoinRecursive(num_searches, searches, results); ////This line is creating an object that will be used to perform a parallel computation usingthe Fork-Join framework.
		ForkJoinPool fjp1 = new ForkJoinPool();// The `ForkJoinPool` is responsible for distributing tasks to worker threads and coordinating their execution.
		fjp1.invoke(thing);
		
		// The code is iterating over the `results` list, which contains arrays of integers. For each array
		// in the list, it checks if the first element (`results.get(i)[0]`) is less than the current minimum
		// value (`min`). If it is, the minimum value is updated to the new value and the variable `finder`
		// is set to the second element of the array (`results.get(i)[1]`). Essentially, this loop is finding
		// the minimum value in the `results` list and keeping track of the index of that minimum value.
		for (int i = 0;i<results.size()-1;i++)
		{
			if(results.get(i)[0] < min)
			{
				min = results.get(i)[0];
				finder = results.get(i)[1];
			}
		}


		tock();
    		if(DEBUG) System.out.println("Search "+searches[finder].getID()+" finished at  "+min + " in " +searches[finder].getSteps());
    	
   		//end timer
   		
   		
    	if(DEBUG) 
		{
    		/* print final state */
    		terrain.print_heights();
    		terrain.print_visited();
    	}
    	
		System.out.printf("Run parameters\n");
		System.out.printf("\t Rows: %d, Columns: %d\n", rows, columns);
		System.out.printf("\t x: [%f, %f], y: [%f, %f]\n", xmin, xmax, ymin, ymax );
		System.out.printf("\t Search density: %f (%d searches)\n", searches_density,num_searches );

		/*  Total computation time */
		System.out.printf("Time: %d ms\n",endTime - startTime );
		int tmp=terrain.getGrid_points_visited();
		System.out.printf("Grid points visited: %d  (%2.0f%s)\n",tmp,(tmp/(rows*columns*1.0))*100.0, "%");
		tmp=terrain.getGrid_points_evaluated();
		System.out.printf("Grid points evaluated: %d  (%2.0f%s)\n",tmp,(tmp/(rows*columns*1.0))*100.0, "%");
	
		/* Results*/
		System.out.printf("Global minimum: %d at x=%.1f y=%.1f\n\n", min, terrain.getXcoord(searches[finder].getPos_row()), terrain.getYcoord(searches[finder].getPos_col()) );
				
    	
    }
		}