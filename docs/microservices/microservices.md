# Développement des microservices pour la société Archidéco

Pour répondre aux exigences de l'architecture du projet **Archidéco**, nous allons détailler les microservices en mettant en avant leurs responsabilités, interactions, choix technologiques et exemples de mise en œuvre.

Cette architecture microservices permet une gestion modulaire et évolutive de toutes les fonctionnalités nécessaires au fonctionnement de la plateforme e-commerce **Archidéco**. Chaque microservice est responsable d'une partie spécifique du système, ce qui facilite le développement, le déploiement et la maintenance. Les intégrations avec des API externes et l'utilisation de technologies modernes garantissent une performance optimale et une excellente expérience utilisateur.

Voici une description approfondie des microservices :

## Table des matières

- [Microservices internes (API principale e-commerce)](#microservices-internes-api-principale-e-commerce)
  - [User Service](./user-service.md)
  - [Order Service](./order-service.md)
  - [Commerce Service](./commerce-service.md)
  - [Customer Service](./customer-service.md)
  - [Emailing Service](#emailing-service)
  - [Analytics Service](./analytics-service.md)
- [Interactions avec catalogue-api et delivery-api](#interactions-avec-catalogue-api-et-delivery-api)
  - [Catalogue Service](../catalogue.md)
  - [Delivery Service](../delivery.md)

Ces services sont intégrés avec les microservices internes pour fournir une solution complète de gestion des commandes et des livraisons, ainsi que la gestion des produits et des catalogues.