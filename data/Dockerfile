FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt start.sh ./

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

RUN chmod +x start.sh

CMD ["./start.sh"]