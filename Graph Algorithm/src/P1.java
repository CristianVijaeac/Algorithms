import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class P1 {
	
	static int numNodes;	//numarul de noduri
	static int numEdges;	//numarul de muchii
	static int numMorePaths;	//numarul de cai alternative
	static long maxCost = Long.MIN_VALUE;		//costul maxim al laturii pe care o eliminam din graf
	
	//structura de date care imi tine minte parintele nodului si costul muchiei pana la el
	
	static class ParCost {
		
		int parent;
		long cost;
		
		public ParCost(){
			parent = -1;
			cost = 0;
		}
		
		public ParCost(int parent,long cost){
			this.parent = parent;
			this.cost = cost;
		}
		
		public String toString(){
			
			return "(" + " " + this.parent + " " + this.cost + " )";
		}
	}
	
	
	//structura de date pentru muchii care memoreaza nodurile si costul drumului dintre ele
	
	static class Edge implements Comparable<Edge>{
		int start;
		int end;
		long cost;
		
		public Edge(){
			start = 0;
			end = 0;
			cost = 0;
		}
		
		public Edge(int start,int end,long cost){
			this.start = start;
			this.end = end;
			this.cost = cost;
		}
		
		public String toString(){
			
			return "(" + " " + this.start + " " + this.end + " " + this.cost + " )";
		}

		//metoda care ne ajuta sa sortam muchiile pentru algoritmul Kruskal
		@Override
		public int compareTo(Edge node) {
			if (this.cost >= node.cost) return 1;
			if (this.cost < node.cost) return -1;
			return 0;
		}

	}

	//metoda care intoarce radacina arborelui din care face parte nodulsi actualizeaza toate nodurile din arbore ca avand acea radacina
	
	public static int Find(int node,int[] rootVect){
		
		if (rootVect[node] != node)
			rootVect[node] = Find(rootVect[node],rootVect);
		
		return rootVect[node];
		
	}
	
	//implementarea algoritmului lui Kruskal + determinarea cailor alternative folosind DFS si LCA in timp logaritmic

	public static ArrayList<Long> minPath(ArrayList<Edge> sortedEdges,ArrayList<Integer> nodes,ArrayList<Integer> morePaths,ArrayList<Edge> edges){
		
		ArrayList<Long> result = new ArrayList<Long>();	//lista rezultatelor
		Edge first = new Edge();	//radacina arborelui AMA
		boolean[] isVisited = new boolean[numNodes];	//tine minte daca un nod a fost sau nu vizitat
		ParCost[][] parentsCost = new ParCost[(int)(Math.log(numNodes) / Math.log(2))][numNodes];		//matricea de parinte-cost(de pe nivelul 2^k,pentru a putea face salturi in arbore) pentru fiecare nod din arbore
		ArrayList<ArrayList<Edge>> track = new ArrayList<ArrayList<Edge>>();	//lista unde memoram rezultatele deja calculate
		
		
		for (int i = 0 ; i < parentsCost.length ; i++)
			for(int j = 0 ; j < numNodes ; j++)
				parentsCost[i][j] = new ParCost();
		
		if (numEdges <= 1) {		//cazul in care avem o singura muchie sau chiar niciuna,intoarcem direct rezultatul
			result.add(sortedEdges.get(0).cost);	
			
			for (int i=0 ; i < numMorePaths ; i++)	//daca ni se mai cer si cai alternative
				result.add(sortedEdges.get(0).cost);
			
			return result;
		}

		ArrayList<ArrayList<Edge>> path = new ArrayList<ArrayList<Edge>>();	//arborele AMA
		
		for (int i = 0 ; i < numNodes ; i++)
			path.add(new ArrayList<Edge>());
		
		for (int i = 0 ; i < numNodes ; i++)
			track.add(new ArrayList<Edge>());
		
		int[] rootVect = new int[numNodes];		//vector ce memoreaza radacinile arborilor din care fac parte nodurile
		int[] depthsTree = new int [numNodes];	//adancimea fiecarui arbore asociat unui nod
		
		for (int i = 0 ; i < numNodes ; i++){
				rootVect[i] = i;
				depthsTree[i] = 0;
		}
		
		int count = 0;
		
		while(count<numEdges && count<numNodes){	//aplicam Kruskal cat timp avem noduri neatasate arborelui sau muchii neparcurse
			
			Edge edge = sortedEdges.get(count);
			
			if (Find(edge.start,rootVect) == Find(edge.end,rootVect)){	//daca nodurile se afla deja in acelasi arbore,nu avem nicio operatie de facut
				count++;
				continue;
			}
			
			if (count == 0){	//daca am gasit prima muchie,inseaman ca am gasit si radacina arborelui pe care o memoram	
				first = edge;
			}
			path.get(edge.start).add(new Edge(edge.start,edge.end,edge.cost));	//adaugam muchia in arborele AMA
			
			int root1 = Find (edge.start,rootVect);	//radacina arborelui din care face parte un nod
			int root2 = Find (edge.end,rootVect);
			
			if (root1 != root2){	//daca radacinile sunt diferinte,inseamna ca trebuie sa unim cei 2 arbore intr-unul singur,iar pentru optimizare
									//acest lucru se realizeaza comparand adancimile,astfel ca vom atasa arborele mai mic la cel mai mare
				if (depthsTree[root1] < depthsTree[root2]){
					rootVect[root1] = root2;
				}
				else if (depthsTree[root1] > depthsTree[root2]){
					rootVect[root2] = root1;
				}
				else{
					rootVect[root1] = root2;
					depthsTree[root1] += 1;
				}
			}	
			
			count++;
	
		}
		
		long sum = 0;
		
		for (ArrayList<Edge> node:path){
			for (int i=0;i<node.size();i++)
				sum+=node.get(i).cost;
		}
		
		System.out.println(path);
		
		result.add(sum);	//adaugam costul minim gasit in lista de rezultate
		
		int depthsNodes[] = new int[numNodes];	//adancimea la care se afla fiecare nod in arbore
		
		for (ArrayList<Edge> neighbours : path){
			for (Edge edge : neighbours){
				path.get(edge.end).add(new Edge(edge.end,edge.start,edge.cost));	//modificarea arborelui pt a fi bidirectional(modificare necesare pentru DFS-ul mai jos efectuat)
			}
		}
		
		
		dfsParents(path,first.start,isVisited,parentsCost,depthsNodes);		//DFS pentru a afla parintii fiecarui nod si costul dintre ei (un nivel distanta)
		
		for (int i = 1 ; i < parentsCost.length ; i++)		//pentru celelalte diferente de nivel de tipul 2^i ,completam matricea cu formulele de mai jos
			for(int j = 0 ; j < numNodes ; j++)
				if (parentsCost[i-1][j].parent != -1){
					parentsCost[i][j].parent = parentsCost[i-1][parentsCost[i-1][j].parent].parent;
					if (parentsCost[i][j].parent == -1){
						parentsCost[i][j].cost=0;
					}
					parentsCost[i][j].cost =  Math.max(parentsCost[i-1][j].cost,parentsCost[i-1][parentsCost[i-1][j].parent].cost);
					
				}
		
		for (int i=0;i<parentsCost.length;i++)
			for (int j=0;j<numNodes;j++)
				System.out.println(parentsCost[i][j]);

		for (int i=0;i<path.size();i++){
			for (Edge edge : path.get(i)){
				track.get(i).add(new Edge (edge.start,edge.end,sum));				//pentru fiecare muchie din AMA,facem costul sa fie egal cu costul total pentru a putea extrage costul total
											//direct din lista in cazul in care muchia alternativa care formeaza un nou AMA se afla deja in arbore sau
											//a fost calculata valoarea
			}
		}
		
		boolean find;
		for (int i = 0 ; i < numMorePaths ; i++){	//pentru fiecare cale alternativa
			find = false;
			Edge edge = edges.get(morePaths.get(i));
			ArrayList<Edge> neighbours = path.get(edge.start);	//extragem vecinii
			ArrayList<Edge> neighbours2 = track.get(edge.start);	
				for (int j = 0 ; j < neighbours.size() ; j++){		//verificam daca muchia pe care vrem sa o introducm pt a forma un nou AMA se afla deja in arbore
					if (neighbours.get(j).end == edge.end && neighbours.get(j).cost == edge.cost){
						find = true;
						result.add(neighbours2.get(j).cost);	//caz in care rezultatul este chiar costul,asa cum am specificat mai sus
						break;
					}else if (neighbours.get(j).end == edge.end && neighbours.get(j).cost != edge.cost){
						find = true;
						result.add(result.get(0)+edge.cost-neighbours.get(j).cost);
						break;
					}
				}
				
			if (!find){		//daca nu este gasita,inseamna ca trebuie facuta inserarea ei
				maxCost = Long.MIN_VALUE;	//costul maxim de pe calea nod-stramos comun
				LCA(edges,edge.start,edge.end,depthsNodes,parentsCost);		//implementarea LCA pentru a gasi stramosul comun al nodurilor care formeaza muchia
				long rez = result.get(0)+edge.cost-maxCost;
				path.get(edge.start).add(new Edge (edge.start,edge.end,edge.cost));	//inseram muchia impreuna cu costul totul pe care il da arborelui pentru a nu fi necesara recalcularea lui
				track.get(edge.start).add(new Edge (edge.start,edge.end,rez));
				result.add(rez);
			}
		}
		
		return result;
	
	}
	
	public static void dfsParents(ArrayList<ArrayList<Edge>> path,int source,boolean[] isVisited,ParCost[][] parentsCost,int[] depthsNodes){
		
		isVisited[source] = true;	//marcam radacina ca fiind vizitata
		
		Stack<Integer> s = new Stack<Integer>();
		s.push(source);		//o urmca pe stiva
		
		while (!s.empty()){	
			
			int node = s.pop();		//scoatem un nod care se afla pe stiva pt a vedea daca trebuie parcurs
			ArrayList<Edge> neighbours = path.get(node);	//extragem vecinii
			for (Edge neighbor : neighbours){		//verificam daca avem vecini nevizitati
					if (!isVisited[neighbor.end]){
						depthsNodes[neighbor.end] = depthsNodes[node] + 1;	//adancimea la care se afla vecinul
						isVisited[neighbor.end] = true;		//il marcam ca vizitat
						parentsCost[0][neighbor.end].parent = node; 	//ii setam parintele
						parentsCost[0][neighbor.end].cost = neighbor.cost;		//si costul de la parinte la nod
						s.push(neighbor.end);
					}
				
			}
		}

	}
	
	//metoda ca face "urcarea" in arbore in functie de distanta necesara primita ca parametru
	public static int goUp(ArrayList<Edge> edges,int node,int dist,ParCost[][] parentsCost){
		
		for (int i = 0 ; i < parentsCost.length ; i++){		//pentru fiecare nivel
			if ((dist & 1) != 0) {		//daca operatia de SI ar da 0,inseamna ca nu trebuie sa urcam acele 2^i nivele sau 1 nivel,in cazul in care ultimul bit ar fi initial 0
										//daca operatia da 1,inseamna ca trebuie sa urcam cu 2^i nivele in arbore
				if (maxCost < parentsCost[i][node].cost)
					maxCost = parentsCost[i][node].cost;	//daca gasim o muchie cu costul mai mare sau o portiune al carei cost maxim este mai mare decat ce am gasit pana acum,updatam costul la noua valoare
				
				node = parentsCost[i][node].parent;		//"urcarea" in arbore
			}
			dist=dist >> 1;
		}
		
		return node;	//se intoarce nodul la care s-a ajuns
	}
	
	public static void LCA (ArrayList<Edge> edges,int node1,int node2,int[] depthsNodes,ParCost parentsCost[][]){
		
		//mai intai trebuie sa ne aflam pe acelasi nivel cu ambele noduri pentru a putea "urca" in paralel dupa aceea
		if(depthsNodes[node1] > depthsNodes[node2]){		
			node1 = goUp (edges,node1,depthsNodes[node1]-depthsNodes[node2],parentsCost);
		}else if (depthsNodes[node1]<depthsNodes[node2]){
			node2 = goUp (edges,node2,depthsNodes[node2]-depthsNodes[node1],parentsCost);
		}
		
		if (node1 == node2){	//daca in urma urcarilor cele 2 noduri coincid,inseamna ca am gasit stramosul comun
			return;
		}
		
		for (int i = parentsCost.length - 1 ; i >= 0 ; i--){	//urcam in paralel cu cele 2 noduri
			
			//daca una din urcari se va produce printr-o muchie/sectiunea cu costul >maxCost,updatam costul
			
			if (maxCost < parentsCost[i][node1].cost && parentsCost[i][node2].cost <= parentsCost[i][node1].cost)	
				maxCost = parentsCost[i][node1].cost;
			
			else if (maxCost < parentsCost[i][node2].cost &&  parentsCost[i][node1].cost < parentsCost[i][node2].cost)
					maxCost = parentsCost[i][node2].cost;
			
				node1 = parentsCost [i][node1].parent;	//urcam in parelel cu cele 2 noduri
				node2 = parentsCost [i][node2].parent;
				
				if (node1==node2) break;	//daca cele 2 noduri coincid,am gasit lca-ul
				
			}
	
		return ;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedReader in = null;
		ArrayList<Integer> nodes = new ArrayList<Integer>();	//lista nodurilor
		ArrayList<Edge> edges = new ArrayList<Edge>();		//lista muchiilor pe care trebuie sa o pastram nesortata pt a putea face rost de muchiile alternative
		ArrayList<Edge> sortedEdges = new ArrayList<Edge>();	//lista muchiilor care va fi sortata
		ArrayList<Integer> morePaths = new ArrayList<Integer>();	//lista muchiilor pe care trebuie sa le inseram in AMA
		ArrayList<Long> result = new ArrayList<Long>();		//lista rezultatelor intoarsa de minPath(kruskal
	
		try {
			in = new BufferedReader(new FileReader("./input p1/kim10.in"));
			String[] aux = in.readLine().split(" ");
			numNodes = Integer.parseInt(aux[0]);
			numEdges = Integer.parseInt(aux[1]);
			numMorePaths = Integer.parseInt(aux[2]);
			
			for(int i = 0 ; i < numNodes ; i++){
				nodes.add(i);
			}
			
			for (int i = 0 ; i < numEdges ; i++){
				aux = in.readLine().split(" ");
				edges.add(new Edge(Integer.parseInt(aux[0])-1,Integer.parseInt(aux[1])-1,Long.parseLong(aux[2])));
				sortedEdges.add(new Edge(Integer.parseInt(aux[0])-1,Integer.parseInt(aux[1])-1,Long.parseLong(aux[2])));
			}
			
			Collections.sort(sortedEdges);	//sortarea muchiilor in functie de cost pentru Kruskal
			
			for (int i = 0 ; i < numMorePaths ; i++){
				String tmp = in.readLine();
				morePaths.add(Integer.parseInt(tmp) - 1);
			}
			
			result=minPath(sortedEdges,nodes,morePaths,edges);	//aplicam algoritmii de mai sus si memoram rezultatele intr-o lista
			
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
			BufferedWriter bufferedOut = new BufferedWriter( new FileWriter("kim.out"));
			PrintWriter out = new PrintWriter(bufferedOut);
			
			for (int i = 0 ; i < result.size() ; i++){
				out.println(result.get(i));		//scrierea in fisier
			}
			
			out.close();
			bufferedOut.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

}
