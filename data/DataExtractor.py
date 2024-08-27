import pandas as pd
from sqlalchemy import create_engine
import requests
import json
import uuid

class DataExtractor:
    def __init__(self, engine, api_url, api_key):
        self.engine = engine
        self.api_url = api_url
        self.api_key = api_key

    def extract_sales_data(self):
        query = """
        SELECT o.*, oi.product_uuid, oi.quantity
        FROM `order` o
        JOIN order_item oi ON o.uuid = oi.order_uuid
        WHERE o.status = 'PROCESSED'
        """
        try:
            with self.engine.connect() as conn:
                sales_data = pd.read_sql(query, conn)
                
            # print(f"Colonnes disponibles dans les données : {sales_data.columns.tolist()}")

            required_columns = ['created_at', 'total_ttc', 'total_ht', 'product_uuid', 'quantity']
            missing_columns = [col for col in required_columns if col not in sales_data.columns]
            if missing_columns:
                raise ValueError(f"Les colonnes requises sont manquantes dans les données: {missing_columns}")

            sales_data['created_at'] = pd.to_datetime(sales_data['created_at'])
            sales_data['vat'] = sales_data['total_ttc'] - sales_data['total_ht']
            sales_data['revenue'] = sales_data['total_ht']
            sales_data['total'] = sales_data['total_ttc']

            product_details = []
            grouped_sales_data = sales_data.groupby('product_uuid')

            for product_uuid, group in grouped_sales_data:
                try:
                    product_info = self.fetch_product_details(product_uuid)
                    if product_info is not None:
                        product_info['quantity'] = group['quantity'].sum()
                        product_details.append(product_info)
                except json.JSONDecodeError as e:
                    print(f"Erreur lors de l'analyse des items JSON: {e}")

            product_data = pd.DataFrame(product_details)
            
            return sales_data, product_data
        except Exception as e:
            print(f"Une erreur est survenue lors de l'extraction des données: {e}")
            return pd.DataFrame(), pd.DataFrame()

    
    def extract_admin_users(self):
        query = """
        SELECT email
        FROM user
        JOIN user_role ON user.uuid = user_role.user_uuid
        JOIN role ON user_role.role_id = role.id
        WHERE role.name IN ('ROLE_ADMIN', 'ROLE_STORE_MANAGER')
        """
        with self.engine.connect() as conn:
            admin_users = pd.read_sql(query, conn)
        return admin_users['email'].tolist()

    
    def fetch_product_details(self, product_uuid):
        try:
            product_uuid = str(uuid.UUID(bytes=product_uuid))
            headers = {'X-API-KEY': self.api_key}
            response = requests.get(f"{self.api_url}/api/catalogue/products/{product_uuid}", headers=headers)
            
            if response.status_code == 200:
                print(f"Produit {product_uuid} trouvé.")
                product_info = response.json()
                return product_info
            else:
                print(f"Produit {product_uuid} non trouvé.")
                return {}
        except ValueError as e:
            print(f"Erreur de conversion UUID pour {product_uuid}: {e}")
            return {}
