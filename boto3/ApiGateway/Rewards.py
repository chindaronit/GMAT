from ApiMethods import *
from GateWayResponses import *

def leaderboard_api(client, apiId, rootResourceId, authorizationType, contentType, Model, url):
    
    # ***************************************************************
    #                     /leaderboard PUT & GET
    # ***************************************************************
    
    leaderboardResourceId = create_resource(client, apiId, rootResourceId, "leaderboard")

    # /leaderboard PUT
    httpMethod = 'PUT'
    integrationHttpMethod = 'PUT'
    leaderboard_put_url = url + 'leaderboard'
    type = 'HTTP'
    passthroughBehavior = "WHEN_NO_MATCH"
    requestModels = {}
    requestParameters = {}

    putMethod(client, apiId, authorizationType, leaderboardResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, leaderboardResourceId, type, integrationHttpMethod, leaderboard_put_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, leaderboardResourceId, httpMethod, contentType, Model)

    # Add error responses for status codes 500, 400, 401
    status_codes = ['500', '400', '401']
    for statusCode in status_codes:
        other_response(client, apiId, leaderboardResourceId, httpMethod, statusCode, contentType, Model)

    # /leaderboard GET
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    leaderboard_get_url = url + 'leaderboard'
    putMethod(client, apiId, authorizationType, leaderboardResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, leaderboardResourceId, type, integrationHttpMethod, leaderboard_get_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, leaderboardResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, leaderboardResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /leaderboard/ PUT and GET methods...")

    # ***************************************************************
    #                     /leaderboard/all GET
    # ***************************************************************

    allResourceId = create_resource(client, apiId, leaderboardResourceId, "all")
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    leaderboard_all_url = url + 'leaderboard/all'
    putMethod(client, apiId, authorizationType, allResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, allResourceId, type, integrationHttpMethod, leaderboard_all_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, allResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, allResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /leaderboard/all/ GET method...")
