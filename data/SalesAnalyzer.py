from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
import datetime
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
            'vat': 'sum',
            'quantity': 'sum',
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

            if len(product_sales) < 5:
                print(f"Pas assez de données pour le produit {product_uuid}. Utilisation d'un modèle simple.")
                self.models[product_uuid] = {'mean': product_sales['quantity'].mean(), 'std': product_sales['quantity'].std()}
            else:
                product_sales['forecast'] = product_sales['quantity'].ewm(span=5, adjust=False).mean()
                self.models[product_uuid] = {'forecast': product_sales['forecast'].iloc[-1]}
                print(f"Modèle de lissage exponentiel appliqué pour le produit {product_uuid}")


    def predict_sales(self, start_date, end_date, freq='D'):
        if not self.models:
            raise ValueError("Les modèles doivent être entraînés avant de faire des prédictions.")

        date_range = pd.date_range(start=start_date, end=end_date, freq=freq)
        predictions = []

        for product_uuid, model_info in self.models.items():
            product_uuid = str(uuid.UUID(bytes=product_uuid))
            product_info = self.product_data[self.product_data['uuid'] == product_uuid]
            if product_info.empty:
                print(f"Aucune information de produit trouvée pour UUID {product_uuid}.")
                continue

            product_info = product_info.iloc[0]
            price_ttc = product_info['price']

            future_data = pd.DataFrame({
                'date': date_range,
                'month': date_range.month,
                'year': date_range.year,
                'week': date_range.isocalendar().week
            })

            if 'model' in model_info:
                model = model_info['model']
                predicted_quantities = model.predict(future_data[['month', 'year', 'week']])
            elif 'forecast' in model_info:
                forecast_value = model_info['forecast']
                predicted_quantities = np.full(len(future_data), forecast_value)
            else:
                mean = model_info['mean']
                std = model_info['std']
                predicted_quantities = np.random.normal(mean, std, len(future_data))

            predicted_quantities = np.round(predicted_quantities).clip(min=0)

            for date, quantity in zip(future_data['date'], predicted_quantities):
                if quantity > 0:
                    predictions.append({
                        'product_uuid': product_uuid,
                        'date': date,
                        'quantity': int(quantity),
                        'total_ttc': price_ttc * quantity
                    })

        if not predictions:
            print("Aucune prédiction générée. Vérifiez les données et les modèles.")

        return pd.DataFrame(predictions)


    def create_predictions_report(self, predictions, end_date, extractor):
        end_date = pd.Timestamp(end_date)
    
        # Calculer la date de début de la période de prédiction "P+1"
        start_date = end_date + pd.Timedelta(days=1)
        prediction_end_date = start_date + pd.Timedelta(days=6)  # Fin de la semaine P+1

        # Filtrer les prévisions pour la période "P+1"
        filtered_predictions = predictions[(predictions['date'] >= start_date) & 
                                        (predictions['date'] <= prediction_end_date)].copy()
        predictions_report = predictions.copy()
        
        end_date = pd.Timestamp(end_date)

        start_date = end_date + pd.Timedelta(days=1)
        end_of_period = start_date + pd.Timedelta(days=6)

        filtered_predictions = predictions_report[(predictions_report['date'] >= start_date) & (predictions_report['date'] <= end_of_period)].copy()
        
        print(f"end_date: {end_date}")
        print(f"filtered_predictions: {filtered_predictions}")

        filtered_predictions['product_name'] = filtered_predictions['product_uuid'].apply(
            lambda x: extractor.fetch_product_details(x).get('name', 'Produit inconnu')
        )

        filtered_predictions = filtered_predictions[['product_name', 'date', 'total_ttc', 'quantity']]
        filtered_predictions.columns = ['Nom du produit', 'Date de prédiction de vente', 'Total TTC', 'Quantité de prédiction']

        filtered_predictions['Total TTC'] = filtered_predictions['Total TTC'].round(2)
        filtered_predictions['Date de prédiction de vente'] = filtered_predictions['Date de prédiction de vente'].dt.strftime('%Y-%m-%d')

        return filtered_predictions.sort_values('Date de prédiction de vente')


    def create_summary_report(self, aggregated_data, freq='W'):
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

    # def create_predictions_report(self, predictions, extractor):
    #     predictions_report = predictions.copy()
    #     predictions_report['product_name'] = predictions_report['product_uuid'].apply(lambda x: extractor.fetch_product_details(x).get('name', 'Produit inconnu'))
    #     predictions_report = predictions_report[['product_name', 'date', 'total_ttc', 'quantity']]
    #     predictions_report.columns = ['Nom du produit', 'Date de prédiction de vente', 'Total TTC', 'Quantité de prédiction']
    #     predictions_report = predictions_report.round({'Total TTC': 2, 'Quantité de prédiction': 0})
    #     predictions_report['Date de prédiction de vente'] = predictions_report['Date de prédiction de vente'].dt.strftime('%Y-%m-%d')
    #     return predictions_report