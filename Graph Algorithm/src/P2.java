import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class P2 {
	
	static int numNodes;	//numarul de noduri
	static int numEdges;	//numarul de muchii
	static int minNumIsolated;	//numarul de noduri izolate
	static boolean hasCycle;	//variabila care detecteaza un ciclu
	
	/*aplicam algoritmul DFS pe care il modifica pentru a putea detecta in ciclu
	Componenta conexa fara ciclu -> arbore -> numar de noduri izolate = 1
	Componenta conexa care are ciclu -> numar de noduri izolate = 0
	*/
	
	public static int DFS(ArrayList<ArrayList<Integer>> edges,int source,boolean[] isVisited,int[] parent){
	
		isVisited[source] = true;	//marcam sursa ca vizitata
		
		for (int i = 0 ; i < edges.get(source).size() ; i++){	//luam fiecare vecin al sursei
			int neighbor = edges.get(source).get(i);
			if (!isVisited[neighbor]){		//daca un vecin nu este vizitat,inseamna ca trebuie sa il vizitam
				parent[neighbor] = source;	//parintele vecinului va fi chiar sursa
				minNumIsolated += DFS(edges,neighbor,isVisited,parent);		//aplicam dfs,de data aceasta sursa fiind vecinul
			}else if (neighbor!=parent[source]){	//daca este vizitat nodul,iar acest NU este parintele sursei(deoarece intr-un graf neorientat
													//un vecin vizitat poate fi chiar parintele,nodul de unde a venit,iar asta nu inseamna ca avem ciclu)
													//inseamna ca ne-am intors intr-un nod vizitat => ciclu
				hasCycle=true;
				
			}
		}
		
		if (hasCycle)	//daca avem ciclu nu avem noduri izolate
			return 0;
		
		return 1;	//daca nu avem,inseamna ca componenta noastra conexa are un nod izolat
	}
	
	//metoda care ruleaza DFS pentru toate componentele conexe/toate nodurile nevizitate
	public static int minIsolated (ArrayList<Integer> nodes,ArrayList<ArrayList<Integer>> edges,boolean[] isVisited){
		
		int parent[] = new int[numNodes];	//vector de parinti pentru noduri
		for (int i = 0 ; i < numNodes ; i++){
			parent[i] = i;
		}
		
		for (int i = 0 ; i < numNodes ; i++){	//pentru toate nodurile nevizitate rulam DFS
			if (!isVisited[i]){
				hasCycle = false;
				minNumIsolated += DFS(edges,i,isVisited,parent);			
			}
		}
		return minNumIsolated;	//intoarcem numarul de noduri izolate gasit
		
	}
	
	public static void main(String[] args) {
		
		BufferedReader in = null;
		ArrayList<Integer> nodes = new ArrayList<Integer>();	//lista de noduri
		ArrayList<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();	//lista de muchii
		
		try {
			in = new BufferedReader(new FileReader("portal.in"));
			String[] aux = in.readLine().split(" ");
			
			numNodes = Integer.parseInt(aux[0]);
			numEdges = Integer.parseInt(aux[1]);
			boolean[] isVisited = new boolean[numNodes];
			
			for(int i = 0 ; i < numNodes ; i++){
				nodes.add(i);
				isVisited[i] = false;
				edges.add(new ArrayList<Integer>());
			}
			
			for (int i = 0 ; i < numEdges ; i++){
				aux = in.readLine().split(" ");
				edges.get(Integer.parseInt(aux[0]) - 1).add(Integer.parseInt(aux[1]) - 1);
				edges.get(Integer.parseInt(aux[1]) - 1).add(Integer.parseInt(aux[0]) - 1);
			}
			
			minNumIsolated = 0;
			minIsolated(nodes,edges,isVisited);		//apelam metoda pentru a determina numarul de noduri izolate
		
		} catch (IOException e) {
			e.printStackTrace();
		
		} finally {
			if (in != null) {
		
				try {
					in.close();
				
				} catch (IOException e) {

				}
			}
		}
		
		try {
			BufferedWriter bufferedOut = new BufferedWriter( new FileWriter("portal.out"));
			PrintWriter out = new PrintWriter(bufferedOut);
			
			out.print(minNumIsolated);	//scriem rezultatul in fisier
			
			out.close();
			bufferedOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
