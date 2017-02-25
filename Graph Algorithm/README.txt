VIJAEAC CRISTIAN-OCTAVIAN
325CB
TEMA 2 PROIECTAREA ALGORITMILOR

1)Problema 1
	Pentru rezolvarea problemei 1 am implementat 3 algoritmi:
	1)Kruskal : acest algoritm l-am implementat pentru a putea gasi AMA si costul minim generat de acesta atunci cand
	putem sa folosim orice muchii fara nicio restrictie.Algoritmul functioneaza in felul urmator:
		-se sorteaza lista de muchii in ordinea costurilor de la mic la mare
		-se construieste un vector care memoreaza radacina arborelui din care face parte un nod (initial fiecare
		nod face parte dintr-un arbore avand ca radacina insusi nodul)
		-pentru fiecare muchie,se verifica daca nodurile se afla in aceiasi arbori,iar daca se afla inseamna ca nu
		avem nimic de facut intrucat nodurile au fost deja puse in AMA
		-daca nu se afla in acelasi arbore,inseamna ca trebuie sa facem uniunea celor 2 arbori in care se afla,iar pentru oprimizare
		uniunea acestora se va face in functie de adancimea fiecarui arbore : se va lipi arborele cu adancimea mai 
		mica,la arborele cu adancimea mai mare
		-daca adancimile sunt egale,nu conteaza cum facem uniunea,ci doare marim adancimea arborelui la care lipim
		-de asemenea,tot pentru optimizare,memorez numai radacina arborelui din care face parte un nod,nu toata calea
		-costul minim se va afla adunand costurile muchiilor care fac parte din AMA

	2)LCA : inaintea de rularea acestui algoritm,verific daca muchia pe care trebuie sa o introducem ca alternativa in 
	AMA se afla deja in arbore.Daca da,inseamna ca noul cost va fi acelasi cu costul mai sus calculat.Daca nu exista deja
	in arbore sau valoarea noului cost nu a fost deja calculata(pastrez evidenta tuturor costurilor noi aflata introducand
	muchii noi) trebuie sa rulam LCA.Acesta functioneaza in felul urmator:
		-se tine evidenta parintilor nodurilor aflati pe nivelul 2^i(astfel impartim arborele in sectiuni si obtinem
		o complexitate de O(nr_noduri * log(nr_noduri)))
		-de asemenea,se tine evidenta costului maxim pe calea nod-parinte,la fel,tot pentru nivelurile 2^i
		-cele 2 de mai sus se calculeaza folosind un DFS
		-cand rulam algoritmul,mai intai verificam daca cele 2 noduri se afla pe acelasi nivel,iar daca nu calculam
		diferenta dintre ele si urcam in arbore pana ajungem pe acelasi nivel.Astfel vom putea urca,dupa aceea,in paralel
		cu cele 2 noduri pana la stramosul comun
		-daca pe parcursul urcarilor (fie pentru echilibrare,fie in paralel) se descopera o muchie cu un cost mai mare
		decat ce am gasit pana atunci,actualizam valoarea
		-se intoarce valoarea iar noul cost se calculeaza dupa formula : costAMAinitial+costMuchieNoua-costMax
		-se adauga muchia impreuna cu valoarea noului cost al noului AMA intr-o lista pentru a nu fi nevoie 
		de recalcularea ei in cazul in care o mai intalnim o data

2)Problema 2
	Pentru rezolvarea celei de-a doua probleme,am folosit un DFS modificat pentru a depista daca o componenta conexa are sau nu
	are ciclu(daca are ciclu inseamna ca nu exista noduri izolate,iar daca nu are ciclu inseamna ca avem de-a face cu un arbore care
	are un nod izolat)
	Algoritmul functioneaza in felul urmator:
		-se pleaca dintr-un nod sursa,care este primul nod nevizitat gasit in vectorul de noduri
		-se marcheaza acest nod ca vizitat,iar apoi se afla vecinii acestuia
		-pentru fiecare vecini,se verifica daca a fost sau nu vizitat,iar daca nu a fost se porneste un nou dfs avand ca sursa acest vecin
		si se actualizeaza vectorul de parinti,astfel ca parintele vecinului este nodul sursa
		-daca ajungem intr-un vecin vizitat,verificam daca avem ciclu in acest caz in functie de conditia ca parintele sursei sa nu fie acest
		nod vizitat(acest lucru se poate intampla din cauza ca avem un graf bidirectional iar de exemplu 1-2 2-1 este considerat un ciclu cand,de fapt,
		este doar o muchie)
		-daca am gasit ciclu,se semnaleaza acest lucru printr-o variabila si se intoarce 0,iar daca nu gasim ciclu se intoarce 1
		-se continua rularea algoritmului DFS pentru toate componentele conexe/nodurile nevizitate pana cand toate devin vizitate
	
	