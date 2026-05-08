import java.util.Scanner;

// Classe auxiliar que representa uma célula da matriz
class Celula {
    public int elemento;
    public Celula direita, abaixo;
    
    public Celula() {
        this.elemento = 0;
        this.direita = null;
        this.abaixo = null;
    }
}

// Classe Matriz dinâmica
class Matriz {
    private Celula inicio; // referência para o canto superior esquerdo
    private int linha, coluna;
    
    // Construtor padrão (3x3)
    public Matriz() {
        this(3, 3);
    }
    
    // Construtor que aloca a matriz dinamicamente, com "linha" linhas e "coluna" colunas
    public Matriz(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        // Para facilitar a montagem, alocamos primeiro um array 2D temporário de células:
        Celula[][] cells = new Celula[linha][coluna];
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                cells[i][j] = new Celula();
            }
        }
        // Agora, atualizamos os ponteiros: para cada célula, se houver à direita ou abaixo, encaminhar.
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                if (j < coluna - 1)
                    cells[i][j].direita = cells[i][j + 1];
                if (i < linha - 1)
                    cells[i][j].abaixo = cells[i + 1][j];
            }
        }
        // A célula de início é a do canto superior esquerdo.
        inicio = cells[0][0];
    }
    
    // Métodos privados para acessar/modificar um elemento na posição (i,j)
    private int getElemento(int i, int j) {
        Celula p = inicio;
        for (int r = 0; r < i; r++) {
            p = p.abaixo;
        }
        for (int c = 0; c < j; c++) {
            p = p.direita;
        }
        return p.elemento;
    }
    
    private void setElemento(int i, int j, int valor) {
        Celula p = inicio;
        for (int r = 0; r < i; r++) {
            p = p.abaixo;
        }
        for (int c = 0; c < j; c++) {
            p = p.direita;
        }
        p.elemento = valor;
    }
    
    // Método que retorna a matriz-soma (elemento a elemento) desta matriz com outra
    public Matriz soma(Matriz m) {
        if(this.linha != m.linha || this.coluna != m.coluna)
            return null; // dimensões incompatíveis
        Matriz resp = new Matriz(this.linha, this.coluna);
        for (int i = 0; i < this.linha; i++) {
            for (int j = 0; j < this.coluna; j++) {
                int s = this.getElemento(i, j) + m.getElemento(i, j);
                resp.setElemento(i, j, s);
            }
        }
        return resp;
    }
    
    // Método que retorna a multiplicação desta matriz com outra.
    // É válida se o número de colunas desta matriz for igual ao número de linhas da outra.
    public Matriz multiplicacao(Matriz m) {
        if(this.coluna != m.linha)
            return null; // multiplicação inválida
        Matriz resp = new Matriz(this.linha, m.coluna);
        for (int i = 0; i < this.linha; i++) {
            for (int j = 0; j < m.coluna; j++) {
                int soma = 0;
                for (int k = 0; k < this.coluna; k++) {
                    soma += this.getElemento(i, k) * m.getElemento(k, j);
                }
                resp.setElemento(i, j, soma);
            }
        }
        return resp;
    }
    
    // Retorna true se a matriz for quadrada
    public boolean isQuadrada() {
        return (linha == coluna);
    }
    
    // Exibe a diagonal principal (do canto superior esquerdo ao inferior direito)
    public void mostrarDiagonalPrincipal() {
        if(isQuadrada()) {
            for (int i = 0; i < linha; i++) {
                System.out.print(getElemento(i, i));
                if(i < linha - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }
    }
    
    // Exibe a diagonal secundária (do canto superior direito ao inferior esquerdo)
    public void mostrarDiagonalSecundaria() {
        if(isQuadrada()) {
            for (int i = 0; i < linha; i++) {
                System.out.print(getElemento(i, coluna - i - 1));
                if(i < linha - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }
    }
    
    // Exibe toda a matriz, uma linha por vez
    public void mostrar() {
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                System.out.print(getElemento(i, j));
                if(j < coluna - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }
    }
    
    // Método estático para ler uma matriz da entrada padrão.
    // Lê primeiro dois inteiros (número de linhas e colunas), depois os elementos (linha a linha)
    public static Matriz lerMatriz(Scanner sc) {
        int l = sc.nextInt();
        int c = sc.nextInt();
        Matriz m = new Matriz(l, c);
        for (int i = 0; i < l; i++){
            for (int j = 0; j < c; j++){
                int valor = sc.nextInt();
                m.setElemento(i, j, valor);
            }
        }
        return m;
    }
}

// Classe principal com a main
public class TP03Q11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Número de casos de teste
        int casos = sc.nextInt();
        for (int t = 0; t < casos; t++) {
            // Lê a primeira matriz
            Matriz m1 = Matriz.lerMatriz(sc);
            // Lê a segunda matriz
            Matriz m2 = Matriz.lerMatriz(sc);
            
            // Exibe as diagonais da primeira matriz
            m1.mostrarDiagonalPrincipal();
            m1.mostrarDiagonalSecundaria();
            
            // Calcula e exibe a matriz soma
            Matriz soma = m1.soma(m2);
            if (soma != null)
                soma.mostrar();
            
            // Calcula e exibe a matriz multiplicação
            Matriz mult = m1.multiplicacao(m2);
            if (mult != null)
                mult.mostrar();
        }
        sc.close();
    }
}