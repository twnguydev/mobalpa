import pandas as pd

class ReportGenerator:
    def __init__(self, sales_data):
        self.sales_data = sales_data
        
        
    def create_sales_report(self):
        sales_report = self.sales_data[['created_at', 'delivery_fees', 'reduction', 'total_ht', 'total_ttc', 'vat', 'quantity']].copy()
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