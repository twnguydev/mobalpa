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
        SELECT o.created_at, o.total_ttc, o.total_ht, o.vat, o.delivery_fees, o.reduction, oi.product_uuid, oi.quantity
        FROM `order` o
        JOIN order_item oi ON o.uuid = oi.order_uuid
        WHERE o.status = 'PROCESSED'
        """
        try:
            with self.engine.connect() as conn:
                sales_data = pd.read_sql(query, conn)

            required_columns = ['created_at', 'total_ttc', 'total_ht', 'vat', 'delivery_fees', 'reduction', 'product_uuid', 'quantity']
            missing_columns = [col for col in required_columns if col not in sales_data.columns]
            if missing_columns:
                raise ValueError(f"Les colonnes requises sont manquantes dans les données: {missing_columns}")

            sales_data['created_at'] = pd.to_datetime(sales_data['created_at'])
            sales_data['total_ht'] = sales_data['total_ht'].astype(float)
            sales_data['total_ttc'] = sales_data['total_ttc'].astype(float)
            sales_data['vat'] = sales_data['vat'].astype(float)
            sales_data['delivery_fees'] = sales_data['delivery_fees'].astype(float)
            sales_data['reduction'] = sales_data['reduction'].astype(float)
            sales_data['quantity'] = sales_data['quantity'].astype(int)

            if sales_data.isnull().any().any():
                print("Données manquantes détectées. Veuillez vérifier les colonnes suivantes pour les valeurs manquantes:")
                print(sales_data.isnull().sum())

            product_details = []
            grouped_sales_data = sales_data.groupby('product_uuid')

            for product_uuid, group in grouped_sales_data:
                try:
                    product_info = self.fetch_product_details(product_uuid)
                    if product_info:
                        product_info['quantity'] = group['quantity'].sum()
                        product_details.append(product_info)
                except json.JSONDecodeError as e:
                    print(f"Erreur lors de l'analyse des items JSON: {e}")
                except requests.RequestException as e:
                    print(f"Erreur lors de la requête API pour le produit {product_uuid}: {e}")

            product_data = pd.DataFrame(product_details)

            if sales_data.empty:
                print("Aucune donnée de vente n'a été extraite.")
            if product_data.empty:
                print("Aucune donnée de produit n'a été extraite.")

            return sales_data, product_data

        except Exception as e:
            print(f"Une erreur est survenue lors de l'extraction des données: {e}")
            return pd.DataFrame(), pd.DataFrame()

    def extract_admin_users(self):
        query = """
        SELECT DISTINCT email
        FROM user
        JOIN user_role ON user.uuid = user_role.user_uuid
        JOIN role ON user_role.role_id = role.id
        WHERE role.name IN ('ROLE_ADMIN', 'ROLE_STORE_MANAGER')
        """
        try:
            with self.engine.connect() as conn:
                admin_users = pd.read_sql(query, conn)
            return admin_users['email'].tolist()

        except Exception as e:
            print(f"Une erreur est survenue lors de l'extraction des utilisateurs administrateurs: {e}")
            return []

    def fetch_product_details(self, product_uuid):
        try:
            try:
                product_uuid_str = str(uuid.UUID(bytes=product_uuid))
            except (TypeError, ValueError):
                product_uuid_str = str(uuid.UUID(product_uuid))
            
            headers = {'X-API-KEY': self.api_key}
            response = requests.get(f"{self.api_url}/api/catalogue/products/{product_uuid_str}", headers=headers)

            if response.status_code == 200:
                # print(f"Produit {product_uuid_str} trouvé.")
                product_info = response.json()
                return product_info
            else:
                print(f"Produit {product_uuid_str} non trouvé (Code: {response.status_code}).")
                return {}

        except ValueError as e:
            print(f"Erreur de conversion UUID pour {product_uuid}: {e}")
            return {}
        except requests.RequestException as e:
            print(f"Erreur lors de la requête API pour {product_uuid}: {e}")
            return {}