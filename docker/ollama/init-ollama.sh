#!/bin/sh
# Pull the model first
ollama pull nomic-embed-text
# Then start the server
ollama serve