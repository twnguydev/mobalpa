from flask import Flask, jsonify, request, abort, make_response, send_file
from SalesAnalyzer import SalesAnalyzer
from ReportGenerator import ReportGenerator
from DataExtractor import DataExtractor
from sqlalchemy import create_engine
import os
from dotenv import load_dotenv
from datetime import datetime
import pandas as pd
import io

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
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='W')
        elif report_type == 'monthly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='ME')
        elif report_type == 'yearly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='YE')
        else:
            raise ValueError("Type de rapport non pris en charge.")
        
        report_generator = ReportGenerator(sales)
        summary_report = report_generator.create_summary_report(freq=report_type[0].upper())
        
        p1_start_date = pd.to_datetime(summary_report['Date de début']).iloc[0]
        p1_end_date = pd.to_datetime(summary_report['Date de fin']).iloc[0]

        p1_duration = p1_end_date - p1_start_date
        start_date = p1_end_date + pd.Timedelta(days=1)
        end_date = start_date + p1_duration
        
        predictions = analyzer.predict_sales(start_date, end_date, freq=freq)
        
        predictions_report = report_generator.create_predictions_report(predictions, start_date, end_date, extractor)

        predictions_json = predictions_report.to_dict(orient='records')

        response = make_response(jsonify(predictions_json))
        response.headers['Content-Type'] = 'application/json; charset=utf-8'
        return response

    except Exception as e:
        return jsonify({'error': str(e)}), 500
      
      
@app.route('/api/summary', methods=['GET'])
# @check_api_key
def get_summary():
    report_type = request.args.get('report_type', 'weekly')

    if report_type not in ['weekly', 'monthly', 'yearly']:
        return jsonify({'error': 'Invalid report_type. Must be "weekly", "monthly", or "yearly".'}), 400

    try:
        extractor = DataExtractor(engine, API_URL, API_KEY)
        sales_data, product_data = extractor.extract_sales_data()

        analyzer = SalesAnalyzer(sales_data, product_data)
        analyzer.preprocess_data()
        
        if report_type == 'weekly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='W')
        elif report_type == 'monthly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='ME')
        elif report_type == 'yearly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='YE')
        else:
            raise ValueError("Type de rapport non pris en charge.")
        
        report_generator = ReportGenerator(sales)
        summary_report = report_generator.create_summary_report(freq=report_type[0].upper())

        summary_json = summary_report.to_dict(orient='records')

        response = make_response(jsonify(summary_json))
        response.headers['Content-Type'] = 'application/json; charset=utf-8'
        return response

    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/api/sales', methods=['GET'])
# @check_api_key
def get_sales():
    start_date = request.args.get('start_date')
    end_date = request.args.get('end_date')
    
    if not start_date or not end_date:
        return jsonify({'error': 'start_date and end_date parameters are required.'}), 400

    try:
        start_date = pd.to_datetime(start_date)
        end_date = pd.to_datetime(end_date)

        extractor = DataExtractor(engine, API_URL, API_KEY)
        sales_data, product_data = extractor.extract_sales_data()

        analyzer = SalesAnalyzer(sales_data, product_data)
        analyzer.preprocess_data()

        sales_data_filtered = analyzer.aggregate_sales(freq='D').query('created_at >= @start_date and created_at <= @end_date')

        sales_json = sales_data_filtered.to_dict(orient='records')

        response = make_response(jsonify(sales_json))
        response.headers['Content-Type'] = 'application/json; charset=utf-8'
        return response

    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/api/forecast/csv', methods=['GET'])
# @check_api_key
def download_forecast_csv():
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
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='W')
        elif report_type == 'monthly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='ME')
        elif report_type == 'yearly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='YE')
        else:
            raise ValueError("Type de rapport non pris en charge.")

        report_generator = ReportGenerator(sales)
        summary_report = report_generator.create_summary_report(freq=report_type[0].upper())

        p1_start_date = pd.to_datetime(summary_report['Date de début']).iloc[0]
        p1_end_date = pd.to_datetime(summary_report['Date de fin']).iloc[0]

        p1_duration = p1_end_date - p1_start_date
        start_date = p1_end_date + pd.Timedelta(days=1)
        end_date = start_date + p1_duration

        predictions = analyzer.predict_sales(start_date, end_date, freq=freq)
        predictions_report = report_generator.create_predictions_report(predictions, start_date, end_date, extractor)

        output = io.StringIO()
        predictions_report.to_csv(output, index=False, encoding='utf-8')
        output.seek(0)

        return send_file(
            io.BytesIO(output.getvalue().encode('utf-8')),
            mimetype='text/csv',
            as_attachment=True,
            download_name='forecast_predictions.csv'
        )

    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/api/summary/csv', methods=['GET'])
# @check_api_key
def download_summary_csv():
    report_type = request.args.get('report_type', 'weekly')

    if report_type not in ['weekly', 'monthly', 'yearly']:
        return jsonify({'error': 'Invalid report_type. Must be "weekly", "monthly", or "yearly".'}), 400

    try:
        extractor = DataExtractor(engine, API_URL, API_KEY)
        sales_data, product_data = extractor.extract_sales_data()

        analyzer = SalesAnalyzer(sales_data, product_data)
        analyzer.preprocess_data()

        if report_type == 'weekly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='W')
        elif report_type == 'monthly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='ME')
        elif report_type == 'yearly':
            freq = 'D'
            sales = analyzer.aggregate_sales(freq='YE')
        else:
            raise ValueError("Type de rapport non pris en charge.")

        report_generator = ReportGenerator(sales)
        summary_report = report_generator.create_summary_report(freq=report_type[0].upper())

        output = io.StringIO()
        summary_report.to_csv(output, index=False, encoding='utf-8')
        output.seek(0)

        return send_file(
            io.BytesIO(output.getvalue().encode('utf-8')),
            mimetype='text/csv',
            as_attachment=True,
            download_name='summary_report.csv'
        )

    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/api/sales/csv', methods=['GET'])
# @check_api_key
def download_sales_csv():
    start_date = request.args.get('start_date')
    end_date = request.args.get('end_date')

    if not start_date or not end_date:
        return jsonify({'error': 'start_date and end_date parameters are required.'}), 400

    try:
        start_date = pd.to_datetime(start_date)
        end_date = pd.to_datetime(end_date)

        extractor = DataExtractor(engine, API_URL, API_KEY)
        sales_data, product_data = extractor.extract_sales_data()

        analyzer = SalesAnalyzer(sales_data, product_data)
        analyzer.preprocess_data()

        sales_data_filtered = analyzer.aggregate_sales(freq='D').query('created_at >= @start_date and created_at <= @end_date')

        output = io.StringIO()
        sales_data_filtered.to_csv(output, index=False, encoding='utf-8')
        output.seek(0)

        return send_file(
            io.BytesIO(output.getvalue().encode('utf-8')),
            mimetype='text/csv',
            as_attachment=True,
            download_name='sales_data.csv'
        )

    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    # app.run(host='localhost', port=8083, debug=True)
    app.run(host='0.0.0.0', port=8083, debug=True)