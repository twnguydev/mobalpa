import unittest
import pandas as pd
from dotenv import load_dotenv
import os
import sys

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from SalesAnalyzer import SalesAnalyzer

class TestSalesAnalyzer(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        load_dotenv()

    def setUp(self):
        self.sales_data = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01', '2023-01-02', '2023-01-03']),
            'totalTtc': [120.0, 130.0, 140.0]
        })
        self.analyzer = SalesAnalyzer(self.sales_data)
    
    def test_preprocess_data(self):
        self.analyzer.preprocess_data()
        self.assertIn('month', self.analyzer.sales_data.columns)
        self.assertIn('year', self.analyzer.sales_data.columns)
        self.assertIn('week', self.analyzer.sales_data.columns)

    def test_aggregate_sales(self):
        self.analyzer.preprocess_data()
        weekly_sales = self.analyzer.aggregate_sales(freq='W')
        self.assertEqual(weekly_sales['totalTtc'].sum(), 390.0)

    def test_train_model(self):
        self.analyzer.preprocess_data()
        self.analyzer.train_model(self.analyzer.sales_data)
        self.assertIsNotNone(self.analyzer.model)

    def test_predict_sales(self):
        self.analyzer.preprocess_data()
        self.analyzer.train_model(self.analyzer.sales_data)
        predictions = self.analyzer.predict_sales(2, freq='ME')
        self.assertEqual(len(predictions), 2)

if __name__ == '__main__':
    unittest.main()