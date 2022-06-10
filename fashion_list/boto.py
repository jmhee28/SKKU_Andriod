import boto3, io, json
from flask import Flask
from flask import request
from flask import jsonify
from werkzeug.serving import WSGIRequestHandler
WSGIRequestHandler.protocol_version = "HTTP/1.1"

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

file_name="fasion.txt"   
bucket_name=""  # Your bucket name
file_key="fasion.txt"    # Same with file name

@app.route("/upload", methods=['GET'])
def upload():
    global file_name, bucket_name, file_key
    
    s3 = boto3.client("s3")

    # case 1 : upload your local file
    s3.upload_file(
	    Filename=file_name,
	    Bucket=bucket_name,
	    Key=file_key,
    )

    # case 2 : upload new file by stringIO body
    contents="My string to save to s3 object"
    fake_handle=io.StringIO(contents)
    s3.put_object(Bucket=bucket_name, Key="stringIO_test.txt", Body=fake_handle.read())

    print("upload finish")
    return jsonify("upload finish")


@app.route("/access", methods=['GET'])
def access_dat():
    global file_name, bucket_name, file_key

    temperature = request.args.get('temperature')    # Input name for searching region

    s3 = boto3.client("s3")
    obj = s3.get_object(
        Bucket=bucket_name,
        Key=file_key,
    )
    if '-' in temperature:
        temperature = '1'


    file_data = io.BytesIO(obj["Body"].read())
    region = "Search failed"
    i=0
    for byte_line in file_data:
        i+=1
        line = byte_line.decode().rstrip()
        line_arr = line.split('=')
        temperature_range = line_arr[0].split('~')
        range_start =temperature_range[0]
        range_end = temperature_range[1]
        if range_end >= temperature >= range_start:
            region = line_arr[1]
            return jsonify(region), 200

    return jsonify(region), 200


if __name__ == "__main__":
    app.run(host='localhost', port=8888)
