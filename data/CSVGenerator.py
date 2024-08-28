import pandas as pd

class CSVGenerator:
    def __init__(self, summary, sales_data, predictions, filename):
        self.summary = summary
        self.sales_data = sales_data
        self.predictions = predictions
        self.filename = filename
    
    def generate_csv(self):
        with open(self.filename, 'w') as f:
            if not self.summary.empty:
                f.write('Détails des ventes P-1\n')
                self.summary.to_csv(f, index=False)
                f.write('\n')
            
            if not self.sales_data.empty:
                f.write('Détails des ventes\n')
                self.sales_data.to_csv(f, index=False)
                f.write('\n')
            
            if not self.predictions.empty:
                f.write('Prédictions\n')
                self.predictions.to_csv(f, index=False)