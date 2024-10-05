from ApiMethods import *
from GateWayResponses import *

def user_api(client, apiId, rootResourceId, authorizationType, contentType, Model, url):

    # ***************************************************************
    #                     /user POST & PUT
    # ***************************************************************
    
    userResourceId = create_resource(client, apiId, rootResourceId, "user")

    # /user POST
    httpMethod = 'POST'
    integrationHttpMethod = 'POST'
    user_post_url = url + 'user'
    type = 'HTTP'
    passthroughBehavior = "WHEN_NO_MATCH"
    requestModels = {
        "application/json": Model,
    }
    requestParameters = {}

    putMethod(client, apiId, authorizationType, userResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, userResourceId, type, integrationHttpMethod, user_post_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, userResourceId, httpMethod, contentType, Model)

    # Add error responses for status codes 500, 400, 401
    status_codes = ['500', '400', '401']
    for statusCode in status_codes:
        other_response(client, apiId, userResourceId, httpMethod, statusCode, contentType, Model)

    # /user PUT
    httpMethod = 'PUT'
    integrationHttpMethod = 'PUT'
    user_put_url = url + 'user'
    putMethod(client, apiId, authorizationType, userResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, userResourceId, type, integrationHttpMethod, user_put_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, userResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, userResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /user POST and PUT methods...")

    # ***************************************************************
    #                     /user/vpa GET
    # ***************************************************************

    vpaResourceId = create_resource(client, apiId, vpaResourceId, "vpa")

    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    user_get_vpa_url = url + 'user/vpa'
    putMethod(client, apiId, authorizationType, vpaResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, vpaResourceId, type, integrationHttpMethod, user_get_vpa_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, vpaResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, vpaResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /user/vpa GET method...")

    # ***************************************************************
    #                     /user/ph GET
    # ***************************************************************

    phoneResourceId = create_resource(client, apiId, phoneResourceId, "ph")

    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    user_get_ph_url = url + 'user/ph'
    putMethod(client, apiId, authorizationType, phoneResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, phoneResourceId, type, integrationHttpMethod, user_get_ph_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, phoneResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, phoneResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /user/ph GET method...")