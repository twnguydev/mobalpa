import pandas as pd

class CSVGenerator:
    def __init__(self, sales_data, predictions, filename):
        self.sales_data = sales_data
        self.predictions = predictions
        self.filename = filename
    
    def generate_csv(self):
        combined = pd.concat([self.sales_data, self.predictions], keys=['Sales', 'Predictions'])
        combined.to_csv(self.filename, index=False)
