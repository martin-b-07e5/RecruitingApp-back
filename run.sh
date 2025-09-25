#!/bin/bash

# Script to run the application in different environments

show_help() {
    echo "Usage: ./run.sh [COMMAND] [ENVIRONMENT]"
    echo ""
    echo "Commands:"
    echo "  local           Run locally with Maven"
    echo "  docker-local    Run with Docker Compose (local)"
    echo "  docker-build    Build Docker image"
    echo "  docker-run      Run Docker container with specific env"
    echo "  help            Show this help message"
    echo ""
    echo "Environments (for docker-run):"
    echo "  local           Use .env.local"
    echo "  docker-local    Use .env.docker.local"
    echo "  docker-remote   Use .env.docker.remote"
    echo ""
    echo "Examples:"
    echo "  ./run.sh local"
    echo "  ./run.sh docker-local"
    echo "  ./run.sh docker-build"
    echo "  ./run.sh docker-run docker-local"
}

case "$1" in
    "local")
        echo "Running locally with Maven..."
        cp .env.local .env
        ./mvnw spring-boot:run
        ;;

    "docker-local")
        echo "Running with Docker Compose..."
        docker compose up --build
        ;;

    "docker-build")
        echo "Building Docker image..."
        docker build -t recruiting-app-backend .
        ;;

    "docker-run")
        if [ -z "$2" ]; then
            echo "Error: Environment not specified"
            echo "Usage: ./run.sh docker-run [local|docker-local|docker-remote]"
            exit 1
        fi

        ENV_FILE=".env.$2"
        if [ "$2" = "local" ]; then
            ENV_FILE=".env.local"
        fi

        if [ ! -f "$ENV_FILE" ]; then
            echo "Error: Environment file $ENV_FILE not found"
            exit 1
        fi

        echo "Running Docker container with $ENV_FILE environment..."
        docker run --rm -p 8080:8080 --env-file "$ENV_FILE" recruiting-app-backend
        ;;

    "help"|"--help"|"-h"|"")
        show_help
        ;;

    *)
        echo "Unknown command: $1"
        echo ""
        show_help
        exit 1
        ;;
esac