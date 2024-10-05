#!/bin/bash
sudo mkdir server
sudo aws s3 sync s3://skywalkerinflux/server/ gmat-server
cd server