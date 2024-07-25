import pandas as pd
from sqlalchemy import create_engine
import requests
from sqlalchemy.orm import sessionmaker

class DataExtractor:
    def __init__(self, host, user, password, database, api_url):
        self.host = host
        self.user = user
        self.password = password
        self.database = database
        self.api_url = api_url

        # Create an SQLAlchemy engine and session
        self.database_url = f"mysql+mysqlconnector://{self.user}:{self.password}@{self.host}/{self.database}"
        self.engine = create_engine(self.database_url)
        self.Session = sessionmaker(bind=self.engine)
    
    def extract_sales_data(self):
        session = self.Session()
        query = """
        SELECT createdAt as date, totalHt, totalTtc, 
               totalTtc - totalHt as vat, status 
        FROM orders 
        WHERE status = 'completed'
        """
        sales_data = pd.read_sql(query, self.engine)
        session.close()
        
        sales_data['date'] = pd.to_datetime(sales_data['date'])
        return sales_data
    
    def extract_admin_users(self):
        session = self.Session()
        query = "SELECT email FROM user WHERE role IN ('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')"
        admin_users = pd.read_sql(query, self.engine)
        session.close()
        return admin_users['email'].tolist()
    
    def fetch_product_details(self, product_id):
        response = requests.get(f"{self.api_url}/products/{product_id}")
        if response.status_code == 200:
            return response.json()
        else:
            return {}