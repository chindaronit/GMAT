from ApiMethods import *
from GateWayResponses import *

def transaction_api(client, apiId, rootResourceId, authorizationType, contentType, Model, url):
    
    # ***************************************************************
    #                     /transactions GET & POST
    # ***************************************************************
    
    transactionResourceId = create_resource(client, apiId, rootResourceId, "transactions")

    # /transactions GET (Get transaction by txnId)
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    transaction_get_url = url + 'transactions/'
    type = 'HTTP'
    passthroughBehavior = "WHEN_NO_MATCH"
    requestModels = {}

    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.userId': True,
        'method.request.querystring.txnId': True,
    }
    putMethod(client, apiId, authorizationType, transactionResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.userId':'method.request.querystring.userId',
        'integration.request.querystring.txnId':'method.request.querystring.txnId'
    }
    putIntegration(client, apiId, httpMethod, transactionResourceId, type, integrationHttpMethod, transaction_get_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, transactionResourceId, httpMethod, contentType, Model)

    # Add responses for status codes 500, 400, 401
    status_codes = ['500', '400', '401','404']
    for statusCode in status_codes:
        other_response(client, apiId, transactionResourceId, httpMethod, statusCode, contentType, Model)
    

    # /transactions POST (Add new transaction)
    httpMethod = 'POST'
    integrationHttpMethod = 'POST'
    transaction_post_url = url + 'transactions/'
    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.userId': True,
    }
    putMethod(client, apiId, authorizationType, transactionResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.userId':'method.request.querystring.userId',
    }
    putIntegration(client, apiId, httpMethod, transactionResourceId, type, integrationHttpMethod, transaction_post_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, transactionResourceId, httpMethod, contentType, Model)

    status_codes = ['500', '400', '401', '404']
    for statusCode in status_codes:
        other_response(client, apiId, transactionResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/ GET and POST methods...")

    # ***************************************************************
    #                     /transactions/all/month GET
    # ***************************************************************
    
    monthResourceId = create_resource(client, apiId, transactionResourceId, "all")
    monthResourceId = create_resource(client, apiId, monthResourceId, "month")
    
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_month_url = url + 'transactions/all/month/'
    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.userId': True,
        'method.request.querystring.month': True,
        'method.request.querystring.year': True,
    }
    putMethod(client, apiId, authorizationType, monthResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.userId': 'method.request.querystring.userId',
        'integration.request.querystring.month': 'method.request.querystring.month',
        'integration.request.querystring.year': 'method.request.querystring.year'
    }
    putIntegration(client, apiId, httpMethod, monthResourceId, type, integrationHttpMethod, all_month_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, monthResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, monthResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/month/ GET method...")

    # ***************************************************************
    #                     /transactions/recenttransaction GET
    # ***************************************************************
    
    recentTransactionResourceId = create_resource(client, apiId, transactionResourceId, "recenttransaction")
    
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    recent_transaction_url = url + 'transactions/recenttransaction/'
    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.userId': True,
    }
    putMethod(client, apiId, authorizationType, recentTransactionResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.userId': 'method.request.querystring.userId',
    }
    putIntegration(client, apiId, httpMethod, recentTransactionResourceId, type, integrationHttpMethod, recent_transaction_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, recentTransactionResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, recentTransactionResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/recenttransaction/ GET method...")

    # ***************************************************************
    #                     /transactions/recentmerchanttransaction GET
    # ***************************************************************
    
    recentMerchantTransactionResourceId = create_resource(client, apiId, transactionResourceId, "recentmerchanttransaction")
    
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    recent_merchant_transaction_url = url + 'transactions/recentmerchanttransaction/'
    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.vpa': True,
    }
    putMethod(client, apiId, authorizationType, recentMerchantTransactionResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.vpa': 'method.request.querystring.vpa',
    }
    putIntegration(client, apiId, httpMethod, recentMerchantTransactionResourceId, type, integrationHttpMethod, recent_merchant_transaction_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, recentMerchantTransactionResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, recentMerchantTransactionResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/recentmerchanttransaction/ GET method...")

    # ***************************************************************
    #                     /transactions/all/merchant GET
    # ***************************************************************
    
    payeeResourceId = create_resource(client, apiId, transactionResourceId, "all")
    payeeResourceId = create_resource(client, apiId, payeeResourceId, "merchant")
    
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_payee_url = url + 'transactions/all/merchant/'
    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.vpa': True,
        'method.request.querystring.month': True,
        'method.request.querystring.year': True,
    }
    putMethod(client, apiId, authorizationType, payeeResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.vpa': 'method.request.querystring.vpa',
        'integration.request.querystring.month': 'method.request.querystring.month',
        'integration.request.querystring.year': 'method.request.querystring.year'
    }
    putIntegration(client, apiId, httpMethod, payeeResourceId, type, integrationHttpMethod, all_payee_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, payeeResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, payeeResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/merchant/ GET method...")

    # ***************************************************************
    #                     /transactions/all/gstin GET
    # ***************************************************************
    
    gstinResourceId = create_resource(client, apiId, transactionResourceId, "all")
    gstinResourceId = create_resource(client, apiId, gstinResourceId, "gstin")
    
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_gstin_url = url + 'transactions/all/gstin/'
    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.gstin': True,
    }
    putMethod(client, apiId, authorizationType, gstinResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.gstin': 'method.request.querystring.gstin',
    }
    putIntegration(client, apiId, httpMethod, gstinResourceId, type, integrationHttpMethod, all_gstin_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, gstinResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, gstinResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/gstin/ GET method...")

    # ***************************************************************
    #                     /transactions/all/gstin/year GET
    # ***************************************************************
    
    yearResourceId = create_resource(client, apiId, gstinResourceId, "year")
    
    httpMethod = 'GET'
    integrationHttpMethod = 'GET'
    all_gstin_year_url = url + 'transactions/all/gstin/year/'
    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.gstin': True,
        'method.request.querystring.year': True,
    }
    putMethod(client, apiId, authorizationType, yearResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.gstin': 'method.request.querystring.gstin',
        'integration.request.querystring.year': 'method.request.querystring.year',
    }
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
    requestParameters = {
        'method.request.header.authorization': True,
        'method.request.querystring.gstin': True,
        'method.request.querystring.month': True,    
        'method.request.querystring.year': True,    
    }
    putMethod(client, apiId, authorizationType, monthResourceId, httpMethod, requestParameters, requestModels)
    requestParameters = {
        'integration.request.header.authorization': 'method.request.header.authorization',
        'integration.request.querystring.gstin':'method.request.querystring.gstin',
        'integration.request.querystring.month':'method.request.querystring.month',
        'integration.request.querystring.year':'method.request.querystring.year'
    }
    putIntegration(client, apiId, httpMethod, monthResourceId, type, integrationHttpMethod, all_gstin_month_url, passthroughBehavior, requestParameters)
    succ_response(client, apiId, monthResourceId, httpMethod, contentType, Model)

    for statusCode in status_codes:
        other_response(client, apiId, monthResourceId, httpMethod, statusCode, contentType, Model)

    print("Successfully created /transactions/all/gstin/month/ GET method...")
