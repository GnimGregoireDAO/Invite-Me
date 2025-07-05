
public class Film {
  
    private int id;
  
    private String titre;
  
    private int annee;

    private int recettes;
  
    private Realisateur realisateur;

    public Film() {
    }

    public Film(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getRecettes() {
        return recettes;
    }

    public void setRecettes(int recettes) {
        this.recettes = recettes;
    }

    public Realisateur getRealisateur() {
        return realisateur;
    }

    public void setRealisateur(Realisateur realisateur) {
        this.realisateur = realisateur;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", annee=" + annee +
                ", recettes=" + recettes +
                ", realisateur=" + realisateur +
                '}';
    }
}
