import schedule
import time
from datetime import datetime
from DataExtractor import DataExtractor
from SalesAnalyzer import SalesAnalyzer
from CSVGenerator import CSVGenerator
from EmailSender import EmailSender
from dotenv import load_dotenv
from sqlalchemy import create_engine
import os

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

try:
    connection = engine.connect()
    print("Connection to MySQL successful!")
    connection.close()
except Exception as e:
    print(f"An error occurred: {e}")

def automate_sales_report(report_type):
    try:
        extractor = DataExtractor(engine, API_URL, API_KEY)
        sales_data, product_data = extractor.extract_sales_data()
        
        analyzer = SalesAnalyzer(sales_data)
        analyzer.preprocess_data()
        
        if report_type == 'weekly':
            sales = analyzer.aggregate_sales(freq='W')
            predictions = analyzer.predict_sales(2, freq='W')
            filename = 'weekly_sales_report.csv'
            subject = 'Weekly Sales Report'
            body = 'Please find attached the weekly sales report.'
        
        elif report_type == 'monthly':
            sales = analyzer.aggregate_sales(freq='M')
            predictions = analyzer.predict_sales(2, freq='M')
            filename = 'monthly_sales_report.csv'
            subject = 'Monthly Sales Report'
            body = 'Please find attached the monthly sales report.'
        
        elif report_type == 'yearly':
            sales = analyzer.aggregate_sales(freq='Y')
            predictions = analyzer.predict_sales(2, freq='Y')
            filename = 'yearly_sales_report.csv'
            subject = 'Yearly Sales Report'
            body = 'Please find attached the yearly sales report.'
        
        csv_generator = CSVGenerator(sales, predictions, filename)
        csv_generator.generate_csv()
        
        admin_users = extractor.extract_admin_users()
        
        email_sender = EmailSender(SMTP_HOST, SMTP_PORT, SMTP_USERNAME, SMTP_PASSWORD)
        email_sender.send_email(admin_users, subject, body, filename)
    except Exception as e:
        print(f"An error occurred: {str(e)}")

schedule.every().friday.at("18:00").do(automate_sales_report, report_type='weekly')

def check_and_run_monthly_task():
    now = datetime.now()
    if now.day == 30 and now.hour == 18 and now.minute == 0:
        automate_sales_report(report_type='monthly')

def check_and_run_yearly_task():
    now = datetime.now()
    if now.month == 1 and now.day == 30 and now.hour == 18 and now.minute == 0:
        automate_sales_report(report_type='yearly')

def main_loop(iterations=None):
    count = 0
    while True:
        schedule.run_pending()
        check_and_run_monthly_task()
        check_and_run_yearly_task()
        time.sleep(1)
        if iterations is not None:
            count += 1
            if count >= iterations:
                break

if __name__ == "__main__":
    main_loop()