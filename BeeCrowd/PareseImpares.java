
import java.util.Scanner;

public class PareseImpares {

    private static void quicksort(int[] array, int esq, int dir) {
        int i = esq, j = dir;
        int pivo = array[(dir+esq)/2];
        while (i <= j) {
            while (array[i] < pivo) i++;
            while (array[j] > pivo) j--;
            if (i <= j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (esq < j)  quicksort(array,esq, j);
        if (i < dir)  quicksort(array, i, dir);
    }

    private static void quicksortDecrescente(int[] array, int esq, int dir) {
        int i = esq, j = dir;
        int pivo = array[(dir+esq)/2];
        while (i <= j) {
            while (array[i] > pivo) i++;
            while (array[j] < pivo) j--;
            if (i <= j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (esq < j) quicksortDecrescente(array, esq, j);
        if (i < dir) quicksortDecrescente(array, i, dir);
    }


    public static void swap(int[] array, int i, int j)
    {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int l = 0, k = 0;
        int num;
        int[] pares = new int[N];
        int[] impares = new int[N];
        for(int i = 0; i < N; i++)
        {
            num = scanner.nextInt();
            if(num % 2 == 0)
            {
                pares[l] = num;
                l++;
            }
            else
            {
                impares[k] = num;
                k++;
            }
        }
        // ordenar pares 
        quicksort(pares, 0, l -1);

        // Escrever pares ordenados
        for(int i = 0 ; i < l; i++)
        {
            System.out.println(pares[i]);
        }


        // ordenar impares 
        quicksortDecrescente(impares, 0, k-1);
        
        // Escrever impares ordenados
        for(int i = 0 ; i < k; i++)
        {
            System.out.println(impares[i]);
        }
    }
}