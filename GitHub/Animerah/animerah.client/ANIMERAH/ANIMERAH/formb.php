<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'vendor/autoload.php';

if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST['email'])) {
    $email = $_POST['email'];

    $mail = new PHPMailer(true);

    try {
        // Paramètres du serveur SMTP
        $mail->isSMTP();
        $mail->Host = 'smtp.gmail.com';  // Utilisez le serveur SMTP de votre choix
        $mail->SMTPAuth = true;
        $mail->Username = 'justtuyi@gmail.com'; // Votre adresse email
        $mail->Password = ''; // Le mot de passe d'application
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
        $mail->Port = 587; // Le port utilisé par Gmail (587)

        // Expéditeur et destinataire
        $mail->setFrom('justtuyi@gmail.com', 'Votre nom');
        $mail->addAddress($email); // L'adresse email du destinataire

        // Contenu de l'email
        $mail->isHTML(true);
        $mail->Subject = 'Confirmation d\'abonnement';
        $mail->Body    = 'Merci de vous être inscrit à notre newsletter!';

        // Envoi de l'email
        if ($mail->send()) {
            echo json_encode(['status' => 'success', 'message' => 'Email envoyé avec succès.']);
        } else {
            echo json_encode(['status' => 'error', 'message' => 'Une erreur est survenue lors de l\'envoi de l\'email.']);
        }
    } catch (Exception $e) {
        echo json_encode(['status' => 'error', 'message' => 'Erreur de PHPMailer: ' . $mail->ErrorInfo]);
    }
} else {
    echo json_encode(['status' => 'error', 'message' => 'Email non fourni.']);
}
?>
