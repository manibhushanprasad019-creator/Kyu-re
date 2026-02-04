# Use a lightweight Java image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy your Java file into the container
COPY Main.java .

# Compile the code
RUN javac Main.java

# Run the bot
CMD ["java", "Main"]
