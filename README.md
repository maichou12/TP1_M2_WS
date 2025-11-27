# Projet Microservices - Architecture Multi-Protocoles

## 📋 Description

Ce projet est une architecture microservices développée avec **JHipster** et **Spring Boot**, implémentant différents protocoles de communication : **REST**, **SOAP**, **GraphQL**, **WebSocket** et **gRPC**. Le projet comprend plusieurs microservices communiquant entre eux pour démontrer l'utilisation de différents styles d'architecture et protocoles de communication.

## 🏗️ Architecture

Le projet est composé de plusieurs microservices :

- **Gateway** : Point d'entrée principal de l'application (API Gateway)
- **MS1** : Microservice implémentant **REST** (gestion des livres), **SOAP** (services bancaires) et **GraphQL** (gestion des livres)
- **MS2** : Microservice implémentant **REST** uniquement (endpoints de base, pas de gestion de livres)
- **MS3** : Microservice implémentant **WebSocket** uniquement (gestion des livres en temps réel)
- **MS4** : Microservice implémentant **gRPC** uniquement (gestion des livres)

### Technologies Utilisées

- **Java 17**
- **Spring Boot 3.4.5**
- **REST API** - MS1 (gestion des livres), MS2 (endpoints de base)
- **Spring Web Services** (SOAP) - MS1 (services bancaires)
- **GraphQL** - MS1 uniquement (gestion des livres)
- **WebSocket** (STOMP) - MS3 uniquement (gestion des livres en temps réel)
- **gRPC** - MS4 uniquement (gestion des livres)
- **JAXB** pour la génération des classes à partir du schéma XSD
- **Protobuf** pour la définition des messages gRPC
- **JHipster 8.11.0**
- **Maven** pour la gestion des dépendances
- **H2 Database** (développement) / **MySQL** (production)
- **Eureka** pour la découverte de services
- **Keycloak** pour l'authentification OAuth2 (optionnel)

## 🎥 Démonstrations Vidéo

Plusieurs vidéos de démonstration sont disponibles dans le dossier `video/` :

### 1. Démonstration SOAP/REST avec Gateway
- **Fichier** : `video/ms1_ms2_avec_gateway_success.mp4`
- **Description** : Démonstration de l'architecture microservices avec MS1, MS2 et la Gateway, incluant les services SOAP et REST.

### 2. Démonstration gRPC (MS4)
- **Fichier** : `video/Book_GRPC.mp4`
- **Description** : Démonstration complète du microservice MS4 implémentant gRPC pour la gestion des livres (CRUD).
- **Fonctionnalités démontrées** :
  - Connexion au serveur gRPC (port 9090)
  - Création d'un livre via gRPC
  - Récupération d'un livre par ID
  - Récupération de tous les livres
  - Mise à jour d'un livre
  - Suppression d'un livre
- **Outils utilisés** : Postman (support gRPC natif)

### 3. Démonstration WebSocket (MS3)
- **Fichier** : `video/Websocket_tp.mp4`
- **Description** : Démonstration du microservice MS3 implémentant uniquement WebSocket (STOMP) pour la gestion des livres en temps réel.
- **Fonctionnalités démontrées** :
  - Connexion WebSocket via l'endpoint `/ws-simple`
  - Création d'un livre en temps réel
  - Récupération d'un livre par ID
  - Récupération de tous les livres (liste complète dans la réponse JSON)
  - Mise à jour d'un livre
  - Suppression d'un livre
- **Outils utilisés** : Postman (WebSocket)
- **Note** : MS3 n'implémente que WebSocket, pas de GraphQL ni de REST pour la gestion des livres

## 🚀 Démarrage Rapide

### Prérequis

- Java 17 ou supérieur
- Maven 3.2.5 ou supérieur
- Docker et Docker Compose (pour les services externes)
- Node.js et npm (pour les outils de développement)

### Installation

1. **Cloner le projet**
   ```bash
   git clone https://github.com/maichou12/TP1_M2_WS.git
   cd TP1_M2_WS
   ```

2. **Démarrer les services externes (optionnel)**
   ```bash
   # Démarrer Keycloak (pour l'authentification)
   docker compose -f ms1/src/main/docker/keycloak.yml up -d
   
   # Démarrer MySQL (pour la production)
   docker compose -f ms1/src/main/docker/mysql.yml up -d
   ```

3. **Compiler et démarrer MS1**
   ```bash
   cd ms1
   ./mvnw clean compile
   ./mvnw spring-boot:run
   ```

L'application sera accessible sur `http://localhost:8081`

## 📡 Services SOAP

### Endpoint WSDL

Le WSDL des services SOAP est disponible à :
```
http://localhost:8081/ws/banque.wsdl
```

### Services Disponibles

#### 1. **getSolde** - Récupérer le solde d'un client

**Requête SOAP** :
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <getSoldeRequest xmlns="http://www.isi.com/banque">
         <tel>22112345678</tel>
      </getSoldeRequest>
   </soap:Body>
</soap:Envelope>
```

**Réponse** :
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <SoldeResponse xmlns="http://www.isi.com/banque">
         <solde>50000</solde>
      </SoldeResponse>
   </soap:Body>
</soap:Envelope>
```

#### 2. **getRole** - Récupérer le rôle d'un client

**Requête SOAP** :
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <getRoleRequest xmlns="http://www.isi.com/banque">
         <tel>22112345678</tel>
      </getRoleRequest>
   </soap:Body>
</soap:Envelope>
```

**Réponse** :
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <RoleResponse xmlns="http://www.isi.com/banque">
         <role>CLIENT</role>
      </RoleResponse>
   </soap:Body>
</soap:Envelope>
```

### Schéma XSD

Le schéma XSD est défini dans `ms1/src/main/resources/schema/schema.xsd` et définit :
- `getSoldeRequest` / `SoldeResponse`
- `getRoleRequest` / `RoleResponse`
- `addClientRequest` / `ClientResponse`
- `addTransferRequest` / `TransferResponse`
- `addPaymentRequest` / `PaymentResponse`

## 🧪 Tester les Services SOAP

### Avec SoapUI ou Postman

1. Importez le WSDL : `http://localhost:8081/ws/banque.wsdl`
2. Créez une nouvelle requête SOAP
3. Utilisez les exemples de requêtes ci-dessus

### Avec cURL

```bash
# Test getSolde
curl -X POST http://localhost:8081/ws \
  -H "Content-Type: text/xml;charset=UTF-8" \
  -H "SOAPAction: \"http://www.isi.com/banque/getSoldeRequest\"" \
  -d '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
         <soap:Body>
            <getSoldeRequest xmlns="http://www.isi.com/banque">
               <tel>22112345678</tel>
            </getSoldeRequest>
         </soap:Body>
      </soap:Envelope>'
```

### Données de Test

Le service `ClientService` contient des données de test :
- **Téléphone** : `22112345678` → Solde : `50000`, Rôle : `CLIENT`
- **Téléphone** : `22112345679` → Solde : `100000`, Rôle : `VIP`
- **Téléphone** : `22112345680` → Solde : `25000`, Rôle : `CLIENT`

## 🧪 Tester les Services gRPC (MS4)

### Avec Postman

1. **Démarrer MS4** :
   ```bash
   cd ms4
   ./mvnw spring-boot:run
   ```

2. **Dans Postman** :
   - Créez une nouvelle requête gRPC
   - URL : `localhost:9090`
   - Service : `BookService`
   - Importez le fichier proto : `ms4/src/main/proto/book.proto`

3. **Exemples de requêtes** :
   - **GetBook** : `{ "id": 1500 }`
   - **GetAllBooks** : `{}`
   - **CreateBook** : `{ "title": "L'Étranger", "prix": 9.50, "author": "Albert Camus", "date_pub": "1942-06-15" }`
   - **UpdateBook** : `{ "id": 1500, "title": "L'Étranger", "prix": 10.00, "author": "Albert Camus", "date_pub": "1942-06-15" }`
   - **DeleteBook** : `{ "id": 1500 }`

## 🧪 Tester les Services WebSocket (MS3)

### Avec Postman

1. **Démarrer MS3** :
   ```bash
   cd ms3
   ./mvnw spring-boot:run
   ```

2. **Dans Postman** :
   - Créez une nouvelle connexion WebSocket
   - URL : `ws://localhost:8083/ws-simple`
   - Cliquez sur "Connect"

3. **Exemples de messages JSON** :
   ```json
   // Créer un livre
   {
     "action": "CREATE",
     "title": "L'Étranger",
     "prix": 9.50,
     "author": "Albert Camus",
     "datePub": "1942-06-15"
   }
   
   // Récupérer tous les livres
   {
     "action": "GET_ALL"
   }
   
   // Récupérer un livre par ID
   {
     "action": "GET",
     "id": 1500
   }
   
   // Mettre à jour un livre
   {
     "action": "UPDATE",
     "id": 1500,
     "title": "L'Étranger (Édition revue)",
     "prix": 10.00,
     "author": "Albert Camus",
     "datePub": "1942-06-15"
   }
   
   // Supprimer un livre
   {
     "action": "DELETE",
     "id": 1500
   }
   ```

## 📁 Structure du Projet

```
tp1/
├── gateway/          # API Gateway
├── ms1/              # Microservice 1 (REST, SOAP, GraphQL)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/groupeisi/m2gl/
│   │   │   │       ├── web/
│   │   │   │       │   ├── rest/
│   │   │   │       │   │   └── BookResource.java
│   │   │   │       │   └── soap/
│   │   │   │       │       └── BanqueEndpoint.java
│   │   │   │       ├── service/
│   │   │   │       │   └── ClientService.java
│   │   │   │       └── config/
│   │   │   │           └── WebServiceConfig.java
│   │   │   └── resources/
│   │   │       └── schema/
│   │   │           └── schema.xsd
│   │   └── test/
│   └── pom.xml
├── ms2/              # Microservice 2 (REST)
├── ms3/              # Microservice 3 (WebSocket)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/groupeisi/m2gl/
│   │   │   │       ├── websocket/
│   │   │   │       │   ├── BookWebSocketController.java
│   │   │   │       │   ├── SimpleWebSocketHandler.java
│   │   │   │       │   └── dto/
│   │   │   │       │       ├── BookMessage.java
│   │   │   │       │       └── BookListResponse.java
│   │   │   │       └── config/
│   │   │   │           ├── WebSocketConfig.java
│   │   │   │           └── SimpleWebSocketConfig.java
│   │   │   └── resources/
│   │   └── test/
│   ├── GUIDE_POSTMAN_WEBSOCKET.md
│   └── pom.xml
├── ms4/              # Microservice 4 (gRPC)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/groupeisi/m2gl/
│   │   │   │       └── grpc/
│   │   │   │           └── BookGrpcService.java
│   │   │   ├── proto/
│   │   │   │   └── book.proto
│   │   │   └── resources/
│   │   └── test/
│   ├── TEST_GRPC.md
│   └── pom.xml
└── video/             # Vidéos de démonstration
    ├── ms1_ms2_avec_gateway_success.mp4
    ├── Book_GRPC.mp4
    ├── Websocket_tp.mp4
    └── image.png
```

## 🔧 Configuration

### Ports par défaut

- **MS1** : `8081` (REST, SOAP, GraphQL)
- **MS2** : `8082` (REST)
- **MS3** : `8083` (WebSocket)
- **MS4** : `8084` (HTTP), `9090` (gRPC)
- **Gateway** : `8080`
- **Eureka** : `8761`
- **Keycloak** : `9080`

### Fichiers de configuration

- `ms1/src/main/resources/config/application.yml` : Configuration principale
- `ms1/src/main/resources/config/application-dev.yml` : Configuration développement
- `ms1/src/main/resources/config/application-prod.yml` : Configuration production

## 🛠️ Développement

### Compiler le projet

```bash
cd ms1
./mvnw clean compile
```

### Générer les classes JAXB

Les classes JAXB sont générées automatiquement lors de la compilation via le plugin `jaxb2-maven-plugin`. Elles sont créées dans :
```
ms1/target/generated-sources/jaxb/com/groupeisi/m2gl/entities/
```

### Exécuter les tests

```bash
./mvnw test
```

### Lancer en mode développement

```bash
./mvnw spring-boot:run
```

## 📚 APIs Disponibles

### MS1 - REST, SOAP et GraphQL
- **REST** : 
  - `GET /api/v1/helloWorld` : Endpoint de test
  - `GET /api/users` : Liste des utilisateurs publics
  - `GET /api/books` : Gestion complète des livres (CRUD)
- **SOAP** : Services bancaires (voir section Services SOAP)
  - WSDL : `http://localhost:8081/ws/banque.wsdl`
  - Endpoints : `getSolde`, `getRole`, `addClient`, `addTransfer`, `addPayment`
- **GraphQL** : 
  - Endpoint : `/graphql`
  - Queries : `allBooks`, `book(id)`
  - Mutations : `createBook`, `updateBook`, `deleteBook`

### MS2 - REST uniquement
- **REST** : 
  - `GET /api/v1/helloWorld` : Endpoint de test
  - `GET /api/users` : Liste des utilisateurs publics
- **Note** : MS2 n'implémente pas la gestion des livres, seulement des endpoints de base

### MS3 - WebSocket uniquement
- **WebSocket** : Gestion des livres en temps réel
  - Endpoint STOMP : `ws://localhost:8083/ws`
  - Endpoint simple (Postman) : `ws://localhost:8083/ws-simple`
  - Actions : `CREATE`, `GET`, `GET_ALL`, `UPDATE`, `DELETE`

### MS4 - gRPC uniquement
- **gRPC Server** : `localhost:9090`
- **Service** : `BookService` (CRUD complet)
  - Méthodes : `GetBook`, `GetAllBooks`, `CreateBook`, `UpdateBook`, `DeleteBook`
- **Documentation** : Voir `ms4/TEST_GRPC.md`

## 🔐 Sécurité

Par défaut, l'application est configurée pour fonctionner **sans authentification** pour faciliter les tests des services SOAP. Pour activer l'authentification OAuth2 :

1. Démarrez Keycloak
2. Décommentez la configuration OAuth2 dans `SecurityConfiguration.java`
3. Configurez les règles d'authentification selon vos besoins

## 📝 Notes Importantes

- Les services SOAP sont accessibles **sans authentification** sur `/ws/**`
- Les données clients sont stockées en mémoire (dans `ClientService`)
- Pour la production, il faudra implémenter une persistance en base de données
- Le schéma XSD peut être modifié, les classes JAXB seront régénérées automatiquement


## 📄 Licence

Ce projet est un projet académique développé dans le cadre du Master 2 GL.

## 👥 Auteurs

- **Maïmouna SARR**
- **Groupe ISI M2GL**

## 📞 Support

Pour toute question ou problème, veuillez ouvrir une issue sur le dépôt GitHub.

## 🔗 Repository GitHub

**Repository** : [https://github.com/maichou12/TP1_M2_WS](https://github.com/maichou12/TP1_M2_WS)

Ce dépôt contient l'ensemble de l'architecture microservices :
- ✅ Gateway (API Gateway)
- ✅ MS1 (REST avec gestion des livres, SOAP services bancaires, GraphQL)
- ✅ MS2 (REST endpoints de base uniquement)
- ✅ MS3 (WebSocket pour gestion des livres en temps réel)
- ✅ MS4 (gRPC pour gestion des livres)
- ✅ Documentation complète
- ✅ Vidéos de démonstration (SOAP/REST, gRPC, WebSocket)

---

**Dernière mise à jour** : Novembre 2025

