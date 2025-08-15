FROM ollama/ollama:0.11.4

# Install curl for healthchecks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

## Copy our startup script
#COPY init-ollama.sh /init-ollama.sh
#RUN chmod +x /init-ollama.sh
#
## Use our script as the container entrypoint
#ENTRYPOINT ["/init-ollama.sh"]