sudo apt-get update
sudo apt-get upgrade
wget https://download.teamviewer.com/download/linux/teamviewer-host_armhf.deb
sudo dpkg -i teamviewer-host_armhf.deb
sudo apt --fix-broken instally


https://www.jetbrains.com/idea/download/
comunity >run /bin/idea.sh

sudo apt-get install chromium-browser –yes

sudo apt-get install maven

sudo apt install postgresql libpq-dev postgresql-client postgresql-client-common -y
sudo su postgres
createuser pi -P --interactive
psql
create database doghouse;
psql doghouse
creat tables;
ALTER USER postgres PASSWORD 'myPassword';
sudo apt install pgadmin3