# Steps To Run Project

1. **Step 1: Open Android Studio**
    - Click on `Get from VCS` paste this link: - [[https://github.com/vcDevelop/Field_Agent.git](https://github.com/vcDevelop/Field_Agent.git)](https://github.com/vcDevelop/FieldAgent.git)
    <br>
    <img src="https://github.com/vcDevelop/Field_Agent/assets/88608116/c2860402-3841-4a6b-a3cc-29299bcca5f5" alt="Step 3 Image" width="800" height="300">

2. **Step 2: Go to menu -> tools -> firebase**
    <br>

3. **Step 3: Click on Authenticate using Google -> Connect to Firebase -> Create Project**
    <br>
   <img src="https://github.com/vcDevelop/Field_Agent/assets/88608116/fe37c578-5392-4812-aae3-dc57c5cae0fa" alt="Step 1 Image" width="800" height="300">

5. **Step 4: In Android Studio**
    - Click on `Cloud Firestore` -> `Get started with Cloud Firestore` -> then go to `Firestore`<br>
      <img src="https://github.com/vcDevelop/Field_Agent/assets/88608116/13b3d3f8-d8b1-4773-8b83-ac06ddec5ca7" alt="Step 3 Image" width="600" height="400">
    - Then go to `Authentication` and click on `Get started`<br>
      <img src="https://github.com/vcDevelop/Field_Agent/assets/88608116/5af660a7-7803-4bef-b1af-15a1a525ea27" alt="Step 3 Image" width="600" height="400">
    - Click on `Email/password` -> enable and save it -> go back to Firebase project -> click on `Cloud Store` -> create Database

6. **Step 5: Change the rules (Paste this) -> and publish**<br>
   <img src="https://github.com/vcDevelop/Field_Agent/assets/88608116/b378c67c-80d9-4f41-90d5-c309fd926a32" alt="Step 3 Image" width="600" height="400">
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
