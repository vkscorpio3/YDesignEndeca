
Prerequisites
	1.  Apache Maven Maven 3.x or higher (on your local machine where you'll do the build)

Instrucions:

#This will build all the submodules of this project
mvn clean package

#Example deployment to dev-orend10
#On your local machine where you have YDesignEndeca checked out from source control
cd YDesignEndeca
mvn clean package


#copy it to dev server under /tmp/YOUR_USERNAME,  for example:
#ssh efordelon@orend10.ydesigngroup.net 'mkdir -p /tmp/`whoami`'
scp dev-orend10/distribution/target/endeca-distribution-dev-orend10.zip  efordelon@orend10.ydesigngroup.net:/tmp/efordelon

#ssh to orend10, for example:
ssh efordelon@orend10.ydesigngroup.net 


#on orend10.ydesigngroup.net box as yourself
cd /tmp/`whoami`/

#backup previous directory if any
mv /tmp/`whoami`/endeca /tmp/`whoami`/endeca-`date +%m.%d.%Y.%H.%M`

unzip /tmp/`whoami`/endeca-distribution-dev-orend10.zip -d /tmp/`whoami`/
sudo su - endeca


#as endeca, copy whatever the previous directory was to /opt/endeca/apps/YDesignStores/.  For example, /tmp/efordelon would be:
cp -r /tmp/efordelon/endeca/YDesignStores/* /opt/endeca/apps/YDesignStores/

#run set templates
/opt/endeca/apps/YDesignStores/control/set_templates.sh 

#run baseline update
/opt/endeca/apps/YDesignStores/control/load_baseline_test_data.sh
/opt/endeca/apps/YDesignStores/control/baseline_update.sh

#restart Tools and Frameworks since that's where the assembler war is deployed
/opt/endeca/ToolsAndFrameworks/11.1.0/server/bin/shutdown.sh

#wait a few seconds before starting it back up
sleep 5
/opt/endeca/ToolsAndFrameworks/11.1.0/server/bin/startup.sh
