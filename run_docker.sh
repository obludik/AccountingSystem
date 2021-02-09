echo "Creating Docker image..."
docker build -t accountingsystem .

echo "Stopping running container..."
docker stop accountingsystem

echo "Removing previous container..."
docker rm accountingsystem

echo "Starting container..."
docker run -p 8045:8045 --name accountingsystem accountingsystem