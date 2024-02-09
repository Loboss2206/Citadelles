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
- Différents types de bots (Bot aléatoire, Bot constructeur, Bot avancé, Bot `Richard`)
- Mise en place d'un système de logs
- Mise en place d'un système de gestion des arguments au lancement de la partie permettant de lancer différents types de parties:
  - Possibilité de lancer 2000 parties d'un coup
  - Possibilité de lancer une partie de demo
  - Possibilité de lancer des parties garder les informations de la partie dans un fichier CSV (cumulables avec les autres paramètres)
- Mise en place d'un bot spécifique demandé en se basant sur un guide pour débutant d'un forum de stratégies de jeu (Bot `Richard`)

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
Le bot `Richard` (du nom de l'auteur originel) est un bot qui implémente une stratégie de jeu basée sur un [guide pour débutant d'un forum de stratégies de jeu](https://forum.trictrac.net/t/citadelles-charte-citadelles-de-base/509).
Ce bot permet d'avoir une stratégie de jeu qui cherche à contrer ses adversaires plutot que de gagner par tout les moyens, de plus dans beaucoups de cas, on doit faire confiance ou espérer que les autres joueurs jouent d'une façon précise, ce qui n'est pas toujours le cas en pratique.

#### Analyse du guide
Dans un premier temps, nous avons dû analyser le guide pour comprendre les comportements à adopter pour ce bot. Nous avons donc dû extraire les informations importantes de ce dernier sachant que un autre utilisateur du forum, Alphonse (ou tt-22a5e3f98e5243b9f1135d1caadc4cc7 pour les intimes) a donné des indications complémentaires sur certains personnages et situations que l'on a aussi dû prendre en compte.

#### Stratégie
La stratégie fournie par le guide donne des conseils pour tout les personnages du jeu (en combinant les réponses) et donne des conseils pour les situations de jeu les plus courantes, ce qui nous a permis de faire une stratégie globale pour le bot.

##### Stratégie générale
Tout d'abord la stratégie "générale" du bot, qui est basée sur les informations du guide:
- Priorité au Roi s'il possède au moins une carte jaune ou s'il est couronné avec moins de cinq joueurs.
- Ensuite, l'Évêque est privilégié s'il a au moins une carte bleue ou s'il a déjà construit cinq quartiers.
- Le Marchand est choisi s'il a au moins une carte verte ou s'il a moins de deux pièces d'or.
- Le Condotierre est sélectionné s'il a au moins une carte rouge ou s'il est le premier joueur avec une pièce d'or et que le tour est supérieur à trois.
- En l'absence de l'Architecte, le joueur peut opter pour l'Assassin s'il peut éliminer un joueur ou l'Architecte s'il possède au moins quatre pièces d'or.
- Si le joueur n'a aucune carte en main, il peut choisir le Magicien s'il y a quelqu'un avec beaucoup de cartes.
- Le Voleur est choisi tôt dans le jeu s'il y a quelqu'un avec beaucoup de pièces d'or et le joueur a peu de pièces d'or.
- Enfin, l'Assassin peut être utilisé pour éliminer un joueur sans cartes si le joueur en a au moins quatre en main.

L'utilisation de cartes violettes ou la construction de quartiers n'est pas mentionnée dans le guide, nous avons donc dû reprendre les stratégies de nos autres bots pour les implémenter dans le bot `Richard`, notamment celles du bot `Strong`.

###### Stratégie spéciale - Avant dernier tour
Lors de l'avant dernier tour : 
- Si le Roi est disponible, le choisir.
- Sinon, si l'Assassin est disponible :
  - Si le Roi n'est pas dans la main et n'a pas été défaussé ce tour, cibler le Roi.
- Sinon, si le Condotierre est disponible :
  - Si ni le Roi ni l'Assassin ne sont dans la main ou n'ont été défaussés ce tour, cibler le joueur en passe de gagner avec le quartier le moins cher.
- Sinon, si l'Évêque est disponible, le choisir.
- Sinon, si le Magicien est disponible, le choisir.

###### Stratégie spéciale - Dernier tour
Il y a une situation particulière pour le dernier tour, pour laquelle le bot va essayer de contrer ses adversaires au maximum. Bien que parfois cette stratégie implique d'être suivi par d'autres joueurs et fait également appelle à des suppositions sur les choix des autres joueurs concernant le choix des personnages.
Voici la stratégie de jeu de `Richard` pour le dernier tour:
Cette stratégie est basée sur le fait que `Richard` calcul quel joueur est en tête et va essayer de contrer ce joueur en priorité.
- Si le joueur en passe de gagner est le premier ou le deuxième joueur :
  - S'il n'y a ni l'Évêque ni le Condotierre parmi les cartes du joueur, mais qu'il possède l'Assassin, il peut choisir de cibler soit l'Évêque soit le Condotierre.
- Si le joueur en passe de gagner est au moins le troisième joueur :
  - Si les cartes du joueur incluent l'Assassin, le Condotierre et l'Évêque :
    - Si le joueur actuel est le premier joueur, il peut choisir le Condotierre.
    - Si le joueur actuel est le deuxième joueur, il peut choisir l'Assassin pour cibler l'Évêque.
  - Si l'Évêque n'est pas parmi les cartes du joueur et qu'il est le premier joueur, et que l'Assassin est disponible, il peut choisir l'Assassin pour cibler le Magicien si le deuxième joueur a autant de cartes que le joueur en passe de gagner.
  - Si l'Évêque n'est pas parmi les cartes du joueur et qu'il est le deuxième joueur, et que le premier joueur a moins de deux cartes en main et que le Magicien est disponible, il peut choisir le Magicien pour cibler le joueur en passe de gagner.
  - Si l'Assassin n'est pas disponible et qu'il est le premier joueur, et que le Condotierre est disponible, il peut choisir le Condotierre.
  - Si l'Assassin n'est pas disponible et qu'il est le deuxième joueur, et que l'Évêque est disponible, il peut choisir l'Évêque.


Le bot `Richard` est donc un bot qui cherche plus a contrer ses adversaires que de gagner ce qui n'est pas forcément la meilleure stratégie à adopter, face à des bots plus agressifs, où `Richard` aura plus de mal à contrer les adversaires et donc à gagner.

#### Implémentation

Nous avons fait face à plusieurs cas de figures lors de l'implémentation de ce bot, dans certains cas, la description faite sur le forum était suffisante pour implémenter le comportement du bot sans trop réfléchir. Mais dans d'autres cas, nous avons dû faire interpréter des informations pour comprendre le comportement attendu et ainsi faire une implémentation qui s'en rapproche le plus possible. Pour finir, certains cas de figures n'étaient pas cités dans le guide, nous avons donc pris ceux de nos autres bots, notaemment pour la jouabilité des cartes violettes qui sont absentes de ce guide mais qui doivent tout des mêmes êtres jouables et implémenter par notre bot.

Toutes ces règles ont été implémentées dans la classe ````Richard```` et qui permet de jouer une partie en suivant les règles de ce guide, bien que certaines règles sont libres d'interprétation, nous avons fait de notre mieux pour implémenter le comportement le plus proche possible de ce qui est décrit dans le guide.

#### Comparaison avec nos autres bots et analyse

D'apres les tests que nous avons pu réalisé pour définir des statistiques sur les bots, le bot `Richard` est plus performant que le bot aléatoire et le bot `Weak` mais il est moins performant que le bot `Strong`.
De manière plus détaillée, `Richard` et notre bot `Weak` ont des comportements très similaires ce qui fait que les chances de victoire sont très proches donnant donc en moyenne une égalité entre les deux bots.
Le bot `Weak` essayant de construire le plus de quartiers possibles, si possible au plus bas coût, fait que la stratégie de `Richard`, bien que différentes dans les faits, sont très proches en terme de résultats.
En revanche, `Richard` est moins performant que le bot `Strong` car ce dernier dispose d'une stratégie de jeu plus optimale que celle de `Richard`.     
La stratégie de notre bot `Strong` est bien précise: 
- Il ne doit poser que très rarement des districts à 1 point
- Les personnages choisit par le bot `Strong` ne sont pas forcement ceux qui sont utilisés dans la stratégie de `Richard`
- Le bot `Strong` n'implémente pas de stratégie de dernier tour visant à contrer les adversaires.
En conséquence, le bot `Strong` est plus performant que `Richard` car il a une stratégie de jeu différentes et plus absolu que celle de `Richard`.

En termes de chiffres, `Richard` a un taux de victoire de environ 5,3% contre 94,7% pour le bot `Strong`, ce qui en fait le second bot le plus performant de notre jeu, derrère le bot `Strong`.


## 2. Architecture et qualité

<!--- Comment est faite l'architecture du projet ? Et quels choix vous ont amené à la réaliser
ainsi ? -->
### Architecture

Notre architecture est plutôt simple, nous utilisons un semblant de modèle MVC.<br>
Nous avons une classe ```Main``` qui est la classe principale de notre programme, c'est elle qui va lancer la partie et qui va gérer les arguments passés au lancement du programme qui n'est dans aucun package.<br>
Puis nous avons aussi 4 packages:
- Un package ```controller``` qui contient:
  - La classe ```Game``` qui est la classe qui gère la partie et qui va appeler les différentes méthodes des autres classes pour faire avancer la partie
  - La classe ```Round``` qui est la classe qui gère un tour de jeu
  - La classe ```EffectController``` qui va appeler les différentes méthodes en fonction du rôle du joueur
- Un package ```model``` qui contient lui même 4 packages:
  - Un package ```bot``` qui contient les différentes classes de bots:
    - La classe ```Player``` qui est la classe mère de tous les bots qui contient tous les attributs des bots et les méthodes communes à tous les bots
    - La classe ```GameActions``` qui est l'interface qui contient les méthodes que tous les bots doivent implémenter
    - La classe ```PlayerComparator``` qui est la classe qui permet de comparer les joueurs en fonction de leur points (ou de leur dernier rôle si ils ont le même nombre de points)
    - La classe ```DispatchState``` qui est l'enum qui contient toutes les actions possibles pour un bot
    - La classe ```BotRandom``` qui est la classe qui gère le bot aléatoire
    - La classe ```BotWeak``` qui est la classe qui gère le bot constructeur
    - La classe ```BotStrong``` qui est la classe qui gère le bot avancé
    - La classe ```Richard``` qui est la classe qui gère le bot Richard
  - Un package ```card``` qui contient les différentes classes de cartes:
    - La classe ```Color``` qui est l'enum qui contient toutes les couleurs possibles pour les cartes et les personnages
    - La classe ```CharacterCard``` qui est l'enum qui contient tous les personnages possibles
    - La classe ```DistrictCard``` qui est l'enum qui contient tous les quartiers possibles
    - La classe ```DistrictCardComparator``` qui est la classe qui permet de comparer les quartiers en fonction de leur coût
    - La classe ```PurpleEffectState``` qui est l'enum qui contient tous les effets possibles pour les quartiers violets
  - Un package ```deck``` qui contient les différentes classes de decks:
    - La classe ```Deck``` qui est la classe générique qui gère les decks
    - La classe ```DeckFactory``` qui est la classe qui permet de créer les decks
  - Un package ```golds``` qui contient une seule classe ```StackOfGolds``` qui est la classe qui gère la pile d'or
- Un package ```view``` qui contient:
  - La classe ```GameView``` qui est la classe qui gère l'affichage de la partie
- Un package ```logger``` qui contient:
  - La classe ```LamaLevel``` qui est l'enum qui contient les niveaux de log qu'on a ajoutés nous-même
  - La classe ```LamaFormatter``` qui est la classe qui gère le format des logs
  - La classe ```LamaLogger``` qui est la classe qui gère les logs

<!---Où trouver les infos (de la java doc, de la doc sur les points et les classes importants ?) -->
###  JavaDoc
La javadoc est disponible dans la branche javadoc de notre projet et permet dStrong


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
Pour ce qui est des fonctionnalités additionnelles, une personne s'est occupé des logs/JCommander, une autre des statistiques en CSV et deux autre du bot `Richard`.
En resumé, tout le monde a participé à chaque milestone et à chaque fonctionnalité de base et additionnelle.

<!---Quel est le process de l'équipe (comment git est utilisé, branching strategy)-->
### Processus de l'équipe
Nous faisions une milestone par semaine, avec des issues représentatives pour chacunes de ces dernières.
Chacun travaillait sur sa branche et faisait des pull requests sur la branche ```dev``` avec au minimum 2 personnes pour valider le merge.
Une fois la fin de la semaine arrivée et toutes les issues réalisées, nous faisions un merge de la branche ```dev``` sur la branche ```master``` et nous taggions la nouvelle version de la branche master.