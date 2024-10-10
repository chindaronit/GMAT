from ApiMethods import put_integration_response,put_method_response

def succ_response(client,apiId,resourceId,httpMethod,contentType,Model):
    statusCode='200'
    responseParameters = {    
        'method.response.header.Access-Control-Allow-Origin': True,
    }

    responseTemplates={}
    selectionPattern=statusCode
    put_method_response(client,apiId,resourceId,httpMethod,statusCode,contentType,Model,responseParameters)
    responseParameters = {
        'method.response.header.Access-Control-Allow-Origin': "'*'",
    }
    put_integration_response(client,apiId,resourceId,httpMethod,statusCode,selectionPattern,responseParameters,responseTemplates)

def other_response(client,apiId,resourceId,httpMethod,statusCode,contentType,Model):
    responseParameters = {}
    responseTemplates={}
    selectionPattern=statusCode
    put_method_response(client,apiId,resourceId,httpMethod,statusCode,contentType,Model,responseParameters)
    put_integration_response(client,apiId,resourceId,httpMethod,statusCode,selectionPattern,responseParameters,responseTemplates)