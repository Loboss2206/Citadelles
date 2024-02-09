# Rapport technique - Team I - LAMA.cpp

## 1. Point d’avancement

<!---un résumé des fonctionnalités réalisées sur tout le jeu (couverture des règles du jeu
indépendamment de ce qui était demandé cette semaine), et éventuellement la liste de ce
qui n'a pas été fait. -->

### Fonctionnalités réalisées

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

<!---un résumé de ce qui a été fait pour les logs (en quelques lignes max, quels choix ont été
faits pour les réaliser) -->
### Logs
Le système de logger a été implémenter en se basant sur les objets inclus dans Java, à savoir, les objets de type ```Logger``` et ```Handler```.
En utilisant un objet de type ```Level``` customisé,nous permettant d'avoir un niveau `VIEW`, `DEMO`, `INFO` de manière à pouvoir afficher les 
logs en fonction du niveau de log demandé.
Par défaut, le logger est configuré pour afficher les logs dans la console produit par la vue, grâce au handler ```ConsoleHandler``` et avec le niveau de log ```VIEW```.
Le logger écrit également les logs dans un fichier ```game.log``` à la racine du projet uniquement la sortie standard de la dernière partie lancée.
Il est cependant possible de changer le niveau de log en modifiant la classe ```Main``` et en changeant le niveau de log.
- Le mode ```VIEW``` permet d'afficher uniquement les méthodes de la vue
- Le mode ```DEMO``` permet d'afficher l'affichage du mode `--2thousands`
- Le mode ```INFO``` permet d'afficher les logs de la partie (début, fin, tour, etc) ainsi que les logs de la vue
- Le mode ```ALL``` permet d'afficher tous les logs
- Le mode ```OFF``` permet de ne pas afficher de logs

A noter que lorsque le mode ```--2thousands``` ou ``` --demo``` est activé, le logger est configuré pour afficher les logs avec le niveau ```DEMO```.

<!--- un résumé de ce qui a été fait pour les statistiques en CSV (en quelques lignes max, quels
choix ont été faits pour les réaliser) -->
### Statistiques en CSV
Si au lancement du programme, le paramètre '--csv' est renseigné, alors le programme va passer en mode CSV et va ainsi faire plusieurs actions succintes: 
- Reperer le chemin du fichier CSV indépendamment du système d'exploitation
- Créer un dossier stats à la racine du projet si il n'existe pas
- Créer un fichier CSV dans le dossier stats si il n'existe pas
- Ajouter une ligne d'entête si le fichier est vide
- Ajouter une ligne de statistiques à la fin de chaque partie jouée (jouée dans le mode CSV)
- Calculer une ligne de statistiques à la fin du CSV contenant des stats globales sur toutes les parties jouées pour chaque types de bot tel que le taux de victoire, le nombre moyen de points par partie par exemple

<!---un résumé de ce qui a été fait pour le bot spécifique demandé, et éventuellement une
comparaison avec votre meilleur bot et une analyse de pourquoi celui qui gagne est le
meilleur -->
### Bot Richard
- TODO PAR MOMEN (il connait le mieux)

## 2. Architecture et qualité

<!--- Comment est faite l'architecture du projet ? Et quels choix vous ont amené à la réaliser
ainsi ? -->
### Architecture

<!---Où trouver les infos (de la java doc, de la doc sur les points et les classes importants ?) -->
###  JavaDoc
La javadoc est disponible dans la branche javadoc de notre projet et permet d'avoir des informations sur toutes les classes et méthodes importantes de notre code.


<!---Etat de la base de code : quelles parties sont bien faites ? Quelles parties sont à refactor et
pourquoi ? Comment la sortie SONAR le montre-elle (ou pas) ? -->
### Qualité du code

#### Tests

Nous avons une couverture de tests d'environ 80% sur notre code, nous avons essayé de tester le plus de méthodes possible
et surtout les plus importantes, mais il reste tout de même encore des parties de certaines méthodes qui restent non testées mais qui pourrait l'être dans le futur.

#### Parties bien faites

Dans notre code, je pense que la plupart des parties sont bien faites, nous avons essayé de respecter les principes SOLID et de faire des classes et méthodes les plus petites possibles.
Pour donner différents exemples, je pense que les classes concernant les bots ainsi que les classes concernant les quartiers ou les loggers sont bien faites.

#### Parties à refactor

Il reste tout de même des parties de notre code qui pourraient être refactorisées, je pourrais par exemple citer:
- L'implémentation des cartes violettes qui pourrait être refactorisée pour être plus propre et pour être plus facilement être adaptable dans le cas de nouvelles cartes
- L'implémentation des personnages qu'on pourrait refactoriser en créant une classe pour chaque personnage héritant tous de la même classe abstraite, cela permettrait des nouveaux personnages bien plus facilement.

#### Comment SONAR nous a aidé ?

Sonar nous a beaucoup aidé à améliorer la qualité de notre code, il nous a permis de voir toutes les parties de notre code qui n'étaient pas couverts par les tests, qui avaient des bugs, des failles de securité ou tout simplement des parties de code qui n'étaient pas optimales et qui contenaient des "code smells".
Avec toutes ces informations, il était simple pour nous de savoir quoi améliorer dans notre code.

## 3. Processus

<!---Qui est responsable de quoi / qui a fait quoi ? -->
### Répartition des tâches
Pour ce qui est de la répartition des tâches, nous nous répartissions les tâches équitablement sur chaque milestone, par exemple, tout le monde a travaillé sur les bots, le refactoring, les tests, les nouvelles features ou encore les réglages de bugs.
Par exemple, chacun a fait 2 personnages et leurs effets et chacun a fait 2/3 quartiers violets et leurs effets.
Pour ce qui est des fonctionnalités additionnelles, une personne s'est occupé des logs/JCommander, une autre des statistiques en CSV et deux autre du bot Richard.
En resumé, tout le monde a participé à chaque milestone et à chaque fonctionnalité de base et additionnelle.

<!---Quel est le process de l'équipe (comment git est utilisé, branching strategy)-->
### Processus de l'équipe
Nous faisions une milestone par semaine, avec des issues représentatives pour chacunes de ces dernières.
Chacun travaillait sur sa branche et faisait des pull requests sur la branche ```dev``` avec au minimum 2 personnes pour valider le merge.
Une fois la fin de la semaine arrivée et toutes les issues réalisées, nous faisions un merge de la branche ```dev``` sur la branche ```master``` et nous taggions la nouvelle version de la branche master.