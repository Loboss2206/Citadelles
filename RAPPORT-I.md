# Rapport technique - Team I - LAMA.cpp

## 1. Point d’avancement

// un résumé des fonctionnalités réalisées sur tout le jeu (couverture des règles du jeu
indépendamment de ce qui était demandé cette semaine), et éventuellement la liste de ce
qui n'a pas été fait.

Le jeu est terminé et fonctionnel à 100%, que ce soit les fonctionnalités de base ou les fonctionnalités additionnelles, parmi celles-ci les plus importantes sont:
- Pouvoir créer et jouer une partie entière de 4 à 7 joueurs
- Implémentation des règles à 100% (même les plus farfelus)
- Avoir une partie avec tous les personnages et leurs effets jouables
- Avoir une partie avec tous les quartiers violets et leurs effets jouables
- Différents types de bots (Bot aléatoire, Bot constructeur, Bot avancé, Bot Richard)
- Mise en place d'un système de logs
- Mise en place d'un système de gestion des arguments au lancement de la partie permettant de lancer différents types de parties:
  - Possibilité de lancer 2000 parties d'un coup
  - Possibilité de lancer une partie de demo
  - Possibilité de lancer des parties garder les informations de la partie dans un fichier CSV (cumulables avec les autres paramètres)
- Mise en place d'un bot spécifique demandé en se basant sur un guide pour débutant d'un forum de stratégies de jeu (Bot Richard)

// un résumé de ce qui a été fait pour les logs (en quelques lignes max, quels choix ont été
faits pour les réaliser)



// un résumé de ce qui a été fait pour les statistiques en CSV (en quelques lignes max, quels
choix ont été faits pour les réaliser)

Si au lancement du programme, le paramètre '--csv' est renseigné, alors le programme va passer en mode CSV et va ainsi faire plusieurs actions succintes: 
- Reperer le chemin du fichier CSV indépendamment du système d'exploitation
- Créer un dossier stats à la racine du projet si il n'existe pas
- Créer un fichier CSV dans le dossier stats si il n'existe pas
- Ajouter une ligne d'entête si le fichier est vide
- Ajouter une ligne de statistiques à la fin de chaque partie jouée (jouée dans le mode CSV)
- Calculer une ligne de statistiques à la fin du CSV contenant des stats globales sur toutes les parties jouées pour chaque types de bot tel que le taux de victoire, le nombre moyen de points par partie par exemple

// un résumé de ce qui a été fait pour le bot spécifique demandé, et éventuellement une
comparaison avec votre meilleur bot et une analyse de pourquoi celui qui gagne est le
meilleur

## 2. Architecture et qualité

// Comment est faite l'architecture du projet ? Et quels choix vous ont amené à la réaliser
ainsi ?


// Où trouver les infos (de la java doc, de la doc sur les points et les classes importants ?)


// Etat de la base de code : quelles parties sont bien faites ? Quelles parties sont à refactor et
pourquoi ? Comment la sortie SONAR le montre-elle (ou pas) ?

## 3. Processus

// Qui est responsable de quoi / qui a fait quoi ? 

Pour ce qui est de la répartition des tâches, nous nous répartissions les tâches équitablement sur chaque milestone, par exemple, tout le monde a travaillé sur les bots, le refactoring, les tests, les nouvelles features ou encore les réglages de bugs.
Par exemple, chacun a fait 2 personnages et leurs effets et chacun a fait 2/3 quartiers violets et leurs effets.
Pour ce qui est des fonctionnalités additionnelles, une personne s'est occupé des logs/JCommander, une autre des statistiques en CSV et deux autre du bot Richard.
En resumé, tout le monde a participé à chaque milestone et à chaque fonctionnalité de base et additionnelle.

// Quel est le process de l'équipe (comment git est utilisé, branching strategy)