from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi

# Replace <db_password> with your actual password, URL-encoded if it contains special characters
password = "u_A4k.Ckn9QD_Tj"
uri = f"mongodb+srv://emmalujosephwork:{password}@cluster0.vux0o.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

# Create a new client and connect to the server
client = MongoClient(uri, server_api=ServerApi('1'))

# Send a ping to confirm a successful connection
try:
    client.admin.command('ping')
    print("Pinged your deployment. You successfully connected to MongoDB!")
except Exception as e:
    print("Connection failed:", e)
