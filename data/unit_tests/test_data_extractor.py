import unittest
from unittest.mock import patch, MagicMock
import pandas as pd
from dotenv import load_dotenv
import os
import sys

# Ajouter le répertoire parent au sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from DataExtractor import DataExtractor

class TestDataExtractor(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        load_dotenv()

    @patch('sqlalchemy.create_engine')
    @patch('pandas.read_sql')
    def test_extract_sales_data(self, mock_read_sql, mock_create_engine):
        mock_engine = MagicMock()
        mock_conn = MagicMock()
        mock_create_engine.return_value = mock_engine
        mock_engine.connect.return_value = mock_conn
        mock_read_sql.return_value = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01']),
            'totalHt': [100.0],
            'totalTtc': [120.0],
            'vat': [20.0],
            'status': ['completed']
        })
        
        extractor = DataExtractor(
            os.getenv('MYSQL_HOST'), 
            os.getenv('MYSQL_USERNAME'), 
            os.getenv('MYSQL_PASSWORD'), 
            os.getenv('MYSQL_DBNAME'), 
            os.getenv('API_URL')
        )
        result = extractor.extract_sales_data()
        
        expected = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01']),
            'totalHt': [100.0],
            'totalTtc': [120.0],
            'vat': [20.0],
            'status': ['completed']
        })
        
        pd.testing.assert_frame_equal(result, expected)
        mock_read_sql.assert_called_once()

    @patch('sqlalchemy.create_engine')
    @patch('pandas.read_sql')
    def test_extract_admin_users(self, mock_read_sql, mock_create_engine):
        mock_engine = MagicMock()
        mock_conn = MagicMock()
        mock_create_engine.return_value = mock_engine
        mock_read_sql.return_value = pd.DataFrame({
            'email': ['admin@example.com']
        })
        
        extractor = DataExtractor(
            os.getenv('MYSQL_HOST'), 
            os.getenv('MYSQL_USERNAME'), 
            os.getenv('MYSQL_PASSWORD'), 
            os.getenv('MYSQL_DBNAME'), 
            os.getenv('API_URL')
        )
        result = extractor.extract_admin_users()
        
        self.assertEqual(result, ['admin@example.com'])
        mock_read_sql.assert_called_once()

    @patch('requests.get')
    def test_fetch_product_details(self, mock_get):
        mock_response = MagicMock()
        mock_get.return_value = mock_response
        mock_response.status_code = 200
        mock_response.json.return_value = {'id': '123', 'name': 'Test Product'}
        
        extractor = DataExtractor(
            os.getenv('MYSQL_HOST'), 
            os.getenv('MYSQL_USERNAME'), 
            os.getenv('MYSQL_PASSWORD'), 
            os.getenv('MYSQL_DBNAME'), 
            os.getenv('API_URL')
        )
        result = extractor.fetch_product_details('123')
        
        self.assertEqual(result, {'id': '123', 'name': 'Test Product'})

if __name__ == '__main__':
    unittest.main()