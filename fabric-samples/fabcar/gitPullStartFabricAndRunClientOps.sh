cd ..
cd ..
git stash
git pull
sudo chmod 777 -R fabric-samples
cd fabric-samples/fabcar
sudo ./startFabric.sh java
cd javascript
sudo ./runClientOps.sh
cd ..
