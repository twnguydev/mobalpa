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

    @patch('smtplib.SMTP')
    def test_send_email_with_attachments(self, mock_smtp):
        mock_server = MagicMock()
        mock_smtp.return_value = mock_server

        with tempfile.NamedTemporaryFile(delete=False) as tmp_file:
            tmp_file.write(b'This is a test attachment')
            tmp_file.flush()
            attachment_path = tmp_file.name

        sender = EmailSender(
            os.getenv('SMTP_HOST'), 
            int(os.getenv('SMTP_PORT')), 
            os.getenv('SMTP_USERNAME'), 
            os.getenv('SMTP_PASSWORD')
        )

        sender.send_email_with_attachments(
            ['tanguy.gibrat@epitech.com'],
            'Subject',
            'Body',
            [attachment_path]
        )

        mock_server.login.assert_called_once_with(os.getenv('SMTP_USERNAME'), os.getenv('SMTP_PASSWORD'))
        mock_server.send_message.assert_called_once()
        os.remove(attachment_path)

if __name__ == '__main__':
    unittest.main()