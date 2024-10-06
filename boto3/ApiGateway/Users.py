from ApiMethods import *
from GateWayResponses import *

def user_api(client, apiId, rootResourceId, authorizationType, contentType, Model, url):

    # ***************************************************************
    #                     /user POST & PUT
    # ***************************************************************
    
    userResourceId = create_resource(client, apiId, rootResourceId, "users")

    # /user POST
    httpMethod = 'POST'
    integrationHttpMethod = 'POST'
    user_post_url = url + 'users/'
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
    user_put_url = url + 'users/'
    putMethod(client, apiId, authorizationType, userResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, userResourceId, type, integrationHttpMethod, user_put_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, userResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, userResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /users/ POST and PUT methods...")

    # ***************************************************************
    #                     /user/get/vpa GET
    # ***************************************************************

    # Create the "get" resource only once
    getResourceId = create_resource(client, apiId, userResourceId, "get")

    # Create "vpa" under "get"
    vpaResourceId = create_resource(client, apiId, getResourceId, "vpa")

    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    user_get_vpa_url = url + 'users/get/vpa/'
    putMethod(client, apiId, authorizationType, vpaResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, vpaResourceId, type, integrationHttpMethod, user_get_vpa_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, vpaResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, vpaResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /users/get/vpa/ GET method...")

    # ***************************************************************
    #                     /user/get/ph GET
    # ***************************************************************

    # Reuse the "get" resource and create "ph" under "get"
    phoneResourceId = create_resource(client, apiId, getResourceId, "ph")

    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    user_get_ph_url = url + 'users/get/ph/'
    putMethod(client, apiId, authorizationType, phoneResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, phoneResourceId, type, integrationHttpMethod, user_get_ph_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, phoneResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, phoneResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /users/get/ph GET method...")
