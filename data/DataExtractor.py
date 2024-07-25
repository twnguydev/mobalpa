import mysql.connector
import pandas as pd
import json
import requests

class DataExtractor:
    def __init__(self, host, user, password, database, api_url):
        self.host = host
        self.user = user
        self.password = password
        self.database = database
        self.api_url = api_url
    
    def extract_sales_data(self):
        conn = mysql.connector.connect(
            host=self.host,
            user=self.user,
            password=self.password,
            database=self.database
        )
        query = """
        SELECT uuid, createdAt as date, items, totalHt, totalTtc, status 
        FROM orders 
        WHERE status = 'completed'
        """
        sales_data = pd.read_sql(query, conn)
        conn.close()
        
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
        conn = mysql.connector.connect(
            host=self.host,
            user=self.user,
            password=self.password,
            database=self.database
        )
        query = "SELECT email FROM user WHERE role IN ('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')"
        admin_users = pd.read_sql(query, conn)
        conn.close()
        return admin_users['email'].tolist()
    
    def fetch_product_details(self, product_id):
        response = requests.get(f"{self.api_url}/products/{product_id}")
        if response.status_code == 200:
            return response.json()
        else:
            return {}