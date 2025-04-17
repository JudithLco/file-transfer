# Safe-Transfer
Application Android, permettant d’échanger des fichiers entre appareils via un serveur.

## Spécifications

### Application Android
- Authentification utilisateur
- Sélection et envoi de fichiers depuis le stockage local
- Réception et gestion des fichiers entrants
- Visualisation des fichiers compatibles (images, PDF, texte)
- Historique des transferts effectués
- Gestion de la durée de vie des fichiers (suppression automatique)
- Chiffrement des fichiers avant envoi
- Déchiffrement des fichiers reçus

### Serveur Ktor
- API REST pour la gestion des transferts
- Stockage temporaire des fichiers
- Authentification et autorisation des requêtes
- Journalisation des opérations
- Nettoyage automatique des fichiers périmés

## Architecture

### Application Android
- **Langage**: Kotlin
- **Architecture**: MVVM
- **Libraries**:
  ? en cours de décision

### Serveur
- **Framework**: Ktor
- **Langage**: Kotlin
- **Base de données**: Ktorm (orm) avec PostgreSQL (pas fixé)

### Chiffrement
- A décider