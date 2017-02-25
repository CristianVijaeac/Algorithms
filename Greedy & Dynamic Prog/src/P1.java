import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class P1 {
	
public static int check(ArrayList<Character> word){
		
		int dLeft = 0;
		int dRight = 0;
		int count = 0;
		
		while ( word.size() > 2 ){	//cat timp mai avem cel putin o litera facem verificari
			
			if ( word.get(0) == word.get ( word.size() - 1 ) ){	//daca cele 2 capete sunt egale: 
				word.remove(0);	//stergem capatul stanga
				word.remove( word.size() - 1 );	//stergem capatul drept
				continue;
			}else{
				
				for(int i = 1 ; i < word.size() - 1 ; i++){	//daca nu sunt egale calculam distanta de la capatul stang pana la intalnirea carecterului din dreapta
					if ( word.get(i) == word.get ( word.size() - 1 ) ){
						dLeft = i;	//daca l-am gasit,memoram pozitia si ne oprim
						break;
					}
					dLeft = 10000;	//daca nu,dam o valoarea foarte mare distantei(de preferat infinit)
				}
				
				for(int i = word.size() - 2 ; i >= 1 ; i--){	//calculam si distanta de la extrema dreapta pana la intalnirea unui caracter egal cu cel din stanga
					if ( word.get(i) == word.get(0) ){
						dRight = word.size() - 1 - i;	//memoram pozitia si iesim
						break;
					}
					dRight=10000;	//daca nu,dam o valoarea foarte mare distantei(de preferat infinit)
				}
				
				if (dRight == 10000 && dLeft == 10000){	//daca nu s-au gasit niciunul din caractere inseamna ca avem 2 caractere diferite => cuvantul nu e palindrom
					count = -1;
					break;
				}
				
				
				if (dLeft <= dRight){	//verificam care distanta este mai mica,ca sa stim ce caracter il vom translata pana la cea mai apropiata extrema
					word.remove(dLeft);	//stergem elementul,pt ca dupa permutari ar ajunge in dreapta,caz in care este acelasi cu extrema stanga=>stergere
					word.remove(word.size() - 1);	//stergem extrema dreapta
					count += dLeft;
				}else{
					word.remove(word.size() - 1 - dRight);	//stergem elementul,pt ca dupa permutari ar ajunge in dreapta,caz in care este acelasi cu extrema stanga=>stergere
					word.remove(0);	//stergem extrema stanga
					count += dRight;
				}
			}
			
			
		}
		return count;	//nr de modificari
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedReader in = null;
		int numWords=0;
		String word;
		ArrayList<Integer> result=new ArrayList<Integer>();
	
		try {
			in = new BufferedReader(new FileReader("joc.in"));
			numWords=Integer.parseInt(in.readLine());	//citim numarul de cuvinte
			//System.out.println(numWords);
			
			for(int i=0;i<numWords;i++){
				ArrayList<Character> letters=new ArrayList<Character>();	//memoram literele intr-un ArrayList
				word=new String(in.readLine());	
				for(int j=0;j<word.length();j++){	
					letters.add(word.charAt(j));	//inseram fiecare litera din fiecare cuvant intr-o lista propie
				}
				result.add(check(letters));	//lista care pastreaza rezultatele pt fiecare cuvant
			}
		
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
                
            	out = new PrintWriter(new FileWriter("joc.out"));
                
                for (int i = 0; i < result.size(); i++) {	//pt fiecare rezultat dupa prelucrarea cuvantului din lista
                    out.println(result.get(i));	//scrie acest rezultat in fisier
                    
                }
                out.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		
		
}


