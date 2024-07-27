import pandas as pd
from sqlalchemy import create_engine
import requests
import json

class DataExtractor:
    def __init__(self, engine, api_url, api_key):
        self.engine = engine
        self.api_url = api_url
        self.api_key = api_key
    
    def extract_sales_data(self):
        query = """
        SELECT *
        FROM order 
        WHERE status = 'COMPLETED'
        """
        with self.engine.connect() as conn:
            sales_data = pd.read_sql(query, conn)
        
        sales_data['date'] = pd.to_datetime(sales_data['date'])
        sales_data['vat'] = sales_data['totalTtc'] - sales_data['totalHt']
        sales_data['revenue'] = sales_data['totalHt']
        sales_data['total'] = sales_data['totalTtc']

        product_details = []
        for items in sales_data['items']:
            item_list = json.loads(items)
            for item in item_list:
                product_info = self.fetch_product_details(item['product_id'])
                product_info['quantity'] = item['quantity']
                product_details.append(product_info)

        product_data = pd.DataFrame(product_details)
        return sales_data, product_data
    
    def extract_admin_users(self):
        query = "SELECT email FROM user WHERE role IN ('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')"
        with self.engine.connect() as conn:
            admin_users = pd.read_sql(query, conn)
        return admin_users['email'].tolist()
    
    def fetch_product_details(self, product_id):
        headers = {'X-API-KEY': self.api_key}
        response = requests.get(f"{self.api_url}/products/{product_id}", headers=headers)
        if response.status_code == 200:
            return response.json()
        else:
            return {}
