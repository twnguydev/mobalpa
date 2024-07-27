import unittest
from unittest.mock import patch, MagicMock
import pandas as pd
from dotenv import load_dotenv
import os
import sys
import json

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
            'uuid': ['123'],
            'date': pd.to_datetime(['2023-01-01']),
            'items': [json.dumps([{'product_id': '1', 'quantity': 2}])],
            'totalHt': [100.0],
            'totalTtc': [120.0],
            'status': ['completed']
        })
        
        extractor = DataExtractor(
            mock_engine,
            os.getenv('API_URL'),
            os.getenv('API_KEY')
        )
        sales_data, product_data = extractor.extract_sales_data()
        
        expected_sales_data = pd.DataFrame({
            'uuid': ['123'],
            'date': pd.to_datetime(['2023-01-01']),
            'items': [json.dumps([{'product_id': '1', 'quantity': 2}])],
            'totalHt': [100.0],
            'totalTtc': [120.0],
            'status': ['completed'],
            'vat': [20.0],
            'revenue': [100.0],
            'total': [120.0]
        })
        
        expected_product_data = pd.DataFrame({
            'id': ['1'],
            'name': ['Test Product'],
            'quantity': [2]
        })
        
        pd.testing.assert_frame_equal(sales_data, expected_sales_data)
        pd.testing.assert_frame_equal(product_data, expected_product_data)
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
            mock_engine,
            os.getenv('API_URL'),
            os.getenv('API_KEY')
        )
        result = extractor.extract_admin_users()
        
        self.assertEqual(result, ['admin@example.com'])
        mock_read_sql.assert_called_once()

    @patch('requests.get')
    def test_fetch_product_details(self, mock_get):
        mock_response = MagicMock()
        mock_get.return_value = mock_response
        mock_response.status_code = 200
        mock_response.json.return_value = {'id': '1', 'name': 'Test Product'}
        
        extractor = DataExtractor(
            MagicMock(),
            os.getenv('API_URL'),
            os.getenv('API_KEY')
        )
        result = extractor.fetch_product_details('1')
        
        self.assertEqual(result, {'id': '1', 'name': 'Test Product'})
        mock_get.assert_called_once_with(f"{os.getenv('API_URL')}/products/1", headers={'X-API-KEY': os.getenv('API_KEY')})

if __name__ == '__main__':
    unittest.main()