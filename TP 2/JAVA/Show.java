
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

class Show {

    private String Show_ID;
    private String Type;
    private String Title;
    private String Director;
    private String[] Cast;
    private String Country;
    private Date Date_Added;
    private int Release_Year;
    private String Rating;
    private String Duration;
    private String[] Listed_In;

    // Construtor padrao
    public Show() {
        this.Show_ID = "NaN";
        this.Type = "NaN";
        this.Title = "NaN";
        this.Director = "NaN";
        this.Cast = new String[0];
        this.Country = "NaN";
        this.Date_Added = new Date(0);
        this.Release_Year = 0;
        this.Rating = "NaN";
        this.Duration = "NaN";
        this.Listed_In = new String[0];
    }

    // Construtor com parametros
    public Show(String Show_ID, String Type, String Title, String Director, String[] Cast, String Country, Date Date_Added, int Release_Year, String Rating, String Duration, String[] Listed_In) {
        this.Show_ID = Show_ID != null ? Show_ID : "NaN";
        this.Type = Type != null ? Type : "NaN";
        this.Title = Title != null ? Title : "NaN";
        this.Director = Director != null ? Director : "NaN";
        this.Cast = Cast != null ? Cast : new String[0];
        this.Country = Country != null ? Country : "NaN";
        this.Date_Added = Date_Added != null ? Date_Added : new Date(0);
        this.Release_Year = Release_Year > 0 ? Release_Year : 0;
        this.Rating = Rating != null ? Rating : "NaN";
        this.Duration = Duration != null ? Duration : "NaN";
        this.Listed_In = Listed_In != null ? Listed_In : new String[0];
    }

    // Getters e Setters
    public String getShow_ID() {
        return Show_ID;
    }

    public void setShow_ID(String Show_ID) {
        this.Show_ID = Show_ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String Director) {
        this.Director = Director;
    }

    public String[] getCast() {
        return Cast;
    }

    public void setCast(String[] Cast) {
        this.Cast = Cast;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public Date getDate_Added() {
        return Date_Added;
    }

    public void setDate_Added(Date Date_Added) {
        this.Date_Added = Date_Added;
    }

    public int getRelease_Year() {
        return Release_Year;
    }

    public void setRelease_Year(int Release_Year) {
        this.Release_Year = Release_Year;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String Duration) {
        this.Duration = Duration;
    }

    public String[] getListed_In() {
        return Listed_In;
    }

    public void setListed_In(String[] Listed_In) {
        this.Listed_In = Listed_In;
    }

    // Metodo escrever
    public String imprimir() {
        return (getShow_ID() + " ## " + getType() + " ## " + getTitle() + " ## " + getDirector() + " ## " + "[" + String.join(", ", getCast()) + "]" + " ## " + getCountry() + " ## " + getDate_Added() + " ## " + getRelease_Year() + " ## " + getRating() + " ## " + getDuration() + " ## " + "[" + String.join(", ", getListed_In()) + "]" + "##");
    }

    // Metodo Ler
    public void Ler(String linha) {
        try (BufferedReader br = new BufferedReader(new FileReader("/tmp/disneyplus.csv"))) {
            String line = br.readLine(); // Ignorar o cabeçalho
            while ((line = br.readLine()) != null) {
                String[] partes = linha.split(",");
                this.setShow_ID(partes[0]);
                this.setType(partes[1]);
                this.setTitle(partes[2] != null ? partes[2] : "NaN");
                this.setDirector(partes[3] != null ? partes[3] : "NaN");
                this.setCast(partes[4] != null ? partes[4].split(",") : new String[0]);
                this.setCountry(partes[5] != null ? partes[5] : "NaN");
                this.setDate_Added(partes[6] != null ? new Date(partes[6]) : new Date(0));
                this.setRelease_Year(!partes[7].isEmpty() ? Integer.parseInt(partes[7]) : 0);  
                this.setRating(partes[8] != null ? partes[8] : "NaN");
                this.setDuration(partes[9] != null ? partes[9] : "NaN");
                this.setListed_In(partes[10] != null ? partes[10].split(",") : new String[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } }

}
