# FICHIER TEMPORAIRE POUR TEST MANUEL DU DEV

## Contexte
Ce fichier décrit comment tester manuellement le plugin creedengo-dashboard en cours de développement.
Ceci est valable le temps de la mise en place d'un système automatique de test d'integration avec plusieurs plugins car le pluigin creedengo-dashboard a besoin de :
- un autre plugin creedengo déjà installé (ex : creedengo-python)
- les profils Creendego installés également
- un projet de test analysé et dont les résultats sont disponibles dans SonarQube

## Pré-requis
- installer Rancher Desktop ou un équivalent pour pouvoir faire du Docker
- récupérer le repository "creedengo-python" qui servira de support pour le SonarQube local

## Process
- 1 - suivre les instructions pour lancer SonarQube avec Docker et vérifier que le Sonarqube fonctionne
  - doc du process : https://github.com/green-code-initiative/creedengo-common/blob/main/doc/HOWTO.md#installing-local-environment-local-sonarqube
  - => à suivre jusqu'à la section "HOWTO reinstall SonarQube (if needed)" (sauf la commande "mvn verify" à ne pas lancer ... pas besoin de lancer les tests)
  - => OBJECTIF : avoir un SonarQube local avec le plugin creedengo-python installé et fonctionnel
- 2 - lancer l'analyse d'un projet de test "environnement réel" :
  - aller dans le répertoire du projet présent dans les source de TI (ex : src/it/test-projects/creedengo-python-plugin-test-project)
  - lancer le shell permettant de faire l'analyse : ./tool_send_to_sonar.sh <SONARQUBE_PORT> <SONARQUBE_TOKEN>
  - vérifier que l'analyse est bien présente dans le SonarQube local (via l'IHM web)
- 3 - builder la partie IHM du plugin creedengo-dashboard
  - aller dans le répertoire "creedengo-dashboard"
  - lancer la commande : ./tool_build.sh
- 4 - builder le plugin sonar "creedengo-dashboard" en local
  - aller dans le répertoire creedengo-dashboard/apps/sonar-plugin
  - lancer la commande : ./tool_build.sh
- 5 installer le plugin buildé dans le sonarqube local (ex : celui de creedengo-python qui est déjà lancé et dispo)
  - lancer la commande : ./tool_docker-install-plugin.sh (WARNING : il y a en dur en param!ètres interne le fait qu'on utilise le plugin creedengo-python comme support, notamment référence au nom du sonar local pour le python => à variabiliser plus tard)
  - lancer la commande de log pour vérifier le bon redémarrage du sonar : ./tool_docker-logs.sh (dans le répertoire "creedengo-python" car c'est lui qui pilote le Soanrqube)
  - WARNING : le sonarqube a changé de port !!! (rechecker le port dans docker pour pouvoir se reconnecter à l'IHM web - les credentials sont conservés et l'analyse projet aussi)
  - vérifier via l'IHM web que le plugin creedengo-dashboard est bien installé (dans le marketplace de la partier d'admin de sonarqube)
  - vérifier que le plugin fonctionne sur l'IHM : aller dans le projet analysé, puis menu "More"