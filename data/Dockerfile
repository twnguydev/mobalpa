FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt start.sh ./
COPY . ./

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 8083

CMD ["gunicorn", "-w", "4", "-b", "0.0.0.0:8083", "api:app"]