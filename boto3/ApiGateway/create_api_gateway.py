import boto3
from ApiMethods import *
from GateWayResponses import *
from Transactions import transaction_api
from Users import user_api
from Rewards import leaderboard_api

# Create a client for the AWS API Gateway service
client = boto3.client('apigateway')

# Define variables for the API Gateway setup
name = 'GMAT'
description = 'API Gateway for transaction, user, and leaderboard microservices in GMAT'
version = 'v-1'
endpointConfigurationType = 'EDGE'
authorizationType = 'NONE'
contentType = 'application/json'
Model = 'Empty'
stageName = 'dev'
base_url = ''

print("Started creating API Gateway for transaction, user, and leaderboard services...")

# Create the API in API Gateway
apiId = create_api(client, name, description, version, endpointConfigurationType)
resourceId = get_parent_id(client, apiId, '/')  # Get the root resource ID

# ***************************************************************
#                     /transaction
# ***************************************************************
# Call the `transaction_api` function to set up all routes related to transactions.
transaction_api(client, apiId, resourceId, authorizationType, contentType, Model, base_url)

# ***************************************************************
#                     /user
# ***************************************************************
# Call the `user_api` function to set up all routes related to users.
user_api(client, apiId, resourceId, authorizationType, contentType, Model, base_url)

# ***************************************************************
#                     /leaderboard
# ***************************************************************users/get/ph/
# Call the `leaderboard_api` function to set up all routes related to leaderboard.
leaderboard_api(client, apiId, resourceId, authorizationType, contentType, Model, base_url)

# ***************************************************************
#                     Deployment
# ***************************************************************
create_deployement(client, apiId, stageName)
print("successfully Deployed API...")
deployment_url = f"https://{apiId}.execute-api.ap-south-1.amazonaws.com/{stageName}/"
print(f"Successfully deployed API to stage '{stageName}' at endpoint: {deployment_url}")
