import unittest
from unittest.mock import patch, MagicMock
from datetime import datetime
import pandas as pd
import schedule
import time
import sys
import os

# Ajouter le répertoire parent au sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

import main

class TestMain(unittest.TestCase):

    @patch('main.DataExtractor')
    @patch('main.SalesAnalyzer')
    @patch('main.CSVGenerator')
    @patch('main.EmailSender')
    def test_automate_sales_report_weekly(self, MockEmailSender, MockCSVGenerator, MockSalesAnalyzer, MockDataExtractor):
        # Mock the data extractor
        mock_extractor = MockDataExtractor.return_value
        mock_extractor.extract_sales_data.return_value = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01']),
            'totalTtc': [120.0]
        })
        mock_extractor.extract_admin_users.return_value = ['admin@example.com']
        
        # Mock the sales analyzer
        mock_analyzer = MockSalesAnalyzer.return_value
        mock_analyzer.aggregate_sales.return_value = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01']),
            'totalTtc': [120.0]
        })
        mock_analyzer.predict_sales.return_value = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-08']),
            'totalTtc': [130.0]
        })
        
        # Call the function
        main.automate_sales_report(report_type='weekly')
        
        # Assertions
        MockCSVGenerator.assert_called_once()
        MockEmailSender.assert_called_once()
        mock_extractor.extract_sales_data.assert_called_once()
        mock_extractor.extract_admin_users.assert_called_once()
        mock_analyzer.aggregate_sales.assert_called_once_with(freq='W')
        mock_analyzer.predict_sales.assert_called_once_with(2, freq='W')
    
    @patch('main.datetime')
    @patch('main.automate_sales_report')
    def test_check_and_run_monthly_task(self, mock_automate_sales_report, mock_datetime):
        # Mock datetime to return the 30th day at 18:00
        mock_datetime.now.return_value = datetime(2023, 1, 30, 18, 0)
        
        # Call the function
        main.check_and_run_monthly_task()
        
        # Assertions
        mock_automate_sales_report.assert_called_once_with(report_type='monthly')

    @patch('main.datetime')
    @patch('main.automate_sales_report')
    def test_check_and_run_yearly_task(self, mock_automate_sales_report, mock_datetime):
        # Mock datetime to return January 30th at 18:00
        mock_datetime.now.return_value = datetime(2023, 1, 30, 18, 0)
        
        # Call the function
        main.check_and_run_yearly_task()
        
        # Assertions
        mock_automate_sales_report.assert_called_once_with(report_type='yearly')
    
    @patch('main.schedule.run_pending')
    @patch('main.check_and_run_monthly_task')
    @patch('main.check_and_run_yearly_task')
    @patch('main.time.sleep', return_value=None)
    def test_main_loop(self, mock_sleep, mock_check_and_run_yearly_task, mock_check_and_run_monthly_task, mock_run_pending):
        # Run the loop for a few iterations
        main.main_loop(iterations=3)
        
        # Assertions
        mock_run_pending.assert_called()
        mock_check_and_run_monthly_task.assert_called()
        mock_check_and_run_yearly_task.assert_called()

if __name__ == '__main__':
    unittest.main()