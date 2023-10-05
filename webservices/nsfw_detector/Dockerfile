FROM python:3.11.4-bookworm
WORKDIR /usr/src/app
COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt
COPY setup.sh ./
RUN chmod +x setup.sh
COPY . .
CMD ["python", "-u", "main.py"]