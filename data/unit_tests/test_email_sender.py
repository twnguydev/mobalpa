import unittest
from unittest.mock import patch, MagicMock
from dotenv import load_dotenv
import os
import sys
import tempfile

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from EmailSender import EmailSender

class TestEmailSender(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        load_dotenv()

    @patch('EmailSender.smtplib.SMTP')
    def test_send_email(self, mock_smtp):
        mock_server = MagicMock()
        mock_smtp.return_value = mock_server

        with tempfile.NamedTemporaryFile(delete=False) as tmp_file:
            tmp_file.write(b'This is a test attachment')
            attachment_path = tmp_file.name
        
        sender = EmailSender(
            os.getenv('SMTP_HOST'), 
            int(os.getenv('SMTP_PORT')), 
            os.getenv('SMTP_USERNAME'), 
            os.getenv('SMTP_PASSWORD')
        )
        sender.send_email(['tanguy.gibrat@epitech.com'], 'Subject', 'Body', attachment_path)
        
        mock_server.starttls.assert_called_once()
        mock_server.login.assert_called_once_with(os.getenv('SMTP_USERNAME'), os.getenv('SMTP_PASSWORD'))
        mock_server.sendmail.assert_called_once()

        os.remove(attachment_path)

if __name__ == '__main__':
    unittest.main()