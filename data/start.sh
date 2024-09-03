# start.sh simplifié
#!/bin/bash

source venv/bin/activate
python api.py &
python main.py &
wait