
def create_api(client,name,description,version,types):
    response = client.create_rest_api(
        name=name,
        description=description,
        version=version,
        endpointConfiguration={'types': [types]}
    )
    return response['id']

def create_resource(client,apiId,parentId,pathPart):
    response=client.create_resource(
        restApiId=apiId,
        parentId=parentId,
        pathPart=pathPart
    )
    return response['id']

def get_parent_id(client,apiId,parent_resource_path):
    response = client.get_resources(
        restApiId=apiId,
        limit=500  # Adjust the limit as needed to list all resources
    )

    parent_resource_id=''
    # Iterate through the resources to find the parent resource
    for resource in response['items']:
        if resource['path'] == parent_resource_path:
            parent_resource_id = resource['id']
            break
        else:
            # Handle the case where the parent resource is not found
            print(f"Parent resource with path '{parent_resource_path}' not found.")

    return parent_resource_id

def putMethod(client,apiId,authorizationType,resourceId,httpMethod,requestParameters,requestModels): 
    client.put_method(
        restApiId=apiId,
        authorizationType=authorizationType,
        resourceId=resourceId,  
        httpMethod=httpMethod,
        requestParameters=requestParameters,
        requestModels=requestModels
    )

def putIntegration(client,apiId,httpMethod,resourceId,type,integrationHttpMethod,url,passthroughBehavior,requestParameters):    
    client.put_integration(
        restApiId=apiId,
        resourceId=resourceId,  
        httpMethod=httpMethod,
        type=type,
        integrationHttpMethod=integrationHttpMethod,
        uri=url,
        passthroughBehavior=passthroughBehavior,
        requestParameters=requestParameters
    )

def put_method_response(client,apiId,resourceId,httpMethod,statusCode,contentType,Model,responseParameters):
    client.put_method_response(
        restApiId=apiId,
        resourceId=resourceId,
        httpMethod=httpMethod,
        statusCode=statusCode,
        responseModels={
            contentType: Model,
        },
        responseParameters=responseParameters
    ) 

def put_integration_response(client,apiId,resourceId,httpMethod,statusCode,selectionPattern,responseParameters,responseTemplates):
    client.put_integration_response(
        restApiId=apiId,
        resourceId=resourceId,
        httpMethod=httpMethod,
        statusCode=statusCode,
        selectionPattern=selectionPattern,
        responseParameters=responseParameters,
        responseTemplates=responseTemplates
    )


def create_deployement(client,apiId,stageName):
    client.create_deployment(
        restApiId=apiId,
        stageName=stageName,
    )
    print("Stage Deployed successfully...")