import unittest
from unittest.mock import patch, MagicMock
from datetime import datetime
import pandas as pd
import schedule
import time
import sys
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

import main

class TestMain(unittest.TestCase):

    @patch('main.DataExtractor')
    @patch('main.SalesAnalyzer')
    @patch('main.CSVGenerator')
    @patch('main.EmailSender')
    def test_automate_sales_report_weekly(self, MockEmailSender, MockCSVGenerator, MockSalesAnalyzer, MockDataExtractor):
        mock_extractor = MockDataExtractor.return_value
        mock_extractor.extract_sales_data.return_value = (
            pd.DataFrame({
                'date': pd.to_datetime(['2023-01-01']),
                'totalTtc': [120.0]
            }),
            pd.DataFrame()
        )
        mock_extractor.extract_admin_users.return_value = ['admin@example.com']

        mock_analyzer = MockSalesAnalyzer.return_value
        mock_analyzer.aggregate_sales.return_value = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01']),
            'totalTtc': [120.0]
        })
        mock_analyzer.create_summary_report.return_value = pd.DataFrame({
            'Date de début': [pd.to_datetime('2023-01-01')],
            'Date de fin': [pd.to_datetime('2023-01-07')]
        })
        mock_analyzer.create_sales_report.return_value = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-01']),
            'totalTtc': [120.0]
        })
        mock_analyzer.predict_sales.return_value = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-08']),
            'totalTtc': [130.0]
        })
        mock_analyzer.create_predictions_report.return_value = pd.DataFrame({
            'date': pd.to_datetime(['2023-01-08']),
            'totalTtc': [130.0]
        })

        main.automate_sales_report(report_type='weekly')

        MockCSVGenerator.assert_called_once()
        MockEmailSender.assert_called_once()
        mock_extractor.extract_sales_data.assert_called_once()
        mock_extractor.extract_admin_users.assert_called_once()
        mock_analyzer.aggregate_sales.assert_called_once_with(freq='W')
        mock_analyzer.predict_sales.assert_called_once()
    
    @patch('main.datetime')
    @patch('main.automate_sales_report')
    def test_check_and_run_monthly_task(self, mock_automate_sales_report, mock_datetime):
        mock_datetime.now.return_value = datetime(2023, 1, 30, 18, 0)

        main.check_and_run_monthly_task()

        mock_automate_sales_report.assert_called_once_with(report_type='monthly')

    @patch('main.datetime')
    @patch('main.automate_sales_report')
    def test_check_and_run_yearly_task(self, mock_automate_sales_report, mock_datetime):
        mock_datetime.now.return_value = datetime(2023, 12, 31, 18, 0)

        main.check_and_run_yearly_task()

        mock_automate_sales_report.assert_called_once_with(report_type='yearly')
    
    @patch('main.schedule.run_pending')
    @patch('main.check_and_run_monthly_task')
    @patch('main.check_and_run_yearly_task')
    @patch('main.time.sleep', return_value=None)
    def test_main_loop(self, mock_sleep, mock_check_and_run_yearly_task, mock_check_and_run_monthly_task, mock_run_pending):
        mock_check_and_run_monthly_task.reset_mock()
        mock_check_and_run_yearly_task.reset_mock()

        schedule.every().day.at("23:42").do(mock_check_and_run_monthly_task)
        schedule.every().day.at("23:42").do(mock_check_and_run_yearly_task)

        main.main_loop(iterations=1)

        self.assertGreater(mock_run_pending.call_count, 0, "run_pending was not called as expected")

        self.assertTrue(mock_check_and_run_monthly_task.call_count > 0, "check_and_run_monthly_task was not called")
        self.assertTrue(mock_check_and_run_yearly_task.call_count > 0, "check_and_run_yearly_task was not called")

if __name__ == '__main__':
    unittest.main()