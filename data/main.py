import schedule
import time
import pandas as pd
from datetime import datetime, timedelta
from DataExtractor import DataExtractor
from SalesAnalyzer import SalesAnalyzer
from CSVGenerator import CSVGenerator
from EmailSender import EmailSender
from dotenv import load_dotenv
from sqlalchemy import create_engine
import os
import logging

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

load_dotenv()

SMTP_HOST = os.getenv('SMTP_HOST')
SMTP_PORT = int(os.getenv('SMTP_PORT'))
SMTP_USERNAME = os.getenv('SMTP_USERNAME')
SMTP_PASSWORD = os.getenv('SMTP_PASSWORD')

MYSQL_HOST = os.getenv('MYSQL_HOST')
MYSQL_PORT = int(os.getenv('MYSQL_PORT'))
MYSQL_USERNAME = os.getenv('MYSQL_USERNAME')
MYSQL_PASSWORD = os.getenv('MYSQL_PASSWORD')
MYSQL_DBNAME = os.getenv('MYSQL_DBNAME')

API_URL = os.getenv('API_URL')
API_KEY = os.getenv('API_KEY')

database_url = f"mysql+mysqlconnector://{MYSQL_USERNAME}:{MYSQL_PASSWORD}@{MYSQL_HOST}:{MYSQL_PORT}/{MYSQL_DBNAME}"
engine = create_engine(database_url)

def check_database_connection():
    try:
        with engine.connect() as connection:
            logging.info("Connection to MySQL successful!")
    except Exception as e:
        logging.error(f"An error occurred while connecting to the database: {e}")
        raise

def automate_sales_report(report_type):
    try:
        extractor = DataExtractor(engine, API_URL, API_KEY)
        sales_data, product_data = extractor.extract_sales_data()

        analyzer = SalesAnalyzer(sales_data, product_data)
        analyzer.preprocess_data()
        analyzer.train_model()

        end_date = datetime.now().date()
        
        if report_type == 'weekly':
            start_date = end_date + timedelta(days=(7 - end_date.weekday()))
            end_date = start_date + timedelta(days=6)
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='W')
            sales_filename = 'weekly_sales_data.csv'
            subject = 'Weekly Sales Report'
            body = 'Please find attached the weekly sales report and predictions.'
        elif report_type == 'monthly':
            start_date = (end_date.replace(day=1) + timedelta(days=31)).replace(day=1)
            end_date = (start_date + pd.DateOffset(months=1) - timedelta(days=1)).date()
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='M')
            sales_filename = 'monthly_sales_data.csv'
            subject = 'Monthly Sales Report'
            body = 'Please find attached the monthly sales report and predictions.'
        elif report_type == 'yearly':
            start_date = datetime(end_date.year + 1, 1, 1).date()
            end_date = datetime(end_date.year + 1, 12, 31).date()
            freq = 'W-MON'
            sales = analyzer.aggregate_sales(freq='Y')
            sales_filename = 'yearly_sales_data.csv'
            subject = 'Yearly Sales Report'
            body = 'Please find attached the yearly sales report and predictions.'
        else:
            raise ValueError("Type de rapport non pris en charge.")

        summary_report = analyzer.create_summary_report(sales, freq=report_type[0].upper())
        sales_report = analyzer.create_sales_report(sales)
        
        predictions = analyzer.predict_sales(start_date, end_date, freq=freq)
        predictions_report = analyzer.create_predictions_report(predictions, end_date, extractor)

        csv_generator = CSVGenerator(summary_report, sales_report, predictions_report, sales_filename)
        csv_generator.generate_csv()

        admin_users = extractor.extract_admin_users()
        logging.info(f"Admin users: {admin_users}")

        email_sender = EmailSender(SMTP_HOST, SMTP_PORT, SMTP_USERNAME, SMTP_PASSWORD)
        email_sender.send_email_with_attachments(admin_users, subject, body, [sales_filename])

        os.remove(sales_filename)
        logging.info(f"{report_type.capitalize()} report generated and sent successfully.")

    except Exception as e:
        logging.error(f"An error occurred while generating {report_type} report: {str(e)}")

def schedule_tasks():
    schedule.every().friday.at("18:00").do(automate_sales_report, report_type='weekly')
    schedule.every().day.at("18:00").do(check_and_run_monthly_task)
    schedule.every().day.at("18:00").do(check_and_run_yearly_task)

def check_and_run_monthly_task():
    now = datetime.now()
    if now.day == 30:
        automate_sales_report(report_type='monthly')

def check_and_run_yearly_task():
    now = datetime.now()
    if now.month == 12 and now.day == 31:
        automate_sales_report(report_type='yearly')

def main_loop(iterations=None):
    count = 0
    while True:
        schedule.run_pending()
        time.sleep(60)
        if iterations is not None:
            count += 1
            if count >= iterations:
                break

def run_reports():
    automate_sales_report(report_type='weekly')
    automate_sales_report(report_type='monthly')
    automate_sales_report(report_type='yearly')

if __name__ == "__main__":
    check_database_connection()
    schedule_tasks()
    main_loop()
    # run_reports()