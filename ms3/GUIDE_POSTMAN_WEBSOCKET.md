# Guide de test WebSocket avec Postman - MS3

## Configuration

- **URL WebSocket STOMP** : `ws://localhost:8083/ws` (pour clients STOMP comme le client HTML)
- **URL WebSocket Simple** : `ws://localhost:8083/ws-simple` (pour Postman - JSON brut)
- **Port HTTP** : 8083

⚠️ **IMPORTANT** : Pour Postman, utilisez `/ws-simple` car Postman envoie du JSON brut, pas des messages STOMP.

## Étapes pour tester avec Postman

### 1. Créer une requête WebSocket

1. Ouvrez Postman
2. Cliquez sur **New** → **WebSocket Request**
3. Dans le champ URL, entrez : `ws://localhost:8083/ws-simple` ⚠️ **Utilisez `/ws-simple` et non `/ws`**
4. Cliquez sur **Connect**

**Pourquoi `/ws-simple` ?**
- `/ws` utilise STOMP et nécessite des messages formatés STOMP
- `/ws-simple` accepte du JSON brut directement, parfait pour Postman

### 2. Vérifier la connexion

Une fois connecté, vous devriez voir :
- Le statut passe à **Connected** (vert)
- Un message de connexion dans la console

### 3. S'abonner aux topics (optionnel mais recommandé)

Avant d'envoyer des messages, vous pouvez vous abonner aux topics pour recevoir les réponses :

**Pour s'abonner, envoyez ces messages STOMP :**

#### S'abonner à `/topic/books` (notifications générales)
```
SUBSCRIBE
id:sub-1
destination:/topic/books

```

#### S'abonner à `/topic/book` (réponses pour un livre spécifique)
```
SUBSCRIBE
id:sub-2
destination:/topic/book

```

#### S'abonner à `/topic/books/list` (liste de tous les livres)
```
SUBSCRIBE
id:sub-3
destination:/topic/books/list

```

**Note** : Postman peut ne pas supporter directement STOMP. Dans ce cas, utilisez le client HTML fourni ou testez directement en envoyant des messages JSON.

### 4. Envoyer des messages JSON

Postman supporte l'envoi de messages JSON directement. Voici les exemples :

#### 4.1. Créer un livre

**Message à envoyer :**
```json
{
  "action": "CREATE",
  "title": "L'Étranger",
  "prix": 9.50,
  "author": "Albert Camus",
  "datePub": "1942-06-15"
}
```

**Réponse attendue :**
```json
{
  "action": "CREATE",
  "id": 1500,
  "title": "L'Étranger",
  "prix": 9.50,
  "author": "Albert Camus",
  "datePub": "1942-06-15",
  "success": true,
  "message": "Book created successfully"
}
```

#### 4.2. Récupérer un livre par ID

**Message à envoyer :**
```json
{
  "action": "GET",
  "id": 1500
}
```

**Réponse attendue :**
```json
{
  "action": "GET",
  "id": 1500,
  "title": "L'Étranger",
  "prix": 9.50,
  "author": "Albert Camus",
  "datePub": "1942-06-15",
  "success": true
}
```

#### 4.3. Récupérer tous les livres

**Message à envoyer :**
```json
{
  "action": "GET_ALL"
}
```

**Réponse attendue :**
```json
{
  "action": "GET_ALL",
  "success": true,
  "message": "Found 2 book(s)",
  "count": 2,
  "books": [
    {
      "action": "BOOK",
      "id": 1500,
      "title": "L'Étranger",
      "prix": 9.50,
      "author": "Albert Camus",
      "datePub": "1942-06-15"
    },
    {
      "action": "BOOK",
      "id": 1501,
      "title": "Le Petit Prince",
      "prix": 12.50,
      "author": "Antoine de Saint-Exupéry",
      "datePub": "1943-04-06"
    }
  ]
}
```

**Où voir la liste ?** 
La liste complète des livres est directement dans la réponse JSON, dans le champ `books`. Vous n'avez pas besoin d'aller sur un topic séparé - tout est dans la réponse que vous recevez dans Postman.

#### 4.4. Mettre à jour un livre

**Message à envoyer :**
```json
{
  "action": "UPDATE",
  "id": 1500,
  "title": "Le Petit Prince",
  "prix": 12.50,
  "author": "Antoine de Saint-Exupéry",
  "datePub": "1943-04-06"
}
```

**Réponse attendue :**
```json
{
  "action": "UPDATE",
  "id": 1500,
  "title": "Le Petit Prince",
  "prix": 12.50,
  "author": "Antoine de Saint-Exupéry",
  "datePub": "1943-04-06",
  "success": true,
  "message": "Book updated successfully"
}
```

#### 4.5. Supprimer un livre

**Message à envoyer :**
```json
{
  "action": "DELETE",
  "id": 1500
}
```

**Réponse attendue :**
```json
{
  "action": "DELETE",
  "id": 1500,
  "success": true,
  "message": "Book deleted successfully"
}
```

## ⚠️ Important : Format des messages

### Structure du message

Tous les messages doivent suivre cette structure :

```json
{
  "action": "CREATE|GET|GET_ALL|UPDATE|DELETE",
  "id": 1500,                    // Requis pour GET, UPDATE, DELETE
  "title": "Titre",              // Requis pour CREATE, UPDATE
  "prix": 12.50,                 // Requis pour CREATE, UPDATE
  "author": "Auteur",            // Requis pour CREATE, UPDATE
  "datePub": "1943-04-06"       // Requis pour CREATE, UPDATE (format ISO: YYYY-MM-DD)
}
```

### Actions disponibles

- **CREATE** : Créer un nouveau livre
  - Requis : `title`, `prix`, `author`, `datePub`
  
- **GET** : Récupérer un livre par ID
  - Requis : `id`
  
- **GET_ALL** : Récupérer tous les livres
  - Aucun champ requis
  
- **UPDATE** : Mettre à jour un livre
  - Requis : `id`, `title`, `prix`, `author`, `datePub`
  
- **DELETE** : Supprimer un livre
  - Requis : `id`

## Limitations de Postman avec WebSocket

Postman supporte WebSocket mais **ne supporte pas directement STOMP**. Pour une meilleure expérience :

1. **Utilisez le client HTML** fourni dans `TEST_WEBSOCKET.md`
2. **Utilisez wscat** (ligne de commande)
3. **Utilisez un client STOMP** comme StompJS dans un navigateur

## Alternative : Client HTML

Pour une meilleure expérience de test, utilisez le fichier HTML fourni dans `TEST_WEBSOCKET.md` qui utilise SockJS et STOMP.js.

## Dépannage

### Problème : "Connection refused"
- Vérifiez que MS3 est démarré
- Vérifiez que le port 8083 est libre
- Vérifiez l'URL : `ws://localhost:8083/ws`

### Problème : Pas de réponse
- Vérifiez que le message JSON est valide
- Vérifiez que l'action est correcte (CREATE, GET, etc.)
- Vérifiez les logs du serveur

### Problème : Erreur de format
- Vérifiez que les dates sont au format ISO : `YYYY-MM-DD`
- Vérifiez que les prix sont des nombres (pas des strings)
- Vérifiez que l'ID est un nombre (pas une string)

## Exemple complet de session

1. **Connecter** : `ws://localhost:8083/ws`
2. **Créer un livre** :
   ```json
   {"action":"CREATE","title":"L'Étranger","prix":9.50,"author":"Albert Camus","datePub":"1942-06-15"}
   ```
3. **Récupérer le livre créé** :
   ```json
   {"action":"GET","id":1500}
   ```
4. **Mettre à jour** :
   ```json
   {"action":"UPDATE","id":1500,"title":"Le Petit Prince","prix":12.50,"author":"Antoine de Saint-Exupéry","datePub":"1943-04-06"}
   ```
5. **Supprimer** :
   ```json
   {"action":"DELETE","id":1500}
   ```

## Notes importantes

- Les réponses sont envoyées en temps réel à tous les clients connectés
- Les dates doivent être au format ISO : `YYYY-MM-DD`
- Les prix sont des nombres décimaux (Double)
- L'ID est auto-généré à partir de 1500

