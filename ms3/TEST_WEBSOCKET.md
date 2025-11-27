# Guide de test pour MS3 - Service WebSocket

## Configuration

- **Port HTTP** : 8083 (par défaut)
- **Endpoint WebSocket** : `ws://localhost:8083/ws`
- **Endpoint WebSocket avec SockJS** : `http://localhost:8083/ws`

## Architecture WebSocket

### Endpoints disponibles

1. **Connexion WebSocket** : `/ws`
2. **Destinations de messages** :
   - `/app/book/create` - Créer un livre
   - `/app/book/get` - Récupérer un livre par ID
   - `/app/book/getAll` - Récupérer tous les livres
   - `/app/book/update` - Mettre à jour un livre
   - `/app/book/delete` - Supprimer un livre

3. **Topics pour recevoir les réponses** :
   - `/topic/books` - Notifications pour toutes les opérations sur les livres
   - `/topic/book` - Réponse pour une opération sur un livre spécifique
   - `/topic/books/list` - Liste de tous les livres

## Test avec un client JavaScript

### Exemple de client HTML/JavaScript

Créez un fichier `test-websocket.html` :

```html
<!DOCTYPE html>
<html>
<head>
    <title>Test WebSocket - MS3</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
    <h1>Test WebSocket - Gestion des Livres</h1>
    
    <div>
        <h2>Connexion</h2>
        <button onclick="connect()">Se connecter</button>
        <button onclick="disconnect()">Se déconnecter</button>
        <p id="status">Non connecté</p>
    </div>

    <div>
        <h2>Créer un livre</h2>
        <input type="text" id="title" placeholder="Titre" value="L'Étranger">
        <input type="number" id="prix" placeholder="Prix" value="9.50" step="0.01">
        <input type="text" id="author" placeholder="Auteur" value="Albert Camus">
        <input type="date" id="datePub" value="1942-06-15">
        <button onclick="createBook()">Créer</button>
    </div>

    <div>
        <h2>Récupérer un livre</h2>
        <input type="number" id="bookId" placeholder="ID" value="1500">
        <button onclick="getBook()">Récupérer</button>
    </div>

    <div>
        <h2>Récupérer tous les livres</h2>
        <button onclick="getAllBooks()">Récupérer tous</button>
    </div>

    <div>
        <h2>Mettre à jour un livre</h2>
        <input type="number" id="updateId" placeholder="ID" value="1500">
        <input type="text" id="updateTitle" placeholder="Titre" value="Le Petit Prince">
        <input type="number" id="updatePrix" placeholder="Prix" value="12.50" step="0.01">
        <input type="text" id="updateAuthor" placeholder="Auteur" value="Antoine de Saint-Exupéry">
        <input type="date" id="updateDatePub" value="1943-04-06">
        <button onclick="updateBook()">Mettre à jour</button>
    </div>

    <div>
        <h2>Supprimer un livre</h2>
        <input type="number" id="deleteId" placeholder="ID" value="1500">
        <button onclick="deleteBook()">Supprimer</button>
    </div>

    <div>
        <h2>Réponses</h2>
        <pre id="responses"></pre>
    </div>

    <script>
        let stompClient = null;
        const serverUrl = 'http://localhost:8083/ws';

        function connect() {
            const socket = new SockJS(serverUrl);
            stompClient = Stomp.over(socket);
            
            stompClient.connect({}, function(frame) {
                document.getElementById('status').textContent = 'Connecté';
                document.getElementById('status').style.color = 'green';
                
                // S'abonner aux notifications
                stompClient.subscribe('/topic/books', function(message) {
                    const response = JSON.parse(message.body);
                    addResponse('Notification: ' + JSON.stringify(response, null, 2));
                });

                stompClient.subscribe('/topic/book', function(message) {
                    const response = JSON.parse(message.body);
                    addResponse('Livre: ' + JSON.stringify(response, null, 2));
                });

                stompClient.subscribe('/topic/books/list', function(message) {
                    const books = JSON.parse(message.body);
                    addResponse('Liste des livres: ' + JSON.stringify(books, null, 2));
                });
            }, function(error) {
                document.getElementById('status').textContent = 'Erreur de connexion';
                document.getElementById('status').style.color = 'red';
                console.error('Erreur:', error);
            });
        }

        function disconnect() {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            document.getElementById('status').textContent = 'Déconnecté';
            document.getElementById('status').style.color = 'red';
        }

        function createBook() {
            if (stompClient === null) {
                alert('Veuillez vous connecter d\'abord');
                return;
            }

            const message = {
                action: 'CREATE',
                title: document.getElementById('title').value,
                prix: parseFloat(document.getElementById('prix').value),
                author: document.getElementById('author').value,
                datePub: document.getElementById('datePub').value
            };

            stompClient.send('/app/book/create', {}, JSON.stringify(message));
            addResponse('Envoi: ' + JSON.stringify(message, null, 2));
        }

        function getBook() {
            if (stompClient === null) {
                alert('Veuillez vous connecter d\'abord');
                return;
            }

            const message = {
                action: 'GET',
                id: parseInt(document.getElementById('bookId').value)
            };

            stompClient.send('/app/book/get', {}, JSON.stringify(message));
            addResponse('Envoi: ' + JSON.stringify(message, null, 2));
        }

        function getAllBooks() {
            if (stompClient === null) {
                alert('Veuillez vous connecter d\'abord');
                return;
            }

            const message = {
                action: 'GET_ALL'
            };

            stompClient.send('/app/book/getAll', {}, JSON.stringify(message));
            addResponse('Envoi: ' + JSON.stringify(message, null, 2));
        }

        function updateBook() {
            if (stompClient === null) {
                alert('Veuillez vous connecter d\'abord');
                return;
            }

            const message = {
                action: 'UPDATE',
                id: parseInt(document.getElementById('updateId').value),
                title: document.getElementById('updateTitle').value,
                prix: parseFloat(document.getElementById('updatePrix').value),
                author: document.getElementById('updateAuthor').value,
                datePub: document.getElementById('updateDatePub').value
            };

            stompClient.send('/app/book/update', {}, JSON.stringify(message));
            addResponse('Envoi: ' + JSON.stringify(message, null, 2));
        }

        function deleteBook() {
            if (stompClient === null) {
                alert('Veuillez vous connecter d\'abord');
                return;
            }

            const message = {
                action: 'DELETE',
                id: parseInt(document.getElementById('deleteId').value)
            };

            stompClient.send('/app/book/delete', {}, JSON.stringify(message));
            addResponse('Envoi: ' + JSON.stringify(message, null, 2));
        }

        function addResponse(text) {
            const responses = document.getElementById('responses');
            responses.textContent = text + '\n\n' + responses.textContent;
        }
    </script>
</body>
</html>
```

## Test avec Postman

Postman supporte WebSocket depuis la version 8.0+ :

1. Ouvrez Postman
2. Cliquez sur **New** → **WebSocket Request**
3. Entrez l'URL : `ws://localhost:8083/ws`
4. Cliquez sur **Connect**
5. Pour envoyer un message, utilisez le format JSON :

### Exemples de messages JSON

#### Créer un livre
```json
{
  "action": "CREATE",
  "title": "L'Étranger",
  "prix": 9.50,
  "author": "Albert Camus",
  "datePub": "1942-06-15"
}
```

#### Récupérer un livre
```json
{
  "action": "GET",
  "id": 1500
}
```

#### Récupérer tous les livres
```json
{
  "action": "GET_ALL"
}
```

#### Mettre à jour un livre
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

#### Supprimer un livre
```json
{
  "action": "DELETE",
  "id": 1500
}
```

## Test avec wscat (ligne de commande)

### Installation de wscat

```bash
npm install -g wscat
```

### Connexion

```bash
wscat -c ws://localhost:8083/ws
```

### Envoyer des messages

Une fois connecté, vous pouvez envoyer des messages JSON :

```json
{"action":"CREATE","title":"L'Étranger","prix":9.50,"author":"Albert Camus","datePub":"1942-06-15"}
```

## Format des messages

### Structure BookMessage

```json
{
  "action": "CREATE|UPDATE|DELETE|GET|GET_ALL",
  "id": 1500,
  "title": "Titre du livre",
  "prix": 12.50,
  "author": "Nom de l'auteur",
  "datePub": "1943-04-06",
  "success": true,
  "message": "Message de réponse"
}
```

### Actions disponibles

- **CREATE** : Créer un nouveau livre
- **GET** : Récupérer un livre par ID
- **GET_ALL** : Récupérer tous les livres
- **UPDATE** : Mettre à jour un livre existant
- **DELETE** : Supprimer un livre

## Notes importantes

- Le serveur doit être démarré avant de tester
- Les dates doivent être au format ISO : `YYYY-MM-DD`
- Les prix sont des nombres décimaux (Double)
- Les réponses sont envoyées via les topics `/topic/books` et `/topic/book`
- Tous les clients connectés reçoivent les notifications en temps réel

## Démarrer le serveur

```bash
cd ms3
mvn spring-boot:run
```

Le serveur WebSocket sera accessible sur `ws://localhost:8083/ws`.

