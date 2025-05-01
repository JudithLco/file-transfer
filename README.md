# File Transfer
Une application simple, permettant d’échanger des fichiers entre appareils via un serveur.

## Cahier des charges
- L'application doit permettre d'envoyer et de recevoir des fichiers via un serveur.
- Le serveur doit permettre la transmission des fichiers via HTTP.
- L'application doit gérer correctement les fichiers reçus, possibilité de sauvegarder temporairement ou de supprimer le fichier après sont utilisation.
- Interface utilisateur simple mais intuitive.
- Facultatif : chiffrement des fichiers avant leur envoi.

## Serveur (Ktor)
Un serveur simple a été mis en place. Il s'agit d'une API REST développée avec **Ktor** en **Kotlin**. Il permet l'upload et la récupération de fichiers, ils sont stockés localement et respectent la demande d'expiration/suppression.

### Endpoints

| Méthode HTTP | Endpoint           | Description |
|--------------|--------------------|-------------|
| `POST`       | `/upload`          | Reçoit un fichier en `multipart/form-data`, l’enregistre sur le disque, et stocke ses métadonnées (nom, hash, durée d’expiration, etc.). |
| `GET`        | `/listAll`         | Retourne la liste des fichiers stockés sur le serveur avec leurs métadonnées. |
| `GET`        | `/file/{id}`       | Permet de télécharger un fichier via son identifiant. Si `deleteAfterUse=true`, le fichier est supprimé après téléchargement. 

## Client (Android)
L'application Android, réalisée avec **Ktor Client** et **Jetpack Compose** permet de communiquer avec le serveur.

![image](https://github.com/user-attachments/assets/c300b2ff-1cd3-443f-8931-74743f79b4c1)
