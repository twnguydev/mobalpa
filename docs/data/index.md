# Documentation du Projet Archidéco - Volet Data

Pour répondre aux besoins d'analyse et de gestion des données de la plateforme e-commerce **Archidéco**, nous allons détailler les différentes composantes du volet "data". Cette documentation couvrira les aspects de l'extraction, de l'analyse, de la génération de rapports et de l'envoi d'emails, ainsi que les technologies utilisées et les exemples de mise en œuvre.

Cette approche modulaire permet une gestion efficace et évolutive des données, facilitant ainsi le développement, le déploiement et la maintenance des fonctionnalités analytiques de la plateforme.

Les différentes API du projet Archidéco sont disponibles aux ports suivants :
- API interne : `8080`
- API Catalogue : `8081`
- API Delivery : `8082`

## Table des matières

- [Extraction des données](#extraction-des-données)
  - [Data Extractor](./data/data-extractor.md)
- [Analyse des données](#analyse-des-données)
  - [Sales Analyzer](./data/sales-analyzer.md)
- [Génération de rapports](#génération-de-rapports)
  - [CSV Generator](./data/csv-generator.md)
- [Envoi d'emails](#envoi-demails)
  - [Email Sender](./data/email-sender.md)
- [Planification des tâches](#planification-des-tâches)
  - [Scheduler](./data/scheduler.md)

## Commandes

- `source venv/bin/activate`: activer l'environnement virtuel.
- `pip install -r requirements.txt`: installer les dépendances.
- `pip list`: vérifier que les dépendances sont bien installées.
- `python -m unittest discover unit_tests`: exécuter les tests unitaires.

## Extraction des données

### Data Extractor

Le composant **Data Extractor** est responsable de l'extraction des données des commandes et des utilisateurs à partir de la base de données MySQL. Il effectue également des appels API pour récupérer les détails des produits commandés.

[En savoir plus](./data/data-extractor.md)

## Analyse des données

### Sales Analyzer

Le **Sales Analyzer** est utilisé pour prétraiter et analyser les données de ventes. Il fournit des agrégations hebdomadaires, mensuelles et annuelles des ventes, et utilise des modèles de régression linéaire pour prédire les ventes futures.

[En savoir plus](./data/sales-analyzer.md)

## Génération de rapports

### CSV Generator

Le **CSV Generator** est responsable de la génération de fichiers CSV contenant les ventes agrégées et les prévisions. Ces fichiers sont ensuite utilisés pour la création de rapports.

[En savoir plus](./data/csv-generator.md)

## Envoi d'emails

### Email Sender

Le **Email Sender** envoie les rapports générés par email aux administrateurs. Il utilise les informations SMTP configurées pour envoyer les fichiers CSV en pièce jointe.

[En savoir plus](./data/email-sender.md)

## Planification des tâches

### Scheduler

Le **Scheduler** planifie et exécute les tâches récurrentes telles que la génération et l'envoi de rapports hebdomadaires, mensuels et annuels. Il utilise la bibliothèque `schedule` pour la planification des tâches.

[En savoir plus](./data/scheduler.md)