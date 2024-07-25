from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
import datetime
import pandas as pd

class SalesAnalyzer:
    def __init__(self, sales_data):
        self.sales_data = sales_data
    
    def preprocess_data(self):
        self.sales_data['month'] = self.sales_data['date'].dt.month
        self.sales_data['year'] = self.sales_data['date'].dt.year
        self.sales_data['week'] = self.sales_data['date'].dt.isocalendar().week
    
    def aggregate_sales(self, freq='W'):
        aggregated_sales = self.sales_data.resample(freq, on='date').sum()
        return aggregated_sales
    
    def train_model(self, sales_data):
        X = sales_data[['month', 'year']]
        y = sales_data['totalTtc']
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)
        self.model = LinearRegression()
        self.model.fit(X_train, y_train)
    
    def predict_sales(self, periods, freq='M'):
        future_dates = pd.date_range(start=datetime.datetime.now(), periods=periods, freq=freq)
        future_data = pd.DataFrame({
            'date': future_dates,
            'month': future_dates.month,
            'year': future_dates.year
        })
        future_data['totalTtc'] = self.model.predict(future_data[['month', 'year']])
        return future_data