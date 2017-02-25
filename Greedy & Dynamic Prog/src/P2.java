import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class P2 {
	
	
	public static int min (int x,int y,int z){	
		
		return ( x > y ) ? ( y > z ? z : y ) : ( x > z ? z : x );	//returneaza minimul dintre cele 3 valori
	}
	
	public static int eval(int[][] words,ArrayList<Integer> word,int nrVariante,int dimVariante,int dimInitial){
		
		int compute[][]=new int[ dimVariante + 1 ][ dimInitial + 1 ];	//initializam matricea pt algoritmul de prog. dinamica
		
		
		for(int i = 0 ; i <= dimVariante ; i++){	//pentru fiecare litera din variante
		
			for(int j = 0 ; j <= dimInitial ; j++){	//pentru fiecare litera din cuvantul initial
			
				if (i == 0)	//daca ne aflam pe prima linia,construim cazul de baza
					compute[i][j] = j;
				
				else if (j == 0)	//daca ne aflam pe prima coloana,construim cazul de baza
					compute[i][j] = i;
				
				else {
					
					if (words[i - 1][word.get(j - 1)] == 1)	//daca valoarea cautata exista in matricea de aparitii(adica pe coloana de litere i-1 din variante)
						compute[i][j] = compute[i - 1][j - 1];	//numarul de modificari din aceasta stare ramane neschimbat fata de precedenta stare
					
					
					else	//daca nu se gaseste litera
						compute[i][j]=1 + min(compute[i][j - 1],compute[i - 1][j],compute[i - 1][j - 1]);	//nr de modificari de la starea curenta este 1 + minim-ul dintre costurile pentru iserare/stergere/modificare
					
				}
			}
		}
		
		return compute[dimVariante][dimInitial];	//elementul final care da numarul de mutari efectuate
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new FileReader("evaluare.in"));
		String[] aux;
		
		aux = in.readLine().split(" ");
		
		int nrVariante = Integer.parseInt(aux[0]);	//citim numarul de variante
		int dimVariante = Integer.parseInt(aux[1]);	//citim numarul de litere al unei variante
		int dimInitial = 0;
	
		int[][] words = new int[dimVariante][256];	//initializam matricea de aparitii(linia i reprezinta coloana de "litere" de pe pozitia i din variante,iar coloanele valorile posibile pt fiecare "litera" din varianta)
		ArrayList<Integer> word = new ArrayList<Integer>();
		
		try {

			for (int i = 0;i < nrVariante;i++){	//citim linie cu linie
				
				aux=in.readLine().split(" ");	//separam dupa spatiu
				
				for(int j = 0 ; j < dimVariante ; j++)	
					words[j][Integer.parseInt(aux[j])] = 1;	//inseram 1 pe linia j(corespunzatoare coloanei de "litere" j din variante) si pe coloana aferenta valorii "literei" noastre pt a arata existenta ei
			}
			
			aux = in.readLine().split(" ");		
			dimInitial = Integer.parseInt(aux[0]);	//citim dimensiunea cuvantului initial
			
			aux = in.readLine().split(" ");
			for (int i = 0 ; i < dimInitial ; i++)	
				word.add(Integer.parseInt(aux[i]));	//bagam literele cuvantului initial intr-o lista de caractere
				
		
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
		PrintWriter out;
		
        try {
            
        	out = new PrintWriter(new FileWriter("evaluare.out"));
        	
        	int rez = eval(words,word,nrVariante,dimVariante,dimInitial);	//apelam functia care calculeaza numarul minim de modificari 
       
        	out.print(rez);	//scriem in fisier rezultatul obtinut
 
            out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	
	}

}
