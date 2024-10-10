import boto3
import os

client = boto3.client('s3')

path='server/'
bucket="gmatserverbucket"

# enumerate local files recursively
for root, dirs, files in os.walk(path):

  for filename in files:

    # construct the full local path
    local_path = os.path.join(root, filename)

    # construct the full Dropbox path
    relative_path = os.path.relpath(local_path, path)
    s3_path = os.path.join('server', relative_path)

    # relative_path = os.path.relpath(os.path.join(root, filename))

    print ('Searching "%s" in "%s"' % (s3_path, bucket))
    try:
        client.head_object(Bucket=bucket, Key=s3_path)
        print("Path found on S3! Skipping %s..." % s3_path)
        
    except:
        print("Uploading %s..." % s3_path)
        client.upload_file(local_path, bucket, s3_path)


