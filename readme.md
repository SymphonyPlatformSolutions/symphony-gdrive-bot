## Symphony Google Drive Bot
Listens for messages containing Google Drive links, then compares the file permissions with the stream membership (excluding bots) and adds any missing members to the file.

### Demo
https://user-images.githubusercontent.com/7151832/189032993-202040fb-e867-47a2-8f4d-7a6e961d4a98.mp4

### Configuration
1. Add the appropriate service account and private key details into `src/main/resources/application.yaml` 
2. Add the `credentials.json` file from Google Cloud Console into `src/main/resources`
3. Upon initial launch, visit the URL presented in the logs to perform the OAuth authorisation flow
