import unittest
import pandas as pd
import os
from dotenv import load_dotenv
import sys

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from CSVGenerator import CSVGenerator

class TestCSVGenerator(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        load_dotenv()

    def setUp(self):
        self.summary = pd.DataFrame({
            'date': pd.to_datetime(['2022-12-31', '2022-12-30']),
            'totalTtc': [110.0, 115.0]
        })
        self.sales_data = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01', '2023-01-02']),
            'totalTtc': [120.0, 130.0]
        })
        self.predictions = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-03', '2023-01-04']),
            'totalTtc': [140.0, 150.0]
        })
        self.filename = 'test_report.csv'
        self.generator = CSVGenerator(self.summary, self.sales_data, self.predictions, self.filename)

    def test_generate_csv(self):
        self.generator.generate_csv()

        self.assertTrue(os.path.exists(self.filename))

        with open(self.filename, 'r') as file:
            content = file.read()

        expected_content = (
            'Détails des ventes P-1\n'
            'date,totalTtc\n'
            '2022-12-31,110.0\n'
            '2022-12-30,115.0\n'
            '\n'
            'Détails des ventes\n'
            'date,totalTtc\n'
            '2023-01-01,120.0\n'
            '2023-01-02,130.0\n'
            '\n'
            'Prédictions\n'
            'date,totalTtc\n'
            '2023-01-03,140.0\n'
            '2023-01-04,150.0\n'
        )
        self.assertEqual(content.strip(), expected_content.strip())
    
    def tearDown(self):
        if os.path.exists(self.filename):
            os.remove(self.filename)

if __name__ == '__main__':
    unittest.main()