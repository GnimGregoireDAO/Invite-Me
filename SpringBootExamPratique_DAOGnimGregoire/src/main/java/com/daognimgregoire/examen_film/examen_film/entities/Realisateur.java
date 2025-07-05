
public class Realisateur {

    private int id;
  
    private String nom;
 
    private int films;

    public Realisateur() {
    }

    public Realisateur(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Realisateur(String nom, int films) {
        this.nom = nom;
        this.films = films;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getFilms() {
        return films;
    }

    public void setFilms(int films) {
        this.films = films;
    }

    @Override
    public String toString() {
        return nom ;

    }
}
