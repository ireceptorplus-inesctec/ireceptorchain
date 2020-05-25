cd ..
cd ..
git stash
git revert 6e6b58d5cf1e83d5c6665419844e31135240acf2
git revert 1712a787a4a7e327afedf8cd82566b115b854b97

sudo chmod 777 -R fabric-samples
cd fabric-samples/fabcar
sudo ./startFabric.sh java
cd javascript
sudo ./runClientOps.sh
cd ..
