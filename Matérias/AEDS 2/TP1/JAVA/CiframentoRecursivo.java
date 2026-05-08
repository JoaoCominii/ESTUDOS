

/**
 * A classe Ciframento escreve uma mensagem criptografada de forma recursiva.
 * Lê palavras da entrada padrão usando a classe MyIO, aplica um ciframento de César ou seja +3 na tabela ASCII.
 * O programa finaliza quando é digitado "FIM".
 */
class CiframentoRecursivo 
{
        public static String CiframentoRec (String palavra)
        {
          return CiframentoRec(palavra, 0);
        }

        public static String CiframentoRec (String palavra, int i)
        {
          if(i == palavra.length())
            return "";
          else{
            char cifrado = (char) (palavra.charAt(i) + 3);
            return cifrado + CiframentoRec(palavra, i+1); 
          }
        }
        public static void main (String[] args){
        String palavra;
        do{
            palavra = MyIO.readLine();
        if(palavra.equals("FIM")) break;
          MyIO.println(CiframentoRec(palavra));
        } while(!palavra.equals("FIM"));
}
}