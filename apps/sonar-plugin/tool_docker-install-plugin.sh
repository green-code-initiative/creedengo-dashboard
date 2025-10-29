#!/usr/bin/env sh

NOM_CONTAINER=sonar_creedengo_dashboard
REPERTOIRE_EXTENSIONS=/opt/sonarqube/extensions/plugins/.

echo "********** AVANT installation du plugin (répertoire '') :"
docker exec $NOM_CONTAINER ls $REPERTOIRE_EXTENSIONS

echo "********** Installation du plugin 'creedengo-dashboard' :"
docker cp target/creedengo-dashboard-plugin-*.jar $NOM_CONTAINER:$REPERTOIRE_EXTENSIONS

echo "********** Installation du plugin 'creedengo-python' :"
docker cp ../creedengo-python-plugin-*.jar $NOM_CONTAINER:$REPERTOIRE_EXTENSIONS

echo "********** APRES installation du plugin :"
docker exec $NOM_CONTAINER ls $REPERTOIRE_EXTENSIONS

echo "********** Redémarrage du container pour prise en compte du nouveau plugin"
docker restart $NOM_CONTAINER
