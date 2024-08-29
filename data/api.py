from flask import Flask, jsonify, request, abort
from SalesAnalyzer import SalesAnalyzer
from DataExtractor import DataExtractor
from sqlalchemy import create_engine
import os
from dotenv import load_dotenv
from datetime import datetime
import pandas as pd

load_dotenv()

app = Flask(__name__)

MYSQL_HOST = os.getenv('MYSQL_HOST')
MYSQL_PORT = int(os.getenv('MYSQL_PORT'))
MYSQL_USERNAME = os.getenv('MYSQL_USERNAME')
MYSQL_PASSWORD = os.getenv('MYSQL_PASSWORD')
MYSQL_DBNAME = os.getenv('MYSQL_DBNAME')
API_URL = os.getenv('API_URL')
API_KEY = os.getenv('API_KEY')

database_url = f"mysql+mysqlconnector://{MYSQL_USERNAME}:{MYSQL_PASSWORD}@{MYSQL_HOST}:{MYSQL_PORT}/{MYSQL_DBNAME}"
engine = create_engine(database_url)

def check_api_key(func):
    def wrapper(*args, **kwargs):
        api_key = request.headers.get('x-api-key')
        if api_key != API_KEY:
            abort(403)
        return func(*args, **kwargs)
    return wrapper

@app.route('/', methods=['GET'])
def index():
    return jsonify({'message': 'Welcome to the Sales Forecast API!'})

@app.route('/api/forecast', methods=['GET'])
# @check_api_key
def get_forecast():
    report_type = request.args.get('report_type', 'weekly')

    if report_type not in ['weekly', 'monthly', 'yearly']:
        return jsonify({'error': 'Invalid report_type. Must be "weekly", "monthly", or "yearly".'}), 400

    try:
        extractor = DataExtractor(engine, API_URL, API_KEY)
        sales_data, product_data = extractor.extract_sales_data()

        analyzer = SalesAnalyzer(sales_data, product_data)
        analyzer.preprocess_data()
        analyzer.train_model()

        end_date = datetime.now().date()
        
        if report_type == 'weekly':
            end_of_last_week = end_date - pd.Timedelta(days=end_date.weekday() + 1)
            start_of_last_week = end_of_last_week - pd.Timedelta(days=6)
            start_date = start_of_last_week
            end_date = end_of_last_week
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='W')
        elif report_type == 'monthly':
            first_day_of_current_month = end_date.replace(day=1)
            last_day_of_last_month = first_day_of_current_month - pd.Timedelta(days=1)
            first_day_of_last_month = last_day_of_last_month.replace(day=1)
            start_date = first_day_of_last_month
            end_date = last_day_of_last_month
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='M')
        elif report_type == 'yearly':
            start_date = datetime(end_date.year - 1, 1, 1).date()
            end_date = datetime(end_date.year - 1, 12, 31).date()
            freq = 'W-MON'
            sales = analyzer.aggregate_sales(freq='Y')
        else:
            raise ValueError("Type de rapport non pris en charge.")
        
        predictions = analyzer.predict_sales(start_date, end_date, freq=freq)
        predictions_report = analyzer.create_predictions_report(predictions, end_date, extractor)

        predictions_json = predictions_report.to_dict(orient='records')

        return jsonify(predictions_json)

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8084, debug=True)