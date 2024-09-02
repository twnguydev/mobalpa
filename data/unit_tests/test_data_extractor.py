import unittest
from unittest.mock import patch, MagicMock
import pandas as pd
from dotenv import load_dotenv
import os
import sys
import json

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
            'created_at': pd.to_datetime(['2023-01-01']),
            'total_ttc': [120.0],
            'total_ht': [100.0],
            'vat': [20.0],
            'delivery_fees': [5.0],
            'reduction': [0.0],
            'product_uuid': ['123e4567-e89b-12d3-a456-426614174000'],
            'quantity': [2]
        })

        with patch('requests.get') as mock_get:
            mock_response = MagicMock()
            mock_get.return_value = mock_response
            mock_response.status_code = 200
            mock_response.json.return_value = {'id': '123e4567-e89b-12d3-a456-426614174000', 'name': 'Test Product'}

            extractor = DataExtractor(
                mock_engine,
                os.getenv('API_URL'),
                os.getenv('API_KEY')
            )
            sales_data, product_data = extractor.extract_sales_data()

            expected_sales_data = pd.DataFrame({
                'created_at': pd.to_datetime(['2023-01-01']),
                'total_ttc': [120.0],
                'total_ht': [100.0],
                'vat': [20.0],
                'delivery_fees': [5.0],
                'reduction': [0.0],
                'product_uuid': ['123e4567-e89b-12d3-a456-426614174000'],
                'quantity': [2]
            })

            expected_product_data = pd.DataFrame({
                'id': ['123e4567-e89b-12d3-a456-426614174000'],
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
        mock_response.json.return_value = {'id': '123e4567-e89b-12d3-a456-426614174000', 'name': 'Test Product'}

        extractor = DataExtractor(
            MagicMock(),
            os.getenv('API_URL'),
            os.getenv('API_KEY')
        )
        result = extractor.fetch_product_details('123e4567-e89b-12d3-a456-426614174000')

        self.assertEqual(result, {'id': '123e4567-e89b-12d3-a456-426614174000', 'name': 'Test Product'})
        mock_get.assert_called_once_with(f"{os.getenv('API_URL')}/api/catalogue/products/123e4567-e89b-12d3-a456-426614174000", headers={'X-API-KEY': os.getenv('API_KEY')})

if __name__ == '__main__':
    unittest.main()