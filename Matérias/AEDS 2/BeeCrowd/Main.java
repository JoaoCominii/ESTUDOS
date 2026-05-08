
import java.util.Scanner;

class Carro { 
	String placa, modelo, tipo, chassi;

	void ler(String linha) {
		String[] partes = linha.split(",");
		placa = partes[0]; 
		modelo = partes[1]; 
		tipo = partes[2]; 
		chassi = partes[3];
	}

	void imprimir() {
		System.out.println(placa + " " + modelo + " " + tipo + " " + chassi);
	}
}

class No{
	public Carro elemento;
	public No esq, dir;

	public No(Carro elemento)
	{
		this.elemento = elemento;
		this.esq = null; this.dir = null;
	}
}

class ArvoreBinaria{
	public No raiz;

	public ArvoreBinaria()
	{
		this.raiz = null;
	}

	public void inserir(Carro x)
	{
		raiz = inserir(x, raiz);
	}

	private No inserir(Carro x, No no)
	{
		if(no == null)
			no = new No(x);
		else if(x.placa.compareTo(no.elemento.placa) < 0)
			no.esq = inserir(x, no.esq);
		else 
			no.dir = inserir(x, no.dir);

		return no;
	}

	public void caminharCentral()
	{
		caminharCentral(raiz);
	}

	private void caminharCentral(No no)
	{
		if(no != null)
		{
			caminharCentral(no.esq);
			no.elemento.imprimir();
			caminharCentral(no.dir);
		}
	}
}


public class Main{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		String linha = scanner.nextLine();
		ArvoreBinaria ab = new ArvoreBinaria();

		while(!linha.equals("FIM"))
		{
			Carro carro = new Carro();
			carro.ler(linha);
			ab.inserir(carro);
			linha = scanner.nextLine();
		}
		ab.caminharCentral();
	}
}