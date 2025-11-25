# Projet Microservices - Services Bancaires SOAP

## 📋 Description

Ce projet est une architecture microservices développée avec **JHipster** et **Spring Boot**, implémentant des services bancaires via des **web services SOAP**. Le projet comprend plusieurs microservices communiquant entre eux pour offrir des fonctionnalités bancaires complètes.

## 🏗️ Architecture

Le projet est composé de plusieurs microservices :

- **Gateway** : Point d'entrée principal de l'application (API Gateway)
- **MS1** : Microservice principal implémentant les services SOAP bancaires
- **MS2** : Microservice secondaire

### Technologies Utilisées

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Web Services** (SOAP)
- **JAXB** pour la génération des classes à partir du schéma XSD
- **JHipster 8.11.0**
- **Maven** pour la gestion des dépendances
- **H2 Database** (développement) / **MySQL** (production)
- **Eureka** pour la découverte de services
- **Keycloak** pour l'authentification OAuth2 (optionnel)

## 🎥 Démonstration Vidéo

Une vidéo de démonstration est disponible dans le dossier `video/` :
- **Fichier** : `video/ms1_ms2_avec_gateway_success.mp4`
- **Image** : `video/image.png`

> **Note** : Pour intégrer la vidéo dans le README GitHub, vous pouvez :
> - Utiliser un lien direct vers la vidéo si elle est hébergée en ligne
> - Utiliser une image cliquable : `[![Vidéo](video/image.png)](video/ms1_ms2_avec_gateway_success.mp4)`


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

## 📁 Structure du Projet

```
tp1/
├── gateway/          # API Gateway
├── ms1/              # Microservice 1 (Services SOAP)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/groupeisi/m2gl/
│   │   │   │       ├── web/
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
└── ms2/              # Microservice 2
```

## 🔧 Configuration

### Ports par défaut

- **MS1** : `8081`
- **MS2** : `8082`
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

## 📚 API REST (Bonus)

En plus des services SOAP, le microservice expose également des endpoints REST :

- `GET /api/v1/helloWorld` : Endpoint de test
- `GET /api/users` : Liste des utilisateurs publics
- `GET /api/books` : Gestion des livres (GraphQL également disponible)

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

- **Groupe ISI M2GL**

## 📞 Support

Pour toute question ou problème, veuillez ouvrir une issue sur le dépôt GitHub.

## 🔗 Repository GitHub

**Repository** : [https://github.com/maichou12/TP1_M2_WS](https://github.com/maichou12/TP1_M2_WS)

Ce dépôt contient l'ensemble de l'architecture microservices :
- ✅ Gateway (API Gateway)
- ✅ MS1 (Services SOAP)
- ✅ MS2 (Microservice secondaire)
- ✅ Documentation complète
- ✅ Vidéo de démonstration

---

**Dernière mise à jour** : Novembre 2025

