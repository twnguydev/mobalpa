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
        self.sales_data = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01', '2023-01-02']),
            'totalTtc': [120.0, 130.0]
        })
        self.predictions = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-03', '2023-01-04']),
            'totalTtc': [140.0, 150.0]
        })
        self.filename = 'test_report.csv'
        self.generator = CSVGenerator(self.sales_data, self.predictions, self.filename)

    def test_generate_csv(self):
        self.generator.generate_csv()
        self.assertTrue(os.path.exists(self.filename))
        os.remove(self.filename)

if __name__ == '__main__':
    unittest.main()