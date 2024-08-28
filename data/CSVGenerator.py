import pandas as pd

class CSVGenerator:
    def __init__(self, sales_data, predictions, filename):
        self.sales_data = sales_data
        self.predictions = predictions
        self.filename = filename
    
    def generate_csv(self):
        if not self.sales_data.empty and not self.predictions.empty:
            combined = pd.concat([self.sales_data, self.predictions], keys=['Sales', 'Predictions'])
        elif not self.sales_data.empty:
            combined = self.sales_data
        elif not self.predictions.empty:
            combined = self.predictions
        else:
            combined = pd.DataFrame()
        
        combined.to_csv(self.filename, index=False)
