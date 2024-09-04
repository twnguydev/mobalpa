import unittest
import pandas as pd
import numpy as np
import uuid
from SalesAnalyzer import SalesAnalyzer

class TestSalesAnalyzer(unittest.TestCase):

    def setUp(self):
        self.product_uuid = str(uuid.UUID(bytes=b'\x12\x34\x56\x78' * 4))
        self.sales_data = pd.DataFrame({
            'created_at': pd.to_datetime(['2023-01-01', '2023-01-02', '2023-01-03']),
            'total_ttc': [120.0, 130.0, 140.0],
            'total_ht': [100.0, 110.0, 120.0],
            'quantity': [1, 2, 3],
            'product_uuid': [self.product_uuid] * 3,
            'vat': [20.0, 20.0, 20.0],
            'delivery_fees': [5.0, 5.0, 5.0],
            'reduction': [0.0, 0.0, 0.0]
        })
        self.product_data = pd.DataFrame({
            'uuid': [self.product_uuid],
            'price': [100.0]
        })
        self.analyzer = SalesAnalyzer(self.sales_data, self.product_data)
    
    def test_preprocess_data(self):
        self.analyzer.preprocess_data()
        self.assertIn('month', self.analyzer.sales_data.columns)
        self.assertIn('year', self.analyzer.sales_data.columns)
        self.assertIn('week', self.analyzer.sales_data.columns)

    def test_aggregate_sales(self):
        self.analyzer.preprocess_data()
        weekly_sales = self.analyzer.aggregate_sales(freq='W')
        self.assertEqual(weekly_sales['total_ttc'].sum(), 390.0)

    def test_train_model(self):
        self.analyzer.preprocess_data()
        self.analyzer.train_model()

        for product_uuid, model_info in self.analyzer.models.items():
            if len(self.analyzer.sales_data[self.analyzer.sales_data['product_uuid'] == product_uuid]) < 5:
                self.assertIn('mean', model_info)
                self.assertIn('std', model_info)
            else:
                self.assertIn('forecast', model_info)
            
            self.assertIn('reliability', model_info)

    def test_predict_sales(self):
        self.analyzer.preprocess_data()
        self.analyzer.train_model()
        predictions = self.analyzer.predict_sales(pd.Timestamp('2023-01-04'), pd.Timestamp('2023-01-10'), freq='D')
        self.assertGreater(len(predictions), 0)
        self.assertIn('product_uuid', predictions.columns)
        self.assertIn('date', predictions.columns)
        self.assertIn('quantity', predictions.columns)
        self.assertIn('total_ttc', predictions.columns)

    def test_create_sales_report(self):
        self.analyzer.preprocess_data()
        report = self.analyzer.create_sales_report(self.sales_data)
        self.assertIn('Date de début', report.columns)
        self.assertIn('Total TTC', report.columns)
        self.assertEqual(report['Date de début'].iloc[0], pd.Timestamp('2023-01-01'))

    def test_create_summary_report(self):
        self.analyzer.preprocess_data()
        summary_report = self.analyzer.create_summary_report(freq='W')
        self.assertIn('Période', summary_report.columns)
        self.assertIn('Nombre total de ventes', summary_report.columns)

    def test_create_predictions_report(self):
        self.analyzer.preprocess_data()
        self.analyzer.train_model()
        predictions = self.analyzer.predict_sales(pd.Timestamp('2023-01-04'), pd.Timestamp('2023-01-10'), freq='D')
        
        class MockExtractor:
            def fetch_product_details(self, uuid):
                return {'name': 'Product Name'}
        
        mock_extractor = MockExtractor()
        predictions_report = self.analyzer.create_predictions_report(predictions, pd.Timestamp('2023-01-04'), pd.Timestamp('2023-01-10'), mock_extractor)
        
        self.assertIn('Nom du produit', predictions_report.columns)
        self.assertIn('Date de prédiction de vente', predictions_report.columns)
        self.assertEqual(predictions_report['Nom du produit'].iloc[0], 'Product Name')

if __name__ == '__main__':
    unittest.main()