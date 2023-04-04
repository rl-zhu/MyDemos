
## Getting Started

### Start the Next.js frontend 
First, go to the folder using```cd next-demos```, and then use the commands to get all dependencies:

```bash
npm i / npm install (some time npm add sass is needed)
# or
yarn install
# or
pnpm install
```


Second, run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

Or you can visit [http://localhost:3000/image_finder](http://localhost:3000/image_finder) and [http://localhost:3000/multithread](http://localhost:3000/multithread) directly

### Start the Next.js Backend 

First go into the folder ```cd springboot_demos``` and use ```./mvnw spring-boot:run``` to use the project

or directly use `java -jar demo-0.0.1-SNAPSHOT.jar`

Notice:

- The multithread will connected the the database, so you want to create a related database to enable the function