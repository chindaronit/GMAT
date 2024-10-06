from ApiMethods import *
from GateWayResponses import *

def transaction_api(client, apiId, rootResourceId, authorizationType, contentType, Model, url):
    
    # ***************************************************************
    #                     /transaction GET & POST
    # ***************************************************************
    
    transactionResourceId = create_resource(client, apiId, rootResourceId, "transactions")

    # /transaction GET
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    transaction_get_url = url + 'transactions/'
    type = 'HTTP'
    passthroughBehavior = "WHEN_NO_MATCH"
    requestModels = {}
    requestParameters = {}
    putMethod(client, apiId, authorizationType, transactionResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, transactionResourceId, type, integrationHttpMethod, transaction_get_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, transactionResourceId, httpMethod, contentType, Model)

    # Add responses for status codes 500, 400, 401
    status_codes = ['500', '400', '401']
    for statusCode in status_codes:
        other_response(client, apiId, transactionResourceId, httpMethod, statusCode, contentType, Model)
    
    # /transaction POST
    httpMethod = 'POST'
    integrationHttpMethod = 'POST'
    transaction_post_url = url + 'transactions/'
    putMethod(client, apiId, authorizationType, transactionResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, transactionResourceId, type, integrationHttpMethod, transaction_post_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, transactionResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, transactionResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/ GET and POST methods...")

    # ***************************************************************
    #                     /transaction/all/user GET
    # ***************************************************************

    resourceId = create_resource(client, apiId, transactionResourceId, "all")
    userResourceId = create_resource(client, apiId, resourceId, "user")
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_user_url = url + 'transactions/all/user/'
    putMethod(client, apiId, authorizationType, userResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, userResourceId, type, integrationHttpMethod, all_user_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, userResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, userResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/user/ GET method...")

    # ***************************************************************
    #                     /transaction/all/month GET
    # ***************************************************************

    monthResourceId = create_resource(client, apiId, resourceId, "month")
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_month_url = url + 'transactions/all/month/'
    putMethod(client, apiId, authorizationType, monthResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, monthResourceId, type, integrationHttpMethod, all_month_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, monthResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, monthResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/month/ GET method...")

    # ***************************************************************
    #                     /transaction/all/payee GET
    # ***************************************************************

    payeeResourceId = create_resource(client, apiId, resourceId, "payee")
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_payee_url = url + 'transactions/all/payee/'
    putMethod(client, apiId, authorizationType, payeeResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, payeeResourceId, type, integrationHttpMethod, all_payee_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, payeeResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, payeeResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/payee/ GET method...")

    # ***************************************************************
    #                     /transaction/all/payerpayee GET
    # ***************************************************************

    payerPayeeResourceId = create_resource(client, apiId, resourceId, "payerpayee")
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_payerpayee_url = url + 'transactions/all/payerpayee/'
    putMethod(client, apiId, authorizationType, payerPayeeResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, payerPayeeResourceId, type, integrationHttpMethod, all_payerpayee_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, payerPayeeResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, payerPayeeResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/payerpayee/ GET method...")

    # ***************************************************************
    #                     /transaction/all/gstin/year GET
    # ***************************************************************

    gstinResourceId = create_resource(client, apiId, resourceId, "gstin")
    yearResourceId = create_resource(client, apiId, gstinResourceId, "year")
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_gstin_year_url = url + 'transactions/all/gstin/year/'
    putMethod(client, apiId, authorizationType, yearResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, yearResourceId, type, integrationHttpMethod, all_gstin_year_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, yearResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, yearResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/gstin/year/ GET method...")

    # ***************************************************************
    #                     /transaction/all/gstin/month GET
    # ***************************************************************

    monthResourceId = create_resource(client, apiId, gstinResourceId, "month")
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_gstin_month_url = url + 'transactions/all/gstin/month/'
    putMethod(client, apiId, authorizationType, monthResourceId, httpMethod, requestParameters, requestModels)
    putIntegration(client, apiId, httpMethod, monthResourceId, type, integrationHttpMethod, all_gstin_month_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, monthResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, monthResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/gstin/month/ GET method...")
