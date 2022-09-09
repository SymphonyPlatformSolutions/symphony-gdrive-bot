## Symphony Google Drive Bot
Listens for messages containing Google Drive links, then compares the file permissions with the stream membership (excluding bots) and adds any missing members to the file.

### Demo
https://user-images.githubusercontent.com/7151832/189032993-202040fb-e867-47a2-8f4d-7a6e961d4a98.mp4

### Configuration
1. Add the appropriate service account and private key details into `src/main/resources/application.yaml` 
2. Add the `credentials.json` file from Google Cloud Console into `src/main/resources`
3. Upon initial launch, visit the URL presented in the logs to perform the OAuth authorisation flow

### Enable Google Drive API
To get started integrating with the Google Drive, you need to enable the Drive API within your app's Cloud Platform project and provide configuration details. This gives you access to the API as well as access to UI integration features.

To enable the Drive API, complete these steps:
1. Go to the [Google API Console](https://console.developers.google.com/)
2. Select a project
![Google API Console](images/google_project.png?raw=true "Google API Console")
3. In the sidebar on the left, click on **APIs & Services** and select APIs
![APIs & Services](images/google_apis_services.png?raw=true "APIs & Services")
4. From the **Library** list of available APIs, click the **Google Drive API** link and click **Enable** API.
![Google Drive API](images/google_drive_api.png?raw=true "Google Drive API")

### Google Drive API Credentials
Once you have enabled the Google Drive API you will need to create credentials that your project will use to interact with the APIs.

To create credentials, complete these steps:
1. Within the Google Drive API click on **Create Credentials**
![Create Credentials](images/google_create_creds.png?raw=true "Create Credentials")
2. Choose the **OAuth client ID** option
3. In the next screen, **Application Type** should be selected as Web Application
4. Then provide a meaningful name for your user account, *(e.g. Gdrive Bot)*
![Credentials](images/google_creds.png?raw=true "Credentials")
5. Then click on **Create**
6. You will then see the following screen to download your OAuth client credentials in the form of a JSON file.
![OAuth Client](images/google_client.png?raw=true "OAuth Client")
