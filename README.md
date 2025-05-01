# File Transfer
Une application  permettant d’échanger des fichiers entre appareils via un serveur.

## Cahier des charges
Objectif : Créer un système sécurisé d'échange de fichiers entre appareils via un serveur.

Fonctionnalités implémentées :
- Envoi/réception de fichiers via HTTP
- Gestion des fichiers temporaires (expiration/suppression automatique)
- Interface intuitive avec Jetpack Compose
- Chiffrement AES (en cours)

## Serveur (Ktor)
Un serveur simple a été mis en place. Il s'agit d'une API REST développée avec **Ktor** en **Kotlin**. Il permet l'upload et la récupération de fichiers, ils sont stockés localement et respectent la demande d'expiration/suppression.

### Endpoints

| Méthode HTTP | Endpoint           | Description |
|--------------|--------------------|-------------|
| `POST`       | `/upload`          | Reçoit un fichier en `multipart/form-data`, l’enregistre sur le disque, et stocke ses métadonnées (nom, hash, durée d’expiration, etc.). |
| `GET`        | `/listAll`         | Retourne la liste des fichiers stockés sur le serveur avec leurs métadonnées. |
| `GET`        | `/file/{id}`       | Permet de télécharger un fichier via son identifiant. Si `deleteAfterUse=true`, le fichier est supprimé après téléchargement.

- POST /upload
![image](https://github.com/user-attachments/assets/8aae11b1-dc70-45cf-aff3-73c48bdfcffd)

- Get /listAll
![image](https://github.com/user-attachments/assets/2bf2eaf3-120c-4c5a-af88-b5a049f3f697)

- Get /file/{id}
![image](https://github.com/user-attachments/assets/040988b6-9e74-4b18-b4b7-c10c291bd058)


## Client (Android)
L'application Android, réalisée avec **Ktor Client** et **Jetpack Compose** permet de communiquer avec le serveur.

- Maquettes initiales
![image](https://github.com/user-attachments/assets/c300b2ff-1cd3-443f-8931-74743f79b4c1)

- Rendu final

![image](https://github.com/user-attachments/assets/2f4ba247-2672-4dc5-b785-9afefe53c89c)
![image](https://github.com/user-attachments/assets/45a58c03-f44c-43b4-9a28-8787ab837583)
![image](https://github.com/user-attachments/assets/054bb891-9c52-4927-b3c5-18f6632d3a48)
![image](https://github.com/user-attachments/assets/e95708dc-f4cc-46a1-ac0a-881b42c1387f)
![image](https://github.com/user-attachments/assets/30fa056f-30d0-46d5-86ea-69b02f96af66)


## Demo
![Animation](https://github.com/user-attachments/assets/b37fcc48-7a5a-42a6-8f13-872fab8bdd2c)


# Lancement du projet
## Serveur
Avant tout vérifier que le port 8080 est disponible. Sinon, il est modifiable dans application.yaml
```
ktor:
  deployment:
    port: 8080
    host: 0.0.0.0
```
Lancer dans le terminal :
```
./gradlew run
```
Ou autrement
![image](https://github.com/user-attachments/assets/5bd434ae-2c02-4493-a231-f00ef0edbcd8)

## Application
Avec Android Studio et un téléphone connecté en mode développeur ou alors un émulateur.
Pour que l'application fonctionne il est absolument primordial de mettre à jour l'IP du serveur dans le fichier ApiService.kt.

```
 private val baseUrl = "http://VOTRE_IP_SERVEUR:8080"
```

Puis lancer l'application.

# Points d'amélioration
- chiffrement des fichiers (en cours)
- https/tls
- vérifications lors de l'upload d'un fichier (taille du fichier à limiter, nom, type réel)
- vérifications hash des fichiers
- ID des fichiers à revoir, peu user-friendly bien que compensé avec le copié-collé
- meilleur UI/UX
- un système d'authentification serait également intéressant à mettre en place
