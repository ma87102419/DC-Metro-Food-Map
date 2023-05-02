FROM python:3

WORKDIR /opt/food-map-data-collector

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY src ./src
COPY data ./data
COPY test.sh .

CMD [ "python", "src/main.py" ]