# Steps To Run Project

1. **Step 1: Open Android Studio**
    - Click on `Get from VCS` paste this link: - (https://github.com/vcDevelop/FieldAgent.git)
    <br>

2. **Step 2: Go to menu -> tools -> firebase**
    <br>

3. **Step 3: Click on Authenticate using Google -> Connect to Firebase -> Create Project**
    <br>

5. **Step 4: In Android Studio**
    - Click on `Cloud Firestore` -> `Get started with Cloud Firestore` -> then go to `Firestore`<br>
    - Then go to `Authentication` and click on `Get started`<br>
    - Click on `Email/password` -> enable and save it -> go back to Firebase project -> click on `Cloud Store` -> create Database

6. **Step 5: Change the rules (Paste this) -> and publish**<br>
    ```firebase
    rules_version = '2';
    service cloud.firestore {
      match /databases/{database}/documents {
        match /{document=**} {
          allow read, write: if request.auth != null;
        }
      }
    }
    ```

9. **Step 7:  After setting up everything go in files and click "sync project with gradle files"**
9. **Step 6: Click on the run button in Android Studio**
