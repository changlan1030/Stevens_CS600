import java.util.ArrayList;
import java.util.LinkedList;

public class Project8_2 {
	public static void main(String[] args) {
		int graph[][] = {
				{ 0, 7, 6, 5, 0, 0, 0 },
				{ 0, 0, 0, 1, 2, 0, 0 },
				{ 0, 0, 0, 3, 0, 9, 0 },
				{ 0, 0, 0, 0, 5, 0, 3 },
				{ 0, 0, 0, 0, 0, 0, 6 },
				{ 0, 0, 0, 0, 0, 0, 8 },
				{ 0, 0, 0, 0, 0, 0, 0 } };
		int s = 0;
		int t = graph.length - 1;
		
		System.out.println("Method 1: use BFS on the residual graph:");
		int maxFlow1 = rGraphBFS(graph, s, t);
		System.out.println("The maximum flow: " + maxFlow1);
		
		System.out.println();
		
		System.out.println("Method 2: use DFS on the residual graph:");
		int maxFlow2 = rGraphDFS(graph, s, t);
		System.out.println("The maximum flow: " + maxFlow2);
		
		System.out.println();
		
		System.out.println("Method 3: use DFS on the original graph:");
		int maxFlow3 = graphDFS(graph, s, t);
		System.out.println("The maximum flow: " + maxFlow3);
	}
	
	public static int rGraphBFS(int[][] graph, int s, int t) {
		// create an array for the residual graph
		int size = graph.length;
		int[][] rGraph = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				rGraph[i][j] = graph[i][j];
			}
		}
		
		// create an array to store the augmenting path
		int[] path = new int[size];
		
		int maxFlow = 0;
		while (isPathBFS(rGraph, s, t, path)) {
			// computer the minimum flow of this path
			int minFlow = Integer.MAX_VALUE;
			for (int v = t; v != s; v = path[v]) {
				int u = path[v];
				minFlow = Math.min(minFlow, rGraph[u][v]);
			}
			
			// compute the residual graph
			for (int v = t; v != s; v = path[v]) {
				int u = path[v];
				rGraph[u][v] = rGraph[u][v] - minFlow;
				rGraph[v][u] = rGraph[v][u] + minFlow;
				int i = u + 1;
				int j = v + 1;
				System.out.print(i + "->" + j + " ");
			}
			
			// compute the maximum flow
			maxFlow += minFlow;
			System.out.println("flow: " + minFlow);
		}
		return maxFlow;
	}
	
	public static boolean isPathBFS(int[][] rGraph, int s, int t, int[] path) {
		// create a queue to store the node
		GenericQueue<Integer> queue = new GenericQueue<>();
		queue.enqueue(s);
		
		// create an array to store if the node is visited
		boolean[] visited = new boolean[rGraph.length];
		visited[s] = true;
		
		// find a path
		while (queue.getSize() > 0) {
			int u = queue.dequeue();
			for (int v = 0; v < rGraph.length; v++) {
				if (visited[v] == false && rGraph[u][v] > 0) {     // if this node is not visited and has edge with that node
					queue.enqueue(v);
					path[v] = u;
					visited[v] = true;
				}
			}
			
			if (visited[t] == true) {
				return true;
			}
		}
		return false;
	}
	
	public static int rGraphDFS(int[][] graph, int s, int t) {
		// create an array for the residual graph
		int size = graph.length;
		int[][] rGraph = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				rGraph[i][j] = graph[i][j];
			}
		}
		
		// create an array to store the augmenting path
		int[] path = new int[size];
		
		int maxFlow = 0;
		while (isPathDFS(rGraph, s, t, path)) {
			// computer the minimum flow of this path
			int minFlow = Integer.MAX_VALUE;
			for (int v = t; v != s; v = path[v]) {
				int u = path[v];
				minFlow = Math.min(minFlow, rGraph[u][v]);
			}
			
			// compute the residual graph
			for (int v = t; v != s; v = path[v]) {
				int u = path[v];
				rGraph[u][v] = rGraph[u][v] - minFlow;
				rGraph[v][u] = rGraph[v][u] + minFlow;
				int i = u + 1;
				int j = v + 1;
				System.out.print(i + "->" + j + " ");
			}
			
			// compute the maximum flow
			maxFlow += minFlow;
			System.out.println("flow: " + minFlow);
		}
		return maxFlow;
	}
	
	public static boolean isPathDFS(int[][] rGraph, int s, int t, int[] path) {
		// create a stack to store the node
		GenericStack<Integer> stack = new GenericStack<>();
		stack.push(s);
		
		// create an array to store if the node is visited
		boolean[] visited = new boolean[rGraph.length];
		visited[s] = true;
		
		// find a path
		while (stack.getSize() > 0) {
			int u = stack.pop();
			for (int v = 0; v < rGraph.length; v++) {
				if (visited[v] == false && rGraph[u][v] > 0) {     // if this node is not visited and has edge with that node
					stack.push(v);
					path[v] = u;
					visited[v] = true;
				}
			}
			
			if (visited[t] == true) {
				return true;
			}
		}
		return false;
	}

	public static int graphDFS(int[][] graph, int s, int t) {
		// create an array to store if the node is visited
		int size = graph.length;
		boolean[] visited = new boolean[size];
		
		// create an array to store the flow on each edge
		int[][] flow = new int[size][size];
		
		// find a path and compute the flow on each edge
		while (DFS(graph, flow, s, t, visited, Integer.MAX_VALUE) > 0) {
			for (int i = 0; i < size; i++) {
				visited[i] = false;
			}
			System.out.println();
		}
		
		// compute the maximum flow
		int maxFlow = 0;
		for (int i = 0; i < size; i++) {
			maxFlow = maxFlow + flow[0][i];
		}
		return maxFlow;
	}
	
	public static int DFS(int[][] graph, int[][] flow, int u, int t, boolean[] visited, int minFlow) {
		if (u == t) {
			return minFlow;
		}
		
		visited[u] = true;
		
		for (int v = 0; v < graph.length; v++) {
			if (visited[v] == false && graph[u][v] - flow[u][v] > 0) {
				int i = DFS(graph, flow, v, t, visited, Math.min(minFlow, graph[u][v] - flow[u][v]));
				if (i > 0) {
					flow[u][v] = flow[u][v] + i;
					flow[v][u] = flow[v][u] - i;
					int a = u + 1;
					int b = v + 1;
					System.out.print(a + "->" + b + " flow: " + i + " ");
					return i;
				}
			}
		}
		return 0;
	}
}

class GenericQueue<E> {
	private LinkedList<E> list = new LinkedList<E>();
	
	public int getSize() {
		return list.size();
	}
	
	public void enqueue(E e) {
		list.addLast(e);
	}
	
	public E dequeue() {
		return list.removeFirst();
	}
}


class GenericStack<E> {
	private ArrayList<E> list = new ArrayList<>();
	
	public int getSize() {
		return list.size();
	}
	
	public void push(E o) {
		list.add(o);
	}
	
	public E pop() {
		E o = list.get(getSize() - 1);
		list.remove(getSize() - 1);
		return o;
	}
}