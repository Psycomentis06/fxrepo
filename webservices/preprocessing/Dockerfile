FROM python:3.11.4-bookworm
WORKDIR /usr/src/app

COPY setup.sh ./
RUN chmod +x setup.sh
RUN ./setup.sh

# not needed for dev environemt
#RUN apt-get update && apt-get install -y --no-install-recommends

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["python", "-u", "app.py"]