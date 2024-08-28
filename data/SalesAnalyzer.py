from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
import datetime
import pandas as pd

class SalesAnalyzer:
    def __init__(self, sales_data, fetch_product_details):
        self.sales_data = sales_data
        self.fetch_product_details = fetch_product_details
        self.model = None
    
    
    def preprocess_data(self):
        if not pd.api.types.is_datetime64_any_dtype(self.sales_data['created_at']):
            self.sales_data['created_at'] = pd.to_datetime(self.sales_data['created_at'], errors='coerce')
        self.sales_data['month'] = self.sales_data['created_at'].dt.month
        self.sales_data['year'] = self.sales_data['created_at'].dt.year
        self.sales_data['week'] = self.sales_data['created_at'].dt.isocalendar().week
    
    
    def aggregate_sales(self, freq='W'):
        if 'created_at' not in self.sales_data.columns:
            raise ValueError("La colonne 'created_at' est requise pour l'agrégation.")

        if not pd.api.types.is_datetime64_any_dtype(self.sales_data.index):
            self.sales_data.set_index('created_at', inplace=True)

        if 'total_ttc' not in self.sales_data.columns:
            raise ValueError("La colonne 'total_ttc' est requise pour l'agrégation.")

        aggregated_sales = self.sales_data.resample(freq).sum(numeric_only=True)
        aggregated_sales.reset_index(inplace=True)
        
        return aggregated_sales
    
    
    def train_model(self):
        if 'month' not in self.sales_data.columns or 'year' not in self.sales_data.columns:
            raise ValueError("Les colonnes 'month' et 'year' sont nécessaires pour l'entraînement du modèle.")
        
        X = self.sales_data[['month', 'year']]
        y = self.sales_data['total_ttc']

        if len(X) < 2:
            raise ValueError("Pas assez de données pour entraîner le modèle.")
        
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)
        self.model = LinearRegression()
        self.model.fit(X_train, y_train)
        print(f"Modèle entraîné avec une précision de {self.model.score(X_test, y_test):.2f}")
    
    
    def predict_sales(self, periods, freq='ME'):
        if self.model is None:
            raise ValueError("Le modèle doit être entraîné avant de faire des prédictions.")
        
        future_dates = pd.date_range(start=datetime.datetime.now(), periods=periods, freq=freq)

        product_uuids = self.sales_data['product_uuid'].unique()
        
        predictions_list = []
        for product_uuid in product_uuids:
            future_data = pd.DataFrame({
                'date': future_dates,
                'month': future_dates.month,
                'year': future_dates.year,
                'product_uuid': product_uuid
            })
            future_data['total_ttc'] = self.model.predict(future_data[['month', 'year']])
            predictions_list.append(future_data)

        all_predictions = pd.concat(predictions_list, ignore_index=True)
        return all_predictions
    
    
    def create_summary_report(self):
        total_summary = {
            'Total des ventes TTC': self.sales_data['total_ttc'].sum(),
            'Total des ventes HT': self.sales_data['total_ht'].sum(),
            'Total de TVA': self.sales_data['vat'].sum(),
            'Total des réductions': self.sales_data['reduction'].sum(),
            'Total des frais de livraison': self.sales_data['delivery_fees'].sum(),
            'Quantité totale vendue': self.sales_data['quantity'].sum()
        }
        summary_df = pd.DataFrame([total_summary])
        return summary_df
    
    
    def create_predictions_report(self, predictions):
        predictions_report = predictions.copy()

        product_names = []
        for index, row in predictions_report.iterrows():
            product_id = row.get('product_uuid', None)
            if product_id:
                product_details = self.fetch_product_details(product_id)
                if 'name' in product_details:
                    product_names.append(product_details['name'])
                else:
                    product_names.append('Produit inconnu')
            else:
                product_names.append('Produit inconnu')

        predictions_report['product_name'] = product_names
        predictions_report.drop(columns=['product_uuid'], inplace=True, errors='ignore')

        print(f"Rapport de prédictions généré: {predictions_report}")
        return predictions_report

