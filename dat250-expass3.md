# DAT250, Experiment Assignment 3

For this assignment I followed the steps provided. I chose Svelte (Vite + plain JavaScript) for the SPA.

### Implementation
I set up a frontend and backend structure to my ptoject and set up my node project using vite in the frontend folder and removed the starter code. 
I then started on the two components. One for creating polls, and one for voting on polls.
I started with stub data so that I could test out the initial behaviour and layout.
After I was satisfied, I moved on and enabled CORS on the server side for testing, by adding '@CrossOrigin(origins = "http:/localhost:5173")' to the top of each controller. 
After that I integrated the frontend with the backend. When the functionality was correct I moved on to some more styling to make the app look better. Finally I deployed the app using 'npm run build' and copied the newly created files into my Spring Boot application. I can now run the app using ./gradlew bootrun.

### Technical problems
The main challenge was getting up to speed with Svelte. I hadn’t used Svelte before, so a lot of time went into learning the basics and fixing small issues along the way.

### Link to repo
https://github.com/Sanderconn/Dat250-expass/tree/main
