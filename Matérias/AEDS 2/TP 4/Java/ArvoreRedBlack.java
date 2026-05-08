import java.io.*;

class Show {
   private String Show_ID;
   private String Type;
   private String Title;
   private String Director;
   private String[] Cast;
   private String Country;
   private String Date_Added;
   private int Release_Year;
   private String Rating;
   private String Duration;
   private String[] Listed_In;

   public Show() {
      this.Show_ID = "NaN";
      this.Type = "NaN";
      this.Title = "NaN";
      this.Director = "NaN";
      this.Cast = new String[0];
      this.Country = "NaN";
      this.Date_Added = "NaN";
      this.Release_Year = 0;
      this.Rating = "NaN";
      this.Duration = "NaN";
      this.Listed_In = new String[0];
   }

   public Show(String var1, String var2, String var3, String var4, String[] var5, String var6, String var7, int var8, String var9, String var10, String[] var11) {
      this.Show_ID = var1 != null ? var1 : "NaN";
      this.Type = var2 != null ? var2 : "NaN";
      this.Title = var3 != null ? var3 : "NaN";
      this.Director = var4 != null ? var4 : "NaN";
      this.Cast = var5 != null ? var5 : new String[0];
      this.Country = var6 != null ? var6 : "NaN";
      this.Date_Added = var7 != null ? var7 : "NaN";
      this.Release_Year = var8 > 0 ? var8 : 0;
      this.Rating = var9 != null ? var9 : "NaN";
      this.Duration = var10 != null ? var10 : "NaN";
      this.Listed_In = var11 != null ? var11 : new String[0];
      this.ordenarBubbleSort(this.Cast);
      this.ordenarBubbleSort(this.Listed_In);
   }

   public String getShow_ID() {
      return this.Show_ID;
   }

   public void setShow_ID(String var1) {
      this.Show_ID = var1;
   }

   public String getType() {
      return this.Type;
   }

   public void setType(String var1) {
      this.Type = var1;
   }

   public String getTitle() {
      return this.Title;
   }

   public void setTitle(String var1) {
      this.Title = var1;
   }

   public String getDirector() {
      return this.Director;
   }

   public void setDirector(String var1) {
      this.Director = var1;
   }

   public String[] getCast() {
      return this.Cast;
   }

   public void setCast(String[] var1) {
      this.Cast = var1;
      this.ordenarBubbleSort(this.Cast);
   }

   public String getCountry() {
      return this.Country;
   }

   public void setCountry(String var1) {
      this.Country = var1;
   }

   public String getDate_Added() {
      return this.Date_Added;
   }

   public void setDate_Added(String var1) {
      this.Date_Added = var1;
   }

   public int getRelease_Year() {
      return this.Release_Year;
   }

   public void setRelease_Year(int var1) {
      this.Release_Year = var1;
   }

   public String getRating() {
      return this.Rating;
   }

   public void setRating(String var1) {
      this.Rating = var1;
   }

   public String getDuration() {
      return this.Duration;
   }

   public void setDuration(String var1) {
      this.Duration = var1;
   }

   public String[] getListed_In() {
      return this.Listed_In;
   }

   public void setListed_In(String[] var1) {
      this.Listed_In = var1;
      this.ordenarBubbleSort(this.Listed_In);
   }

   public String imprimir() {
      String var1 = "[" + String.join(", ", this.removerEspacos(this.Cast)) + "]";
      String var2 = "[" + String.join(", ", this.removerEspacos(this.Listed_In)) + "]";
      String var10000 = this.getShow_ID().trim();
      return "=> " + var10000 + " ## " + this.getTitle().trim() + " ## " + this.getType().trim() + " ## " + this.getDirector().trim() + " ## " + var1 + " ## " + this.getCountry().trim() + " ## " + this.Date_Added.trim() + " ## " + this.getRelease_Year() + " ## " + this.getRating().trim() + " ## " + this.getDuration().trim() + " ## " + var2 + " ##";
   }

   private String[] removerEspacos(String[] var1) {
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = var1[var3].trim();
      }

      return var2;
   }

   public String clone() {
      Show var1 = new Show(this.Show_ID, this.Type, this.Title, this.Director, this.Cast, this.Country, this.Date_Added, this.Release_Year, this.Rating, this.Duration, this.Listed_In);
      return var1.imprimir();
   }

   public static Show[] Ler() {
      String var0 = "/tmp/disneyplus.csv";
      Show[] var1 = new Show[1368];
      int var2 = 0;

      try {
         BufferedReader var3 = new BufferedReader(new FileReader(var0));

         try {
            boolean var5 = true;

            String var4;
            while((var4 = var3.readLine()) != null && var2 < 1368) {
               if (var5) {
                  var5 = false;
               } else {
                  String[] var6 = var4.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     var6[var7] = var6[var7].replaceAll("^\"|\"$", "").trim();
                     if (var6[var7].isEmpty()) {
                        var6[var7] = "NaN";
                     }
                  }

                  String[] var14 = var6[4].split(",");
                  String[] var8 = var6[10].split(",");
                  Show var9 = new Show(var6[0], var6[1], var6[2], var6[3], var14, var6[5], var6[6], Integer.parseInt(var6[7]), var6[8], var6[9], var8);
                  var1[var2++] = var9;
               }
            }
         } catch (Throwable var11) {
            try {
               var3.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }

            throw var11;
         }

         var3.close();
      } catch (IOException var12) {
         System.err.println("Erro ao ler o arquivo: " + var12.getMessage());
      } catch (NumberFormatException var13) {
         System.err.println("Erro ao converter ano de lançamento: " + var13.getMessage());
      }

      return var1;
   }

   private void ordenarBubbleSort(String[] var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2 - 1; ++var3) {
         for(int var4 = 0; var4 < var2 - var3 - 1; ++var4) {
            String var5 = var1[var4].trim().toLowerCase();
            String var6 = var1[var4 + 1].trim().toLowerCase();
            if (var5.compareTo(var6) > 0) {
               String var7 = var1[var4];
               var1[var4] = var1[var4 + 1];
               var1[var4 + 1] = var7;
            }
         }
      }

   }

   public static String converterDataISO(String var0) {
      if (var0 != null && !var0.equals("NaN")) {
         try {
            String[] var1 = var0.split(" ");
            if (var1.length < 3) {
               return "0000-00-00";
            } else {
               int var2 = Integer.parseInt(var1[1].replace(",", ""));
               int var3 = Integer.parseInt(var1[2]);
               boolean var4 = false;
               byte var8;
               switch (var1[0]) {
                  case "January":
                     var8 = 1;
                     break;
                  case "February":
                     var8 = 2;
                     break;
                  case "March":
                     var8 = 3;
                     break;
                  case "April":
                     var8 = 4;
                     break;
                  case "May":
                     var8 = 5;
                     break;
                  case "June":
                     var8 = 6;
                     break;
                  case "July":
                     var8 = 7;
                     break;
                  case "August":
                     var8 = 8;
                     break;
                  case "September":
                     var8 = 9;
                     break;
                  case "October":
                     var8 = 10;
                     break;
                  case "November":
                     var8 = 11;
                     break;
                  case "December":
                     var8 = 12;
                     break;
                  default:
                     var8 = 0;
               }

               return String.format("%04d-%02d-%02d", var3, Integer.valueOf(var8), var2);
            }
         } catch (Exception var7) {
            return "0000-00-00";
         }
      } else {
         return "0000-00-00";
      }
   }
}

class NoAN {
    public Show show;
    public NoAN esq, dir;
    public boolean cor; // true = vermelho, false = preto

    public NoAN(Show show) {
        this(show, false);
    }

    public NoAN(Show show, boolean cor) {
        this.show = show;
        this.cor = cor;
        this.esq = this.dir = null;
    }
}

public class ArvoreRedBlack {
    private NoAN raiz;
    private int comparacoes = 0;

    public ArvoreRedBlack() {
        raiz = null;
    }

    public boolean pesquisar(String title) {
        StringBuilder caminho = new StringBuilder("raiz  ");
        boolean achou = pesquisar(title, raiz, caminho);
        System.out.println("=>" + caminho.append(achou ? " SIM" : " NAO").toString());
        return achou;
    }

    private boolean pesquisar(String title, NoAN i, StringBuilder caminho) {
        boolean resp;
        if (i == null) {
            resp = false;
        } else if (title.equals(i.show.getTitle())) {
            resp = true;
        } else if (title.compareTo(i.show.getTitle()) < 0) {
            caminho.append("esq ");
            resp = pesquisar(title, i.esq, caminho);
        } else {
            caminho.append("dir ");
            resp = pesquisar(title, i.dir, caminho);
        }
        return resp;
    }

    public void inserir(Show show) throws Exception {
        if (raiz == null) {
            raiz = new NoAN(show, false);
        } else if (raiz.esq == null && raiz.dir == null) {
            if (show.getTitle().compareTo(raiz.show.getTitle()) < 0) {
                raiz.esq = new NoAN(show, true);
            } else {
                raiz.dir = new NoAN(show, true);
            }
        } else if (raiz.esq == null) {
            if (show.getTitle().compareTo(raiz.show.getTitle()) < 0) {
                raiz.esq = new NoAN(show, true);
            } else if (show.getTitle().compareTo(raiz.dir.show.getTitle()) < 0) {
                raiz.esq = new NoAN(raiz.show, true);
                raiz.show = show;
            } else {
                raiz.esq = new NoAN(raiz.show, true);
                raiz.show = raiz.dir.show;
                raiz.dir.show = show;
            }
            raiz.esq.cor = raiz.dir.cor = false;
        } else if (raiz.dir == null) {
            if (show.getTitle().compareTo(raiz.show.getTitle()) > 0) {
                raiz.dir = new NoAN(show, true);
            } else if (show.getTitle().compareTo(raiz.esq.show.getTitle()) > 0) {
                raiz.dir = new NoAN(raiz.show, true);
                raiz.show = show;
            } else {
                raiz.dir = new NoAN(raiz.show, true);
                raiz.show = raiz.esq.show;
                raiz.esq.show = show;
            }
            raiz.esq.cor = raiz.dir.cor = false;
        } else {
            inserir(show, null, null, null, raiz);
        }
        raiz.cor = false;
    }

    private void balancear(NoAN bisavo, NoAN avo, NoAN pai, NoAN i) {
        if (pai != null && pai.cor) {
            // 4 tipos de reequilíbrio e acoplamento
            if (pai == avo.dir) {
                if (i == pai.dir) {
                    avo = rotacaoEsq(avo);
                } else {
                    avo = rotacaoDirEsq(avo);
                }
            } else {
                if (i == pai.esq) {
                    avo = rotacaoDir(avo);
                } else {
                    avo = rotacaoEsqDir(avo);
                }
            }
            if (bisavo == null) {
                raiz = avo;
            } else if (avo.show.getTitle().compareTo(bisavo.show.getTitle()) < 0) {
                bisavo.esq = avo;
            } else {
                bisavo.dir = avo;
            }
            avo.cor = false;
            if (avo.esq != null) avo.esq.cor = true;
            if (avo.dir != null) avo.dir.cor = true;
        }
    }

    private void inserir(Show show, NoAN bisavo, NoAN avo, NoAN pai, NoAN i) throws Exception {
        if (i == null) {
            if (show.getTitle().compareTo(pai.show.getTitle()) < 0) {
                i = pai.esq = new NoAN(show, true);
            } else {
                i = pai.dir = new NoAN(show, true);
            }
            if (pai.cor) {
                balancear(bisavo, avo, pai, i);
            }
        } else {
            // Achou um 4-no: é preciso fragmentá-lo e reequilibrar a árvore
            if (i.esq != null && i.dir != null && i.esq.cor && i.dir.cor) {
                i.cor = true;
                i.esq.cor = i.dir.cor = false;
                if (i == raiz) {
                    i.cor = false;
                } else if (pai != null && pai.cor) {
                    balancear(bisavo, avo, pai, i);
                }
            }
            if (show.getTitle().compareTo(i.show.getTitle()) < 0) {
                inserir(show, avo, pai, i, i.esq);
            } else if (show.getTitle().compareTo(i.show.getTitle()) > 0) {
                inserir(show, avo, pai, i, i.dir);
            } else {
                throw new Exception("Erro inserir (elemento repetido)!");
            }
        }
    }

    private NoAN rotacaoDir(NoAN no) {
        NoAN noEsq = no.esq;
        no.esq = noEsq.dir;
        noEsq.dir = no;
        return noEsq;
    }

    private NoAN rotacaoEsq(NoAN no) {
        NoAN noDir = no.dir;
        no.dir = noDir.esq;
        noDir.esq = no;
        return noDir;
    }

    private NoAN rotacaoDirEsq(NoAN no) {
        no.dir = rotacaoDir(no.dir);
        return rotacaoEsq(no);
    }

    private NoAN rotacaoEsqDir(NoAN no) {
        no.esq = rotacaoEsq(no.esq);
        return rotacaoDir(no);
    }

    public static void main(String[] args) throws Exception {
        ArvoreRedBlack arvore = new ArvoreRedBlack();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String linha;

        // Carregar todos os shows do CSV
        Show[] todosShows = Show.Ler();

        // Inserção por ID (sem HashMap)
        while (!(linha = br.readLine()).equals("FIM")) {
            Show show = null;
            for (int i = 0; i < todosShows.length && show == null; i++) {
                if (todosShows[i] != null && todosShows[i].getShow_ID().equals(linha)) {
                    show = todosShows[i];
                }
            }
            if (show != null) {
                arvore.inserir(show);
            }
        }

        // Pesquisa por título (sem ArrayList)
        long inicio = System.currentTimeMillis();
        while (!(linha = br.readLine()).equals("FIM")) {
            arvore.pesquisar(linha);
        }
        long fim = System.currentTimeMillis();

        // Log
        String matricula = "846713";
        double tempo = (fim - inicio) / 1000.0;
        PrintWriter log = new PrintWriter("846713_avinegra.txt");
        log.println(matricula + "\t" + tempo + "\t" + 0); // comparações não estão sendo contadas aqui
        log.close();
    }
}