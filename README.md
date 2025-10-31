# Pay My Buddy – Backend Java Spring Boot

Ce projet fait partie du parcours **Développeur d’application Java** d’OpenClassrooms.  
Il s’agit d’une application web Spring Boot qui permet de gérer les comptes, les connexions entre utilisateurs et les transferts d’argent avec une commission de 0,5 %.

---

## Configuration

L’application utilise **MySQL** et **Flyway** pour la base de données.  
Les identifiants de connexion ne sont **pas inclus dans le code source** : ils doivent être fournis via des **variables d’environnement** ou un **fichier de configuration externe**.

### Variables d’environnement

Avant de lancer l’application, configurez les variables suivantes :

```bash
export DB_URL="jdbc:mysql://localhost:3306/paymybuddy?serverTimezone=UTC"
export DB_USER="root"
export DB_PASSWORD="votre_mot_de_passe"
```

Fichier de configuration externe (alternative)

Vous pouvez aussi créer un dossier `config` à côté du fichier JAR, contenant :

```
config/
 └── application.properties
paymybuddy.jar
```

Contenu du fichier `config/application.properties` :

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

Spring Boot lira automatiquement ce fichier au démarrage, ce qui permet de modifier la configuration sans reconstruire le JAR.

## Lancement de l’application
En mode développement (IntelliJ, Eclipse ou CLI)

`mvn spring-boot:run`

En mode production (JAR)

`java -jar target/paymybuddy-0.0.1-SNAPSHOT.jar`


L’application sera accessible à l’adresse :
`http://localhost:8080`

## Technologies utilisées

- Java 21

- Spring Boot 3 (Web, Data JPA, Security, Validation, Actuator)

- MySQL 8

- Flyway pour la gestion des migrations

- Maven

- Lombok

## Bonnes pratiques

- Aucune donnée sensible n’est stockée dans le code ou sur GitHub.

- Les mots de passe sont externalisés via des variables d’environnement.

- L’accès à la base de données est sécurisé.

## Auteur

Projet développé par Désirée TELARETTI dans le cadre du parcours OpenClassrooms.
