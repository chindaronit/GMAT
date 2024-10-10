#!/bin/bash
mkdir server
sudo aws s3 sync s3://gmatserverbucket/server/ server
cd server
sudo yum install -y npm
sudo npm install
sudo npm start