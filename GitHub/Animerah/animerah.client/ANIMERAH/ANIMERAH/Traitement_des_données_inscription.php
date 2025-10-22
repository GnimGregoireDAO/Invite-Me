<?php
// Connexion à la base de données
try {
    $host = 'localhost';
    $dbname = 'mon_site'; //  base de données
    $username = 'root';   //  utilisateur MySQL
    $password = '';       //  mot de passe MySQL

    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Erreur : Connexion à la base de données échouée. " . $e->getMessage());
}

// Démarrer la session
session_start();

// Validation et traitement des données
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Vérifiez si les données proviennent de l'inscription ou de la connexion
    if (isset($_POST['nom'], $_POST['prenom'], $_POST['email'], $_POST['pass'], $_POST['password2'])) {
        // Traitement pour l'inscription
        $nom = htmlspecialchars($_POST['nom']);
        $prenom = htmlspecialchars($_POST['prenom']);
        $email = htmlspecialchars($_POST['email']);
        $password = htmlspecialchars($_POST['pass']);
        $password2 = htmlspecialchars($_POST['password2']);

        // Vérifications de base
        if (empty($nom) || empty($prenom) || empty($email) || empty($password) || empty($password2)) {
            die("Erreur : Tous les champs sont obligatoires.");
        }

        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            die("Erreur : L'adresse email n'est pas valide.");
        }

        if ($password !== $password2) {
            die("Erreur : Les mots de passe ne correspondent pas.");
        }

        // Hashage du mot de passe
        $passwordHashed = password_hash($password, PASSWORD_BCRYPT);

        // Insertion dans la base de données
        try {
            $stmt = $pdo->prepare("INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe) VALUES (:nom, :prenom, :email, :mot_de_passe)");
            $stmt->bindParam(':nom', $nom);
            $stmt->bindParam(':prenom', $prenom);
            $stmt->bindParam(':email', $email);
            $stmt->bindParam(':mot_de_passe', $passwordHashed);
            $stmt->execute();

            // Redirection après inscription réussie
            $_SESSION['success_message'] = "Inscription réussie ! Vous pouvez maintenant vous connecter.";
            header("Location: index.html");
            exit;
        } catch (PDOException $e) {
            die("Erreur : " . $e->getMessage());
        }
    } elseif (isset($_POST['username'], $_POST['pass'])) {
        // Traitement pour la connexion
        $email = htmlspecialchars($_POST['username']);
        $password = htmlspecialchars($_POST['pass']);

        // Vérifications de base
        if (empty($email) || empty($password)) {
            die("Erreur : Tous les champs sont obligatoires.");
        }

        // Requête pour récupérer l'utilisateur
        try {
            $stmt = $pdo->prepare("SELECT * FROM utilisateurs WHERE email = :email");
            $stmt->bindParam(':email', $email);
            $stmt->execute();

            $user = $stmt->fetch(PDO::FETCH_ASSOC);

            // Vérification du mot de passe
            if ($user && password_verify($password, $user['mot_de_passe'])) {
                // Démarrer une session pour l'utilisateur
                $_SESSION['user_id'] = $user['id'];
                $_SESSION['user_name'] = $user['nom'];
                $_SESSION['user_email'] = $user['email'];

                // Redirection vers une page sécurisée
                header("Location: dashboard.php");
                exit;
            } else {
                die("Erreur : Email ou mot de passe incorrect.");
            }
        } catch (PDOException $e) {
            die("Erreur : " . $e->getMessage());
        }
    } else {
        die("Erreur : Données non reconnues.");
    }
}
?>
