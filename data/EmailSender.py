import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders

class EmailSender:
    def __init__(self, smtp_server, smtp_port, username, password):
        self.smtp_server = smtp_server
        self.smtp_port = smtp_port
        self.username = username
        self.password = password
    
    def send_email_with_attachments(self, to_emails, subject, body, attachments):
        msg = MIMEMultipart()
        msg['From'] = self.username
        msg['To'] = ', '.join(to_emails)
        msg['Subject'] = subject
        
        msg.attach(MIMEText(body, 'plain'))
        
        for file in attachments:
            attachment = MIMEBase('application', 'octet-stream')
            try:
                with open(file, 'rb') as f:
                    attachment.set_payload(f.read())
                encoders.encode_base64(attachment)
                attachment.add_header('Content-Disposition', f'attachment; filename={file}')
                msg.attach(attachment)
            except Exception as e:
                print(f"An error occurred while attaching {file}: {str(e)}")
        
        with smtplib.SMTP(self.smtp_server, self.smtp_port) as server:
            server.starttls()
            server.login(self.username, self.password)
            server.send_message(msg)