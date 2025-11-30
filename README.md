# **Pay My Buddy – Application Web Java Spring Boot**

Projet réalisé dans le cadre du parcours **Développeur d’Application Java – OpenClassrooms**.  
Il s’agit d’une application web permettant de :

- créer un compte utilisateur
    
- ajouter des connexions (amis)
    
- réaliser des transferts d’argent
    
- calculer automatiquement une commission de **0,5 %**
    
- consulter l’historique des opérations
    

---

# **Architecture du projet**

L'application suit une architecture en couches claire :

- **Controller (Web & REST)**
    
- **Service (logique métier + transactions @Transactional)**
    
- **DAL / Repository (Spring Data JPA)**
    
- **Modèle / Entités (JPA)**
    

Le design suit les conventions Spring Boot 3, SOLID et MVC.

---

# **Modèle Physique de Données (MPD)**

Le MPD complet est disponible dans :  
📁 `database/MPD.png`  
📁 `database/README.md`

Résumé :

- `users` : comptes utilisateurs
    
- `user_connection` : connexions (relation N:N)
    
- `transactions` : transferts avec commission
    

Chaque table comporte :

- clé primaire
    
- contraintes d’intégrité (`CHECK`, `UNIQUE`, `FOREIGN KEY`)
    
- règles métier (pas d’auto-transfert, montants positifs, etc.)
    

---

# **Scripts SQL fournis**

Ils se trouvent dans : `database/`

- `schema.sql` – création complète du schéma
    
- `data.sql` – jeu d’essai cohérent
    
- `test_queries.sql` – requêtes de validation
    

---

# **Connexion sécurisée à la base de données**

Aucune donnée sensible n’est stockée dans le dépôt GitHub.

Les identifiants sont fournis via :

### Variables d’environnement

`export DB_URL="jdbc:mysql://localhost:3306/paymybuddy?serverTimezone=UTC" export DB_USER="root" export DB_PASSWORD="votre_mot_de_passe"`

### Ou un fichier externe `config/application.properties`

`spring.datasource.url=${DB_URL} spring.datasource.username=${DB_USER} spring.datasource.password=${DB_PASSWORD} spring.jpa.hibernate.ddl-auto=validate spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect`

---

# **Couche DAL + gestion des transactions**

La couche DAL est basée sur :

- **Spring Data JPA**
    
- Repositories (`UserRepository`, `TransactionRepository`, etc.)
    

La logique métier et les accès à la base sont gérés dans :

- `UserService`
    
- `TransactionService`
    

Les méthodes critiques sont annotées avec :

`@Transactional`

Ce qui garantit :

- **commit automatique** si l’opération réussit
    
- **rollback automatique** si une exception métier (`BusinessException`) est levée
    

---

# **Interface Web (Thymeleaf)**

L’interface respecte les maquettes du projet OpenClassrooms.  
Les pages HTML incluent :

- Login
    
- Register
    
- Profil
    
- Connexions
    
- Transferts
    

Le front consomme la couche Service et applique :

- bonnes pratiques d’accessibilité (WCAG)
    
- labels `sr-only`
    
- placeholders explicites
    
- navigation simple et conforme aux spécifications
    

---

# **API REST**

3 contrôleurs REST :

- `/api/auth`
    
- `/api/connections`
    
- `/api/transactions`
    

Format JSON validé via des DTO (`RegisterRequest`, `TransferRequest`, etc.)

---

# **Tests (Unitaires + Intégration)**

Technos utilisées : **JUnit 5, Mockito, Spring Boot Test, MockMvc**.

✔ Tests des contrôleurs Web  
✔ Tests API REST  
✔ Tests Services  
✔ Tests d’intégration (base MySQL réelle + transactions rollback)  
✔ JaCoCo ≈ **80 % de couverture**

Les rapports se trouvent dans :

- `target/site/jacoco/index.html`
    
- `target/reports/surefire.html`
    

---

# **Lancement de l’application**

### Développement

`mvn spring-boot:run`

### Production

`java -jar test/paymybuddy-0.0.1-SNAPSHOT.jar`

Accès :  
[http://localhost:8080](http://localhost:8080)

---

# **Déploiement**

L’application peut être déployée via :

- JAR autonome
    
- configuration externalisée (`config/application.properties`)
    

---

# Auteur

Projet développé par **Désirée TELARETTI**  
Parcours : _Développeur d’application Java – OpenClassrooms_
