# Base de données – _Pay My Buddy_

## Description

Ce dossier contient la conception et l’implémentation de la base de données relationnelle pour l’application **Pay My Buddy**, qui permet aux utilisateurs d’effectuer des transferts d’argent entre amis.

Le modèle physique de données (**MPD**) a été conçu à partir du diagramme UML fourni, en respectant la cohérence des relations et l’intégrité référentielle.

---

## Structure du schéma

|Table|Description|Clés et contraintes principales|
|---|---|---|
|**users**|Contient les informations sur les utilisateurs|`users_id` (PK), `email` UNIQUE, `created_at` TIMESTAMP|
|**user_connection**|Gère les connexions entre utilisateurs (relation M:N)|PK composée (`user_id`, `connections_id`), FKs vers `users`, `ON DELETE CASCADE`, `CHECK user_id <> connections_id`|
|**transactions**|Historique des transferts d’argent entre utilisateurs|`trans_id` (PK), FKs (`sender_id`, `receiver_id`) vers `users`, `ON DELETE RESTRICT`, `CHECK amount > 0`, `CHECK fee >= 0`, `CHECK sender_id <> receiver_id`|

---

## Schéma physique (MPD)

_(extrait simplifié)_

```sql
CREATE TABLE users (
                       users_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_connection (
                                 user_id INT NOT NULL,
                                 connections_id INT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (user_id, connections_id),
                                 CONSTRAINT fk_uc_user FOREIGN KEY (user_id) REFERENCES users(users_id) ON DELETE CASCADE,
                                 CONSTRAINT fk_uc_connection FOREIGN KEY (connections_id) REFERENCES users(users_id) ON DELETE CASCADE,
                                 CONSTRAINT ck_uc_diff CHECK (user_id <> connections_id)
);

CREATE TABLE transactions (
                              trans_id INT AUTO_INCREMENT PRIMARY KEY,
                              description VARCHAR(200),
                              amount DECIMAL(19,2) NOT NULL,
                              sender_id INT NOT NULL,
                              receiver_id INT NOT NULL,
                              fee DECIMAL(19,2) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_tx_sender FOREIGN KEY (sender_id) REFERENCES users(users_id) ON DELETE RESTRICT,
                              CONSTRAINT fk_tx_receiver FOREIGN KEY (receiver_id) REFERENCES users(users_id) ON DELETE RESTRICT,
                              CONSTRAINT ck_amount_positive CHECK (amount > 0),
                              CONSTRAINT ck_fee_nonnegative CHECK (fee >= 0),
                              CONSTRAINT ck_no_self_transfer CHECK (sender_id <> receiver_id)
);
```

---

## Données de test

- 8 utilisateurs : _Friends_ + _How I Met Your Mother_

- 10 connexions (1:N)

- 8 transactions (avec commission 0,5 %)


Les scripts `data.sql` et `test_queries.sql` permettent de valider la cohérence de la base et les principales requêtes fonctionnelles.

---

## Instructions d’utilisation

### Configuration de la base de données (via variables d’environnement)

L’application utilise une base de données **MySQL** locale.  
Pour des raisons de sécurité, les identifiants de connexion ne sont plus stockés dans le projet.

Avant de démarrer l’application, définissez les variables d’environnement suivantes :

```bash
export DB_URL="jdbc:mysql://localhost:3306/paymybuddy?serverTimezone=UTC"
export DB_USER="paymybuddy_user"
export DB_PASSWORD="rootroot"
```

Vous pouvez aussi créer un fichier config/application.properties à côté du JAR :

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Étapes d’installation

- Créez la base de données et l’utilisateur local :

 ```sql
CREATE DATABASE paymybuddy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'paymybuddy_user'@'localhost' IDENTIFIED BY 'rootroot';
GRANT SELECT, INSERT, UPDATE, DELETE ON paymybuddy.* TO 'paymybuddy_user'@'localhost';
FLUSH PRIVILEGES;
```

- Importez la structure et les données de test :

```
mysql -u root -p paymybuddy < database/schema.sql
mysql -u root -p paymybuddy < database/data.sql
```

- Démarrez l’application :

`mvn spring-boot:run`

Ces identifiants sont fournis uniquement à des fins de test local.
En production, il est recommandé d’utiliser des variables d’environnement ou un fichier de configuration externe.

---

## Intégrité et sécurité

- Clés primaires et étrangères définies sur toutes les relations.

- Aucune donnée orpheline possible.

- Données sensibles (mots de passe) stockées sous forme **hashée** (`password_hash`).

- Contraintes `CHECK` garantissant la validité des montants et relations.

- `ON DELETE RESTRICT` protège l’historique des transactions.
