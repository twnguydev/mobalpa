#!/bin/bash

source venv/bin/activate
pip install --no-cache-dir -r requirements.txt
pip install --upgrade flask werkzeug
# pip list
# python -m unittest discover -s unit_tests
python main.py &
python api.py &
wait