import boto3 

user_file = open("./instance_script.sh", "r")
user_src = '\n'.join(user_file)

ec2=boto3.client('ec2')

def create_instance(type,server,src):
    instance = ec2.run_instances(
        ImageId='ami-078264b8ba71bc45e',
        InstanceType=type,
        MaxCount=1,
        MinCount=1,
        KeyName='gmatKeyPair',
        SecurityGroups=['default'],
        IamInstanceProfile={
            'Name': 'ec2s3Connector'
        },
        UserData=src
    ) 
    InstanceId=instance['Instances'][0]['InstanceId']
    ec2_r = boto3.resource('ec2')
    instance=ec2_r.Instance(InstanceId)
    while(instance.state['Name']!='running'):
        instance=ec2_r.Instance(InstanceId)

    print(server + ' Server Created!')
    print("endpoint : "+ ec2.describe_instances(InstanceIds=[InstanceId])['Reservations'][0]['Instances'][0]['PublicDnsName']) 

create_instance('t2.micro','gmat-server',user_src)