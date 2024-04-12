import os
import openai
import requests
from dotenv import load_dotenv

load_dotenv()
API_KEY = os.getenv('API_KEY')
url = 'https://api.openai.com/v1/chat/completions'
headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + API_KEY}
data = {"model": "gpt-3.5-turbo", "messages": [{"role": "user", "content": "Rozumiesz język polski?"}]}
r = requests.post(url, headers=headers, json=data)
print(r.text)
