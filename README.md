<h1>Steps To Run Project</h1>
<ul>
  <li>
    <h4>Step 1: Open Android Studio</h4>
    <p>Click on Get from VCS paste this link: <a href="https://github.com/vcDevelop/Field_Agent.git">https://github.com/vcDevelop/Field_Agent.git</a></p>
    <img src="https://github.com/vcDevelop/Field_Agent/assets/88608116/218791ce-080a-4af7-a7c9-bdb0320c69f2/WhatsApp%20Image%202024-03-09%20at%204%2015%2011%20PM.jpg" alt="WhatsApp Image 2024-03-09 at 4 15 11 PM">
  </li>
<li>
      <h4>Step 2: Go to menu -> tools -> firebase </h4>
</li>
      <li>
            <h4> Step 3:Click on Authenticate using Google ->Connect to Firebase ->Create Project-></h4>
        <img src="https://github.com/vcDevelop/Field_Agent/assets/88608116/218791ce-080a-4af7-a7c9-bdb0320c69f2/WhatsApp%20Image%202024-03-09%20at%204%2015%2011%20PM.jpg" alt="WhatsApp Image 2024-03-09 at 4 15 11 PM">
      </li>
      <li>
            <h4> Step 4:In Android Studio Click on Cloud firestore -> get start with cloud firestore -> that go to firestore -><br> Than go to Authentication and click on get started <br>
             Click on Email/password ->enable and save it -> go back to firebase project click on cloud store -> create Database 
            </h4>
      </li>
      <li>
            <h4> Step 5: Change the rules (Paste this )->and publish -><br>
            "rules_version = '2';
             service cloud.firestore {
             match /databases/{database}/documents {
             match /{document=**} {
             allow read, write: if request.auth != null;
          }
         }
        }" 

            </h4>
      </li>
      <li>
            <h4> Step 6: </h4>
      </li>
</ul>
