from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_absolute_error, mean_squared_error
import pandas as pd
import numpy as np
import uuid

class SalesAnalyzer:
    def __init__(self, sales_data, product_data):
        self.sales_data = sales_data
        self.product_data = product_data
        self.models = {}

    def preprocess_data(self):
        if 'created_at' not in self.sales_data.columns:
            raise ValueError("La colonne 'created_at' est manquante dans les données.")

        self.sales_data['created_at'] = pd.to_datetime(self.sales_data['created_at'], errors='coerce')
        self.sales_data['month'] = self.sales_data['created_at'].dt.month
        self.sales_data['year'] = self.sales_data['created_at'].dt.year
        self.sales_data['week'] = self.sales_data['created_at'].dt.isocalendar().week

        required_columns = ['created_at', 'month', 'year', 'week', 'total_ttc', 'total_ht', 'quantity', 'product_uuid']
        for col in required_columns:
            if col not in self.sales_data.columns:
                raise ValueError(f"La colonne requise '{col}' est manquante dans les données.")

    def aggregate_sales(self, freq='W'):
        if not pd.api.types.is_datetime64_any_dtype(self.sales_data['created_at']):
            self.sales_data['created_at'] = pd.to_datetime(self.sales_data['created_at'], errors='coerce')

        aggregated_sales = self.sales_data.groupby(pd.Grouper(key='created_at', freq=freq)).agg({
            'total_ttc': 'sum',
            'total_ht': 'sum',
            'quantity': 'sum',
            'vat': 'sum',
            'delivery_fees': 'sum',
            'reduction': 'sum'
        }).reset_index()

        return aggregated_sales

    def train_model(self):
        for product_uuid in self.sales_data['product_uuid'].unique():
            product_sales = self.sales_data[self.sales_data['product_uuid'] == product_uuid]

            product_sales = product_sales.sort_values(by='created_at')
            product_sales.set_index('created_at', inplace=True)
            product_sales['quantity'] = product_sales['quantity'].astype(float)
            
            # print(f"Product sales : {product_sales}")

            if len(product_sales) < 5:
                # print(f"Pas assez de données pour le produit {product_uuid}. Utilisation d'un modèle simple.")
                self.models[product_uuid] = {'mean': product_sales['quantity'].mean(), 'std': product_sales['quantity'].std()}
                reliability = self._calculate_reliability_simple_model(product_sales['quantity'], product_sales)
                self.models[product_uuid]['reliability'] = reliability
            else:
                train_data, test_data = train_test_split(product_sales, test_size=0.2, shuffle=False)

                train_data['forecast'] = train_data['quantity'].ewm(span=5, adjust=False).mean()
                self.models[product_uuid] = {'forecast': train_data['forecast'].iloc[-1]}
                reliability = self._calculate_reliability(train_data, test_data)
                self.models[product_uuid]['reliability'] = reliability
                
            if product_uuid not in self.models:
                print(f"Aucun modèle n'a pu être créé pour le produit {product_uuid}.")
                continue

            if 'forecast' not in self.models[product_uuid] and ('mean' not in self.models[product_uuid] or 'std' not in self.models[product_uuid]):
                print(f"Modèle incomplet pour le produit {product_uuid}. Vérifiez les données.")

    def _calculate_reliability_simple_model(self, actual_quantities, product_sales):
        mean_quantity = product_sales['quantity'].mean()
        product_sales['forecast'] = mean_quantity
        actual = actual_quantities
        forecast = product_sales['forecast']

        mae = np.mean(np.abs(actual - forecast))
        rmse = np.sqrt(np.mean((actual - forecast) ** 2))
        
        # print(f"MAE: {mae}, RMSE: {rmse}")

        reliability = max(0, 100 - (mae + rmse))
        # print(f"Fiabilité des prévisions pour le produit avec modèle simple: {reliability}%")
        return round(reliability, 2)
                
    def _calculate_reliability(self, train_data, test_data):
        test_data['forecast'] = train_data['forecast'].iloc[-1]
        actual = test_data['quantity']
        forecast = test_data['forecast']

        mae = mean_absolute_error(actual, forecast)
        rmse = np.sqrt(mean_squared_error(actual, forecast))

        reliability = max(0, 100 - (mae + rmse))
        # print(f"Fiabilité des prévisions: {reliability}%")
        return round(reliability, 2)

    def predict_sales(self, start_date, end_date, freq='D'):
        if not self.models:
            raise ValueError("Les modèles doivent être entraînés avant de faire des prédictions.")

        date_range = pd.date_range(start=start_date, end=end_date, freq=freq)
        predictions = []

        for product_uuid, model_info in self.models.items():
            product_uuid_str = str(uuid.UUID(bytes=product_uuid))
            product_info = self.product_data[self.product_data['uuid'] == product_uuid_str]
            
            if product_info.empty:
                print(f"Aucune information de produit trouvée pour UUID {product_uuid_str}.")
                continue

            product_info = product_info.iloc[0]
            price_ttc = product_info['price']

            future_data = pd.DataFrame({
                'date': date_range,
                'month': date_range.month,
                'year': date_range.year,
                'week': date_range.isocalendar().week
            })

            if 'forecast' in model_info:
                forecast_value = model_info['forecast']
                predicted_quantities = np.full(len(future_data), forecast_value)
            elif 'mean' in model_info and 'std' in model_info:
                mean = model_info['mean']
                std = model_info['std']
                predicted_quantities = np.random.normal(mean, std, len(future_data))
            else:
                print(f"Aucun modèle trouvé pour le produit {product_uuid_str}.")
                continue

            predicted_quantities = np.round(predicted_quantities).clip(min=0)
            
            reliability = model_info.get('reliability', 0)

            for date, quantity in zip(future_data['date'], predicted_quantities):
                if quantity > 0:
                    predictions.append({
                        'product_uuid': product_uuid_str,
                        'date': date,
                        'quantity': int(quantity),
                        'total_ttc': price_ttc * quantity,
                        'reliability': reliability
                    })

        if not predictions:
            print("Aucune prédiction générée. Vérifiez les données et les modèles.")

        return pd.DataFrame(predictions)
    
    def create_sales_report(self, sales_data):
        sales_report = sales_data[['created_at', 'delivery_fees', 'reduction', 'total_ht', 'total_ttc', 'vat', 'quantity']].copy()
        sales_report.rename(columns={
            'created_at': 'Date de début',
            'delivery_fees': 'Frais de livraison',
            'reduction': 'Réduction',
            'total_ht': 'Total HT',
            'total_ttc': 'Total TTC',
            'vat': 'TVA',
            'quantity': 'Quantité d\'éléments'
        }, inplace=True)
        sales_report = sales_report.round(2)
        return sales_report
      

    def create_summary_report(self, freq='W'):
        aggregated_data = self.sales_data.copy()
        aggregated_data['created_at'] = pd.to_datetime(aggregated_data['created_at'])

        now = pd.Timestamp.now()

        if freq == 'W':
            end_date = now - pd.Timedelta(days=now.weekday() + 1)
            start_date = end_date - pd.Timedelta(days=6)
            frequency = 'Semaine'

        elif freq == 'M':
            first_day_of_current_month = now.replace(day=1)
            end_date = first_day_of_current_month - pd.Timedelta(days=1)
            start_date = end_date.replace(day=1)
            frequency = 'Mois'

        elif freq == 'Y':
            start_date = pd.Timestamp(year=now.year - 1, month=1, day=1)
            end_date = pd.Timestamp(year=now.year - 1, month=12, day=31)
            frequency = 'Année'

        else:
            raise ValueError("La fréquence doit être 'W', 'M', ou 'Y'.")

        filtered_data = aggregated_data[(aggregated_data['created_at'] > start_date) & (aggregated_data['created_at'] <= end_date)]

        total_summary = {
            'Période': frequency,
            'Date de début': start_date.strftime('%Y-%m-%d'),
            'Date de fin': end_date.strftime('%Y-%m-%d'),
            'Nombre total de ventes': len(filtered_data),
            'Quantité totale vendue': round(filtered_data['quantity'].sum(), 2),
            'Total des frais de livraison': round(filtered_data['delivery_fees'].sum(), 2),
            'Total des réductions': round(filtered_data['reduction'].sum(), 2),
            'Total de TVA': round(filtered_data['vat'].sum(), 2),
            'Total des ventes HT': round(filtered_data['total_ht'].sum(), 2),
            'Total des ventes TTC': round(filtered_data['total_ttc'].sum(), 2)
        }
        summary_df = pd.DataFrame([total_summary])
        return summary_df
      
      
    def create_predictions_report(self, predictions, start_date, end_date, extractor):
        start_date = pd.Timestamp(start_date)
        end_date = pd.Timestamp(end_date)

        predictions_report = predictions.copy()

        filtered_predictions = predictions_report[
            (predictions_report['date'] >= start_date) & 
            (predictions_report['date'] <= end_date)
        ].copy()

        filtered_predictions['product_name'] = filtered_predictions['product_uuid'].apply(
            lambda x: extractor.fetch_product_details(x).get('name', 'Produit inconnu')
        )

        filtered_predictions = filtered_predictions[['product_name', 'date', 'total_ttc', 'quantity', 'reliability']]
        filtered_predictions.columns = ['Nom du produit', 'Date de prédiction de vente', 'Total TTC', 'Quantité de prédiction', 'Fiabilité (%)']

        filtered_predictions['Total TTC'] = filtered_predictions['Total TTC'].round(2)
        filtered_predictions['Date de prédiction de vente'] = filtered_predictions['Date de prédiction de vente'].dt.strftime('%Y-%m-%d')

        return filtered_predictions.sort_values('Date de prédiction de vente')