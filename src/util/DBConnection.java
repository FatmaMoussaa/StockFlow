package util;

import java.io.FileInputStream;   // pour lire un fichier du disque
import java.io.IOException;       // erreur possible à la lecture
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;      // classe Java faite pour lire un fichier clé=valeur

public class DBConnection {

    // Nom du fichier qui contient URL, user et mot de passe
    private static final String CONFIG_FILE = "config.properties";

    // Chargé UNE seule fois au démarrage de l'application
    private static final Properties props = loadConfig();

    // Lit config.properties et retourne son contenu
    private static Properties loadConfig() {
        Properties p = new Properties();                         // objet vide
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) { // ouvre le fichier (fermé automatiquement)
            p.load(in);                                          // remplit p avec db.url, db.user, db.password
        } catch (IOException e) {                                // fichier absent ou illisible
            throw new RuntimeException(
                    "config.properties introuvable : copiez config.example.properties "
                            + "en config.properties et renseignez vos identifiants.", e);
        }
        return p;                                                // on renvoie les valeurs lues
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");              // charge le driver PostgreSQL
        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL introuvable !");
            e.printStackTrace();
        }
        return DriverManager.getConnection(
                props.getProperty("db.url"),                         // lu depuis le fichier
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }
}